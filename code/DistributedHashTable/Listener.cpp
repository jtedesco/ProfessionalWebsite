/*
 * This class represents the process that will run and listen for input from stdin, and
 * 	create input commands and send them to the introducer as they appear.
 *
 *  Created on: Mar 14, 2011
 *      Author: Jon
 */

#include "Listener.h"
#include "Node.h"
#include "NodeData.h"
#include "SHA1/sha1.h"
#include <arpa/inet.h>
#include <stdio.h>
#include <sstream>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <pthread.h>
#include <vector>
#include <iostream>
#include <string>
#include <stdlib.h>
#include <math.h>
#include <iostream>
#include <fstream>
using namespace std;

#define MAX_RECONNECT_ATTEMPTS  10

/**
 * Builds a listener object, given only the port number for the introducer
 *
 *  @param  introducerPort  The port on which to connect to the introducer
 *  @param  m               The parameter for the hash table
 */
Listener::Listener(int introducerPort, int m){
    this->introducerPort = introducerPort;
    this->m = m;
    totalMessageCount = 0;
}

/**
 * Free all memory associated with this object
 */
Listener::~Listener(){
    // None
}

/**
 * Intialize data structures and connections for this listener.
 *
 * 	@return	true on success, false otherwise
 */
bool Listener::initialize(){

    // Fork a new process for the introducer
    
    if(!fork()) {

        // Create a new NodeData struct to build the introducer
        NodeData introducerData;
        introducerData.nodePortNumber = introducerPort;
        introducerData.nodeId = 0;

        // Build a new introducer and initialize it

        Node introducer(introducerData,introducerData,introducerData,m);

        // Start running the introducer
        introducer.listenForCommands();
        pthread_exit(NULL);

    }
    lastPortAssigned = introducerPort;
    totalMessageCount = 0;
    return true;
}

/**
 * Start up the listener, and begin listening for command line input
 */
void Listener::run() {

    // Begin looping
    while(true) {

        // Wait for console input
        string input;
        getline(cin, input);
        if(input.size() == 0) {
            continue;
        }

        // Parse & send the command
        bool successful = handleCommand(input);
        if(!successful) {
            cout << "Error handling command, try again:" << endl;
            continue;
        }
    }
}

/**
 * Start up the listener, and begin reading input from a given input file
 *
 *  @param  inputFile   The file from which to read commands
 */
void Listener::runFromFile(filebuf * inputFile) {

    // Open the input file
    istream inputFileStream(inputFile);

    // Begin looping
    while(inputFileStream.good()) {

        // Wait for console input
        string input;
        getline(inputFileStream, input);
        if(input.size() == 0) {
            continue;
        }

        // Parse & send the command
        bool successful = handleCommand(input);
        if(!successful) {
            cout << "Error handling command, try again:" << endl;
            continue;
        }
    }
}

/**
 * Helper function to parse (fill) a command object from stdin and send it to the introducer
 *
 * 	@param	inputLine			The string pulled from stdin
 */
