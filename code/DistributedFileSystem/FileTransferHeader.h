/*
 * Simple struct for communicating information between processes. This header
 * 	would be followed by a file transfer, if the header does not indicate
 * 	and error.
 *
 *  Created on: Feb 20, 2011
 *      Author: jon
 */

#ifndef FILETRANSFERHEADER_H
#define FILETRANSFERHEADER_H

#include <stdio.h>
#include <stdlib.h>

/**
 * This header will be sent before file transfers to indicate the size of the file
 * 	about to be transferred, as well as whether there was an error.
 *
 * This header may serve the purpose of indicating an error as well, if the 'filePath'
 * 	field is the string literal "ERROR".
 */
struct FileTransferHeader {
	char filePath[256];
	size_t fileSize;
};

#endif /* FILETRANSFERHEADER_H */
