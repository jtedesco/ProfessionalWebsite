/**
 * This class represents a special DSM node, called the 'Coordinator', that will read commands from 
 * an input file, and issue commands just like any other node. However, this node will also act as 
 * the central authority for controlling when nodes enter the critical section.
 *
 * This includes this class's implementation.
 * 
 *  Created on: April 19, 2011
 *      Author: Jon
 */


#include "Coordinator.h"
#include "DSM.h"
#include "Common.h"

#include <iostream>
#include <fstream>
#include <vector>
#include <netinet/in.h>
#include <stdlib.h>

/**
 * The primary constructor for the DSM node, builds a DSM node given the NodeData struct representing 
 *  this node, a map of node ids to port numbers, a map of memory ids to node ids, and a map of memory
 *  ids to data entries.
 * 
 *  @param  nodeData                Contains the basic data for this node, i.e. its id and port
 *  @param  nodeCommunicationData   Contains ports on which to connect to any node (indexed by node id)
 *  @param  memoryNodeIdIndex       Contains node ids that have a given memory id (indexed by memory id)
 *  @param  nodeMemoryData          Contains all memory data that this node holds (indexed by memory id)
 *  @param  nodeIds                 A list of all the node ids in the system
 *  @param  inputFile               The input file for reading commands 
 */
Coordinator::Coordinator(NodeData & nodeData, map<int, int> & nodeCommunicationData, map<int, int> & memoryNodeIdIndex, 
        map<int, unsigned char> & nodeMemoryData, vector<int> & nodeIds, filebuf * inputFile) :
        DSM(nodeData, nodeCommunicationData, memoryNodeIdIndex, nodeMemoryData, nodeIds),
        inputFile(inputFile) {

    // Initialize the mutex
    pthread_mutex_init(&mutex, NULL);
}

/**
 * Frees all memory associated with this node
 */
Coordinator::~Coordinator() { 
    
    // Close the input file
    inputFile->close();
    
    // Cleanup the thread data
    pthread_mutex_destroy(&mutex);
}

/**
 * Starts the Coordinator node running, and reading commands from the input file.
 */
void Coordinator::start() {
    
    // Get an input stream for the command file
    istream commandFileFileInputStream(inputFile);
    
    // Initialize the incoming port connection
	if(!initializeIncomingPort()) {
        cout << "Failed to broadcast initialization data to all DSM nodes!" << endl;\
		pthread_exit(NULL);
    }
    
    // Initialize the command listener thread, and launch it to listen for commands
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);
    pthread_create(&commandListenerThread, &attr, listenForCommandsConcurrently, this);
    pthread_attr_destroy(&attr);
    
    // Announce to all nodes the Coordinator's port
    if(!broadcastCoordinatorData()) {
       cout << "Failed to broadcast Coordinator data to DSM nodes!" << endl;
        pthread_exit(NULL);
    }
    
    // Loop through the entire command file
    while(commandFileFileInputStream.good()) {
        
        // Get a line from the command file
        string input;
        getline(commandFileFileInputStream, input);
        if(input.size() == 0) {
            continue;
        }
        
       // Tokenize this command
        vector<string> inputTokens;
        tokenize(input, inputTokens, ":");
        
        // Try to parse this tokenized input, and if it fails, skip it
        Command command;
        if(!parseCommand(inputTokens, command, input)) {
            continue;
        }
        
    }
    
    // Processed all commands, now just loop indefinitely until someone kills us
    cout << "Coordinator processed all commands, but still listening for connections." << endl;
    while(true);
}


/**
 * Helper function to parse a line of input from the command file. If parsed successfully, this function
 *  will also send the command to the corresponding node, and sleep as needed.
 * 
 *  @param inputTokens      The tokenized line of input from the command file
 *  @param command          The command struct to fill with the data parsed from input
 *  @param input            The raw string input for this command
 * 
 *  @return true on success, false if we fail to parse the command for some reason
 */
