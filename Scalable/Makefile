# G van Andel
# makefile

JC=javac
JT=-g -d bin/ -cp src
JF=-g -d . -cp src

defualt: all

test : 
	$(JC) $(JT) src/cs455/scaling/*/*.java

all : 
	$(JC) $(JF) src/cs455/scaling/*/*.java

clean :
	rm -f bin/cs455/scaling/*/*.class
	rm -f cs455/scaling/*/*.class 

package : clean
	tar -cvf van_Andel-Gerhard-HW2-PC.tar --exclude='bin' --exclude='.[^/]*' *
