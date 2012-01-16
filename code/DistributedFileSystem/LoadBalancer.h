/*
 * The outline for the load balancer process
 *
 *  Created On: Feb 13, 2011
 *      Author: Jon Tedesco
 */

#ifndef LOADBALANCER_H
#define LOADBALANCER_H

#include "FileRequest.h"
#include "ServerConnectionData.h"

#include <pthread.h>
#include <semaphore.h>
#include <string.h>
#include <map>
#include <iostream>
#include <queue>

using namespace __gnu_cxx;
using std::string;
using namespace std;

#define MYIP "127.0.0.1"
#define BACKLOG 10
#define BUFFERSIZE 1024

/**
 * This class represents the load balancer process.
 */
class LoadBalancer {

	public:

		/**
		 * Default constructor for the load balancer. This constructor will
		 * 	initialize all LoadBalancer fields.
		 *
		 * 	@param	loadBalancerRequestPort	 	 The port on which to listen for requests from the client
		 * 	@param	loadBalancerFileTransferPort The port on which to listen for responses from servers
		 * 	@param	loadBalancerFailurePort  	 The port on which to listen for failure notifications
		 *  @param	clientTransferPort			 The port on which to connect to the client to transfer files
		 * 	@param	processIdsFile 			 	 The path to the file with process ids
		 * 	@param	serverLoadsFile 		 	 The path to the file holding the server loads
		 * 	@param	fileMapFilePath			 	 The path to the file map file
		 */
		LoadBalancer(int loadBalancerRequestPort, int loadBalancerFileTransferPort, int loadBalancerFailurePort,
					int clientTransferPort, string processIdsFilePath, string serverLoadsFilePath, string fileMapFilePath);

		/**
		 * Destructor for the load balancer. Frees all memory for every object
		 * 	in the LoadBalancer.
		 */
		virtual ~LoadBalancer();

		/**
		 * The 'requestListenerThread' will run this function. This function will be responsible
		 * 	for listening to requests from the client and inserting them at the back of the queue
		 *
		 * 		(Must be static for POSIX)
		 *
		 * 	@param	data	Any data that this thread needs (required by POSIX library)
		 */
		static void* listenForClientRequests(void* data);

		/**
		 * The 'requestResponseThread' will run this function. This function will be responsible
		 * 	for monitoring the file request queue for requests, and transferring the file data back
		 * 	to the client
		 *
		 * 		(Must be static for POSIX)
		 *
		 * 	@param	data	Any data that this thread needs (required by POSIX library)
		 */
		static void* handleFileRequests(void* data);

		/**
		 * The 'failureDetectionThread' will run this function. This function will be responsible
		 * 	for monitoring the server pool for failures. On failure, it will identify the server that
		 * 	has failed, and call 'redistributedFiles' to reconfigure the system
		 *
		 * 		(Must be static for POSIX)
		 *
		 * 	@param	data	Any data that this thread needs (required by POSIX library)
		 */
		static void* monitorServerPool(void* data);

		/**
		 * This function will be called to redistributed the files in the event that one of the
		 * 	server processes fails, and to update the data structures to remove this server from
		 * 	our records
		 *
		 * 	@param	failedServerId	The server id of the server from our server pool that has failed
		 * 	@return	true on success, false otherwise
		 */
		bool handleServerFailure(int failedServerId);

		/**
		 * Initializes the system, by building all threads, opening port connection data, initializing
		 * 	data structures, and distributing files (will NOT start threads)
		 *
		 * 	@return true on success, false otherwise
		 */
		bool intialize();

		/**
		 * Actually starts the load balancer running. At this point, all threads of computation will
		 * 	begin.
		 *
		 * Note that this function will not return until the entire file system is shutdown (will
		 * 	return immediately with 'false' on error)
		 *
		 * 	@return true on success, false otherwise
		 */
		bool start();

	private:


		/**
		 * Holds the queue of files that have been requested
		 */
		queue<struct FileRequest * > * fileRequestQueue;

		/**
		 * Mutex for accessing the file request queue
		 */
		pthread_mutex_t fileRequestQueueMutex;

		/**
		 * This will count the number of items on the queue, so that that the response thread
		 *  can sleep whenever no requests are in the queue
		 */
		sem_t fileRequestSemaphore;

		/**
		 * This thread will continuously monitor incoming requests to the load
		 * 	balancer, and when it receives a request, will enqueue the file request
		 */
		pthread_t requestListenerThread;

		/**
		 * This thread will pull a file request from the file request queue whenever
		 * 	one exists. It will sleep whenever the queue is empty (implemented with a semaphore)
		 */
		pthread_t requestResponseThread;

		/**
		 * This thread will monitor a certain port for failure notices
		 */
		pthread_t failureDetectionThread;

		/**
		 * Process ids file path
		 */
		string processIdsFilePath;

		/**
		 * Server loads file path
		 */
		string serverLoadsFilePath;

		/**
		 * File map conf file path
		 */
		string fileMapFilePath;

		/**
		 * The port on which the load balancer will listen for failure notifications from the servers
		 */
		int loadBalancerFailurePort;

		/**
		 * The port on which to listen for requests from the client
		 */
		int loadBalancerRequestPort;

		/**
		 * The port on which to listen for file transfers from servers
		 */
		int loadBalancerFileTransferPort;

		/**
		 * The port on which to connect to the client to transfer files
		 */
		int clientTransferPort;

		/**
		 * Mutex for accessing any server data listed below (hash map, server loads, or PIDs)
		 */
		pthread_mutex_t serverDataMutex;

		/**
		 * Hash table of filenames to array of server id's that have copies of the given file.
		 * 	The array will be unordered, and not indicate the priority of the servers
		 */
		map<string, int*> * fileLocations;

		/**
		 * An array of integers, indexed as serverLoads[server_id], which will return the
		 * 	current 'load', measured in bytes, of the server.
		 */
		int * serverLoads;

		/**
		 * Array of integers that maps server id's to process id's (indexed by server ids)
		 */
		int * processIds;

		/**
		 * The number of servers in our ring (before any fail)
		 */
		int numberOfServers;

		/**
		 * The global counter for the number of file requests that have happened
		 */
		int requestNumber;


		/**
		 * The state of the system in terms of file requests and file sizes
		 */
		int ** serverStates;

		/**
		 * Helper functions
		 */
		void spawnServerPool();
		void tokenize(const string& str, vector<string>& tokens, const string& delimiters=" ");
		void readFileMap();
		ServerConnectionData buildServerConnectionData(int index);
		void cleanup();
		int setupClientSocket();
		int setupServerSocket(int port);
		int findBestCandidate(int*goals, int numServers,int banned,int banned2);
		void copyFile(int sourc, int dest, string fileName);
		static void updateServerLoads(LoadBalancer* This, int serverId, int fileSize);
                void sendError(int clientSocketFd);

		/**
		 * Workaround for needing static functions for threads
		 */
		struct ThreadFunctionArguments {
			LoadBalancer* This ;
			void* actualArguments;
			ThreadFunctionArguments( LoadBalancer* t, void* p): This(t), actualArguments(p) {}
		};

};

#endif /* LOADBALANCER_H */
