/*
 * The implementation for the load balancer process
 *
 *  Created On: Feb 13, 2011
 *      Author: jon
 */

#include "LoadBalancer.h"
#include "DistributedServer.h"
#include "FileTransferHeader.h"

#include <iostream>
#include <sstream>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <pthread.h>
#include <arpa/inet.h>
#include <errno.h>
#include <netinet/in.h>
#include <netdb.h>
#include <signal.h>
#include <sys/time.h>
#include <limits.h>

using namespace __gnu_cxx;
using std::string;
using namespace std;

// Global vars for killing the process tree
int id = -1;
LoadBalancer * thisLoadBalancer;
DistributedServer * thisDistributedServer;

/**
 * Signal handler
 */
void handler(int caller) {
	switch(caller){
		case SIGHUP:
		case SIGINT:
		case SIGQUIT:
		case SIGKILL:
		case SIGABRT:
		case SIGSTOP:

			// Output error messages and free memory, then end this process
			if(id==-1){
				cout<<"Shutting Down Load Balancer"<<endl;
				delete thisLoadBalancer;
			} else {
				cout<<"Shutting Down Server #"<<id<<endl;
				delete thisDistributedServer;
			}
			exit(0);

		default:
			break;
	}
}


/**
 * Install signal handler
 */
void installHandler() {
  struct sigaction setup_action;
  sigset_t block_mask;

  sigemptyset (&block_mask);

  /* Block other terminal-generated signals while handler runs. */
  sigaddset (&block_mask, SIGINT);
  sigaddset (&block_mask, SIGQUIT);
  sigaddset (&block_mask, SIGSTOP);
  sigaddset (&block_mask, SIGKILL);
  sigaddset (&block_mask, SIGABRT);

  setup_action.sa_handler = handler;
  setup_action.sa_mask = block_mask;
  setup_action.sa_flags = 0;

  //Actually 'install' the handler for each of the specified signals
  sigaction (SIGINT, &setup_action, NULL);
  sigaction (SIGQUIT, &setup_action, NULL);
  sigaction (SIGSTOP, &setup_action, NULL);
  sigaction (SIGKILL, &setup_action, NULL);
  sigaction (SIGABRT, &setup_action, NULL);
}


/**
 * Default constructor for the load balancer. This constructor will
 * 	initialize all LoadBalancer fields.
 *
 * 	@param	loadBalancerRequestPort	 	 The port on which to listen for requests from the client
 * 	@param	loadBalancerFileTransferPort The port on which to listen for responses from servers
 * 	@param	loadBalancerFailurePort  	 The port on which to listen for failure notifications
 *  @param	clientTransferPort			 The port on which to connect to the client to transfer files
 * 	@param	processIdsFile 			 	 The path to the file with process ids
 * 	@param	serverLoadsFile 		 	 The path to the file holding the server loads
 * 	@param	fileMapFilePath			 	 The path to the file map file
 */
LoadBalancer::LoadBalancer(int loadBalancerRequestPort, int loadBalancerFileTransferPort, int loadBalancerFailurePort,
		int clientTransferPort, string processIdsFilePath, string serverLoadsFilePath, string fileMapFilePath) : 
		processIdsFilePath(processIdsFilePath), serverLoadsFilePath(serverLoadsFilePath), loadBalancerFailurePort(loadBalancerFailurePort),
		loadBalancerRequestPort(loadBalancerRequestPort), loadBalancerFileTransferPort(loadBalancerFileTransferPort), clientTransferPort(clientTransferPort) {
		this->fileMapFilePath = string(fileMapFilePath);
}

/**
 * Initializes the system, by building all threads and initializing data structures
 *
 * 	@return true on success, false otherwise
 */
bool LoadBalancer::intialize(){

	// Setup semaphores and mutex's
	sem_init(&fileRequestSemaphore, 0, 0);
	pthread_mutex_init(&fileRequestQueueMutex, NULL);
	pthread_mutex_init(&serverDataMutex, NULL);

	// Allocate space for our queue and hash map
	fileRequestQueue = new queue<FileRequest *>();
	fileLocations = new map<string, int*>();

	// Install the signal handler so that we cleanup after ourselves properly if we're killed
	installHandler();

	// Read in the file map file and initialize basic data structures from that
	readFileMap();

	// Initialize the global request counter
	requestNumber = 0;

	// Initialize array
	serverStates = new int*[2];
	serverStates[0] = new int[numberOfServers];
	serverStates[1] = new int[numberOfServers];
	
	// Success
	return true;
}

