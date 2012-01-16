/*
 * This header holds command data. We chose to use a struct rather than an object (even though
 *  the latter would have been a nicer design choice), because we wanted greater control over
 *  the memory management of the command structures.
 *
 * We use an enum to for the command codes, and a command struct to hold data necessary for a command.
 *
 *  Created on: Mar 13, 2011
 *      Author: Jon
 */

#ifndef COMMAND_H_
#define COMMAND_H_

#include "FileData.h"
#include "NodeData.h"

#define MAX_TABLE_SIZE  32

/**
 * Enum representing the commands our system supports
 */
enum CommandType {

	// Correspond to commands from the handout
	ADD_NODE = 0,
	ADD_FILE = 1,
	DEL_FILE = 2,
	FIND_FILE = 3,
	GET_TABLE = 4,
	QUIT = 5,
    FIND_FINGER = 6
};

/**
 *	Holds the necessary data for a command.
 *
 *		ADD_FILE, DEL_FILE, and FIND_FILE:	commandCode, fileData
 *		ADD_NODE, GET_TABLE:				commandCode, nodeData
 */
struct Command {

	/**
	 * A constant corresponding to the specific command.
	 */
	CommandType commandCode;

	/**
	 * Used for adding nodes or getting the table of a node, to specify the node in question
	 * 	and its connection information for finger talbes
	 */
	NodeData nodeData;

    /**
	 * An extra set of data used by some of the commands
     */
    NodeData extraData;

    /**
     * Counter for the total number of messages used for this command. This total should be printed out once a command
     *  is finished, in the 'base case' of the command.
     */
    int messageCount;

    /**
     * Counter for the total number of messages of all commands. This total is only touched for a QUIT command, and
     *  the counters for messages handled for all commands is kept locally at each node, and added into this upon a QUIT.
     */
    int totalMessageCount;

    /**
     * This will represent the finger table of a new node when it is being added, and will be populated as the command
     *  circulates around the ring.
     */
    NodeData addNodeFingerTable[MAX_TABLE_SIZE];

    /**
     * Struct that holds all the data associated with a file.
     */
    FileData fileData;
};


#endif /* COMMAND_H_ */
