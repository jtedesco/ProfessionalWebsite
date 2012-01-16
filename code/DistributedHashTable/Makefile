#################################################### Compiler Flags ####################################################

CC=g++
CFLAGS= -c -g -Wall -lpthread -lm

############################################## Default Action for 'make all' ###########################################

all: chord_sys

############################################## The executable to be made ###############################################

chord_sys:    Listener.o Node.o sha1.o chord_sys.o
			$(CC) -lpthread -lm Listener.o Node.o sha1.o chord_sys.o -o chord_sys

##################################################### Components of each ###############################################

Listener.o: Listener.cpp Listener.h FileData.h NodeData.h Command.h
			$(CC) $(CFLAGS) Listener.cpp Listener.h

Node.o: Node.cpp Node.h FileData.h NodeData.h Command.h
			$(CC) $(CFLAGS) Node.cpp Node.h

sha1.o: SHA1/sha1.c SHA1/sha1.h
			$(CC) $(CFLAGS) SHA1/sha1.c SHA1/sha1.h

chord_sys.o: chord_sys.cc  FileData.h NodeData.h Command.h
			$(CC) $(CFLAGS) chord_sys.cc

##################################################### Cleaning Script ##################################################
clean:
			rm -rf SHA1/*o SHA1/*gch *o *gch chord_sys