bool Coordinator::parseCommand(vector<string> & inputTokens, Command & command, string & input) {
  
    // Check to see if the number of tokens is too short
    if(inputTokens.size()<3) {
        cout << "Error parsing command '" << input << "', not enough parameters for any command!" << endl;
        return false;
    }
    
    // Parse the first three entries of the command token
    unsigned int timeToSleep = atoi(inputTokens[0].c_str());
    int nodeId = atoi(inputTokens[1].c_str());
    CommandType commandCode = (CommandType) atoi(inputTokens[2].c_str());
    
    // Check that nodeId is in the list of node ids
    bool found = false;
    for(unsigned int i=0; i<nodeIds.size(); i++) {
        if(nodeId == nodeIds[i]) {
            found = true;
        }
    }
    if(!found) {
        cout << "Error parsing command '" << input << "', node '" << nodeId << "' does not exist!" << endl;
        return false;
    }
    
    // First, sleep the specified amount of time
    usleep(timeToSleep*1000);
    
    // Handle the specific case
    switch(commandCode) {
        
        // Handle a LOCK command
        case LOCK:
            
            // Check for the right number of arguments!
            if(inputTokens.size()==3) {
                
                // Build the LOCK command
                Command lockCommand;
                lockCommand.commandCode = LOCK;
                lockCommand.nodeId = nodeId;

                // Send it
                if(!sendCommand(lockCommand, nodeId)) {
                    cout << "Failed to send LOCK command '" << input << "' to node " << nodeId << "!" << endl;
                    return false;
                }
            } else {
                cout << "Error parsing LOCK command '" << input << "', too many parameters!" << endl;
                return false;
            }
            break;
            
        // Handle an UNLOCK command
        case UNLOCK:
            
            // Check for the right number of arguments!
            if(inputTokens.size()==3) {
                
                // Build the UNLOCK command
                Command unlockCommand;
                unlockCommand.commandCode = UNLOCK;
                unlockCommand.nodeId = nodeId;

                // Send it
                if(!sendCommand(unlockCommand, nodeId)) {
                    cout << "Failed to send UNLOCK command '" << input << "' to node " << nodeId << "!" << endl;
                    return false;
                }
                
            } else {
                cout << "Error parsing UNLOCK command '" << input << "', too many parameters!" << endl;
                return false;
            }
            break;
            
        // Handle an ADD command
        case ADD:
            
            // Check to see that there are some arguments here
            if(inputTokens.size() >= 5) {
                
                // Build the trivial parts of the ADD command
                Command addCommand;
                addCommand.commandCode = ADD;
                
                // 'Pop' off the last entry, our integer literal
                int literal = atoi(inputTokens[inputTokens.size()-1].c_str());
                inputTokens.pop_back();
                addCommand.sum = literal;
                
                // Initialize our array of node ids
                for(int i = 0; i<MAX_NODE_COUNT; i++) {
                    addCommand.memoryLocationIds[i] = -1;
                }
                for(unsigned int i = 2; i<inputTokens.size(); i++) {
                    int thisNodeId = atoi(inputTokens[i].c_str());
                    addCommand.memoryLocationIds[i-2] = thisNodeId;
                }
                
                // Try to send this command
                if(!sendCommand(addCommand, nodeId)) {
                    cout << "Failed to send ADD command '" << input << "' to node " << nodeId << "!" << endl;
                    return false;
                }               

            } else {
                cout << "Invalid syntax for ADD command '" << input << "', parameters missing!" << endl;
                return false;
            }
            break;
            
        // Parse a print statement
        case PRINT:
            
            // Check to see that there is exactly one argument
            if(inputTokens.size() == 4) {
                
                // Build the command
                Command printCommand;
                printCommand.commandCode = PRINT;
                printCommand.memoryLocation = atoi(inputTokens[3].c_str());
                // Try to send this command
                if(!sendCommand(printCommand, nodeId)) {
                    cout << "Failed to send PRINT command '" << input << "' to node " << nodeId << "!" << endl;
                    return false;
                }               

            } else {
                cout << "Invalid syntax for PRINT command '" << input << "', wrong number of parameters!" << endl;
                return false;
            }
            break;
            
            
        // Otherwise, fail, it's something we don't recognize
        default:
            cout << "Did not recognize command '" << input << "'!" << endl;
            return false;
    }
    
    // Else, success!
    return true;
}


