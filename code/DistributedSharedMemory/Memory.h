#ifndef MEMORY_H_
#define MEMORY_H_


/**
 * Outlines data for a memory entry, to implement caching.
 */
typedef struct {

	/**
	 * The port number on which to connect to this node
	 */
	bool isDirty;

	/**
	 * The id of this node
	 */
	unsigned char value;
} Memory;


#endif /* MEMORY_H_ */