bool Listener::handleCommand(const string & inputLine){

   // Tokenize the input
    vector<string> inputTokens;
    tokenize(inputLine, inputTokens, " ");

    // Error check
    if(inputTokens.size() == 0) {
        cout << "Error parsing input!" << endl;
        return false;
    }

    // Parse the command out of the input
    if(!strcmp(inputTokens[0].c_str(), "ADD_NODE")) {

        // Check for correct usage of this command
        if(inputTokens.size()<2){
            cout << "Improper usage of 'ADD_NODE'!" << endl;
            cout << "   Usage:      ADD_NODE <id> [<ids>]" << endl;
            return false;
        }

        // Handle ADD_NODE command
        return handleAddNode(inputTokens);

    } else if(!strcmp(inputTokens[0].c_str(), "ADD_FILE")) {

        // Check for proper usage of this command
        if(inputTokens.size() != 3) {
            cout << "Improper usage of 'ADD_FILE'!" << endl;
            cout << "   Usage:      ADD_FILE <filename> <ip_address>" << endl;
            return false;
        }

        // Handle the ADD_FILE command
        return handleAddFile(inputTokens);

    } else if(!strcmp(inputTokens[0].c_str(), "DEL_FILE")) {

        // Check for proper usage of this command
        if(inputTokens.size() != 2) {
            cout << "Improper usage of 'DEL_FILE'!" << endl;
            cout << "   Usage:      DEL_FILE <filename>" << endl;
            return false;
        }

        // Handle a DEL_FILE command
        return handleDelFile(inputTokens);

    } else if(!strcmp(inputTokens[0].c_str(), "FIND_FILE")) {

        // Check for proper usage of this command
        if(inputTokens.size() != 2) {
            cout << "Improper usage of 'FIND_FILE'!" << endl;
            cout << "   Usage:      FIND_FILE <filename>" << endl;
            return false;
        }

        // Handle FIND_FILE command
        return handleFindFile(inputTokens);

    } else if(!strcmp(inputTokens[0].c_str(), "GET_TABLE")) {

        // Check for proper usage of this command
        if(inputTokens.size() != 2) {
            cout << "Improper usage of 'GET_TABLE'!" << endl;
            cout << "   Usage:      GET_TABLE <node_id>" << endl;
            return false;
        }

        // Handle GET_TABLE command
        return handleGetTable(inputTokens);

    } else if(!strcmp(inputTokens[0].c_str(), "QUIT")) {

        // Check for proper usage of this command
        if(inputTokens.size() != 1) {
            cout << "Improper usage of 'QUIT'!" << endl;
            cout << "   Usage:      QUIT" << endl;
            return false;
        }

        // Handle QUIT command
        return handleQuit(inputTokens);

    } else if(!strcmp(inputTokens[0].c_str(), "SLEEP")) {

        // Check for proper usage of this command
        if(inputTokens.size() != 2) {
            cout << "Improper usage of 'SLEEP'!" << endl;
            cout << "   Usage:      SLEEP <seconds>" << endl;
            return false;
        }

        // Handle SLEEP command
        return handleSleep(inputTokens);

    } else {
        cout << "Invalid command!" << endl;
        return false;
    }
}

/**
 * Handle an ADD_NODE command request
 *
 *  @param The parsed input, as a list of strings
 */
bool Listener::handleAddNode(vector<string> & inputTokens) {

    // Parse the first node entry and send a command to add this node
    int newNodeId = atoi(inputTokens[1].c_str());

    // Check if this is valid (between 0 and 2^m exclusive)
    bool successful = true;
    if(newNodeId > 0 && (newNodeId < (1<<m))) {

        // Create a new command
        Command anotherCommand;
        anotherCommand.commandCode = ADD_NODE;
        anotherCommand.nodeData.nodeId = newNodeId;
        anotherCommand.messageCount = 0;

        // Find a new port for this node
        lastPortAssigned++;
        anotherCommand.nodeData.nodePortNumber = lastPortAssigned;

        // Send this command to the introducer
        if(!sendCommand(anotherCommand)) {
            cout << "Error adding node with id " << newNodeId << endl;
            successful = false;
        } else {

            // Update the local total message count
            totalMessageCount++;
        }

    } else {
        cout << "'" << inputTokens[1] << "' is not a valid node id!" << endl;
        successful = false;
    }

    // Check if this is a 'multi'-add
    if(inputTokens.size()>2) {

        // Parse out all additional adds (all beyond the first)
        successful = true;
        for (unsigned int index = 2; index < inputTokens.size(); index++) {

            // Get the integer id of the node we're trying to add
            newNodeId = atoi(inputTokens[index].c_str());

            // Check if this is valid (between 0 and 2^m exclusive)
            if(newNodeId > 0 && newNodeId < (2<<m)) {

                // Create a new command
                Command anotherCommand;
                anotherCommand.commandCode = ADD_NODE;
                anotherCommand.nodeData.nodeId = newNodeId;
                anotherCommand.messageCount = 0;

                // Find a new port for this node
                lastPortAssigned++;
                anotherCommand.nodeData.nodePortNumber = lastPortAssigned;

                // Send the command to the introducer, skip it if it fails
                if(!sendCommand(anotherCommand)) {
                    cout << "Failed to send 'ADD_NODE' command for id " << newNodeId << ", skipping..." << endl;
                    successful = false;
                } else {

                    // Update the local total message count
                    totalMessageCount++;
                }

            } else {
                cout << "Invalid node id: '" << inputTokens[index] << "'!" << endl;
                successful = false;
            }
        }
    }

    // Else success
    return successful;
}

/**
 * Handle an ADD_FILE command request
 *
 *  @param The parsed input, as a list of strings
 */