/**
 * Destructor for the load balancer. Frees all memory for every object
 * 	in the LoadBalancer.
 */
LoadBalancer::~LoadBalancer(){
	cleanup();
}

/**
 * Helper function to cleanup all data for this process
 */
void LoadBalancer::cleanup(){

	// Empty the queue
	while(!fileRequestQueue->empty()) {
		fileRequestQueue->pop();
	}

	// Free memory being used
	delete fileRequestQueue;
	delete fileLocations;
	delete serverLoads;
	delete processIds;

	// Destroy the mutexes and semaphores
	pthread_mutex_destroy(&fileRequestQueueMutex);
	pthread_mutex_destroy(&serverDataMutex);
	sem_destroy(&fileRequestSemaphore);
}

/**
 * The 'requestListenerThread' will run this function. This function will be responsible
 * 	for listening to requests from the client and inserting them at the back of the queue
 *
 * 	@param	data	A pointer to an integer representing the file descriptor for the socket
 * 					on which to listen for file requests
 */
void* LoadBalancer::listenForClientRequests(void* data){

	// Get 'this' out of the data passed, and the actual data
	ThreadFunctionArguments* arguments = static_cast<ThreadFunctionArguments*>(data);
	LoadBalancer* This = arguments->This;
	int socketFd = *((int*) arguments->actualArguments);

	// Data structures
	int transferSocketFd;
	struct sockaddr_in newAddressStructure;
	socklen_t newSize = sizeof(struct sockaddr_in);
	int BUFFER_SIZE = 1024;

	//Block, waiting to accept incoming connection
	if ((transferSocketFd = accept(socketFd, (struct sockaddr *)&newAddressStructure, &newSize)) == -1) {
		close(transferSocketFd);
		close(socketFd);
		perror("Error Accepting Connection for Failure Notification");
		pthread_exit(NULL);
	}

	while(true) {


		// Pull up to BUFFER_SIZE bytes from the socket, the filename must be at most this many
		char filename[BUFFER_SIZE];
		int msgLength;
		if((msgLength = recv(transferSocketFd, &filename, BUFFER_SIZE, 0)) == -1) { //error case
			close(transferSocketFd);
			close(socketFd);
			perror("Error Recieving Data from Socket in Request Listener in Load Balancer");
			pthread_exit(NULL);

		// Add this file request to the queue
		} else if(msgLength>0){
			
			// Build the file request struct
			FileRequest * fileRequest = new FileRequest;
			strcpy(fileRequest->filePath, filename);
			// Get a lock on the request queue and add it, and add one to the semaphore
			pthread_mutex_lock(&(This->fileRequestQueueMutex));
			sem_post(&(This->fileRequestSemaphore));
			(This->fileRequestQueue)->push(fileRequest);
			pthread_mutex_unlock(&(This->fileRequestQueueMutex));
		}
	}

	// Close
	close(transferSocketFd);
	close(socketFd);
}


/**
 * The 'requestResponseThread' will run this function. This function will be responsible
 * 	for monitoring the file request queue for requests, and transferring the file data back
 * 	to the client
 *
 * 	@param	data	Any data that this thread needs (required by POSIX library) -- NULL, or no data in this case
 */
