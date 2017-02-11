# G van Andel
# makefile

JC=javac
JF=-g -d bin/ -cp src

defualt: all

all : 
	$(JC) $(JF) src/cs455/overlay/*/*.java

clean :
	rm bin/cs455/overlay/*/*.class 

package :
	rm bin/cs455/overlay/*/*.class 
	tar -cvf van_Andel-Gerhard-HW1-PC.tar --exclude='test.sh' --exclude='.[^/]*' *
