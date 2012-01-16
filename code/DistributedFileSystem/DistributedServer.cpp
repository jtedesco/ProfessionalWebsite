/*
 * DistributedServer.cpp
 *
 *  Created on: Feb 13, 2011
 */

#include "DistributedServer.h"

#include <iostream>
#include <pthread.h>
#include <stdio.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/sendfile.h>
#include <netdb.h>
#include <sstream>
#include <string.h>
#include <string>
#include <fcntl.h>
#include "FileTransferHeader.h"
using namespace std;

//Declaring the Function in Special Way for threads
extern "C" {
 int serverOkMsg = -10;

/**
 * The 'hearbeatListenerThread' will run in this function, listening for heartbeats
 * 	from another process on the port provided
 *
 * 	@param	data	Any data that this thread needs (required by POSIX library)
 */
void*  listenForHeartbeats(void* data){

	//Pull Out data from the parameter
	unsigned int* rawData = (unsigned int*)data;
	std::ostringstream  Lport,Fport;
	unsigned int sendID = rawData[0];
 	Lport << rawData[1];
	Fport << rawData[2];

	//Setup the Socket
	struct addrinfo hints, *res;
	int sockfd, new_fd;
	
	// first, load up address structs with getaddrinfo():
	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_UNSPEC;  // use IPv4 or IPv6, whichever
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = AI_PASSIVE;     // fill in my IP for me
	int error = getaddrinfo(NULL, Lport.str().c_str() , &hints, &res);
	if(error != 0){
		perror(gai_strerror(error));
		pthread_exit(NULL);
	}
	
	// Create a socket for listening for heartbeats
	sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
	if(sockfd == -1){
		perror("socket failed!");
		pthread_exit(NULL);
	}

	// Bind this port to the port assigned to this server for heartbeat listening
	if(bind(sockfd, res->ai_addr, res->ai_addrlen)==-1){
		perror("bind failed!");
		close(sockfd);
		pthread_exit(NULL);
	}
	
	//Listen
	if(listen(sockfd,1)==-1){
		perror("listen failed!");
		close(sockfd);
		pthread_exit(NULL);
	}

	// 'accept' structures
	struct sockaddr_storage their_addr;
	socklen_t addr_size = sizeof their_addr;

	// Loop, connecting with servers for heartbeating if another dies
	while(true){

		// Accept an incoming connection to setup the ring
		new_fd = accept(sockfd, (struct sockaddr *)&their_addr, &addr_size);
		if(new_fd== -1){
			perror("Failed to create heartbeat connection");
			close(sockfd);
			pthread_exit(NULL);
		}

		// Setup more structures and let the user know that this connection was successful
		cout<<"Heartbeat Connection Made"<<endl;
		int flags = fcntl(new_fd, F_GETFL, 0);
		fcntl(new_fd, F_SETFL, flags | O_NONBLOCK);
		int status = 1;	//The server is alive
		int misses = 0;	//The server has not missed any heartbeats
		int* msg = new int();

		// While the server we're listening to has not died
		while(status != 0){

			// Try to grab the heartbeat, don't block
			status = recv(new_fd,msg,sizeof(int),0);

			// If we received a heartbeat
			if(status != -1 && status != 0){

				// We received the right data
				if(*msg == serverOkMsg){
					misses = 0;
				}

			// No message received
			}else{

				// Tally this miss
				misses++;

				// If we haven't heard from him three times, consider him dead
				if(misses >= 2){
					status = 0;	
					continue;
				}
			}

			// Wait one second before checking again
			sleep(1);
		}

		// Process Failed
		cout<<"Server "<<sendID<<" has failed\n";
		close(new_fd);
		const char* servPort = Fport.str().c_str();
		struct addrinfo hints, *servinfo, *p;

		// Setup data structures to contact load balancer
		int rv, sockfd;
		memset(&hints, 0, sizeof hints);
		hints.ai_family = AF_UNSPEC;
		hints.ai_socktype = SOCK_STREAM;

		// Resolve address
		if ((rv = getaddrinfo("127.0.0.1", servPort, &hints, &servinfo)) != 0) {
			fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
			pthread_exit(NULL);
		}

		// Connect to the first we can (suggested from Beej's guide)
		for(p = servinfo; p != NULL; p = p->ai_next) {
			if ((sockfd = socket(p->ai_family, p->ai_socktype, p->ai_protocol)) == -1) {
				perror("client: socket");
				continue;
			}
			if (connect(sockfd, p->ai_addr, p->ai_addrlen) == -1) {
				close(sockfd);
				continue;
			}
			break;
		}

		// Send the process failed message (just the server id that failed)
		unsigned int* failmsg = new unsigned int(sendID);
		send(sockfd,failmsg,sizeof(int),0);

		// Close our sending socket
		close(sockfd);
	}

	// If this ever breaks out, shut down
	close(sockfd);
	pthread_exit(NULL);
}

/**
 * The 'heartbeatSenderThread' will run in this function, sending heartbeats periodically
 * 	to another process on the port provided
 *
 * 	@param	data	Any data that this thread needs (required by POSIX library)
 */
void* sendHeartbeats(void* data){

	//Pull Out data
	unsigned int* rawData = (unsigned int*)data;
	std::ostringstream  port;
 	port << rawData[0];

	//Setup the Socket
	struct addrinfo hints, *res;
	int sockfd;
	
	// Setup structures for connection
	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_UNSPEC;  // use IPv4 or IPv6, whichever
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = AI_PASSIVE;     // fill in my IP for me
	if(getaddrinfo(NULL, port.str().c_str() , &hints, &res) != 0){
		perror("getaddrinfo failed!");
		pthread_exit(NULL);
	}
	
	// Make a client socket to connect
	sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
	if(sockfd == -1){
		perror("socket failed!");
		pthread_exit(NULL);
	}

	//Pause for servers to get ready
	sleep(2);

	//Connect to partner in ring
	if(connect(sockfd, res->ai_addr, res->ai_addrlen)== -1){
		perror("connect failed!");
		close(sockfd);
		pthread_exit(NULL);
	}

	//Send HeartBeats every second
	int* msg = new int(serverOkMsg);
	while(true){
		if(send(sockfd,msg,sizeof(int),MSG_NOSIGNAL)<=0){
			pthread_exit(NULL);
		}
		sleep(1);
	}

	// If this ever breaks, shut down
	close(sockfd);
	pthread_exit(NULL);
}

/**
 * The 'requestHandlerThread' will run in this function, listening for file requests
 * 	from the load balancer on the port provided
 *
 * 	@param	data	Any data that this thread needs (required by POSIX library)
 */
void* listenForFileRequests(void* data){

	//Pull Out data and setup data structures
	unsigned int* rawData = (unsigned int*)data;
	std::ostringstream  Lport,myIDStream;
	myIDStream << rawData[0];
	string myID  = myIDStream.str();
	Lport << rawData[1];
	struct addrinfo hints, *res;
	int sockfd, new_fd;
	
	// first, load up address structs with getaddrinfo():
	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_UNSPEC;  // use IPv4 or IPv6, whichever
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = AI_PASSIVE;     // fill in my IP for me
	int error = getaddrinfo(NULL, Lport.str().c_str() , &hints, &res);
	if(error != 0){
		perror(gai_strerror(error));
		pthread_exit(NULL);
	}
	
	// make a socket:
	sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
	if(sockfd == -1){
		perror("socket failed!");
		pthread_exit(NULL);
	}

	// bind it to the port we passed in to getaddrinfo():
	if(bind(sockfd, res->ai_addr, res->ai_addrlen)==-1){
		perror("bind failed!");
		close(sockfd);
		pthread_exit(NULL);
	}
	
	//Listen
	if(listen(sockfd,1)==-1){
		perror("listen failed!");
		close(sockfd);
		pthread_exit(NULL);
	}

	//Start Accepting File requests
	struct sockaddr_storage their_addr;
	socklen_t addr_size = sizeof their_addr;
	while(true){

		// Block, waiting until we hear a file request
		new_fd = accept(sockfd, (struct sockaddr *)&their_addr, &addr_size);
		if(new_fd== -1){
			perror("acceptfailed!");
			close(sockfd);
			pthread_exit(NULL);
		}

		// Figure out our folder
		string folder = "server_";
		folder = folder + myID;
		char filename[256];
		int size = 1;

		// Receive the file requests
		while(size > 0){

			// Receive a filename from the load balancer
			size = recv(new_fd,&filename,sizeof(filename),0);

			// Check for errors
			if(size != -1 && size != 0){

				// Form the file path
				string file;
				file.assign(filename,size);
				string path = folder+"/"+file;

				// Open the file we will send
				FILE* fileFD = fopen(path.c_str(),"r");

				// Error-check
				if(fileFD != NULL){

					//Find the size of the file
					fseek(fileFD, 0L, SEEK_END);
					int fileSize = ftell(fileFD);
					fseek(fileFD, 0L, SEEK_SET);
					fclose(fileFD);

					// Build the transfer header
					FileTransferHeader *header = new FileTransferHeader();
					strcpy(header->filePath,filename);
					header->fileSize = fileSize;
					
					// Send the header over
					if(send(new_fd,header,sizeof(*header),0) == -1){
						perror("Send Header Failed");
					}

					// Open the file and send it over
					int filefd = open(path.c_str(),O_RDONLY);
					if(sendfile(new_fd,filefd,NULL,fileSize)==-1){
						perror("Send File Failed");
					}

					// Notify the user that we send this file
					cout<<"Server Sent "<<filename<<endl;

					// Cleanup
					delete header;
					close(filefd);

				// If we couldn't find this file
				}else{

					// Sned the error message back to the load balancer
					FileTransferHeader *header = new FileTransferHeader();
					strcpy(header->filePath,"ERROR");
					header->fileSize = 0;
					if(send(new_fd,header,sizeof(*header),0) == -1){
						perror("Send Header Failed");
					}
				} 
			}
		}
			
	}
	close(sockfd);
	pthread_exit(NULL);
}
} //extern

