/*
 * Represents a generic node in the distributed hash table. Any functions of this class should be
 * 	overridden in the 'Introducer' class which need to be different for the designated 'introducer'
 * 	node.
 *
 *  Created on: Mar 13, 2011
 *      Author: Jon
 */

#ifndef NODE_H_
#define NODE_H_

#include "Command.h"
#include "NodeData.h"

#include <pthread.h>
#include <semaphore.h>
#include <string.h>
#include <map>
#include <iostream>
#include <queue>

using namespace __gnu_cxx;
using std::string;
using namespace std;


/**
 * Represents a generic node in the distributed hash table. Any functions of this class should be
 * 	overridden in the 'Introducer' class which need to be different for the designated 'introducer'
 * 	node.
 */
class Node {

	public:

		/**
		 * Builds a node, given a node data struct, information on its predecessor and successor in the hashtable, and m
		 */
		Node(NodeData & nodeData, NodeData & successor, NodeData & predecessor, int m);

		/**
		 * Builds a node, given information about the predecessor, successor, m, and the new file map for this node
		 */
		Node(NodeData & nodeData, NodeData & successor, NodeData & predecessor, map<string, FileData> * newFileMap, int m);

		/**
		 * Frees all memory associated with a Node
		 */
		virtual ~Node();

        /**
         * Starts the node running, and loops, waiting for input
         */
        void listenForCommands();

		/**
		 * Handles a generic input command, and delegates the work to one of the helper functions
		 *
		 * 	@return	true on success, false otherwise
		 */
		bool handleCommand(Command & command);

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
         *  @return true on success, false otherwise
		 */
		virtual bool addNode(Command addCommand);

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
         *  @return true on success, false otherwise
		 */
		virtual int addFile(FileData & newFileData);

		/**
		 * Handles an DEL_FILE command.
		 *
		 * Specifically, each node checks to see if this file hashes to itself, and if so, removes
		 * 	the file and outputs:
		 * 		1) The hash key of the file (from the filename)
		 * 		2) A message saying this file has been deleted
		 *
		 * 	@param	fileToDelete The FileData containing information about the file to delete
         *  @return true on success, false otherwise
		 */
		virtual int deleteFile(FileData & fileToDelete);

		/**
		 * Handles an FIND_FILE command.
		 *
		 * Specifically, if this node contains the file, output:
		 * 		1) The key hashed from the filename
		 * 		2) This node's id (the node id containing the file)
		 * 		3) The IP address of the file (essentially the file data)
		 *
		 * 	@param	fileToFind	 The FileData containing information about the file we are trying to find
         *  @return true on success, false otherwise
		 */
		virtual int findFile(FileData & fileToFind);

		/**
		 * Handles an GET_TABLE command.
		 *
		 * Specifically, if this node's id matches the requested node id, it will output:
		 * 		1) The finger table of the node
		 * 		2) The keys stored at this node
		 *
		 * 	@param	nodeId	 	The id of the node whose table this command is asking us to print.
         *  @return true on success, false otherwise
		 */
		virtual bool getTable(NodeData targetNode);

        /**
         * Handles a QUIT command. Specifically,each node will connect to its successor, and deliver the QUIT
         *  message, and then free all memory and exit. However, each node ensure that its message is delivered
         *  before exiting.
         *
         *  @param  command     The command this node received
         */
         void quit(Command & command);

	private:

        /**
         * Builds all data structures, opens listener port, and starts this node running.
         *
         * 	@return	true on success, false otherwise
         */
        bool initialize(NodeData & nodeData);

        /**
         *Helper Function to find the best finger to foward a request to given a target ID
         */
        int findBestFinger(int targetId);

        /**
         * Wrapper for forwardCommand it takes a node data rather than a finger number
         *
         */
        bool sendCommand(Command & command, NodeData & sendData);

        /**
         * Helper function to send a command to this node's sucessor in the hash table (entry 0)
         *
         *  @param  command  The command to send
         *  @return True on success, false otherwise
         */
        bool forwardCommand(Command & command, int tagret);

        /**
         * Helper function to show who is connected
         */
        void *get_in_addr(struct sockaddr *sa);

        /**
         * Helper function to handle a new command connection, the entry point of the command handler thread.
         *
         *  @param      The node data for the node handling this new command
         *  @return     Any return data from the thread (Required by POSIX)
         */
        static void * handleConcurrentCommand(void* nodeData);

        /**
         * Helper function to create a file map
         */
        map<string, FileData> * createFileMap(int newId);

        /**
         * Helper function to compute the hash of the file (using the SHA1 library)
         *
         *  @param  inputTokens  The tokenized input
         */
        unsigned long* computeHash(char* fileName);

		/**
		 * Mutex for accessing the data structures at this node.
		 */
		pthread_mutex_t nodeDataStructuresMutex;

		/**
		 * Maps file names to file data structs. This is used to keep track of the files at this node.
		 */
		map<string, FileData> * fileMap;

		/**
		 * Forms the finger table for this node. Indexed by integers, it maps integers in the range [0, m-1] to
		 * 	connection data structs. This is used for the purposes of contacting other servers in the ring.
		 */
		vector<NodeData> * fingerTable;

		/**
		 *	The port on which this node is listening
		 */
		int nodePort;

        /**
         * File descriptor the node accepts connections with
         */
        int nodeFD;

        /**
         * File descriptor of a new accepted connection
         */
        int newFD;

		/**
		 * The id of this node
		 */
		int nodeId;

        /**
         *Holds infomation about this node's predecessor
         */
        NodeData predecessor;

		/**
		 * the M value
		 */
		int M;

		/**
		 * Count kept locally for all messages <i>SENT</i> by this node.
		 */
		int totalMessageCount;
};


#endif /* NODE_H_ */
