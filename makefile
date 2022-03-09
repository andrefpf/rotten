# JGROUPS = ./bin/jgroups-5.0.0.Final.jar
JGROUPS   = ./bin/jgroups-4.2.4.Final.jar
CLASSPATH = $(JGROUPS):bin

all:
	javac -cp $(CLASSPATH) ./src/com.dist/*.java -d ./bin 

run:
	@ java -cp $(CLASSPATH) com.dist.Main

clean:
	rm -r ./bin/com