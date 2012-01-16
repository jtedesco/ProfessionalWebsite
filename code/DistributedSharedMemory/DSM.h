/**
 * This class represents the standard DSM node that will exist in the pool of processes
 *  at the heart of this application. This node contains some memory data, connection 
 * data for all other nodes, and other essential DSM data structures.
 * 
 * This file outlines the class only.
 *
 *
 *  Created on: April 19, 2011
 *      Author: Jon
 */
#ifndef DSM_H_
#define DSM_H_

#include "Command.h"
#include "NodeData.h"
#include "Memory.h"

#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <queue>

using namespace std;

/**
 * This class represents the standard DSM node that will exist in the pool of processes
 *  at the heart of this application. This node contains some memory data, connection 
 *  data for all other nodes, and other essential DSM data structures.
 */
class DSM {

    public:           

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
        DSM(NodeData & nodeData, map<int, int> & nodeCommunicationData, map<int, int> & memoryNodeIdIndex, 
                map<int, unsigned char> & nodeMemoryData, vector<int> & nodeIds);

        /**
         * Frees all memory associated with this node
         */
        virtual ~DSM();

        /**
         * Starts the DSM node running, and listens for commands.
         */
        virtual void start();

        /**
         * Loops, listening for commands, and on receipt of a command, calls the 'handleCommand' 
         *  function.
         */
        virtual void listenForCommands();

        /**
         * Helper function to create a socket, bind it to this node's port, and start listening on
         *  it.
         * 
         * @return true on success, false otherwise
         */
        bool initializeIncomingPort();

        /**
         * Handles a generic input command, and delegates the work to one of the helper functions.
         *
         *  @param  command         The command we're handling
         *  @param  responseSocket  The socket file descriptor on which to respond to this command
         * 
         * 	@return	true on success, false otherwise
         */
        bool handleCommand(Command & command, int responseSocket);

        /**
         * Handles a LOCK commands, maintaining release consistency. This function is responsible
         *  for cleaning up all connections, and completely handling a LOCK command.
         * 
         *  @param  responseSocket  The socket file descriptor on which to respond to this command
         *  @param  nodeId          The node id of the node that wants to enter the critical section
         */
        // should not have been virtual
        bool handleLock(Command command);

        /**
         * Handles a UNLOCK commands, maintaining release consistency. This function is responsible
         *  for cleaning up all connections, and completely handling an UNLOCK command.
         */
        // should not have been virtual
        bool handleUnlock(Command command);

        /**
         * Handles a ADD commands. This function is responsible for cleaning up all connections, 
         * and completely handling an ADD command.
         * 
         *  @param  responseSocket  The socket file descriptor on which to respond to this command
         *  @param  memoryLocationIds       The list of memory locations we want to add as described
         *                                  in the 'Command' documentation.
         */
        virtual bool handleAdd(int * memoryLocationIds, int literal); 

        /**
         * Handles a PRINT commands. This function is responsible for cleaning up all connections, 
         * and completely handling a PRINT command.
         * 
         *  @param  responseSocket  The socket file descriptor on which to respond to this command
         *  @param  memoryLocation        The memory location id to print
         */
        virtual bool handlePrint(int memoryLocation);

        virtual Memory performRead(int memoryLocation,int nodeID); 

        /**
         * Handles a READ commands. This function is responsible for cleaning up all connections, 
         * and completely handling a READ command.
         * 
         *  @param  responseSocket  The socket file descriptor on which to respond to this command
         *  @param  memoryLocation  The id of the memory location to read from
         */
        virtual bool handleRead(int responseSocket, int memoryLocation);

        /**
         * Handles a WRITE commands. This function is responsible for cleaning up all connections, 
         * and completely handling a WRITE command.
         * 
         *  @param  responseSocket  The socket file descriptor on which to respond to this command
         *  @param  memoryLocation  The id of the memory location to write to
         */
        virtual bool handleWrite(Command command);

        virtual bool handleLockAcquired();

        /**
         * Sends a given command to a specified node id, using the connection data structures for
         *  this node.
         * 
         *  @param  command The command to send
         *  @param  nodeId  The id to send this command to
         *  @return true on success, false otherwise
         */
        bool sendCommand(Command & command, int nodeId);

       /**
        * helper function that will the value of a given memory location either in cache
        * ,your memory, or in someelse's memory
        *
        * @param memoryLocation        The memory location id to read
        */
        unsigned char getMemoryLocation(int memoryLocation);

        /**
         * Helper function to print all of the data for this DSM with nice, clean formatting.
         */
        void printDebugInformation();

    protected:
            
        /**
         * Contains ports on which to connect to any node (indexed by node id, contains port numbers)
         */
        map<int, int> nodeCommunicationData;

        /**
         * Contains node ids that have a given memory id (indexed by memory id, contains node ids)
         */
        map<int, int> memoryNodeIdIndex;

        /**
         * Contains all other node ids
         */
        vector<int> nodeIds;

        /**
         * The id of this DSM node
         */
        int id;

        /**
         * The port on which this DSM node is listening
         */
        int port;

        /**
         * The file descriptor for the socket bound to this node's incoming port.
         */
        int socketFd;
            
    private:

        // get sockaddr, IPv4 or IPv6: See who connected to server
        void *get_in_addr(struct sockaddr *sa);

        // tells wether command can be executed or should be queued up
        bool canExecuteCommand(CommandType type);

        bool connectTo(int & coordinatorFd, int port);

        bool sendCoordinatorCommand(Command command);

        /**
         * Processes all commands that have been queued up, waiting for a lock to be acquired.
         */
        bool processQueuedCommands();
        
        bool writeBackCache();

        int addMsgCount;

        // flag telling if the lock has been acquired
        bool hasLock;

        /**
         * Contains all memory data that this node holds (indexed by memory id)
         */
        map<int, unsigned char> nodeMemoryData;

        map<int, Memory> cache;

        /**
         * The port on which to connect to the 'Coordinator' node
         */
        int coordinatorPort;

        /**
         *Stores all the commands  to be done once the node acquires the lock
         */
        queue<Command> unprocessedCommands; 
};

#endif
