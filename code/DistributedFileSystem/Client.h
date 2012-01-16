/**
 * Client script used to send file requests to the load balancer
 */

#ifndef CLIENT_H_
#define CLIENT_H_

#include "cstdio"
#include "cstdlib"
#include "cstring"
#include "unistd.h"
#include "errno.h"
#include "netdb.h"
#include "sys/types.h"
#include "netinet/in.h"
#include "sys/socket.h"
#include "arpa/inet.h"
#include <iostream>
#include <fstream>
#include <string>
#include <vector>

using namespace std;



/**
 * Represents the client process
 */
class Client {

    public:
       
		/**
		 * Default constructor (for completeness)
		 */
        Client();

        /**
         * Constructor that will be used to build the client
         *
         * 	@param	port		the port to which to send file requests to the load balancer
         * 	@param	hostname	the host of the load balancer
         * 	@param	inputFile	the file to parse to read file requests
         */
        Client(char* port, char* hostname, char* inputFile);
        
        /**
         * Cleanup memory from the client
         */
        ~Client();

        /**
         * Encapsulates the action of forming a connection with the load balancer
         */
        int connectToServer(char* port, char* hostname);

        /**
         * Read files to request from input file
         *
         * 	@param	inputFile	the path to the request file
         */
        void parseFileList(char* inputFile);

        /**
         * Primary function that will loop, sending requests from the input file to the load balancer
         */
        void sendFileRequests();

        /**
         * Function to receive a response from the file system
         */
        int recieveFile(int clientFd);

        /**
         * Builds the server thread that will listen for responses from the load balancer,
         * 	and loops, blocking until a file response arrives
         */
        int makeServer();

    private: 
        
        // Our socket
        int sockfd;

        /**
         * List of the file to request
         */
        vector<char*> fileList; 

        /**
         * Helper function to save a file that we receive from the load balancer
         */
        void saveFile(string fileContents);

        /**
         * Helper function for socket connections
         */
        void* get_in_addr(struct sockaddr *sa);

        /**
         * Helper function to parse filenames from the input file
         */
        char* getFileName(char* input);

        /**
         * Helper function to send the request to the load balancer
         */
        void sendRequest(char* fileName);

        /**
         * Helper function to write out the contents of the files we receive
         */
        void writeFile(char* path, char* fileData, size_t fileSize);
};













#endif

