/*
 * This outlines data required for connections between nodes. Each node will have this data for every node to which
 * 	it is connected. In this application, then every node will have this data for every other node in the pool of DSM
 *      nodes.
 *
 *  Created on: April 19, 2011
 *      Author: Jon
 */

#ifndef NODEDATA_H_
#define NODEDATA_H_


/**
 * Outlines data needed for one node to connect to another.
 */
struct NodeData {

	/**
	 * The port number on which to connect to this node
	 */
	int nodePortNumber;

	/**
	 * The id of this node
	 */
	int nodeId;
};


#endif /* NODEDATA_H_ */