/**
 * Helper function to broadcast the coordinator's 
 * 
 *  @return true on success, false otherwise
 */
bool Coordinator::broadcastCoordinatorData() {
    
    // Build this initialization command
    Command command;
    command.commandCode = INITIALIZE;
    command.coordinatorPort = port;
    
    // Loop through each node id
    for(unsigned int index=0; index<nodeIds.size(); index++) {
        
        // Send the initialize command to this node
        if(!sendCommand(command, nodeIds[index])) {
            return false;
        }
    }
    
    // Otherwise, success
    return true;
}


/**
 * Loops, listening for commands, and on receipt of a command, calls the 'handleCommand' 
 *  function. This function have a single thread running it, listening for commands such
 *  as lock/unlock.
 */
void * Coordinator::listenForCommandsConcurrently(void * data) {
    
    // Get a pointer to 'this', the coordinator
    Coordinator * This = (Coordinator *) data;
    
    // Some data structures to prepare to accept the connection
    struct sockaddr_storage their_addr; // connector's address information
    socklen_t sin_size;
    sin_size = sizeof their_addr;

    // Listen indefinitely for commands
    while(true) {
        
        // Accept a new command, and record the socket on which to respond to this command
        int commandSocketFd = accept(This->socketFd, (struct sockaddr *)&their_addr, &sin_size);
        if (commandSocketFd == -1) {
            perror("accept");
            continue;
        }
        
        // Read the command from the socket
        int bytesRead = 0;
        Command incomingCommand;
        if ((bytesRead = recv(commandSocketFd, &incomingCommand, sizeof(incomingCommand), 0)) == -1) {
            perror("Coordinator failed to receive command from socket!");
            close(commandSocketFd);
            pthread_exit(NULL);
        }
        
        // Attempt to handle the command, and exit if it fails
        if(!This->handleCommand(incomingCommand, commandSocketFd)) {
            close(commandSocketFd);
            pthread_exit(NULL);
        }

        // Cleanup, close this socket
        close(commandSocketFd);
    }
}

bool Coordinator::handleCommand(Command & command, int responseSocket)
{
    switch(command.commandCode)
    {

        case LOCK:
            close(responseSocket);
            return handleLock(command.nodeId);

        case UNLOCK:
            close(responseSocket);
            return handleUnlock();

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
bool Coordinator::handleLock(int nodeId) {
    
    // First, check to see if the queue is empty, and if so, immediately grant a lock request
    if(lockRequestQueue.empty()) {
        
        // Build the command
        Command lockAcquiredCommand;
        lockAcquiredCommand.commandCode = LOCK_ACQUIRED;
        
        // Send it to this node
        if(!sendCommand(lockAcquiredCommand, nodeId)) {
            cout << "Failed to send LOCK_ACQUIRED command!" << endl;
            return false;
        }
        
    }
        
    // Add this node to the queue
    lockRequestQueue.push(nodeId);
    
    return true;
}

/**
 * Handles a UNLOCK commands, maintaining release consistency. This function is responsible
 *  for cleaning up all connections, and completely handling an UNLOCK command.
 * 
 *  @param  nodeId          The node id of the node that is exiting the critical section
 */
bool Coordinator::handleUnlock() {
    
    // Remove the next guy from the queue if there is one
    lockRequestQueue.pop();
    
    // If there's no one in the queue, this is in error, just ignore it
    if(lockRequestQueue.size() > 0) {
        int nextNodeId = lockRequestQueue.front();

        // Build a LOCK_ACQUIRED command
        Command lockAcquiredCommand;
        lockAcquiredCommand.commandCode = LOCK_ACQUIRED;

        // Send it to the next node
        if(!sendCommand(lockAcquiredCommand, nextNodeId)) {
            cout << "Failed to send LOCK_ACQUIRED command!" << endl;
            return false;
        }
    }
    return true;
}
