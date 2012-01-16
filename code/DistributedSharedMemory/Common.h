/* 
 * File:   Common.h
 * Author: jon
 *
 * Created on April 26, 2011, 2:26 PM
 * 
 * Holds some common functionality for the rest of the project.
 */

#include <string>
#include <vector>
#include <map>
using namespace std;

#ifndef COMMON_H
#define	COMMON_H

/**
 * Helper function that tokenizes strings, used to parse input to this process. If tokens is not empty
 *  when this function is called, it will be emptied before inserting the tokens from this input.
 *
 *  @param  str         The string to tokenize
 *  @param  tokens      An empty vector to be filled with the tokens
 *  @param  delimiters  The delimiting string (space, comma, etc)
 */
void tokenize(const string & str, vector<string> & tokens, const string & delimiters);


/**
 * Helper function to populate a memory map from the 'memory_map.conf' file. This will return the global
 *  memory map of memory locations mapped to nodes where they should be held.
 * 
 * @param memoryMapFileInputStream      The file input stream for the memory map file
 * @param memoryMap                     A map to be filled with memory location map information
 * @return true on success, false otherwise
 */
bool populateMemoryMap(istream & memoryMapFileInputStream, map<int, int> & memoryMap);


/**
 * Helper function to populate a vector of node ids and a map of node ids to ports from an input
 *  stream from the configuration file.
 * 
 * @param configurationFileInputStream  An input file stream from the configuration file
 * @param nodeIds                       A vector that will be filled with all node ids
 * @param nodePorts                     A map that will be filled with all port data for nodes
 * @return true on success, false otherwise.
 */
bool populateCommunicationData(istream & configurationFileInputStream, vector<int> & nodeIds, map<int, int> & nodePorts);

#endif	/* COMMON_H */
