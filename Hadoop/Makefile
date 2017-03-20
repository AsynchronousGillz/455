# G van Andel
# makefile

JC=javac
JF=-g -d bin/ -cp src

defualt: all

all : 
	$(JC) $(JF) src/cs455/hadoop/*/*.java

clean :
	rm cs455/hadoop/*/*.class 

package : clean
	tar -cvf van_Andel-Gerhard-HW3-PC.tar --exclude='/' --exclude='.[^/]*' *
