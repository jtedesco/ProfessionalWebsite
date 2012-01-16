/**
 * This class represents the standard DSM node that will exist in the pool of processes
 *  at the heart of this application. This node contains some memory data, connection 
 * data for all other nodes, and other essential DSM data structures.
 * 
 * This file holds the implementation of this class.
 *
 *
 *  Created on: April 19, 2011
 *      Author: Jon
 */

#include "DSM.h"
#include "NodeData.h"
#include "Command.h"
#include "Memory.h"

#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <map>
#include <string.h>
#include <sstream>


#define MAX_RECONNECT_ATTEMPTS  10
#define BUFFER_SIZE             1024
#define BACKLOG                 100

/**
 * The primary constructor for the DSM node, builds a DSM node given the NodeData struct representing 
 *  this node, a map of node ids to port numbers, a map of memory ids to node ids, and a map of memory
 *  ids to data entries.
 * 
 *  @param  nodeData                Contains the basic data for this node, i.e. its id and port
 *  @param  nodeCommunicationData   Contains ports on which to connect to any node (indexed by node id)
 *  @param  memoryNodeIdIndex       Contains node ids that have a given memory id (indexed by memory id)
 *  @param  nodeMemoryData          Contains all memory data that this node holds (indexed by memory id)
 *  @param  nodeIds                 Contains a list of all node ids in the system
 */
DSM::DSM(NodeData & nodeData, map<int, int> & nodeCommunicationData, map<int, int> & memoryNodeIdIndex, 
        map<int, unsigned char> & nodeMemoryData, vector<int> & nodeIds) : nodeCommunicationData(nodeCommunicationData),
        memoryNodeIdIndex(memoryNodeIdIndex), nodeIds(nodeIds), id(nodeData.nodeId),
        port(nodeData.nodePortNumber), nodeMemoryData(nodeMemoryData) {
    hasLock = false;
    addMsgCount = 0;
    
}

/**
 * Frees all memory associated with this node
 */
DSM::~DSM() {
    // Nothing to do here
}

/**
 * Starts the DSM node running, and listens for commands.
 */
void DSM::start() {
	//Setup Listening port
	initializeIncomingPort();
	//Listen for commands
	listenForCommands();
}

/**
 * Helper function to create a socket, bind it to this node's port, and start listening on
 *  it.
 * 
 * @return true on success, false otherwise
 */
bool DSM::initializeIncomingPort() {
    
    // Initialize some variables
    char fingerPortString[BUFFER_SIZE];
    sprintf(fingerPortString, "%d", port);
    struct addrinfo hints, *servinfo, *p;
    int yes=1;
    int returnValue;
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // use my IP

    // Try to get connection info for this port
    if ((returnValue = getaddrinfo(NULL, fingerPortString, &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(returnValue));
        return false;
    }

    // Loop through all the results and bind to the first we can
    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((socketFd = socket(p->ai_family, p->ai_socktype,
                p->ai_protocol)) == -1) {
            perror("Failed to create socket!");
            continue;
        }

        if (setsockopt(socketFd, SOL_SOCKET, SO_REUSEADDR, &yes,
                sizeof(int)) == -1) {
            perror("Failed to set options on the new socket!");
            return false;
        }

        if (bind(socketFd, p->ai_addr, p->ai_addrlen) == -1) {
            close(socketFd);
            perror("Failed to bind new socket to desired port!");
            continue;
        }
        break;
    }

    // If we really failed to connect, fail
    if (p == NULL)  {
        return false;
    }
    freeaddrinfo(servinfo);

    // Try to start listening, fail if that doesn't work
    if (listen(socketFd, BACKLOG) == -1) {
        perror("Failed to listen!");
        return false;
    }
    // Otherwise, assume success
    return true;
}

/**
 * Loops, listening for commands, and on receipt of a command, calls the 'handleCommand'
 *  function
 */
void DSM::listenForCommands() {

    struct sockaddr_storage their_addr; // connector's address information

    socklen_t sin_size;
    int newFd;

    sin_size = sizeof their_addr;

    while(true)
    {  // main accept() loop
        newFd = accept(socketFd, (struct sockaddr *)&their_addr, &sin_size);
        if (newFd == -1) {
            perror("accept");
            break;
        }

        int numbytes = 0;
        Command incomingCommand;
        // read in command - print error and close fd on failure
        if ((numbytes = recv(newFd, &incomingCommand, sizeof(incomingCommand), 0)) == -1)
        {
            perror("recv");
            close(newFd);
        }

        if(canExecuteCommand(incomingCommand.commandCode))
        {
            if(!handleCommand(incomingCommand, newFd))
            {
                cout << "Unable to handle command" << endl;
            }
        }
        else
        {
            unprocessedCommands.push(incomingCommand);
            close(newFd);
        }

    }

}

