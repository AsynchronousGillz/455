README
======

ASSIGNMENT
==========
https://www.cs.colostate.edu/~cs555/CS555-Fall2015-HW1.pdf

This package includes the following files.

README.txt [This file]
Makefile
src/
└── cs455
    └── fileSystem
        ├── chunk
        │   ├── AbstractServer.java
        │   ├── ChunkNode.java
        │   ├── ChunkServer.java
        │   ├── Connection.java
        │   ├── MessagingProcessor.java
        │   └── MessagingSender.java
        ├── client
        │   └── MessagingClient.java
        ├── controller
        │   ├── Master.java
        │   └── MasterServer.java
        ├── msg
        │   └── ProtocolMessage.java
        ├── task
        │   ├── ChunkTask.java
        │   ├── HeartMaxTask.java
        │   ├── HeartMinTask.java
        │   └── Task.java
        └── util
            ├── Dijkstra.java
            ├── MessagePair.java
            ├── MessageQueue.java
            └── RegistryInfo.java

To compile:
    make all

To clean:
    make clean

