#################################################### Compiler Flags ####################################################

CC=g++
CFLAGS= -c -g -Wall -O3

############################################## Default Action for 'make all' ###########################################

all: dsm coordinator

############################################## The executable to be made ###############################################

dsm:    Common.o Coordinator.o DSM.o dsm.o 
			$(CC) -lpthread -lm Common.o Coordinator.o DSM.o dsm.o -o dsm

coordinator:    Common.o Coordinator.o DSM.o coordinator.o
			$(CC) -lpthread -lm Common.o Coordinator.o DSM.o coordinator.o -o coordinator

##################################################### Components of each ###############################################


Common.o: Common.cpp Common.h NodeData.h Command.h
			$(CC) $(CFLAGS) Common.cpp
	
Coordinator.o: Common.o Coordinator.cpp Coordinator.h
			$(CC) $(CFLAGS) Coordinator.cpp Coordinator.h

DSM.o: Common.o DSM.cpp DSM.h
			$(CC) $(CFLAGS) DSM.cpp DSM.h

dsm.o: Common.o dsm.cc
			$(CC) $(CFLAGS) dsm.cc

coordinator.o: Common.o coordinator.cc
			$(CC) $(CFLAGS) coordinator.cc


##################################################### Cleaning Script ##################################################
clean:
			rm -rf *o *gch dsm coordinator
