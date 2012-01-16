#################################################### Compiler Flags ############################################################

CC=g++
CFLAGS=-c -g -Wall -lpthread

############################################## Default Action for 'make all' ###################################################

all: start-server start-client

############################################## The two executables to be made ##################################################

start-server:    StartFileSystem.o DistributedServer.o LoadBalancer.o
			$(CC) -lpthread LoadBalancer.o StartFileSystem.o DistributedServer.o -o start-server

start-client:    Client.o StartClient.o
			$(CC) -lpthread Client.o StartClient.o -o start-client

##################################################### Components of each #######################################################

StartClient.o: StartClient.cpp
			$(CC) $(CFLAGS) StartClient.cpp	

StartFileSystem.o: StartFileSystem.cpp
			$(CC) $(CFLAGS) StartFileSystem.cpp	

DistributedServer.o: DistributedServer.cpp DistributedServer.h
			$(CC) $(CFLAGS) DistributedServer.cpp DistributedServer.h

LoadBalancer.o: LoadBalancer.cpp LoadBalancer.h FileRequest.h FileTransferHeader.h ServerConnectionData.h
			$(CC) $(CFLAGS) LoadBalancer.cpp LoadBalancer.h FileRequest.h FileTransferHeader.h ServerConnectionData.h

Client.o: Client.cpp Client.h
			$(CC) $(CFLAGS) Client.cpp Client.h
			
##################################################### Cleaning Script #######################################################
clean:
			rm -rf *o start-server start-client
