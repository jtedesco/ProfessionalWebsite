/*
 * This header holds command data. We chose to use a struct rather than an object (even though
 *  the latter would have been a nicer design choice), because we wanted greater control over
 *  the memory management of the command structures.
 *
 * We use an enum to for the command codes, and a command struct to hold data necessary for a command.
 *
 *  Created on: April 19, 2011
 *      Author: Jon Tedesco
 */

#ifndef COMMAND_H_
#define COMMAND_H_

#define MAX_NODE_COUNT  100

/**
 * Enum representing the commands our system supports
 */
enum CommandType {

	// Correspond to commands from the handout
	LOCK = 1,
	UNLOCK = 2,
	ADD = 3,
	PRINT = 4,

        /**
         * Retrieves the value of a memory location from a given node
         */
	READ = 5,

	/**
	 * Writes a given value to a specified memory location
	 */
	WRITE = 6,

	/**
	 * This code denotes a response to some command. While it is not absolutely necessary, it should make things easier,
         *  and adds some completeness to our design. This will also need to be sent in respons to each
         *  command a node issues, to ensure we don't exhibit any race conditions.
 	 */
	RESPONSE = 7,
        
        /**
         * This code represents the one time command issued by the Coordinator to announce its port to
         *  all DSM nodes.
         */
        INITIALIZE = 8,
        
        /**
         * This code represents a command notifying a node that it has acquired the lock. It is sent
         *  in response to a LOCK request, not necessarily immediately following the request.
         */
        LOCK_ACQUIRED = 9
};

/**
 *	Holds the necessary data for a command.
 */
typedef struct {

    /**
     * A constant corresponding to the specific command.
     */
    CommandType commandCode;

    /**
     * Provides a list of memory locations to retrieve. The first entries of this array will be filled with memory location
     *  ids. The first entry (iterating from index 0) that contains -1 represents the end of the list. Hence, an empty
     *  list contains all -1's.
     */
    int memoryLocationIds[MAX_NODE_COUNT+2];

    /**
     * This variable is filled on a response to an 'ADD' command and contains the sum of the memory 
     *  locations requested. Likewise, when this command is sent from the Coordinator initially, this
     *  holds the integer literal given initially, to which to add the actual sum.
     */
    char sum;
    
    /**
     * The data to be written, if this is a WRITE command.
     */
    int writeData;
    
    /**
     * Holds the node id of the node that wants to enter or exit the critical section, when sent to the 
     *  Coordinator from a typical DSM node.
     */
    int nodeId;
    
    /**
     * This variable holds the ID of the relevant memory location for a PRINT, READ, or WRITE command.
     */
    int memoryLocation;
    
    /**
     * This variable holds the port on which to connect to the Coordinator, on an INITIALIZE command
     */
    int coordinatorPort;
} Command;


#endif /* COMMAND_H_ */