void* LoadBalancer::handleFileRequests(void* data){

	// Get 'this' out of the data passed, and the actual data
	ThreadFunctionArguments* arguments = static_cast<ThreadFunctionArguments*>(data);
	LoadBalancer* This = arguments->This;

	while(true) {
		
		// Wait until they're something to process in the queue
		sem_wait(&(This->fileRequestSemaphore));

		// Lock the queue and pull the next request from the file
		pthread_mutex_lock(&(This->fileRequestQueueMutex));
		FileRequest * fileRequest = (This->fileRequestQueue)->front();
		
		char * filename = new char[256];
		strcpy(filename, fileRequest->filePath);
		(This->fileRequestQueue)->pop();
		pthread_mutex_unlock(&(This->fileRequestQueueMutex));

		// Find the servers that have this file
		pthread_mutex_lock(&(This->serverDataMutex));
		int * serversWithFile = (*(This->fileLocations))[filename];

		// Find the loads of each server
		int firstServerLoad = (This->serverLoads)[serversWithFile[0]];
		int secondServerLoad = (This->serverLoads)[serversWithFile[1]];

		// Find the server with the lighter load, and the port on which this server is listening
		int serverId;
		if(firstServerLoad <= secondServerLoad) {
			serverId = serversWithFile[0];
		} else {
			serverId = serversWithFile[1];
		}
		
		int port = (This->loadBalancerFailurePort) + (1 + serverId);
		char serverPort[256];
		sprintf(serverPort, "%d", port);
		pthread_mutex_unlock(&(This->serverDataMutex));


		// Get client socket to send the request to the server
		int serverSocketFd = This->setupClientSocket();

		// Get the information for connecting to localhost on the port specified
		struct addrinfo hostInfo, * myInfo = NULL;
		memset(&hostInfo, 0, sizeof(hostInfo));
		hostInfo.ai_family = AF_INET;
		hostInfo.ai_socktype = SOCK_STREAM;
		int tmp;
		if ((tmp = getaddrinfo("localhost", serverPort, &hostInfo, &myInfo)) != 0) {
			fprintf(stderr, "Error Using 'getaddrinfo()': %s\n", gai_strerror(tmp));
			pthread_exit(NULL);
		}

		// Create a flag to indicate whether the server connection fails
        bool failWhileAccess = false;

		// Connect to the server, set our flag and output an error if connecting fails
		if (connect(serverSocketFd, myInfo->ai_addr, myInfo->ai_addrlen) != 0) {
			close(serverSocketFd);
			perror("Error Establishing Connection to Server Process");
			failWhileAccess = true;
		}

		// If the connection to the server succeeded
		if(!failWhileAccess) {

			//Free Address Structure
			freeaddrinfo(myInfo);

			// Send a request for the file to the server we chose (just send the filename)
			int len = strlen(filename)+1;
			if (len != send(serverSocketFd, filename, len, 0)) {void copyFile(int sourc, int dest, string fileName);
					perror("Error Sending Filename to Server");
					pthread_exit(NULL);
			}
			delete filename;
		}

		// Get a socket for connecting to the client
		int clientSocketFd = This->setupClientSocket();

		// Form a connection to the client
		myInfo = NULL;
		memset(&hostInfo, 0, sizeof(hostInfo));
		hostInfo.ai_family = AF_INET;
		hostInfo.ai_socktype = SOCK_STREAM;
		char clientPort[256];
		sprintf(clientPort, "%d", This->clientTransferPort);
		if ((tmp = getaddrinfo("localhost", clientPort, &hostInfo, &myInfo)) != 0) {
			fprintf(stderr, "Error Using 'getaddrinfo()': %s\n", gai_strerror(tmp));
			close(clientSocketFd);
			close(serverSocketFd);
			pthread_exit(NULL);
		}
		if (connect(clientSocketFd, myInfo->ai_addr, myInfo->ai_addrlen) != 0) {
			close(clientSocketFd);
			close(serverSocketFd);
			close(serverSocketFd);
			perror("Error Establishing Connection to Client Process");
			pthread_exit(NULL);
		}

		// If we failed to connect to the server, send an error message to the client
		if(failWhileAccess) {
			This->sendError(clientSocketFd);
			close(clientSocketFd);
			continue;
		}

		// Read the header for the file transfer
		FileTransferHeader header;
		if((recv(serverSocketFd, &header, sizeof(FileTransferHeader), 0)) != sizeof(FileTransferHeader)) {
			This->sendError(clientSocketFd);
		}

		// If there was no error, transfer the file data that follows
		if(strcmp(header.filePath, "ERROR")!=0) {

			// Setup data structures
			int msgLength = 0;
			size_t numBytesToRead = header.fileSize;
			char buffer[header.fileSize];
			char * temp = buffer;

			// Loop, receiving data from the client
			while(numBytesToRead > 0) {

				// Receive some data from the client, check for errors
				if((msgLength = recv(serverSocketFd, buffer, header.fileSize, 0))<=0) {

					// Send an error message to the client
					This->sendError(clientSocketFd);
					break;
				}

				// Otherwise, this was successful
				else if(msgLength > 0) {
					numBytesToRead-=msgLength;
					temp = temp+ msgLength;
        		}
			}

			// Now that we have the data from the server, contact the client
			if(numBytesToRead == 0) {

				// Forward the header to the client
				if (sizeof(FileTransferHeader) != send(clientSocketFd, &header, sizeof(FileTransferHeader), 0)) {
						close(clientSocketFd);
						close(serverSocketFd);
						perror("Error Sending Header to Client");
						pthread_exit(NULL);
				}

				// If we successfully read the entire file into our buffer, forward the file contents
				if ((int) header.fileSize != send(clientSocketFd, buffer, header.fileSize, 0)) {
						close(clientSocketFd);
						close(serverSocketFd);
						perror("Error Sending File to Client");
						pthread_exit(NULL);
				}
				cout<<"Balancer Sent "<<header.filePath <<"\n";
			}

		// Otherwise, there was an error, let the client know
		} else {

			// Forward the error header to the client
			This->sendError(clientSocketFd);
		}

		//Update the server_loads file
		updateServerLoads(This, serverId, header.fileSize);
		close(clientSocketFd);
		close(serverSocketFd);

	}
}

