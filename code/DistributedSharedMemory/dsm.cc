
#include "Common.h"
#include "DSM.h"

#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <map>

using namespace std;


/**
 * Using all the parsed data from the input files, launch each DSM node with the appropriate data.
 * 
 * @param nodeIds       A list of all node ids we found.
 * @param nodePorts     A map of node ids to their port numbers
 * @param memoryMap     A map of memory ids to the node that initially has it
 * 
 * @return      0 on success, nonzero error code on error
 */
int launchDSMNodes(vector<int> & nodeIds, map<int, int> nodePorts, map<int, int> memoryMap) {
    
    // Iterate through each node in our list of node ids
    for(unsigned int index = 0; index < nodeIds.size(); index++) {
        
        // Get all data that we already have out for this node
        int nodeId = nodeIds[index];
        int port = nodePorts[nodeId];
        
        // Build the initial memory map for this node using our memory map
        map<int, unsigned char> nodeMemoryMap;
        for(unsigned int searchIndex = 0; searchIndex < memoryMap.size(); searchIndex++) {
            
            // Check to see if this entry in the memory map is our node
            if(memoryMap[searchIndex] == nodeId) {
                
                // Add this memory location to this node's memory map, and give it value 0 initially
                nodeMemoryMap[searchIndex] = 0;
            }
        }
        
        // Now we have all data, fork a process for this new DSM node
        if(!fork()) {
            
            // Child process!
            
            // Create a new node object
            NodeData nodeData;
            nodeData.nodeId = nodeId;
            nodeData.nodePortNumber = port;
            DSM dsmNode(nodeData, nodePorts, memoryMap, nodeMemoryMap, nodeIds);
            
            // Start it!
            dsmNode.start();
            exit(0);
            
        }
    }
    
    cout << "Successfully launched " << nodeIds.size() << " DSM nodes" << endl;
    while(true){}
    
    return 0;
}


/**
 * The entry point for launching all DSM nodes.
 *  Created on: April 19, 2011
 *      Author: Jon
 * 
 * @param argc  The number of command line arguments
 * @param argv  The command line arguments
 * @return      0 on success, nonzero on error or failure of some kind
 */
int main(int argc, char *argv[]) {

    // Check for valid usage
    if(argc != 2) {
        cout << "Invalid Usage!" << endl;
        cout << "\tUsage:  ./dsm <id>" << endl;
        return 1;
    }
    
    // Open 'membership.conf', and fail on error
    filebuf configurationFile;
    configurationFile.open("membership.conf", ios::in);
    if(!configurationFile.is_open()) {
        cout << "Could not open 'membership.conf'!" << endl;
        return 2;
    }
    istream configurationFileInputStream(&configurationFile);
    

    // Open 'memory_map.conf', and fail on error
    filebuf memoryMapFile;
    memoryMapFile.open("memory_map.conf", ios::in);
    if(!memoryMapFile.is_open()) {
        cout << "Could not open 'memory_map.conf'!" << endl;
        return 3;
    }
    istream memoryMapFileInputStream(&memoryMapFile);
    
    // Iterate through each line of the memory map input file, and build a memory map data structure
    map<int, int>  memoryMap;
    if(!populateMemoryMap(memoryMapFileInputStream, memoryMap)) {
        configurationFile.close();
        return 3;
    }
    
    // Iterate through each line of the configuration file, and build a vector of node ids we find, 
    //  a map of node ids to port numbers
    vector<int> nodeIds;
    map<int, int> nodePorts;
    if(!populateCommunicationData(configurationFileInputStream, nodeIds, nodePorts)) {
        configurationFile.close();
        return 3;
    }

    // Launch all DSM nodes using this parsed data
    return launchDSMNodes(nodeIds, nodePorts, memoryMap);
}
