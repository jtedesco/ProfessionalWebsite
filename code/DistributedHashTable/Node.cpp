/*
 * Represents a generic node in the distributed hash table. Any functions of this class should be
 * 	overridden in the 'Introducer' class which need to be different for the designated 'introducer'
 * 	node.
 *
 *  Created on: Mar 14, 2011
 *      Author: Jon
 */

#include "Node.h"

#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "SHA1/sha1.h"

// amount of incoming connections that can be queued
#define BACKLOG 5
#define MAX_RECONNECT_ATTEMPTS  10


/**
 * Builds a node, given a node data struct, information on its predecessor and successor in the hashtable, and m
 */
Node::Node(NodeData & nodeData, NodeData & successor, NodeData & predecessor, int m) :
        nodePort(nodeData.nodePortNumber), nodeId(nodeData.nodeId), predecessor(predecessor), M(m) {

    // Allocate space for data structures
    fileMap = new map<string, FileData>;
    fingerTable = new vector<NodeData>(m);
    totalMessageCount = 0;

    // Initialize mutex
    pthread_mutex_init(&nodeDataStructuresMutex, NULL);

    // Try to initialize this node's successor, and fail if this fails
    if(!this->initialize(successor)) {
        cout << "Failed to initialize node" << successor.nodeId << "!" << endl;
        pthread_exit(NULL);
    }
}


/**
 * Builds a node, given information about the predecessor, successor, m, and the new file map for this node
 */
Node::Node(NodeData & nodeData,NodeData & successor, NodeData & predecessor, map<string, FileData> * NewFileMap, int m) :
        nodePort(nodeData.nodePortNumber), nodeId(nodeData.nodeId),predecessor(predecessor), M(m) {

    // Allocate space for data structures
    fileMap = NewFileMap;
    fingerTable = new vector<NodeData>(m);
    totalMessageCount = 0;

    // Initialize mutex
    pthread_mutex_init(&nodeDataStructuresMutex, NULL);

    // Try to initialize this node's successor, and fail if this fails
    if(!this->initialize(successor)) {
        cout << "Failed to initialize node" << successor.nodeId << "!" << endl;
        pthread_exit(NULL);
    }
}


/**
 * Frees all memory associated with a Node
 */
Node::~Node(){

    // Cleanup data structures
    pthread_mutex_destroy(&nodeDataStructuresMutex);
    delete fileMap;
    delete fingerTable;
}


/**
 * Builds all data structures, opens listener port, and starts this node running.
 *
 * 	@return	true on success, false otherwise
 */