/**
 * Helper function to send an error header back to the client
 */
void LoadBalancer::sendError(int clientSocketFd) {

	// Build the error header
    FileTransferHeader errorHeader;
    strcpy(errorHeader.filePath, "ERROR");
    errorHeader.fileSize = -1;
    
    //Send it to the client
    if(send(clientSocketFd, &errorHeader, sizeof(errorHeader), 0) != sizeof(errorHeader)) {
        perror("Error Sending error header to Client");
    }

}

/**
 * Parse the server_loads file, and add on this request to the file
 *
 *	@param	serverId	The server id who this next request is for
 *	@param	fileSize	The file size that we're requesting from the server
 */
void LoadBalancer::updateServerLoads(LoadBalancer * This, int serverId, int fileSize){

	// If this is a typical update to the file, and not the first time
	if((This->requestNumber) > 0) {

		// Increment the global request counter
		(This->requestNumber)++;

		// Navigate to the end of the file
		FILE * serverLoadsFile = fopen((This->serverLoadsFilePath).c_str(), "r+");
		char buffer[BUFFERSIZE];
	        while(!feof(serverLoadsFile)) {
		        fgets(buffer, BUFFERSIZE, serverLoadsFile);
		}
	
		// Print out the global request number
		fprintf(serverLoadsFile, "%d", This->requestNumber);

		// Get the state data for server 0
		int previousFileRequests = (This->serverStates)[0][0];
		int previousFileSize = (This->serverStates)[1][0];

		// Output server 0's data
		if(0==serverId) {
			fprintf(serverLoadsFile, "		%d	%d\t", previousFileRequests+1, previousFileSize+fileSize);

			// Update serverStates if server 0 was the one to receive the request
			(This->serverStates)[0][0] = previousFileRequests+1;
			(This->serverStates)[1][0] = previousFileSize+fileSize;
		} else {
			fprintf(serverLoadsFile, "		%d	%d\t", previousFileRequests, previousFileSize);
		}

		// Print out the data for each other server
		for(int index = 1; index < This->numberOfServers; index++){

			// Get the state of this server
			int previousFileRequests = (This->serverStates)[0][index];
			int previousFileSize = (This->serverStates)[1][index];

			// Output this server's data
			if(index==serverId) {
				fprintf(serverLoadsFile, "		%d	%d\t", previousFileRequests+1, previousFileSize+fileSize);

				// Update serverStates if this was the server to receive the request
				(This->serverStates)[0][index] = previousFileRequests+1;
				(This->serverStates)[1][index] = previousFileSize+fileSize;

			} else {
				fprintf(serverLoadsFile, "		%d	%d\t", previousFileRequests, previousFileSize);
			}
		}
		fprintf(serverLoadsFile, "\n");
		fclose(serverLoadsFile);

	// Write the first request to the file
	} else {

		// Update the global request counter
		(This->requestNumber)++;
		
		// Open the file, create it if it does not exist
		FILE * serverLoadsFile = fopen((This->serverLoadsFilePath).c_str(), "w");

		// Print the file header
		fprintf(serverLoadsFile, "Request No. \t");
		fprintf(serverLoadsFile, " Server %d\t", 0);
		for(int index = 1; index < This->numberOfServers; index++){
			fprintf(serverLoadsFile, "\t\t Server %d\t", index);
		}
		fprintf(serverLoadsFile, "\n\n");
		fprintf(serverLoadsFile, "\tRequests  | Total Size		");
		for(int index = 1; index < This->numberOfServers; index++){
			fprintf(serverLoadsFile, "  Requests  | Total Size\t");
		}
		fprintf(serverLoadsFile, "\n");


		// Print out the global request number
		fprintf(serverLoadsFile, "%d", This->requestNumber);

		// Print out the initial loads of all servers
		if(0==serverId) {

			// Initialize serverStates
			(This->serverStates)[0][0] = 1;
			(This->serverStates)[1][0] = fileSize;

			fprintf(serverLoadsFile, "		%d	%d\t", 1, fileSize);

		} else {

			// Initialize serverStates
			(This->serverStates)[0][0] = 0;
			(This->serverStates)[1][0] = 0;

			fprintf(serverLoadsFile, "		%d	%d\t", 0, 0);

		}
		for(int index = 1; index < This->numberOfServers; index++){
			if(index==serverId) {
				fprintf(serverLoadsFile, "		%d	%d\t", 1, fileSize);
				(This->serverStates)[0][index] = 1;
				(This->serverStates)[1][index] = fileSize;
			} else {
				fprintf(serverLoadsFile, "		%d	%d\t", 0, 0);
				// Initialize serverStates
				(This->serverStates)[0][index] = 0;
				(This->serverStates)[1][index] = 0;
			}

			
		}
		fprintf(serverLoadsFile, "\n");
		fclose(serverLoadsFile);
	}
}



