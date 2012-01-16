/*
 * Represents a client file request
 *
 *  Created On: Feb 13, 2011
 *      Author: Jon Tedesco
 */


#ifndef FILEREQUEST_H
#define FILEREQUEST_H

/**
 * A file request object given the path to the file being requested
 */
struct FileRequest {
		/**
		 * The file path for this file request
		 */
		char filePath[256];
};

#endif /* FILEREQUEST_H */