bool Listener::handleAddFile(vector<string> & inputTokens) {

    // Form the command
    Command newCommand;
    newCommand.commandCode = ADD_FILE;
    strcpy(newCommand.fileData.filename, inputTokens[1].c_str());
    newCommand.fileData.filename[strlen(inputTokens[1].c_str())] = '\0';
    strcpy(newCommand.fileData.ipAddress, inputTokens[2].c_str());
    newCommand.fileData.ipAddress[strlen(inputTokens[2].c_str())] = '\0';
    newCommand.messageCount = 0;

    // Compute the hash of this new file and save it
    newCommand.fileData.filenameHashData = computeHash(newCommand.fileData.filename);

    // Send the command to the introducer
    if(!sendCommand(newCommand)) {

        cout << "Failed to send 'ADD_FILE' command for '" << inputTokens[1] << "'!" << endl;
        return false;

    } else {

        // Update the local total message count
        totalMessageCount++;
    }

    // Else success
    return true;
}

/**
 * Handle an DEL_FILE command request
 *
 *  @param The parsed input tokens, as a list of strings
 */
bool Listener::handleDelFile(vector<string> & inputTokens){

    // Form the command
    Command newCommand;
    newCommand.commandCode = DEL_FILE;
    strcpy(newCommand.fileData.filename, inputTokens[1].c_str());
    newCommand.fileData.filename[strlen(inputTokens[1].c_str())] = '\0';
    newCommand.messageCount = 0;

    // Compute the hash of this new file and save it
    newCommand.fileData.filenameHashData = computeHash(newCommand.fileData.filename);

    // Send the command to the introducer
    if(!sendCommand(newCommand)) {
        cout << "Failed to send 'DEL_FILE' command for '" << inputTokens[1] << "'!" << endl;
        return false;
    } else {

        // Update the local total message count
        totalMessageCount++;
    }

    // Else success
    return true;
}

/**
 * Handle an FIND_FILE command request
 *
 *  @param The parsed input tokens, as a list of strings
 */
bool Listener::handleFindFile(vector<string> & inputTokens) {

    // Form the command
    Command newCommand;
    newCommand.commandCode = FIND_FILE;
    strcpy(newCommand.fileData.filename, inputTokens[1].c_str());
    newCommand.fileData.filename[strlen(inputTokens[1].c_str())] = '\0';
    newCommand.messageCount = 0;

    // Compute the hash of this new file and save it
    newCommand.fileData.filenameHashData = computeHash(newCommand.fileData.filename);

    // Send the command to the introducer
    if(!sendCommand(newCommand)) {
        cout << "Failed to send 'FIND_FILE' command for '" << inputTokens[1] << "'!" << endl;
        return false;
    } else {

        // Update the local total message count
        totalMessageCount++;
    }

    // Else success
    return true;
}

/**
 * Handle an GET_TABLE command
 *
 *  @param The parsed input tokens, as a list of strings
 */
bool Listener::handleGetTable(vector<string> & inputTokens) {

    // Form the command
    Command newCommand;
    newCommand.commandCode = GET_TABLE;
    newCommand.messageCount = 0;
    int targetNodeId = atoi(inputTokens[1].c_str());
    if(targetNodeId > 0 || ((targetNodeId==0) && (strcmp(inputTokens[1].c_str(), "0")==0))) {
        newCommand.nodeData.nodeId = targetNodeId;
    } else {
        cout << "'" << inputTokens[1] << "' is not a valid node id!" << endl;
        return false;
    }

    // Send the command to the introducer
    if(!sendCommand(newCommand)) {
        cout << "Failed to send 'GET_TABLE' command for node " << inputTokens[1] << "!" << endl;
        return false;
    } else {
        // Update the local total message count
        totalMessageCount++;
    }

    // Else success
    return true;
}

/**
 * Handle an QUIT command
 *
 *  @param The parsed input tokens, as a list of strings
 */
bool Listener::handleQuit(vector<string> & inputTokens) {

    // Form the command
    Command newCommand;
    newCommand.commandCode = QUIT;
    newCommand.messageCount = 0;

    // Update the local total message count, and initialize the total message count parameter for the QUIT command
    totalMessageCount++;
    newCommand.totalMessageCount = totalMessageCount;

    // Send the command to the introducer
    if(!sendCommand(newCommand)) {
        cout << "Failed to send 'QUIT' command!" << endl;
        return false;
    }

    // Else success, wait a second for everyone to quit, and then quit
    sleep(1);
    exit(0);
}


/**
 * Handle a SLEEP command
 *
 *  @param The parsed input tokens, as a list of strings
 */