/**
 * The 'failureDetectionThread' will run this function. This function will be responsible
 * 	for monitoring the server pool for failures. On failure, it will identify the server that
 * 	has failed, and call 'handleServerFailure' to reconfigure the system
 *
 * 	@param	data	A pointer to an integer representing the file descriptor for the socket
 * 					on which to listen for failure notifications
 */
void* LoadBalancer::monitorServerPool(void* data){

	// Get 'this' out of the data passed, and the actual data
	ThreadFunctionArguments* arguments = static_cast<ThreadFunctionArguments*>(data);
	LoadBalancer* This = arguments->This;
	int socketFd = *((int*) arguments->actualArguments);

	// Data structures
	int newSocketFd = 0;
	struct sockaddr_in newAddressStructure;
	socklen_t newSize = sizeof(struct sockaddr_in);

	while(true) {

		//Block, waiting to accept incoming connection
		if ((newSocketFd = accept(socketFd, (struct sockaddr *)&newAddressStructure, &newSize)) == -1) {
			close(socketFd);
			close(newSocketFd);
			perror("Error Accepting Connection for Failure Notification");
			pthread_exit(NULL);
		}

		// Pull just an integer from the message -- the server id that failed
		int failedServerId, msgLength;
		if((msgLength = recv(newSocketFd, &failedServerId, sizeof(int), 0)) == -1) {
			close(socketFd);
			close(newSocketFd);
			perror("Error Recieving Data from Socket in Failure Detector in Load Balancer");
			pthread_exit(NULL);
		} else if(msgLength>0){
			This->handleServerFailure(failedServerId);
		}

		// Close this socket (don't reuse it)
		close(newSocketFd);
	}
}


/**
 * This function will be called to redistributed the files in the event that one of the
 * 	server processes fails, and to update the data structures to remove this server from
 * 	our records
 *
 * 	@param	failedServerId	The server id of the server from our server pool that has failed
 * 	@return	true on success, false otherwise
 */
