# G van Andel
# makefile

JC=javac
JF=-g -d bin/ -cp src

defualt: all

all : 
	$(JC) $(JF) src/cs455/fileSystem/*/*.java

clean :
	rm cs455/fileSystem/*/*.class 

package : clean
	tar -cvf van_Andel-Gerhard-HW1-PC.tar --exclude='/' --exclude='.[^/]*' *
