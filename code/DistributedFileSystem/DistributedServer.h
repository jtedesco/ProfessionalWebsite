/*
 * DistributedServer.h
 *
 *  Created on: Feb 13, 2011
 *      Author: jon
 */

#ifndef DISTRIBUTEDSERVER_H
#define DISTRIBUTEDSERVER_H

#include <pthread.h>
#include <string>
#include "ServerConnectionData.h"
using namespace std;

/**
 * This class represents a server process in
 */
class DistributedServer {

	public:

		/**
		 * Constructs a server object, given the relevant data for connecting to the
		 * 	load balancer
		 *
		 * 	@param	connectionData	The connection data for forming the ring of servers
		 * 							and communicating with the load balancer
		 * 							(This object will exist on heap, allocated in the laod
		 * 								balancer)
		 */
		DistributedServer(ServerConnectionData connectionData);


		/**
		 * Frees all memory associated with this server, including the connection
		 * 	data passed into the constructor
		 */
		virtual ~DistributedServer();


		/**
		 * This function will initialize the server, launching all threads of this server and
		 * 	initiating its portion of the heartbeating ring.
		 *
		 * Note this function must be entered by the load balancer. Inside this function, this
		 * 	class can fork into another process, splitting from the load balancer
		 */
		void initialize();

	private:
		
		/**
		 * This thread will be responsible for listening for heartbeats on the port given in the constructor.
		 * 	On timeout, this thread will send a notification to the load balancer that the thread it was
		 * 	listening to has failed.
		 *
		 * When it has not received any message, it will sleep until either a message was received or a
		 * 	timeout occurred.
		 */
		pthread_t heartbeatListenerThread;

		/**
		 * This thread will be responsible for sending a heartbeat periodically on a given time interval. When
		 * 	it is not sending heartbeats, it will sleep.
		 */
		pthread_t heartbeatSenderThread;

		/**
		 * This thread will handle requests that this sever receives. This server will not cache
		 *	requests. It assumes that the load balancer will send requests synchronously, i.e. it will
		 *	wait for the file to be sent back before requesting another file.
		 */
		pthread_t requestHandlerThread;

		/**
		 * This mutex controls access to all data in this object, to prevent race conditions between the two threads
		 */
		pthread_mutex_t serverDataMutex;
		
		/**
		 * The 'server id' of this server. 
		 */
		unsigned int serverID;

		/**
		 * The port on which to notify the load balancer of failures (provided on creation)
		 */
		unsigned int loadBalancerFailurePort;

		/**
		 * The port on which to listen for heartbeats from another process (provided on creation)
		 */
		unsigned int heartbeatListenerPort;

		/**
		 * The port to which to send heartbeats periodically (provided on creation)
		 */
		unsigned int heartbeatSendToPort;

		/**
		 * The port on which to listen for file requests. The server will also send responses on this same port (provided on creation)
		 */
		unsigned int fileRequestPort;

		/**
		 * The 'server id' of the server this server is listening to. If this server stops receiving heartbeats,
		 * 	it will send this id back to the load balancer to notify it that the other server has failed
		 */
		unsigned int idOfHeartbeatSender;

		/**
		* Message sent between servers to indicate the servers are alive (the 'live' heartbeat message)
		*/
		int serverOkMsg;
};

#endif /* DISTRIBUTEDSERVER_H */
