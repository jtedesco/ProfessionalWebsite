/**
 * This class represents a special DSM node, called the 'Coordinator', that will read commands from 
 * an input file, and issue commands just like any other node. However, this node will also act as 
 * the central authority for controlling when nodes enter the critical section.
 *
 * This represents the decomposition only.
 * 
 *  Created on: April 19, 2011
 *      Author: Jon
 */

#ifndef COORDINATOR_H_
#define COORDINATOR_H_

#include "Command.h"
#include "NodeData.h"
#include "DSM.h"

#include <queue>
#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <pthread.h>

using namespace std;

/**
 * This class represents the process that will run and listen for input from stdin, and
 * 	create input commands and send them to the introducer as they appear.
 */
class Coordinator : public DSM {

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
             *  @param  nodeIds                 A list of all the node ids in the system
             *  @param  inputFile               The input file for reading commands 
             */
            Coordinator(NodeData & nodeData, map<int, int> & nodeCommunicationData, map<int, int> & memoryNodeIdIndex, 
                    map<int, unsigned char> & nodeMemoryData, vector<int> & nodeIds, filebuf * inputFile);
            /**
             * Frees all memory associated with this node
             */
            virtual ~Coordinator();
            
            /**
             * Starts the Coordinator node running, and reading commands from the input file.
             */
            virtual void start();
            
            /**
             * Loops, listening for commands, and on receipt of a command, calls the 'handleCommand' 
             *  function. This function have a single thread running it, listening for commands such
             *  as lock/unlock.
             */
            static void * listenForCommandsConcurrently(void * data);

            bool handleCommand(Command & command, int responseSocket);
            
            /**
             * Handles a LOCK commands, maintaining release consistency. This function is responsible
             *  for cleaning up all connections, and completely handling a LOCK command.
             * 
             *  @param  nodeId          The node id of the node that wants to enter the critical section
             */
            bool handleLock(int nodeId); 
        
            /**
             * Handles a UNLOCK commands, maintaining release consistency. This function is responsible
             *  for cleaning up all connections, and completely handling an UNLOCK command.
             */
            bool handleUnlock();
            
        private:
            
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
            bool parseCommand(vector<string> & inputTokens, Command & command, string & input);

            /**
             * Helper function to broadcast the coordinator's 
             * 
             *  @return true on success, false otherwise
             */
            bool broadcastCoordinatorData();
            
            /**
             * A queue of node ids that are waiting to enter the critical section, pending Coordinator
             *  approval. This does not need to be locked, it should only be accessed from the
             *  commandListenerThread.
             */
            queue<int> lockRequestQueue;
            
            /**
             * A handle on the input file
             */
            filebuf * inputFile;
            
            /**
             * Thread for listening for connections to the coordinator
             */
            pthread_t commandListenerThread;
            
            /**
             * Mutex for accessing local data 
             */
            pthread_mutex_t mutex;
};

#endif
