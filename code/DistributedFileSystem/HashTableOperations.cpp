/**
 * This file contains common comparison functions used for hashtable
 * 	usage in other files.
 */
#include <stdio.h>
#include <string.h>

/**
 * Compares strings (for string-indexed hashtables)
 */
struct eqstr{
  bool operator()(const char* s1, const char* s2) const {
    return strcmp(s1,s2)==0;
  }
};
