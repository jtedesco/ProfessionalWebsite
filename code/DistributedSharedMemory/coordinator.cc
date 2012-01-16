/**
 * This file holds entry point for the coordinator process
 */

#include "Common.h"
#include "Coordinator.h"
#include "DSM.h"

#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <map>

using namespace std;

// These values are just the initial guesses, they will be adjusted if they conflict with any
//  DSM node port or id.
#define COORDINATOR_ID          -1
#define COORDINATOR_PORT        50000

/**
 * The entry point for launching the coordinator node and beginning to read commands.
 *  Created on: April 19, 2011
 *      Author: Jon
 * 
 * @param argc  The number of command line arguments
 * @param argv  The command line arguments
 * @return      0 on success, 1 on error or failure of some kind
 */
int main(int argc, char *argv[]) {
    
    // First, check for proper usage
    if(argc != 2) {
        cout << "Invalid Usage!" << endl;
        cout << "\tUsage:  ./coordinator <command_file>" << endl;
        return 1;
    }
    
    // Try to open the given file path to the command file
    filebuf commandFile;
    commandFile.open(argv[1], ios::in);
    if(!commandFile.is_open()) {
        cout << "Could not open command file '" << argv[1] << "' to read commands!" << endl;
        return 2;
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
    
    // Choose a port and id for the coordinator node
    int coordinatorPort = COORDINATOR_PORT;
    int coordinatorId = COORDINATOR_ID;
    bool foundCollision = false;
    do {
        // No collision on this iteration yet
        foundCollision = false;
        
        // Check against the other node data
        for(unsigned int index = 0; index < nodeIds.size(); index++) {
            if(nodeIds[index]==coordinatorId || nodePorts[nodeIds[index]]==coordinatorPort) {
                foundCollision = true;
            }
        }
        
        // Update our guess if there was some collision
        if(foundCollision) {
            coordinatorId++;
            coordinatorPort++;
        }
        
      // Keep going until we didn't have any problems
    } while(foundCollision);

    // Build the coordinator node
    NodeData nodeData;
    nodeData.nodeId = coordinatorId;
    nodeData.nodePortNumber = coordinatorPort;
    map<int, unsigned char> nodeFileData; // There's no data stored at the coordinator, leave this empty
    Coordinator coordinator(nodeData, nodePorts, memoryMap, nodeFileData, nodeIds, &commandFile);
    
    // Start the coordinator running
    coordinator.start();
}