bool Listener::handleSleep(vector<string> & inputTokens) {

    // Get the number of seconds to sleep
    int secondsToSleep = atoi(inputTokens[1].c_str());

    // Sleep
    sleep(secondsToSleep);

    // Else success
    return true;
}

/**
 * Helper function to send a command struct to the introducer.
 *
 *    @param  command     The command to send to the introducer
 */
bool Listener::sendCommand(const Command & command){

    // Try to send it a few times, and just fail after ten attempts
    bool successful = false;
    int attemptCount = 0;
    while(!successful && attemptCount <= MAX_RECONNECT_ATTEMPTS) {

        // Update attempt counter, and if we were not successful, pause for an instant to wait for the node
        attemptCount++;
        if(!successful) {
            usleep(500);
        }

        // Connect to the introducer
        int introducerSocket = connectToIntroducer();
        if(introducerSocket == -1) {
            successful = false;
            continue;
        }

        // Send this command to the introducer
        if (sizeof(command) != send(introducerSocket, &command, sizeof(command), 0)) {
            close(introducerSocket);
            successful = false;
            continue;
        }

        // Else, success
        successful = true;
        close(introducerSocket);
    }

    // Return whether or not we were successful
    return successful;
}

/**
 * Helper function to setup a client socket to the introducer and connect
 */
int Listener::connectToIntroducer(){

    //Create a new socket for our 'client'
    int dummy = 0, clientSocketFd = 0;
    if ((clientSocketFd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
        cout << "Error creating client socket to connect to introducer!" << endl;
        return -1;
    }

    //Make the socket reusable
    if (setsockopt(clientSocketFd, SOL_SOCKET, SO_REUSEADDR, (char *)&dummy, sizeof(dummy)) == -1) {
        close(clientSocketFd);
        cout << "Error setting client socket options for connecting to the introducer!" << endl;
        return -1;
    }

    // Get the information for connecting to the introducer
    struct addrinfo hostInfo, * myInfo = NULL;
    char introducerPortString[256];
    sprintf(introducerPortString, "%d", introducerPort);
    memset(&hostInfo, 0, sizeof(hostInfo));
    hostInfo.ai_family = AF_INET;
    hostInfo.ai_socktype = SOCK_STREAM;
    int tmp;
    if ((tmp = getaddrinfo("localhost", introducerPortString, &hostInfo, &myInfo)) != 0) {
        cout << "Error using 'getaddrinfo()': " << gai_strerror(tmp) << endl;
        return -1;
    }

    // Try to connect to the introducer
    if (connect(clientSocketFd, myInfo->ai_addr, myInfo->ai_addrlen) != 0) {
        close(clientSocketFd);
        cout << "Error establishing connection to introducer!" << endl;
        return -1;
    }

    // Success, return this connected socket
    return clientSocketFd;
}

/**
 * Helper function that tokenizes strings
 *
 *  @param  str         The string to tokenize
 *  @param  tokens      An empty vector to be filled with the tokens
 *  @param  delimiters  The delimiting string (space, comma, etc)
 */
void Listener::tokenize(const string & str, vector<string> & tokens, const string & delimiters) {

    // Skip delimiters at beginning.
    string::size_type lastPos = str.find_first_not_of(delimiters, 0);

    // Empty the input vector
    tokens.empty();

    // Find first "non-delimiter".
    string::size_type pos     = str.find_first_of(delimiters, lastPos);

    while (string::npos != pos || string::npos != lastPos)
    {
        // Found a token, add it to the vector.
        tokens.push_back(str.substr(lastPos, pos - lastPos));

        // Skip delimiters.  Note the "not_of"goal[newServer]--;
        lastPos = str.find_first_not_of(delimiters, pos);

        // Find next "non-delimiter"
        pos = str.find_first_of(delimiters, lastPos);
    }
}

/**
 * Helper function to compute the hash of the file (using the SHA1 library)
 *
 *  @param  inputTokens  The tokenized input
 */
int Listener::computeHash(char* fileName) {

    // Handle a null filename (error case)
    if(fileName == NULL) {
        cout << "Invalid Filename" << endl;
        return -1;
    }

    // Initialize the data to hash
    int key = -1;
    SHA1Context sha;
    SHA1Reset(&sha);
    SHA1Input(&sha, (unsigned char*) fileName, strlen(fileName));

    // Get the hash
    if (!SHA1Result(&sha)) {
        cout << "Error computing filename hash !" << endl;
        return -1;
    } else {
        key = sha.Message_Digest[4]%((int)pow(2,m));
    }

    // Return the file hash as computed by SHA-1
    return key;
}
