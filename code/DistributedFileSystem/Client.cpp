/**
 * Implementation of the client process
 */

#include "Client.h"
#include "FileRequest.h"
#include "FileTransferHeader.h"
#include <algorithm>
#include "sys/stat.h"
#include "fcntl.h"

using namespace std;

/**
 * Constant to determine the maximum length of an input file name
 */
#define MAXFILENAMESIZE 255

/**
 * The port on which to listen for responses from the load balancer
 */
#define PORT "65332"
#define BACKLOG 10

/**
 * Default constructor (for completeness)
 */
Client::Client(){
}

/**
 * Constructor that will be used to build the client
 *
 * 	@param	port		the port to which to send file requests to the load balancer
 * 	@param	hostname	the host of the load balancer
 * 	@param	inputFile	the file to parse to read file requests
 */
Client::Client(char* port, char* hostname, char* inputFile) {

	// Connect to the load balancer
    connectToServer(port, hostname);

    // Parse the file list
    parseFileList(inputFile);
}

/**
 * Cleanup memory from the client
 */
Client::~Client() {
    // All data is on the stack
}

/**
 * Encapsulates the action of forming a connection with the load balancer
 */
int Client::connectToServer(char* port, char* hostname) {

	// Setup data structures
    struct addrinfo hints, *servinfo, *p;
    int rv;
    char s[INET6_ADDRSTRLEN];
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;

    // Get address stuff
    if ((rv = getaddrinfo(hostname, port, &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        return 1;
    }

    // Fix for connecting to localhost potentially (and potential connection problems)
    for(p = servinfo; p != NULL; p = p->ai_next) {

    	//Build the socket
        if ((this->sockfd = socket(p->ai_family, p->ai_socktype,
                p->ai_protocol)) == -1) {
            perror("client: socket");
            continue;
        }

        //Connect
        if (connect(this->sockfd, p->ai_addr, p->ai_addrlen) == -1) {
            close(this->sockfd);
            continue;
        }

        break;
    }

    // Error-check
    if (p == NULL) {
        fprintf(stderr, "client: failed to connect\n");
        return 2;
    }

    // Connect to the load balancer
    inet_ntop(p->ai_family, get_in_addr((struct sockaddr *)p->ai_addr),
            s, sizeof s);
    cout <<"Connecting to LoadBalancer"<<endl;

    // Cleanup
    freeaddrinfo(servinfo);

    // Success
    return 0;
}


/**
 * Helper function for socket connections
 */
void *Client::get_in_addr(struct sockaddr *sa) {
    if (sa->sa_family == AF_INET) {
        return &(((struct sockaddr_in*)sa)->sin_addr);
    }
    return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

/**
 * Read files to request from input file
 *
 * 	@param	inputFile	the path to the request file
 */
void Client::parseFileList(char* inputFile) {

	// Open the input file
    ifstream inFile(inputFile);
    if(!inFile) {
        cout << "Cannot open input file." << endl;
        return;
    }

    // Read in string of at most MAXFILENAMESIZE + '<', '>' and '\0'
    char* inputLine = new char[MAXFILENAMESIZE + 3];
    char* fileName;

    // Loop through all the lines of the file
    while(!inFile.eof()) {

    	// Get this line
        inFile.getline(inputLine, MAXFILENAMESIZE + 3);

        // Error-check
        if(inFile) {

        	// Parse the file name from out of the angle brackets
            fileName = getFileName(inputLine);
            
            // Add it to the list of files to request
            if(fileName != NULL) {
                fileList.push_back(fileName);
            }

        }
    }

    // Cleanup
    delete inputLine;
    inputLine = NULL;
    inFile.close();
}

/**
 * Helper function to parse filenames from the input file
 */
char* Client::getFileName(char* input) {

	// Error-check
    if(input == NULL)
        return NULL;

    size_t inputLen = 0;
    inputLen = strlen(input);

    // Check for the angle brackets
    if(input[0] == '<' && input[inputLen-1] == '>') {

    	// Parse out the file name
        char* fileName = new char[inputLen - 1];
        strncpy(fileName, (input+1), (inputLen - 2));
        fileName[inputLen-2] = '\0';

        return fileName;
    }
    
    // Fail on error
    return NULL;
}


/**
 * Primary function that will loop, sending requests from the input file to the load balancer
 */
void Client::sendFileRequests() {

	// Find the number of files
    int numberOfFilesToRequest = fileList.size();

    // For each file
    for(int i = 0; i < numberOfFilesToRequest; i++) {

    	// Send the request
        sendRequest(fileList[i]);

        // Wait
        sleep(1);
    }
}

/**
 * Helper function to send the request to the load balancer
 */
void Client::sendRequest(char* fileName) {

	// Error Check
    if(fileName != NULL) {

    	// Build the file request
        struct FileRequest fReq;
        strcpy(fReq.filePath, fileName);

        // Let the user know we're requesting this file
        cout << "Requesting file "<< fReq.filePath << endl;

        // Send the request
        if (send(sockfd, (void*)&fReq, sizeof(fReq), 0) == -1) {
            perror("send");
        }
    }
}

/**
 * Function to receive a response from the file system
 */
int Client::recieveFile(int clientFd) {

	// Prepare data structures
    int msgLength;
    FileTransferHeader* header = new FileTransferHeader;

    // Try to receive the file transfer header, fail on error
    if((msgLength = recv(clientFd, header, sizeof(FileTransferHeader), 0)) == -1) {

        delete header;
        close(clientFd);
        perror("Error Recieving Data from Socket in Request Listener in Load Balancer");
        return 1;

    } else if(msgLength > 0) {

    	// Let the client know we are receiving the file
        cout << "Receiving file " << header->filePath << endl;
        
        if(!(strcmp(header->filePath,"ERROR"))) {
            return 2;
        }
    }

    // Prepare a buffer to receive the file data
    size_t numBytesToRead = header->fileSize;
    char* file = new char[numBytesToRead];
    char* temp = file;

    // Receive the file
    while(numBytesToRead > 0) {

    	// Fill the buffer and error check
        if((msgLength = recv(clientFd, temp, numBytesToRead, 0)) == -1) {

            close(clientFd);
            perror("Error Recieving Data from Socket in Request Listener in Load Balancer");
            return 1;

        // Update remaining
        } else if(msgLength > 0) {
            numBytesToRead-=msgLength;
            temp = temp+ msgLength;
        }
    }

    // Output the file in the client directory
    writeFile(header->filePath, file, header->fileSize);
    
    return 0;
}

/**
 * Helper function to write out the contents of the files we receive
 */
void Client::writeFile(char* path, char* fileData, size_t fileSize) {

	// Build the file path
    char writePath[strlen(path) + 8];
    strcpy(writePath,"client/");
    strcat(writePath,path);
  
    // Open the file, should create it
    FILE * fptr = fopen(writePath, "w");

    // error check
    if(fptr == NULL) {
        perror("Error opening new file for write");
        return;
    }

    // Write it out
    fwrite (fileData , 1 , fileSize , fptr );

    // Cleanup
    fclose (fptr);

    return;
}


/**
 * Builds the server thread that will listen for responses from the load balancer,
 * 	and loops, blocking until a file response arrives
 */
int Client::makeServer() {

	// Setup data structures
    int serverfd, new_fd;  // listen on sock_fd, new connection on new_fd
    struct addrinfo hints, *servinfo, *p;
    struct sockaddr_storage their_addr; // connector's address information
    socklen_t sin_size;
    int yes=1;
    char s[INET6_ADDRSTRLEN];
    int rv;
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // use my IP

    // Get address information
    if ((rv = getaddrinfo(NULL, PORT, &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        return 1;
    }

    // Fix from Beej's guide for 'localhost' issues
    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((serverfd = socket(p->ai_family, p->ai_socktype,
                p->ai_protocol)) == -1) {
            perror("server: socket");
            continue;
        }

        if (setsockopt(serverfd, SOL_SOCKET, SO_REUSEADDR, &yes,
                sizeof(int)) == -1) {
            perror("setsockopt");
            exit(1);
        }

        // Bind to this listener port
        if (bind(serverfd, p->ai_addr, p->ai_addrlen) == -1) {
            close(serverfd);
            perror("server: bind");
            continue;
        }

        break;
    }

    // Error check
    if (p == NULL)  {
        fprintf(stderr, "server: failed to bind\n");
        return 2;
    }

    // Cleanup
    freeaddrinfo(servinfo);

    // Make sure this is working
    if (listen(serverfd, BACKLOG) == -1) {
        perror("listen");
        exit(1);
    }
    sin_size = sizeof their_addr;
    

    // Loop, receiving files
    while(true) {

    	// Block on a connection and error check
    	new_fd = accept(serverfd, (struct sockaddr *)&their_addr, &sin_size);
    	if (new_fd == -1) {
        	perror("accept");
    	}

    	// maintenance
    	inet_ntop(their_addr.ss_family,
        get_in_addr((struct sockaddr *)&their_addr), s, sizeof s);

    	// Receive the file
        if(recieveFile(new_fd) == 2) {
            cout << "Error processing file request" << endl;
        }

        // Cleanup
        close(new_fd);
    }

    return new_fd;
}