bool Node::initialize(NodeData & successor){

    // Initialize this node's finger table to just point to this node's successor
    for(int index = 0; index < M; index++) {
        (*fingerTable)[index] = successor;
    }

    // Initialize some variables
    char fingerPortString[BUFFER_SIZE];
    sprintf(fingerPortString, "%d", nodePort);
    struct addrinfo hints, *servinfo, *p;
    int yes=1;
    int returnValue;
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // use my IP

    // Try to get connection info for the
    if ((returnValue = getaddrinfo(NULL, fingerPortString, &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(returnValue));
        return false;
    }

    // Loop through all the results and bind to the first we can
    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((nodeFD = socket(p->ai_family, p->ai_socktype,
                p->ai_protocol)) == -1) {
            perror("Failed to create socket!");
            continue;
        }

        if (setsockopt(nodeFD, SOL_SOCKET, SO_REUSEADDR, &yes,
                sizeof(int)) == -1) {
            perror("Failed to set options on the new socket!");
            return false;
        }

        if (bind(nodeFD, p->ai_addr, p->ai_addrlen) == -1) {
            close(nodeFD);
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
    if (listen(nodeFD, BACKLOG) == -1) {
        perror("Failed to listen!");
        return false;
    }

    // Otherwise, assume success
    return true;
}


/**
 * Starts the node running, and loops, waiting for input
 */
void Node::listenForCommands(){

    // Some data structures
    struct sockaddr_storage their_addr; // connector's address information
    socklen_t sin_size;
    char s[INET6_ADDRSTRLEN];
    sin_size = sizeof their_addr;

    // Listen indefintely for commands
    while(true) {

        // Accept a new command connection
        newFD = accept(nodeFD, (struct sockaddr *)&their_addr, &sin_size);
        if (newFD == -1) {
            perror("accept");
            continue;
        }

        // Initialize this new thread
        pthread_t handleCommandThread;
        pthread_attr_t attr;
        pthread_attr_init(&attr);
        pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);

        // Launch a new detached thread to handle the command
        pthread_create(&handleCommandThread, &attr, handleConcurrentCommand, this);

        // Cleanup after launching the thread
        pthread_attr_destroy(&attr);
        inet_ntop(their_addr.ss_family,
            get_in_addr((struct sockaddr *)&their_addr),
            s, sizeof s);
        sleep(1);
        close(newFD);
    }
}


/**
 * Helper function to handle a new command connection, the entry point of the command handler thread.
 *
 *  @param      The node data for the node handling this new command
 *  @return     Any return data from the thread (Required by POSIX)
 */
void * Node::handleConcurrentCommand(void* nodeData) {

    // Cast the parameter to something useful
    Node * currentNode = (Node *) nodeData;

    // Receive the command from the socket
    int numbytes = 0;
    Command incomingCommand;
    if ((numbytes = recv(currentNode->newFD, &incomingCommand, sizeof(incomingCommand), 0)) == -1) {
        perror("Failed to receive command from socket!");
        close(currentNode->newFD);
        pthread_exit(NULL);
    }

    // Attempt to handle the command, and exit if it fails
    if(!currentNode->handleCommand(incomingCommand)) {
        cout << "Failed to handle command!" << endl;
        close(currentNode->newFD);
        pthread_exit(NULL);
    }

    // Cleanup and exit, we're all done
    close(currentNode->newFD);
    return NULL;
}


/**
 * Helper function to show who is connected
 */
void *Node::get_in_addr(struct sockaddr *sa) {
    if (sa->sa_family == AF_INET) {
        return &(((struct sockaddr_in*)sa)->sin_addr);
    }
    return &(((struct sockaddr_in6*)sa)->sin6_addr);
}


/**
 * Handles a generic input command, and delegates the work to one of the helper functions
 *
 * 	@return	true on success, false otherwise
 */
bool Node::handleCommand(Command & command){

    // The node ID of the next node
    int nextNodeID;

    // Call the appropriate handler function based on the command code
    switch (command.commandCode) {

        case ADD_NODE:
            addNode(command);
            break;

        case ADD_FILE:

            command.messageCount++;

            // Try to add the file to this node, and if it doesn't belong here, forward the command
            if((nextNodeID = addFile(command.fileData)) != -1) {

                // Attempt to forward the command to this node's best guess at who should handle it, and fail if this fails
                if(!forwardCommand(command, nextNodeID)) {
                    return false;
                } else {
                    totalMessageCount++;
                }

            } else {

                // Print out the success message with the total number of messages taken
                cout << "Handled 'ADD_FILE " << command.fileData.filename << "' in " << command.messageCount << " messages" << endl;
            }
            break;


        case DEL_FILE:

            command.messageCount++;

            // Check to see if this file was here, and forward it if it was not
            if((nextNodeID = deleteFile(command.fileData)) != -1) {

                // Attempt to forward the command to this node's best guess at who should handle it, and fail if this fails
                if(!forwardCommand(command, nextNodeID)) {
                    return false;
                } else {
                    totalMessageCount++;
                }
            }
            else {

                // Print out the success message with the total number of messages taken
                cout << "Handled 'DEL_FILE " << command.fileData.filename << "' in " << command.messageCount << " messages" << endl;
            }
            break;

        case FIND_FILE:

            command.messageCount++;

            // Check to see if this file was here, and forward it if it was not
            if((nextNodeID = findFile(command.fileData)) != -1) {

                // Attempt to forward the command to this node's best guess at who should handle it, and fail if this fails
                if(!forwardCommand(command, nextNodeID)) {
                    return false;
                } else {
                    totalMessageCount++;
                }
            }
            else {

                // Print out the success message with the total number of messages taken
                cout << "Handled 'FIND_FILE " << command.fileData.filename << "' in " << command.messageCount << " messages" << endl;
            }
            break;

        case GET_TABLE:

            getTable(command.nodeData);
            break;

	    case QUIT:

            quit(command);
            break;

        default:

            // If we don't recognize this command, fail
            return false;
    }
    return true;
}


/**
 * Handles an ADD_NODE command. (Note that this will be executed once for each add in a multi-add command)
 * 	Specifically, this function will update the finger table of this node to include the new node if necessary,
 * 	and if it preceeds the new node, initialize and create the new node in the ring.
 *
 * First, this function will cause this node to respond to the sender with its own finger table for initializing
 * 	the finger table of the new node.
 * If this node still preceeds the new node, it should forward this command to the node whose id is closest to that of
 * 	the new node (up to and including the node just past the new node's location), and wait for a response, which will
 * 	be the finger table of that next node already in the ring. (This skips ahead in the ring in logarithmic time but
 * 	still skips one past to make sure that the new node's furthest fingers are still initialized).
 * If this node is the node directly preceeding the new node in the ring, it should initialize the new node and add it
 * 	to the ring by updating its own finger table at that of the new node, using the finger table data for its current
 * 	successor for the new node's table. Likewise, if this node directly preceeds the new node, it should output the
 * 	finger table of the new node.
 *
 * 	@param	newNodeData	 The data for the node we are trying to add
 */
bool Node::addNode(Command addCommand){

    addCommand.messageCount++;

    // Grab the new node's data
    NodeData newNodeData = addCommand.nodeData;

    // If the 'new' node is me
    if(nodeId == newNodeData.nodeId){

        // If node already exists, say so, otherwise, handle it and announce success.
        if(addCommand.fileData.filenameHashData != -1){

            // If this node already exists, output a nice error message
      	    cout << "Node already exists! Handled 'ADD_NODE " << newNodeData.nodeId <<"' in " << addCommand.messageCount << " messages" << endl;

        }else{

            // Initialize this node's finger table to be the finger table passed in
	        for(int j=0;j<M;j++){
              (*fingerTable)[j].nodeId = addCommand.addNodeFingerTable[j].nodeId;
              (*fingerTable)[j].nodePortNumber = addCommand.addNodeFingerTable[j].nodePortNumber;
            }

            // Announce success of ADD_NODE and output this node's finger table
            cout << "Finger Table for node " << nodeId << ":" << endl;
            cout << "Index\t" << "  | Node Id" << endl;
	        for(unsigned int i=0;i<fingerTable->size(); i++){
	            cout << "\t" << i << "  " << (*fingerTable)[i].nodeId << endl;
	        }
            cout<<"'ADD_NODE "<<newNodeData.nodeId<<"' took "<<addCommand.messageCount<<" messages."<<endl;
        }

    } else {

        // Build the finger table for the new node
        int newNodeId = newNodeData.nodeId;
        NodeData selfData;
        selfData.nodeId = nodeId;
        selfData.nodePortNumber = nodePort;
        for(int k=0; k<M; k++){
          int targetId = (newNodeId + (int)pow(2,k))%(int)pow(2,M);
          if(findBestFinger(targetId) == -1){
            addCommand.addNodeFingerTable[k] = selfData;
          }
        }

        //Check if this node needs to spawn the new node
        if(findBestFinger(newNodeData.nodeId) == -1){

            // Create the new file map for the new node
            map<string, FileData> * newFileMap = createFileMap(newNodeData.nodeId);

            // Spawn the new node, and start it listening
            if(fork()==0){

                Node newNode = Node(newNodeData, selfData, predecessor, newFileMap, M);
                newNode.listenForCommands();

            }else{

                  // Update my predecessor info
                  predecessor = newNodeData;
            }
        }

        // If this node's successor is not the introducer, forward it. If it is, send the command to the new node.
        if((*fingerTable)[0].nodeId != 0){

          // Forward command to successor
          if(!forwardCommand(addCommand,0)){
            return false;
          } else {
            totalMessageCount++;
          }

        }else{

          // Send command to the new node
          addCommand.fileData.filenameHashData = -1;
          if(!sendCommand(addCommand,addCommand.nodeData)){
            return false;
          } else {
            totalMessageCount++;
          }
        }

        //Check my fingers for updates
        for(unsigned int i=0;i<fingerTable->size(); i++){

            // In this case, we're done
            if((*fingerTable)[i].nodeId == newNodeData.nodeId){
                return true;
            }

            //Check for update
            int bestID = (int)(nodeId+pow(2,i))%(int)pow(2,M);
            if(bestID<= newNodeData.nodeId && (newNodeData.nodeId < (*fingerTable)[i].nodeId||(*fingerTable)[i].nodeId < bestID)){

                // My finger table
                (*fingerTable)[i].nodeId = newNodeData.nodeId;
                (*fingerTable)[i].nodePortNumber = newNodeData.nodePortNumber;
            }
        }
    }

    // Success!
    return true;
}


/**
 * Helper function to create a file map
 */
map<string, FileData> * Node::createFileMap(int newId){

   // Get the file table and an iterator through it
   map<string, FileData> *NewMap = new map<string, FileData>;
   map<string, FileData>::iterator it;

   // Generate this file map
    for(it = fileMap->begin();it !=fileMap->end();){
        if((*it).second.filenameHashData<=newId){
	        NewMap->insert(pair<string, FileData>(it->first,it->second));
            fileMap->erase(it++);
        }else{
	        it++;
        }
    }
    return NewMap;
}



/**
 * Helper function to compute the hash of the file (using the SHA1 library)
 *
 *  @param  inputTokens  The tokenized input
 */
unsigned long* Node::computeHash(char* fileName) {

    // Handle a null filename (error case)
    if(fileName == NULL) {
        return NULL;
    }

    // Initialize the data to hash
    unsigned long * key = NULL;
    SHA1Context sha;
    SHA1Reset(&sha);
    SHA1Input(&sha, (unsigned char*) fileName, strlen(fileName));

    // Get the hash
    if (!SHA1Result(&sha)) {
        return key;
    } else {
        key = new unsigned long(sha.Message_Digest[4]);
    }

    // Return the file hash as computed by SHA-1
    return key;
}


/**
 * Handles an ADD_FILE command.
 *
 * Specifically, each node checks to see if the new file hashes to itself, and if so,
 * 	updates the data structures at this node. Otherwise, it forwards the command onto the node
 * 	whose id is less than or equal to the hash value and closest to the value.
 * If this node adds the file, it should output the key of the file (hashed from the filename), and
 * 	its node id.
 *
 * 	@param	newFileData	 FileData structure with the data for this new file
 */
int Node::addFile(FileData & newFileData){

    // Find this node's best guess at the node who should store this file
    int nextNodeID = findBestFinger(newFileData.filenameHashData);

    // If this is the best node, add it, otherwise, return the other node's id
    if(nextNodeID == -1) {

        // Add the new file to this node
        pthread_mutex_lock(&nodeDataStructuresMutex);
        unsigned long* key = computeHash(newFileData.filename);

        // Check if this already exists, and fail if it does
        if((*fileMap).find(newFileData.filename) == (*fileMap).end()) {

            // Add correctly
            (*fileMap).insert(pair<string, FileData>(newFileData.filename, newFileData));
            cout << "Added file '" << newFileData.filename << "' with key " << (*key) << " at node " << nodeId << endl;

        } else {
            cout << "Failed to add file '" << newFileData.filename << "'. File already exists!" << endl;
        }
        pthread_mutex_unlock(&nodeDataStructuresMutex);

        // Cleanup
        delete key;

    }

    // Return the id of the node who should handle this command
    return nextNodeID;
}


/**
 * Handles an DEL_FILE command.
 *
 * Specifically, each node checks to see if this file hashes to itself, and if so, removes
 * 	the file and outputs:
 * 		1) The hash key of the file (from the filename)
 * 		2) A message saying this file has been deleted
 *
 * 	@param	fileToDelete The FileData containing information about the file to delete
 */
int Node::deleteFile(FileData & fileToDelete){

    // Find this node's best guess at the node who has this file
    int nextNodeID = findBestFinger(fileToDelete.filenameHashData);

    // If this is the node where the file should be, delete it
    if(nextNodeID == -1) {

        // Find the file in the file map, and remove it
        pthread_mutex_lock(&nodeDataStructuresMutex);
        unsigned long* key = computeHash(fileToDelete.filename);
        map<string, FileData>::iterator it;
        it = (*fileMap).find(fileToDelete.filename);
        if(it != (*fileMap).end()) {

            // If we succeeded, say so and remove it
            (*fileMap).erase(it);
            cout << "Deleted File with Key: " << (*key) << " at Node: " << nodeId << endl;
            cout << fileToDelete.filename << " deleted" << endl;

        } else {

            // Othwerwise, output an error message
            cout << "File '" << fileToDelete.filename << "' doesn't exist! " ;
        }

        // Cleanup
        delete key;
        pthread_mutex_unlock(&nodeDataStructuresMutex);
    }

    // Return the id of the node who should handle this command
    return nextNodeID;
}


/**
 * Handles an FIND_FILE command.
 *
 * Specifically, if this node contains the file, output:
 * 		1) The key hashed from the filename
 * 		2) This node's id (the node id containing the file)
 * 		3) The IP address of the file (essentially the file data)
 *
 * 	@param	fileToFind	 The FileData containing information about the file we are trying to find
 */
int Node::findFile(FileData & fileToFind){

    // Find this node's best guess at the node who has this file
    int nextNodeID = findBestFinger(fileToFind.filenameHashData);

    // If this node should have the file, try to find it
    if(nextNodeID == -1) {

        // Look for it in the file map
        pthread_mutex_lock(&nodeDataStructuresMutex);
        unsigned long* key = computeHash(fileToFind.filename);
        FileData filedata = (*fileMap)[fileToFind.filename];
        if(strcmp(filedata.filename, fileToFind.filename)==0) {

            // If we find it, output the necessary data
            cout << "Found file '" << fileToFind.filename << "' with Key: " << (*key) << " at Node: " << nodeId << " with ip " << filedata.ipAddress << endl;

        } else {

            // If it doesn't exist, (we couldn't find it!)
            cout << "File '" << fileToFind.filename << "' doesn't exist! " ;
        }

        // Cleanup
        delete key;
        pthread_mutex_unlock(&nodeDataStructuresMutex);
    }

    // Return the id of the node who should handle this command
    return nextNodeID;
}

/**
 * Handles an GET_TABLE command.
 *
 * Specifically, if this node's id matches the requested node id, it will output:
 * 		1) The finger table of the node
 * 		2) The keys stored at this node
 *
 * 	@param	targetNode  The information about the node whose table we're asking for
 */
bool Node::getTable(NodeData targetNode){

    // If this node is the node whose table we want,
    if(nodeId == targetNode.nodeId){

        // Print out this node's table
        cout << "Finger Table for node " << nodeId << ":" << endl;
        cout << "Index\t" << "  | Node Id" << endl;
        for(unsigned int i=0;i<fingerTable->size(); i++){
            cout << "\t" << i << "  " << (*fingerTable)[i].nodeId << endl;
        }

        // Print out the files at this node
        cout << "Keys at node " << nodeId << ":" << endl;
        map<string, FileData>::iterator it;
        int keys = 0;
        for(it = fileMap->begin();it !=fileMap->end(); it++){
            if(strlen((*it).second.filename) != 0){
                cout << "\t" << (*it).second.filename << endl;
                keys++;
            }
        }
        if(keys==0) {
            cout << "\t[None]" << endl;
        }

    } else {

        // Otherwise, find the node whose table we want
        int bestFinger = findBestFinger(targetNode.nodeId);

        // Handle the case with a bad input parameter
        if(bestFinger == -1){
           cout << "No Node with ID " << targetNode.nodeId << "!" << endl;
           return false;
        }

        // Forward the command
        Command getTableCommand;
        getTableCommand.commandCode = GET_TABLE;
        getTableCommand.nodeData = targetNode;
        if(!forwardCommand(getTableCommand, bestFinger)) {
            cout << "Failed to forward GET_TABLE command!" << endl;
        } else {
            totalMessageCount++;
        }
    }
    return true;
}

/**
 * Given an ID this function will return the next finger that is below that ID. -1 is returned
 *  when this is the last node before/at the target node
 *
 *  @param  targetId    The node id we're looking for
 *  @return the index of the node closest, -1 if this is the node, or on error.
 */
int Node::findBestFinger(int targetId){

    // If this is the node with that id
    if(nodeId == targetId){
        return -1;
    }

    // If its after this node's predecessor but before this node, then this is the best node in the table
    if(predecessor.nodeId<targetId &&(nodeId > targetId || nodeId <= predecessor.nodeId)){
      return -1;
	}

	// Prep for finding the best
	int best = fingerTable->size() -1;
    int bestId = -1;

    // Look for the closest node in this node's table
	for(unsigned int i=0;i<fingerTable->size(); i++){

        // If the node is in the finger table, return it
	    if((*fingerTable)[i].nodeId == targetId){
	        return i;
	    }

        //
        int fingerId = (int)(nodeId+pow(2,i))%(int)pow(2,M);
	    if(fingerId<targetId&&bestId<fingerId){
    		best = i;
            bestId = fingerId;
	    }
	}

	// Return the best finger in this table
	return best;
}
/**
 * Handles a QUIT command. Specifically,each node will connect to its successor, and deliver the QUIT
 *  message, and then free all memory and exit. However, each node ensure that its message is delivered
 *  before exiting.
 *
 *  @param  command     the old command we received
 */
 void Node::quit(Command & command){

    // Prepare the command
    Command quitCommand;
    quitCommand.commandCode = QUIT;
    quitCommand.messageCount = command.messageCount+1;

    // Add the tally for all messages in this QUIT command (+1 for the message from this QUIT command)
    quitCommand.totalMessageCount = command.totalMessageCount + totalMessageCount + 1;

    // If this node's successor is the introducer,output the number of messages it took but don't forward the message
    if((*fingerTable)[0].nodeId !=0) {
        if(!forwardCommand(quitCommand,0)) {
            //error, we're gonna quit anyways, just ignore it
        }
    } else {
        quitCommand.totalMessageCount--;

        // Print out the number of messages for the QUIT command
        if(quitCommand.messageCount == 1) {
            cout << "Handled QUIT with " << quitCommand.messageCount << " message" << endl;
        } else {
            cout << "Handled QUIT with " << quitCommand.messageCount << " total messages" << endl;
        }

        // Print out the total number of messages for the lifespan of the program
        if(quitCommand.totalMessageCount == 1) {
            cout << "Program created a total number of " << quitCommand.totalMessageCount << " message" << endl;
        } else {
            cout << "Program created a total number of " << quitCommand.totalMessageCount << " total messages" << endl;
        }
    }

    // Call this object's destructor and exit this process
    exit(0);
 }


/**
 * Helper function to send a command to any node in the hash table, given the data on it
 *
 *  @param  command     The command to send
 *  @param  sendData    The data for the node we are sending to
 *  @return True on success, false otherwise
 */
bool Node::sendCommand(Command & command, NodeData & sendData) {

    // Try to connect up to a certain number of times, and then permanently fail
    bool successful = false;
    int attemptCount = 0;
    char errorMessage[256];
    int clientSocketFd;
    while(!successful && attemptCount <= MAX_RECONNECT_ATTEMPTS) {

        // Update attempt counter, and if we were not successful, pause for an instant to wait for the node
        attemptCount++;
        if(!successful) {
            usleep(500);
        }

        // Create a new socket for our 'client'
        int dummy;
        if ((clientSocketFd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
            sprintf(errorMessage, "Error creating client socket to connect to finger of node %d!", nodeId);
            successful = false;
            continue;
        }

        // Make the socket reusable
        if (setsockopt(clientSocketFd, SOL_SOCKET, SO_REUSEADDR, (char *)&dummy, sizeof(dummy)) == -1) {
            close(clientSocketFd);
            sprintf(errorMessage, "Error setting client socket options for connecting to the finger of node %d!", nodeId);
            successful = false;
            continue;
        }

        // Get the information for connecting to the other node
        struct addrinfo hostInfo, * myInfo = NULL;
        char fingerPortString[256];
        NodeData fingerData = sendData;
        sprintf(fingerPortString, "%d", fingerData.nodePortNumber);
        memset(&hostInfo, 0, sizeof(hostInfo));
        hostInfo.ai_family = AF_INET;
        hostInfo.ai_socktype = SOCK_STREAM;
        int tmp;
        if ((tmp = getaddrinfo("localhost", fingerPortString, &hostInfo, &myInfo)) != 0) {
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
        cout << errorMessage << endl;
    }
    // Return whether or not we were successful
    return successful;
}


/**
 * Helper function to send a command to finger of this node in the hash table. Special use case of send command,
 *  simply wraps sendsCommand.
 *
 *  @param  command  The command to send
 *  @param  target   The target index in the finger table
 *  @return True on success, false otherwise
 */
bool Node::forwardCommand(Command & command, int target) {

    // Check to see that we have that finger
    if ((int)fingerTable->size() <= target || target < 0) {
        cout << "Error sending command : No finger with tag " << target << "  for node " << nodeId << "!" << endl;
        return false;
    }

    // Get the information for connecting to the other node
    NodeData successorData = (*fingerTable)[target];

    // Send this command to the successor of this node
    return sendCommand(command, successorData);
}