// return true if the command can be executed
bool DSM::canExecuteCommand(CommandType type)
{
    switch(type)
    {
        case LOCK:
            return true;

        case UNLOCK:
            break;

        case ADD:
            break;

        case PRINT:
            break;

        case READ:
            return true;

        case WRITE:
            return true;

        case INITIALIZE:
            return true;

        case LOCK_ACQUIRED:
            return true;
            
        default:
            // the command shouldn't have gotten to this function
            return false;
    }

    // commands falling out of the switch should execute if the lock has been acquired
    if(hasLock)
    {
        return true;
    }
    return false;
}

/**
 * Handles a generic input command, and delegates the work to one of the helper functions.
 *
 *  @param  command         The command we're handling
 *  @param  responseSocket  The socket file descriptor on which to respond to this command
 * 
 *  @return true on success, false otherwise
 */
bool DSM::handleCommand(Command & command, int responseSocket) {

    bool wasSuccess;
    // Switch on the command code, and call the appropriate method
    switch(command.commandCode)
    {
        
        case LOCK:
            close(responseSocket);
            return handleLock(command);
            
        case UNLOCK:
            close(responseSocket);
            return handleUnlock(command);
            
        case ADD:
            close(responseSocket);
            return handleAdd(command.memoryLocationIds, command.sum);
            
        case PRINT:
            close(responseSocket);
            return handlePrint(command.memoryLocation);
            
        case READ:
            return handleRead(responseSocket, command.memoryLocation);
            
        case WRITE:
            wasSuccess = handleWrite(command);
            close(responseSocket);
            return wasSuccess;
            
        case INITIALIZE:
            addMsgCount++;
            //cout << "Total Msg Count: " << addMsgCount << " " << id << endl;
            coordinatorPort = command.coordinatorPort;
            return true;

        case LOCK_ACQUIRED:
            close(responseSocket);
            return handleLockAcquired();

        default:
            
            // We failed to determine the command type!
            return false;
    }
}

/**
 * Handles a LOCK commands, maintaining release consistency. This function is responsible
 *  for cleaning up all connections, and completely handling a LOCK command.
 * 
 *  @param  nodeId          The node id of the node that wants to enter the critical section
 */
bool DSM::handleLock(Command command)
{
    addMsgCount+=2;
    cout << "Node " << this->id << " requested the lock" << endl;
    return sendCoordinatorCommand(command);
}


/**
 * Handles a UNLOCK commands, maintaining release consistency. This function is responsible
 *  for cleaning up all connections, and completely handling an UNLOCK command.
 */
bool DSM::handleUnlock(Command command)
{
    cout << "Node " << this->id << " released the lock" << endl;
    bool wasSuccess;
    wasSuccess = writeBackCache();
    
    if(!sendCoordinatorCommand(command))
    {
        return false;
    }
    this->hasLock = false;
    return wasSuccess;
}

bool DSM::writeBackCache()
{
    addMsgCount+=2;
    map<int, Memory>::iterator pos;
    Command writeCommand;
    writeCommand.commandCode = WRITE;

    bool wasSuccess = true;
    int sendFd;
    for (pos = cache.begin(); pos != cache.end(); pos++)
    {
        if(pos->second.isDirty)
        {
            addMsgCount++;
            writeCommand.memoryLocation = pos->first;
            writeCommand.writeData = pos->second.value;

            connectTo(sendFd, nodeCommunicationData[memoryNodeIdIndex[pos->first]]);
            if (send(sendFd, &writeCommand, sizeof(writeCommand), 0) == -1)
            {
                perror("send");
                wasSuccess = false;
            }

            recv(sendFd, NULL, 1, 0);
            close(sendFd);
        }
    }
    cache.clear();
    return wasSuccess;
}

bool DSM::handleLockAcquired()
{
    addMsgCount++;
    cout << "Node " << this->id << " acquired the lock"<< endl;
    this->hasLock = true;
    //cout << "Total Msg Count: " << addMsgCount << " " << id << endl;
    return processQueuedCommands();
}

