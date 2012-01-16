/*
 * The entry point for the application.
 *
 *  Created on: Mar 14, 2011
 *      Author: Jon
 */

#include <iostream>
#include <fstream>
using namespace std;

#include "Listener.h"
#include "stdlib.h"
#define  basePortNumber     40000


/**
 * The entry point for the distributed hash table, starts up the listener and introducer.
 */
int main(int argc, char *argv[]) {

    // Pull the 'm' parameter from command line, and if it exists, the input file path
    int m;
    filebuf inputFile;
    if(argc<2 || argc>3) {
        cout << "Invalid usage!" << endl;
        cout << "Usage:     ./chord_sys <m> [<inputFile>]" << endl;
        return 1;
    } else {

        // Parse the first argument
        m = atoi(argv[1]);
        if (m <= 0) {
            cout << "Invalid usage!" << endl;
            cout << "Usage:     ./chord_sys <m>" << endl;
            return 1;
        }

        // Check for an input file
        if(argc==3) {

            // Try to open the given input file
            inputFile.open(argv[2], ios::in);

            // Handle the case where this file is locked or doesn't exist
            if(!inputFile.is_open()) {
                cout << "Error opening input file!" << endl;
                return 1;
            }
        }
    }

    // Start the program
    cout << "Starting listener..." << endl;

    // Build a listener object and initialize it
    Listener listener(basePortNumber, m);
    if(!listener.initialize()) {
        cout << "Failed to initialize listener!" << endl;
    }

    // Run the listener (no input file)
    if(argc==2) {
        listener.run();
    }

    // Run the listener from an input file
    if(argc==3) {
        listener.runFromFile(&inputFile);
    }

    return 0;
}
