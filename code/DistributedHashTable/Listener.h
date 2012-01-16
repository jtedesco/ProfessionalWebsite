/*
 * This class represents the process that will run and listen for input from stdin, and
 * 	create input commands and send them to the introducer as they appear.
 *
 *  Created on: Mar 13, 2011
 *      Author: Jon
 */
#ifndef LISTENER_H_
#define LISTENER_H_

#include "Command.h"

#include <iostream>
#include <string>
#include <vector>
using namespace std;

/**
 * This class represents the process that will run and listen for input from stdin, and
 * 	create input commands and send them to the introducer as they appear.
 */
class Listener {

    public:

        /**
         * Builds a listener object, given only the port number for the introducer
         *
         *  @param  introducerPort  The port on which to connect to the introducer
         *  @param  m               The parameter for the hash table
         */
        Listener(int introducerPort, int m);

        /**
         * Free all memory associated with this object
         */
        virtual ~Listener();

        /**
         * Intialize data structures and connections for this listener.
         *
         * 	@return	true on success, false otherwise
         */
        bool initialize();

        /**
         * Start up the listener, and begin listening for command line input
         */
        void run();

        /**
         * Start up the listener, and begin reading input from a given input file
         *
         *  @param  inputFile   The file from which to read commands
         */
        void runFromFile(filebuf * inputFile);

        /**
         * Helper function to parse (fill) a command object from stdin and send it to the introducer
         *
         * 	@param	inputLine			The string pulled from stdin
         */
        bool handleCommand(const string & inputLine);

    private:

        /**
         * The port number on which to connect to the introducer
         */
        int introducerPort;

        /**
         * The parameter for the hash table
         */
        int m;

        /**
         * The total count of messages sent in the lifetime of this listener
         */
        int totalMessageCount;

        /**
         * The last port number assigned to a node (walks up as we assign nodes)
         */
        int lastPortAssigned;

        /**
         * Helper function to setup a client socket to the introducer and connect
         *
         *   @return   A nonzero socket file descriptor on succes (already connected), -1 on failure
         */
        int connectToIntroducer();

        /**
         * Handle an ADD_NODE command request
         *
         *  @param  inputTokens  The parsed input, as a list of strings
         */
        bool handleAddNode(vector<string> & inputTokens);

        /**
         * Handle an ADD_FILE command request
         *
         *  @param  inputTokens  The parsed input, as a list of strings
         */
        bool handleAddFile(vector<string> & inputTokens);

        /**
         * Handle an DEL_FILE command request
         *
         *  @param  inputTokens  The parsed input tokens, as a list of strings
         */
        bool handleDelFile(vector<string> & inputTokens);

        /**
         * Handle an FIND_FILE command request
         *
         *  @param  inputTokens  The parsed input tokens, as a list of strings
         */
        bool handleFindFile(vector<string> & inputTokens);

        /**
         * Handle an GET_TABLE command
         *
         *  @param  inputTokens  The parsed input tokens, as a list of strings
         */
        bool handleGetTable(vector<string> & inputTokens);

        /**
         * Handle an QUIT command
         *
         *  @param  inputTokens  The parsed input tokens, as a list of strings
         */
        bool handleQuit(vector<string> & inputTokens);

        /**
         * Handle a SLEEP command
         *
         *  @param  inputTokens  The parsed input tokens, as a list of strings
         */
        bool handleSleep(vector<string> & inputTokens);

        /**
         * Helper function that tokenizes strings
         *
         *  @param  str         The string to tokenize
         *  @param  tokens      An empty vector to be filled with the tokens
         *  @param  delimiters  The delimiting string (space, comma, etc)
         */
        void tokenize(const string & str, vector<string> & tokens, const string & delimiters);

        /**
         * Helper function to send a command struct to the introducer.
         *
         *    @param  command     The command to send to the introducer
         */
        bool sendCommand(const Command & command);

        /**
         * Helper function to compute the hash of the file (using the SHA1 library)
         *
         *  @param  inputTokens  The tokenized input
         */
        int computeHash(char* fileName);
};

#endif
