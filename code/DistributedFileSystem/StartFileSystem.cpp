/*
 * Provides a hook to start up the file system, provided all files required
 * 	are in the current working directory.
 *
 *  Created on: Feb 13, 2011
 *      Author: jon
 */


#include <iostream>
#include "LoadBalancer.h"
#include "DistributedServer.h"
using namespace std;

/**
 * The port on which to listen for requests from the client
 */
#define REQUEST_LISTENER_PORT 53203

/**
 * The port on which to listen for responses from servers
 */
#define	RESPONSE_LISTENER_PORT 53204

/**
 * The port on which to listen for failure notifications
 */
#define FAILURE_NOTIFICATION_PORT 53205

/**
 * The port on which to connect to the client to transfer files
 */
#define CLIENT_TRANSFER_PORT 65332

/**
 * Entry point to the application.
 */
int main(int argc, char **argv) {
	
	// Create the load balancer
	LoadBalancer* thisServer = new LoadBalancer(REQUEST_LISTENER_PORT, RESPONSE_LISTENER_PORT, FAILURE_NOTIFICATION_PORT,
												CLIENT_TRANSFER_PORT, "process_ids","server_loads","file_map.conf");

	// Initialize it and start it up
	thisServer->intialize();
	cout<<"Starting Load Balancer"<<endl;
	thisServer->start();

	// Exit and cleanup
	delete thisServer;
	pthread_exit(NULL);
}
