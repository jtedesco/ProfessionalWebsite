/*
 * This outlines data required for connections between nodes. Each node will have
 * 	this data for every node to which it is connected.
 *
 *  Created on: Mar 13, 2011
 *      Author: Jon
 */

#ifndef NODEDATA_H_
#define NODEDATA_H_

#define	MYIP	"127.0.0.1"

/**
 * Outlines data needed for one node to connect to another.
 */
struct NodeData {

	/**
	 * The port number on which to connect to the other node
	 */
	int nodePortNumber;

	/**
	 * The id of the other node
	 */
	int nodeId;
};


#endif /* NODEDATA_H_ */
