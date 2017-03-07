README
======

This package includes the following files.

README.txt [This file]
Makefile
src
└── cs455
    └── scaling
        ├── client
        │   ├── Client.java
        │   ├── Receiver.java
        │   └── Sender.java
        ├── msg
        │   ├── Hash.java
        │   └── Message.java
        ├── server
        │   ├── NioServer.java
        │   ├── Server.java
        │   └── TaskManager.java
        ├── task
        │   ├── ReadTask.java
        │   ├── SendTask.java
        │   └── Task.java
        └── util
            ├── Processor.java
            ├── Queue.java
            └── ThreadPool.java

To compile:
    make all

To clean:
    make clean
