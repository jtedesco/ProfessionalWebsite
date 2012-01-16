/*
** client.cpp -- a stream socket client demo
*/

#include "cstdio"
#include "cstdlib"
#include "cstring"
#include <string>
#include <iostream>
#include "unistd.h"
#include "errno.h"
#include "netdb.h"
#include "sys/types.h"
#include "netinet/in.h"
#include "sys/socket.h"
#include "pthread.h"
#include "arpa/inet.h"
#include "Client.h"

using namespace std;

/**
 * The path to the file that has a filename on each line (as <FILENAME>).
 *		This file will be parsed by the client to send requests.
 */
#define FILELISTPATH "file_list"

/**
 * The port on which to connect to the load balancer
 */
#define BALANCERPORT "53203"

/**
 * Found this to be 'safer' than using 'localhost'
 */
#define HOSTNAME     "127.0.0.1"


// Hack for accessing data in a thread
typedef struct RequestThreadData {
    Client* client;

} RequestThreadData;

// Declare this function
void* requestFiles(void* threadData);


/**
 * Entry point for the client (takes no parameters)
 */
int main(int argc, char *argv[]) {

	// Create a new client object and stick it in the thread data struct
    RequestThreadData* threadData = new RequestThreadData;
    threadData->client = new Client(BALANCERPORT, HOSTNAME, FILELISTPATH);
    Client client = *(threadData->client);

    // Create a new thread for sending file requests
    pthread_t requestThread;
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);

    // Start this thread
    pthread_create(&requestThread, &attr, requestFiles, (void*)threadData);

    // Startup a server here, to asynchronously accept file transfers from the file system
    client.makeServer();

    return 0;
}

/**
 * This function will be run in the file request thread, and read files from the
 * 	input file and request them from the file system
 */
void * requestFiles(void * threadData) {

	// Get the thread data out of our struct
    RequestThreadData data = *((RequestThreadData*)threadData);
    Client client = *(data.client);

    // Send the file requests to the file system
    client.sendFileRequests();

    // Say that we're finished and kill this request thread
    cout<<"Requested All Files From File System"<<endl;
    pthread_exit(NULL);
}


