README
======

This package includes the following files.

README.txt [This file]
MAKEFILE
cs455
    └── overlay
        ├── msg
        │   ├── EdgeMessage.java - The message sent when a MessagingNode connects to another MessagingNode
        │   ├── InitiateMessage.java - The Message sent from the Registry to the MessagingNodes to start
        │   ├── OverlayMessage.java - Contains the overlay sent to the MessagingNodes
        │   ├── ProtocolMessage.java - The class that holds the byte array of each message sent
        │   ├── RegistationMessage.java - Contains serveral messages.
        │   ├── StatisticsMessage.java - Contains the message of stats
        │   └── TaskMessage.java - The message sent between MessageingNodes
        ├── node
        │   ├── AbstractServer.java - Abstract class of the servers
        │   ├── MessagingClient.java - Connects to the registry
        │   ├── MessagingConnection.java - Connects to other MessaingNodes
        │   ├── MessagingNode.java - MessagingNode main driver with interface
        │   ├── MessagingProcessor.java - Process messages in the inbox MessageQueue
        │   ├── MessagingSender.java - Sends messages in the outbox MessageQueue
        │   ├── MessagingServer.java - Message server that extends AbstractServer
        │   ├── Registry.java - Registry main driver with interface
        │   └── RegistryServer.java - Registry server that extends AbstractServer
        └── util
            ├── Dijkstra.java - Held by the MessagingServer and used to computer shortest path
            ├── MessagePair.java - Used within the MessageQueue. Conatins a ProtocolMessage and MessagingConnection
            ├── MessageQueue.java - Used to store and process messages.
            ├── RegistryInfo.java - Used to register MessagingNodes
            └── StatisticsCollector.java - Used on the MessagingNode to collect information

To compile:
    make all

To clean:
    make clean