bool LoadBalancer::handleServerFailure(int failedServerId){

	cout<<"Load Balancer hears that "<<failedServerId<<" has failed\n";

	int numFiles = 0;
	//count failed files
	map<string, int*>::iterator it;
	for(it=(*fileLocations).begin(); it != (*fileLocations).end(); it++){
		if((*it).second[0] == failedServerId || (*it).second[1] == failedServerId){
			numFiles++;
		} 
	}
	
	//Find Total Number of Bytes passed by Good Servers
	int totalBytes = 0;
	for(int i = 0; i<numberOfServers;i++){
		if(i != failedServerId){
			if(serverLoads[i] <= 0){
				serverLoads[i]=1;
			}
			totalBytes = totalBytes+serverLoads[i];
		}
	}
	//Find avg bytes per good server
	//int avgBytes = totalBytes/numberOfServers;
	//Target Number of Files on each good server
	int goal[numberOfServers];
	//Set the goals based on the inverse of server load
	for(int i =0; i<numberOfServers;i++){
		if(i != failedServerId){
			goal[i] = (int)(numFiles * ((1.0 - (float)serverLoads[i]/(float)totalBytes)/(float)(numberOfServers-1)));
		}else{
			goal[i] = -99999;
		}
	}
	//Now that we know the excepted number of new files per good server we can move the files	
	for(it=(*fileLocations).begin(); it != (*fileLocations).end(); it++){
		//Identify lost files and recopy them
		if((*it).second[0] == failedServerId){
			int alreadyHas = (*it).second[1];
			int newServer = findBestCandidate(goal,numberOfServers,failedServerId,alreadyHas);
			(*it).second[0]=newServer;			
			goal[newServer]--;
			copyFile(alreadyHas,newServer,(*it).first);
			
		} 
		if((*it).second[1] == failedServerId){
			int alreadyHas = (*it).second[0];
			int newServer = findBestCandidate(goal,numberOfServers,failedServerId,alreadyHas);
			(*it).second[1]=newServer;
			goal[newServer]--;
			copyFile(alreadyHas,newServer,(*it).first);
		}
	}
	return true;
}

/**
 * Actually starts the load balancer running. At this point, all threads of computation will
 * 	begin.
 *
 * Note that this function will not return until the entire file system is shutdown (will
 * 	return immediately with 'false' on error)
 *
 * 	@return true on success, false otherwise
 */
bool LoadBalancer::start(){

	//Create attributes
	pthread_attr_t attr;
	pthread_attr_init(&attr);
	pthread_attr_setdetachstate(&attr,PTHREAD_CREATE_JOINABLE);
	
	// Setup socket to listen for client requests & launch this thread (pass in the FD as a parameter)
	int incomingClientRequestFileDescriptor = setupServerSocket(loadBalancerRequestPort);
	if(pthread_create(&requestListenerThread, &attr, &LoadBalancer::listenForClientRequests, new ThreadFunctionArguments(this, (void*) &incomingClientRequestFileDescriptor))<0){
		perror("Error Launching Client Listener Thread from Load Balancer");
		pthread_exit(NULL);
	}
	
	// Setup socket to listen for server failure notifications and launch the thread (pass FD as parameter)
	cout << "Launching failure detection thread on port " << loadBalancerFailurePort << "\n";

	int loadBalancerFailureDetectionFileDescriptor = setupServerSocket(loadBalancerFailurePort);
	if(pthread_create(&failureDetectionThread, &attr, &LoadBalancer::monitorServerPool, new ThreadFunctionArguments(this, (void*) &loadBalancerFailureDetectionFileDescriptor))<0){
		perror("Error Launching Failure Detection Thread from Load Balancer");
		pthread_exit(NULL);
	}

	// Launch thread for fetching files from the queue (no port required, always acts as a client)
	if(pthread_create(&requestResponseThread, &attr, &LoadBalancer::handleFileRequests, new ThreadFunctionArguments(this, (void*) NULL))<0){
		perror("Error Launching Response Thread from Load Balancer");
		pthread_exit(NULL);
	}
	
	// Launch server pool and output 'process_ids' file
	spawnServerPool();

	//Destroy attribute
	pthread_attr_destroy(&attr);
	
	// Sleep, waiting for threads to finish (should never finish unless whole process)
	pthread_join(requestListenerThread, NULL);
	pthread_join(failureDetectionThread, NULL);
	pthread_join(requestResponseThread, NULL);
	return true;
}


/**
 * Helper function to spawn the entire pool of server processes, output PID data to 'process_ids', and update data structures
 */
