/**
 * This file provides commonly used code to all portions of the project.
 *  Created on: April 19, 2011
 *      Author: Jon
 */

#ifndef COMMON_H_
#define COMMON_H_

#include <string>
#include <vector>
#include <iostream>
#include <fstream>
#include <stdlib.h>

#include "Common.h"

using namespace std;

/**
 * Helper function that tokenizes strings, used to parse input to this process. If tokens is not empty
 *  when this function is called, it will be emptied before inserting the tokens from this input.
 *
 *  @param  str         The string to tokenize
 *  @param  tokens      An empty vector to be filled with the tokens
 *  @param  delimiters  The delimiting string (space, comma, etc)
 */
void tokenize(const string & str, vector<string> & tokens, const string & delimiters) {

    // Skip delimiters at beginning.
    string::size_type lastPos = str.find_first_not_of(delimiters, 0);

    // Empty the input vector
    tokens.empty();

    // Find first "non-delimiter".
    string::size_type pos     = str.find_first_of(delimiters, lastPos);

    while (string::npos != pos || string::npos != lastPos) {
        
        // Found a token, add it to the vector.
        tokens.push_back(str.substr(lastPos, pos - lastPos));

        // Skip delimiters.  Note the "not_of"goal[newServer]--;
        lastPos = str.find_first_not_of(delimiters, pos);

        // Find next "non-delimiter"
        pos = str.find_first_of(delimiters, lastPos);
    }
}

/**
 * Helper function to populate a memory map from the 'memory_map.conf' file. This will return the global
 *  memory map of memory locations mapped to nodes where they should be held.
 * 
 * @param memoryMapFileInputStream      The file input stream for the memory map file
 * @param memoryMap                     A map to be filled with memory location map information
 * @return true on success, false otherwise
 */
bool populateMemoryMap(istream & memoryMapFileInputStream, map<int, int> & memoryMap) {
    
    // Read through this file stream
    while(memoryMapFileInputStream.good()) {

        // Read a line from the file, skip it if it's blank
        string memoryMapLine;
        getline(memoryMapFileInputStream, memoryMapLine);
        if(memoryMapLine.size() == 0) {
            continue;
        }

        // Parse this input line into tokens, and check for errors
        vector<string> memoryMapLineTokens;
        tokenize(memoryMapLine, memoryMapLineTokens, " ");
        if(memoryMapLineTokens.size() != 2) {
            cout << "Invalid memory map file!" << endl;
            return false;
        }

        // Parse out the tokens and add this data to our data structure
        int memoryId = atoi(memoryMapLineTokens[0].c_str());
        int nodeId = atoi(memoryMapLineTokens[1].c_str());
        memoryMap[memoryId] = nodeId;
    }
    
    // Success!
    return true;
}

/**
 * Helper function to populate a vector of node ids and a map of node ids to ports from an input
 *  stream from the configuration file.
 * 
 * @param configurationFileInputStream  An input file stream from the configuration file
 * @param nodeIds                       A vector that will be filled with all node ids
 * @param nodePorts                     A map that will be filled with all port data for nodes
 * @return true on success, false otherwise.
 */
bool populateCommunicationData(istream & configurationFileInputStream, vector<int> & nodeIds, map<int, int> & nodePorts) {
    
    // Read through the configuration file
    while(configurationFileInputStream.good()) {
        
        // Read a line from the file, skip it if it's blank
        string nodeConfiguration;
        getline(configurationFileInputStream, nodeConfiguration);
        if(nodeConfiguration.size() == 0) {
            continue;
        }
        
        // Parse this input line into tokens, and check for errors
        vector<string> configurationTokens;
        tokenize(nodeConfiguration, configurationTokens, " ");
        if(configurationTokens.size() != 2) {
            cout << "Invalid configuration file!" << endl;
            return false;
        }
        
        // Parse out the port and node id from this line
        int nodeId = atoi(configurationTokens[0].c_str());
        int port = atoi(configurationTokens[1].c_str());
        
        // Add this node and node data to the two data structure we're building
        nodeIds.push_back(nodeId);
        nodePorts[nodeId] = port;
    }
    
    // Else success
    return true;
}
#endif