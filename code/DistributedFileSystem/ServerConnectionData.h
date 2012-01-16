/*
 * ServerConnectionData.h
 *
 *  Created on: Feb 13, 2011
 *      Author: jon
 */

#ifndef SERVERCONNECTIONDATA_H
#define SERVERCONNECTIONDATA_H

/**
 * Holds data that each server process needs to have to function properly
 *
 *	serverID				The id of the server that will recieve this struct, so that each server knows its id. We need
 *								this information because each server must know in which directory to look for its files.
 *
 * 	loadBlancerFailurePort	The port on which to notify the load balancer of process failures
 *
 * 	heartbeatListenerPort	The port on which to listen for heartbeating from the 'previous' process in the ring
 *
 * 	heartbeatSendToPort		The port to which to send this process's heartbeats, on which another process should be
 * 								listening for heartbeats
 *
 * 	fileRequestPort			The port on which to listen for file requests from the load balancer
 *
 * 	idOfHeartbeatSender		The server id of the process this process receives heartbeats from
 * 								(So that if we detect a failure, we know what ID to send to the load balancer)
 */
struct ServerConnectionData {
	unsigned int serverID;
	unsigned int loadBalancerFailurePort;
	unsigned int heartbeatListenerPort;
	unsigned int heartbeatSendToPort;
	unsigned int fileRequestPort;
	unsigned int idOfHeartbeatSender;
};

#endif /* SERVERCONNECTIONDATA_H */