void LoadBalancer::spawnServerPool(){

	
	// Launch server pool
	for(int index = 0; index < numberOfServers; index++){

		// Build the connection data for this server process
		
		ServerConnectionData serverConnectionData = buildServerConnectionData(index);
		
		// Fork this server process off
		int childPID = fork();

		// Load balancer process
		if(childPID) {

			// Save the PID of the child
			processIds[index] = childPID;
			thisLoadBalancer = this;

		// Child process - begin server process
		} else {
			
			id = index;
			DistributedServer * thisServer = new DistributedServer(serverConnectionData);
			thisDistributedServer = thisServer;
			thisServer->initialize();
			delete thisServer;
			pthread_exit(NULL);
		}
	}
	
	// Output our data to the 'process_ids' file
	FILE * processIdsFile = fopen(processIdsFilePath.c_str(), "w");
	for(int index = 0; index < numberOfServers; index++){
		int serverPID = processIds[index];
		fprintf(processIdsFile, "%d		%d\n", index, serverPID);
	}
	fclose(processIdsFile);
	
}

/**
 * Helper function that tokenizes strings
 */
void LoadBalancer::tokenize(const string & str, vector<string> & tokens, const string & delimiters) {

    // Skip delimiters at beginning.
    string::size_type lastPos = str.find_first_not_of(delimiters, 0);

    // Find first "non-delimiter".
    string::size_type pos     = str.find_first_of(delimiters, lastPos);

    while (string::npos != pos || string::npos != lastPos)
    {
        // Found a token, add it to the vector.
        tokens.push_back(str.substr(lastPos, pos - lastPos));
        // Skip delimiters.  Note the "not_of"goal[newServer]--;
        lastPos = str.find_first_not_of(delimiters, pos);
        // Find next "non-delimiter"
        pos = str.find_first_of(delimiters, lastPos);
    }
}


/**
 * Helper function to read the file map and initialize the data structures (do the basics)
 */
void LoadBalancer::readFileMap(){

	// Open this file map file and check for errors
	FILE * fileInputFile = fopen(fileMapFilePath.c_str(), "r");
	if (fileInputFile != NULL) {

		// Buffer data for reading the file
		vector<string> tokens;
		char buffer[BUFFERSIZE];

		// Read in and parse the number of servers
       		fgets(buffer, BUFFERSIZE, fileInputFile);
        	numberOfServers = atoi(buffer);

        	// Allocate space for process ids and server load arrays
        	serverLoads = new int[numberOfServers];
        	processIds = new int[numberOfServers];

        	// Initialize server loads to 0
        	for(int index = 0; index<numberOfServers; index++) {
        		serverLoads[index] = 0;
        	}

		// Read each line from the file
        	while(!feof(fileInputFile)) {

	        	// Pull out a line from the text file and tokenize it
		        fgets(buffer, BUFFERSIZE, fileInputFile);
		        string stringBuffer = string(buffer);
		        tokenize(stringBuffer, tokens);

		        // Process each token in the line (only three total)
		        string fileName = tokens[0];
		        int serverId = atoi(tokens[1].c_str());
		        int server2Id = atoi(tokens[2].c_str());

		        // Add this data to our hash table
		        int * serverIds = new int[2];
		        serverIds[0] = serverId;
		        serverIds[1] = server2Id;
		        fileLocations->insert(pair<string, int*>(fileName, serverIds));

			// Clear the vector of tokens
			tokens.clear();
	        }

	        // Close the file
	        fclose(fileInputFile);

	} else {
		cout << "Error opening '" << fileMapFilePath << "'!";
	}
}


/**
 * Helper function that builds the server connection data
 */
ServerConnectionData LoadBalancer::buildServerConnectionData(int index) {
	// Build the ServerConnectionData to give to this server process
	ServerConnectionData connectionData;
	connectionData.serverID = index;
	connectionData.loadBalancerFailurePort = loadBalancerFailurePort;
	connectionData.fileRequestPort = loadBalancerFailurePort + (1+index); // File request port will be the failure port +1, +2, ...

	//If we're spawning the first process
	if(index == 0) {

		// Heartbeating port will be failure port +1+numberOfServers, +2+numberOfServers, ...
		connectionData.heartbeatListenerPort = loadBalancerFailurePort + (numberOfServers+1+index); // This heartbeating port
		connectionData.heartbeatSendToPort = loadBalancerFailurePort + (numberOfServers+1+(index+1));
		connectionData.idOfHeartbeatSender = numberOfServers-1;

	// If we're spawning the last process
	} else if(index==numberOfServers-1) {

		// Heartbeating port will be failure port +1+numberOfServers, +2+numberOfServers, ...
		connectionData.heartbeatListenerPort = loadBalancerFailurePort + (numberOfServers+1+index); // This heartbeating port
		connectionData.heartbeatSendToPort = loadBalancerFailurePort + (numberOfServers+1); // 'Next' process's is index=0
		connectionData.idOfHeartbeatSender = index-1;

	// If it's anywhere in between
	} else {

		// Heartbeating port will be failure port +1+numberOfServers, +2+numberOfServers, ...
		connectionData.heartbeatListenerPort = loadBalancerFailurePort + (numberOfServers+1+index); // This heartbeating port
		connectionData.heartbeatSendToPort = loadBalancerFailurePort + (numberOfServers+1+(index+1)); // Next process's heartbeating port
		connectionData.idOfHeartbeatSender = index-1;
	}
	return connectionData;
}


