README
======
As part of this assignment you will be working with datasets released by the United States Census Bureau. You will be developing MapReduce programs that parse and process the 1990 US Census dataset to support knowledge extraction over demographic data from all fifty states. Additional information about the assignment is available here.

ASSIGNMENT
==========
https://www.cs.colostate.edu/~cs455/CS455-Spring17-HW3-PC.pdf

To compile:
    ant

To clean:
    ant clean

For Q7 and Q8:
    javac util/FinalQuestion.java
    java util/FinalQuestion Hadoop_output.txt

For Q9:
    On a per-state basis, what is the percentage of residences based on race that rented vs. owned.

This package includes the following files.

├── build.xml
├── README.txt
├── src
│   └── cs455
│       └── hadoop
│           ├── data
│           │   ├── DQ1.java
│           │   ├── DQ2.java
│           │   ├── DQ3.java
│           │   ├── DQ4.java
│           │   ├── DQ5.java
│           │   ├── DQ6.java
│           │   ├── DQ7.java
│           │   ├── DQ8.java
│           │   └── DQ9.java
│           ├── job
│           │   └── CensusJob.java
│           ├── mapper
│           │   └── CensusStateMapper.java
│           ├── reducer
│           │   └── CensusStateReducer.java
│           ├── util
│           │   ├── CollectData.java
│           │   ├── ReduceStateData.java
│           │   └── WritableStateData.java
│           └── writables
│               ├── WQ1.java
│               ├── WQ2.java
│               ├── WQ3.java
│               ├── WQ4.java
│               ├── WQ5.java
│               ├── WQ6.java
│               ├── WQ7.java
│               ├── WQ8.java
│               └── WQ9.java
└── util
    ├── FinalQuestion.java
    ├── large_info.txt
    ├── Q7_Q8.txt
    └── small_info.txt