bool DSM::processQueuedCommands()
{
    Command command;
    bool wasSuccess = true;
    unsigned int size = unprocessedCommands.size();
    for(unsigned int count = 0; count < size; count++)
    {
        command = unprocessedCommands.front();
        if(this->hasLock)
        {
            switch(command.commandCode)
            {
                case UNLOCK:
                    wasSuccess = handleUnlock(command);
                    break;

                case ADD:
                    wasSuccess = handleAdd(command.memoryLocationIds, command.sum);
                    //cout << "Unbuffering Add Command " << id << endl;
                    break;

                case PRINT:
                    wasSuccess = handlePrint(command.memoryLocation);
                    break;

                default:
                    wasSuccess = false;
            }
        }
        else
        { // an unlock command was processed
            break;
        }
        unprocessedCommands.pop();
    }

    return wasSuccess;
}

bool DSM::sendCoordinatorCommand(Command command)
{
    int coordinatorFd;
    if(connectTo(coordinatorFd, this->coordinatorPort))
    {
        if (send(coordinatorFd, (void*)&command, sizeof(command), 0) == -1)
        {
            perror("send");
            return false;
        }
        close(coordinatorFd);
        return true;
    }
    return false;
}

bool DSM::connectTo(int & coordinatorFd, int port)
{
    stringstream ss;
    ss << port;
    string strPort = ss.str();

    struct addrinfo hints, *servinfo, *p;
    int rv;

    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;

    if ((rv = getaddrinfo("127.0.0.1", strPort.c_str(), &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        return false;
    }

    // loop through all the results and connect to the first we can
    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((coordinatorFd = socket(p->ai_family, p->ai_socktype,
                p->ai_protocol)) == -1) {
            perror("client: socket");
            continue;
        }

        if (connect(coordinatorFd, p->ai_addr, p->ai_addrlen) == -1) {
            close(coordinatorFd);
            perror("client: connect");
            continue;
        }

        break;
    }

    if (p == NULL) {
        fprintf(stderr, "client: failed to connect\n");
        return false;
    }

    freeaddrinfo(servinfo); // all done with this structure

    return true;
}

/**
 * Handles a ADD commands. This function is responsible for cleaning up all connections, 
 * and completely handling an ADD command.
 * 
 *  @param  responseSocket  The socket file descriptor on which to respond to this command
 *  @param  memoryLocationIds       The list of memory locations we want to add as described
 *                                  in the 'Command' documentation.
 */
bool DSM::handleAdd(int * memoryLocationIds, int literal) {

    addMsgCount++;
    cout << "Node " << this->id << " handled ADD command"<< endl;
    int j=0;
    int size =0;
    
    //initialize sum to the literal at the end
    unsigned char sum = literal;
    
    //find how many memory locations i have to read
    while(memoryLocationIds[j] != -1){
            map<int,int>::iterator iter = memoryNodeIdIndex.find(memoryLocationIds[j]);
            //Address was not valid
            if(iter == memoryNodeIdIndex.end()){	
                    cout<<"Invalid Memory Address - Add"<<endl;
                    return false;
            }
            size++;
            j++;
    }

    //Sum over the values in memory
    for(int i=2; i<size; i++){
            sum = sum + getMemoryLocation(memoryLocationIds[i]);
    }

    //Write value to cache to be written later
    int writeLoc = memoryLocationIds[1];
    map<int,int>::iterator ownerNode = memoryNodeIdIndex.find(writeLoc);
    //I am the owner
    if(ownerNode->second == id){
            map<int,unsigned char>::iterator data = nodeMemoryData.find(writeLoc);
            data->second = sum;
            return true;
    }

    //Is that loc already in our cache?
    map<int,Memory>::iterator it = cache.find(writeLoc);
    //Erase it if it is
    if(it != cache.end()){
            cache.erase(writeLoc);	
    }

    //Now add/readd to cache and mark it dirty
    Memory mem;
    mem.value = sum;
    mem.isDirty = true;
    cache.insert(pair<int,Memory>(writeLoc,mem));

    //cout << "Total Msg Count: " << addMsgCount << " " << id << endl;
    return true;
}

/**
 * Handles a PRINT commands. This function is responsible for cleaning up all connections, 
 * and completely handling a PRINT command.
 * 
 *  @param  responseSocket  The socket file descriptor on which to respond to this command
 *  @param  memoryLocation        The memory location id to print
 */
bool DSM::handlePrint(int memoryLocation) {
    
    //Check whose is supposed to have it
    addMsgCount++;
    map<int,int>::iterator iter = memoryNodeIdIndex.find(memoryLocation);
        
    //Address was not valid
    if(iter == memoryNodeIdIndex.end()){	
            cout<<"Invalid Memory Address - print"<< memoryLocation << endl;
            return false;
    }

    unsigned char printVal = getMemoryLocation(memoryLocation);
    cout << "The value " << (int)printVal << " is stored in memory address " << memoryLocation << " at node " << id << endl;

    return true;
}
/**
 *helper function that will the value of a given memory location either in cache
 *,your memory, or in someelse's memory
 *
 *@param memoryLocation        The memory location id to read
 */
unsigned char DSM::getMemoryLocation(int memoryLocation){
	//Now we need to find the value stored a given the memory location	
	//check cache;
	map<int,Memory>::iterator it = cache.find(memoryLocation);
	if(it != cache.end()){
		return it->second.value;	
	}
	
	//Check whose is supposed to have it
	map<int,int>::iterator iter = memoryNodeIdIndex.find(memoryLocation);
	//Address was not valid
	if(iter == memoryNodeIdIndex.end()){	
		cout<<"Invalid Memory Address - getMem"<<endl;
		return 0;
	}
	
	//Somebody has it
	if(iter->second == id){
		//I have it now return it
		map<int,unsigned char>::iterator data = nodeMemoryData.find(memoryLocation);
		return data->second;
	}
	
	//Need to read it from someone else
	Memory readData = performRead(memoryLocation,iter->second);
        addMsgCount+=2;
	//Got data now cache it and print it
	cache.insert(pair<int,Memory>(memoryLocation,readData));
	return readData.value;

}  
/**
 *helper function that will send a read command to another node then wait for response then return the data
 *
 *@param memoryLocation        The memory location id to read
 *@param targetNodeId          The node id of where to send the read
 */
Memory DSM::performRead(int memoryLocation,int targetNodeId) {
	Memory data;
	data.value = -1;
	data.isDirty = false;

	//Create Read Command
	Command command;
	command.commandCode = READ;
	command.memoryLocation = memoryLocation;

	//Send a read command to other node
	int dummy;
	char errorMessage[256];
    int clientSocketFd;

    if ((clientSocketFd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
    	sprintf(errorMessage, "Error creating client socket to connect to finger of node %d!", targetNodeId);
       	cout<<errorMessage<<endl;
     	return data;
    }

    // Make the socket reusable
    if (setsockopt(clientSocketFd, SOL_SOCKET, SO_REUSEADDR, (char *)&dummy, sizeof(dummy)) == -1) {
        close(clientSocketFd);
        sprintf(errorMessage, "Error setting client socket options for connecting to the finger of node %d!", targetNodeId);
        cout<<errorMessage<<endl;
     	return data;
    }

    // Get the information for connecting to the other node
    struct addrinfo hostInfo, * myInfo = NULL;
    char fingerPortString[256];
    int targetPort = nodeCommunicationData[targetNodeId];
    sprintf(fingerPortString, "%d", targetPort);
    memset(&hostInfo, 0, sizeof(hostInfo));
	hostInfo.ai_family = AF_INET;
    hostInfo.ai_socktype = SOCK_STREAM;
    int tmp;
    if ((tmp = getaddrinfo("localhost", fingerPortString, &hostInfo, &myInfo)) != 0) {
     	sprintf(errorMessage, "Error using 'getaddrinfo()': %s", gai_strerror(tmp));
		cout<<errorMessage<<endl;
     	return data;
    }

    // Try to connect to the target node
    if (connect(clientSocketFd, myInfo->ai_addr, myInfo->ai_addrlen) != 0) {
     	close(clientSocketFd);
		cout<<"Perform Read Connect Failed!"<<endl;
        return data;
    }

    // Send this command to target node
    if (sizeof(command) != send(clientSocketFd, &command, sizeof(command), 0)) {
    	close(clientSocketFd);
        cout<<"Perform Read Send Failed!"<<endl;
		return data;
	}
	
	
	//Receive the reponse
	int numbytes = 0;
    Command incomingCommand;
    // read in command - print error and close fd on failure
    if ((numbytes = recv(clientSocketFd, &incomingCommand, sizeof(incomingCommand), 0)) == -1)
    {
        cout<<"Perform Read Recieve Failed!"<<endl;
        close(clientSocketFd);
		return data;
    }

	data.value = incomingCommand.sum;

	close(clientSocketFd);
	return data;
}

/**
 * Handles a READ commands. This function is responsible for cleaning up all connections, 
 * and completely handling a READ command.
 * 
 *  @param  responseSocket  The socket file descriptor on which to respond to this command
 *  @param  memoryLocation  The id of the memory location to read from
 */
bool DSM::handleRead(int responseSocket, int memoryLocation) {
	unsigned char memValue = nodeMemoryData[memoryLocation];

	Command command;
	command.sum = memValue;
	// Send reponse back to inquier
    if (sizeof(command) != send(responseSocket, &command, sizeof(command), 0)) {
            close(responseSocket);
			cout<<"Sender Hung Up"<<endl;
            return false;
    }
	close(responseSocket);
    return true;
}

/**
 * Handles a WRITE commands. This function is responsible for cleaning up all connections, 
 * and completely handling a WRITE command.
 * 
 *  @param  responseSocket  The socket file descriptor on which to respond to this command
 *  @param  memoryLocation  The id of the memory location to write to
 */
bool DSM::handleWrite(Command command)
{
    if(nodeMemoryData.find(command.memoryLocation) != nodeMemoryData.end())
    {
        nodeMemoryData[command.memoryLocation] = command.writeData;
        return true;
    }
    return false;
}


/**
 * Sends a given command to a specified node id, using the connection data structures for
 *  this node.
 * 
 *  @param  command       The command to send
 *  @param  targetNodeId  The id to send this command to
 *  @return true on success, false otherwise
 */
bool DSM::sendCommand(Command & command, int targetNodeId) {
    
    // Try to connect up to a certain number of times, and then permanently fail
    bool successful = false;
    int attemptCount = 0;
    char errorMessage[256];
    int clientSocketFd;
    while(!successful && attemptCount <= MAX_RECONNECT_ATTEMPTS)
    {

        // Update attempt counter, and if we were not successful, pause for an instant to wait for the node
        attemptCount++;
        if(!successful) {
            usleep(500);
        }

        // Create a new socket for our 'client'
        int dummy;
        if ((clientSocketFd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
            sprintf(errorMessage, "Error creating client socket to connect to finger of node %d!", targetNodeId);
            successful = false;
            continue;
        }

        // Make the socket reusable
        if (setsockopt(clientSocketFd, SOL_SOCKET, SO_REUSEADDR, (char *)&dummy, sizeof(dummy)) == -1) {
            close(clientSocketFd);
            sprintf(errorMessage, "Error setting client socket options for connecting to the finger of node %d!", targetNodeId);
            successful = false;
            continue;
        }

        // Get the information for connecting to the other node
        struct addrinfo hostInfo, * myInfo = NULL;
        char fingerPortString[256];
        int targetPort = nodeCommunicationData[targetNodeId];
        sprintf(fingerPortString, "%d", targetPort);
        memset(&hostInfo, 0, sizeof(hostInfo));
        hostInfo.ai_family = AF_INET;
        hostInfo.ai_socktype = SOCK_STREAM;
        int tmp;
        if ((tmp = getaddrinfo("127.0.0.1", fingerPortString, &hostInfo, &myInfo)) != 0) {
            sprintf(errorMessage, "Error using 'getaddrinfo()': %s", gai_strerror(tmp));
            successful = false;
            continue;
        }

        // Try to connect to the finger
        if (connect(clientSocketFd, myInfo->ai_addr, myInfo->ai_addrlen) != 0) {
            close(clientSocketFd);
            successful = false;
            continue;
         }

        // Send this command to this node's finger
        if (sizeof(command) != send(clientSocketFd, &command, sizeof(command), 0)) {
            close(clientSocketFd);
            successful = false;
            continue;
        }
        // Else, success
        successful = true;
        close(clientSocketFd);

    }

    if(!successful) {
        close(clientSocketFd);
    }
    // Return whether or not we were successful
    return successful;
}

/**
 * Helper function to print all of the data for this DSM with nice, clean formatting.
 */
void DSM::printDebugInformation() {
    cout << "Coordinator starting!" << endl;
    cout << "Node ids:" << endl;
    for(unsigned int i=0; i<nodeIds.size(); i++) {
        cout << "       Node Id " <<  nodeIds[i] << " exists." << endl;
    }
    cout << endl << "Node communication data:" << endl;
    for(unsigned int i=0; i<nodeIds.size(); i++) {
        cout << "       Node " << nodeIds[i] << " can be contacted on port " << 
                nodeCommunicationData[nodeIds[i]] << "!" << endl;
    }
    cout << endl << "Memory map data:" << endl;
    for(unsigned int i=0; i<memoryNodeIdIndex.size(); i++) {
        cout << "       Memory location " << i << " is stored at node " << 
                memoryNodeIdIndex[i] << "!" << endl;
    }    
}