/**
 * Function to setup a generic server socker
 *
 * 	@param	port	The port to attempt to open
 *
 * 	@return	The file descriptor for the server socket on success, -1 on failure
 */
int LoadBalancer::setupServerSocket(int port) {

	//Create the server socket
	int serverSocketFD, dummy = 1;
	if ((serverSocketFD = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
		perror("Error Creating Server Socket");
		return -1;
	}

	//Make the socket reusable
	if (setsockopt(serverSocketFD, SOL_SOCKET, SO_REUSEADDR, (char *)&dummy, sizeof(dummy)) == -1) {
		close(serverSocketFD);
		perror("Error Setting Server Socket Options");
		return -1;
	}

	//Initialize the socket structure
	struct sockaddr_in mainSocketStructure;
	mainSocketStructure.sin_family = AF_INET;
	mainSocketStructure.sin_port = htons(port);
	mainSocketStructure.sin_addr.s_addr = inet_addr(MYIP);

	//Bind the socket to the given port
	if (bind(serverSocketFD, (struct sockaddr *)&mainSocketStructure, sizeof (mainSocketStructure)) != 0) {
		close(serverSocketFD);
		perror("Error Binding Server Socket to Port");
		return -1;
	}

	//Listen on our new socket for incoming connections
	if (listen(serverSocketFD, BACKLOG) != 0) {
		close(serverSocketFD);
		perror("Error Listening on Server Socket");
		return -1;
	}

	// Return the file descriptor for the socket that we bound to this port, -1 on failure
	return serverSocketFD;
}


/**
 * Function to setup a generic client socket, and return its file descriptor
 *
 * 	@return	the file descriptor for the client socket on success, -1 on failure
 */
int LoadBalancer::setupClientSocket() {

	//Create a new socket for our 'client'
	int clientSocketFD, dummy;
	if ((clientSocketFD = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
		perror("Error Creating Client Socket");
		return -1;
	}

	//Make the socket reusable
	if (setsockopt(clientSocketFD, SOL_SOCKET, SO_REUSEADDR, (char *)&dummy, sizeof(dummy)) == -1) {
		close(clientSocketFD);
		perror("Error Setting Client Socket Options");
		return -1;
	}

	// Return the FD
	return clientSocketFD;
}

/**
 * Helper function to find the 'best' server while redistributing the files after a server failure.
 * 	This uses the 'goal' data to find the server we should redistribute the files to
 */
int LoadBalancer::findBestCandidate(int*goals,int numServers,int banned,int banned2){
	int bestValue = INT_MIN;
	int bestIndex = -1;
	for(int i=0;i<numServers;i++){
		if(goals[i] > bestValue && i != banned && i !=banned2){
			bestValue = goals[i];
			bestIndex = i;
		}
	}
	return bestIndex;
}

/**
 * Helper function to copy a file from one servers folder to another
 */
void LoadBalancer::copyFile(int sourc, int dest, string fileName){

	// The base folder name
	string baseFolder = "server_";

	// Build the paths
	std::ostringstream  sourcString,destString;
	sourcString << sourc;
	destString << dest;
	string sourcFile = baseFolder + sourcString.str()+"/" + fileName;
	string destFile = baseFolder + destString.str()+"/" + fileName;

	// Output that we're copying this file
	cout<<"Copying "<<sourcFile<<" to "<<destFile<<endl;

	// Copy the file
	ifstream inputStream(sourcFile.c_str(), fstream::binary);
  	ofstream outputStream(destFile.c_str(), fstream::trunc|fstream::binary);
  	outputStream << inputStream.rdbuf();
}