/**
 * Constructs a server object, given the relevant data for connecting to the
 * 	load balancer
 *
 * 	@param	connectionData	The connection data for forming the ring of servers
 * 							and communicating with the load balancer
 * 							(This object will exist on heap, allocated in the load balancer)
 */
DistributedServer::DistributedServer(ServerConnectionData connectionData) {

	//Just Copy the values over
	serverID = connectionData.serverID;
	loadBalancerFailurePort = connectionData.loadBalancerFailurePort;
	heartbeatListenerPort = connectionData.heartbeatListenerPort;
	heartbeatSendToPort = connectionData.heartbeatSendToPort;
	fileRequestPort = connectionData.fileRequestPort;
	idOfHeartbeatSender = connectionData.idOfHeartbeatSender;
	serverOkMsg = -10;
}


/**
 * Frees all memory associated with this server, including the connection
 * 	data passed into the constructor
 */
DistributedServer::~DistributedServer(){
	// Everything created on the stack
}


/**
 * This function will initialize the server, launching all threads of this server and
 * 	initiating its portion of the heartbeating ring.
 *
 * Note this function must be entered by the load balancer. Inside this function, this
 * 	class can fork into another process, splitting from the load balancer
 */
void DistributedServer::initialize(){

	// Let the user know that the server is launching
	cout<<"Starting Distributed Server"<<endl;

	//Create attributes
	pthread_attr_t attr;
	pthread_attr_init(&attr);
	pthread_attr_setdetachstate(&attr,PTHREAD_CREATE_DETACHED);
	
	//Create Args and Launch the three threads
	//Heartbeat Listener
	unsigned int* listenerArgs = new unsigned int[3];
	listenerArgs[0] = idOfHeartbeatSender;
	listenerArgs[1] = heartbeatListenerPort;
	listenerArgs[2] = loadBalancerFailurePort;

	// Launch the heartbeat listener
	if(pthread_create(&heartbeatListenerThread, &attr, &listenForHeartbeats, (void *)listenerArgs)<0){
		perror("Error Launching Heartbeat Listener Thread from Server");
		pthread_exit(NULL);
	}
	
	//Heartbeat Sender
	unsigned int* senderArgs = new unsigned int[1];
	senderArgs[0] = heartbeatSendToPort;
	
	// Launch the heartbeating thread
	if(pthread_create(&heartbeatSenderThread, &attr, &sendHeartbeats, (void *)senderArgs)<0){
		perror("Error Launching Heartbeat Sender Thread from Server");
		pthread_exit(NULL);
	}
	
	//File Request Server
	unsigned int* fileArgs = new unsigned int[2];
	fileArgs[0] = serverID;
	fileArgs[1] = fileRequestPort;

	// Launch the file request listener
	if(pthread_create(&requestHandlerThread, &attr, &listenForFileRequests, (void *)fileArgs)<0){
		perror("Error Launching File Request Handler Thread from Server");
		pthread_exit(NULL);
	}
}

