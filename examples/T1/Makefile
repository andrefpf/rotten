all: clean server client

server:
	@ gcc -I include/ src/server.c src/hangman.c -o server

client: hangman
	@ gcc -I include/ src/client.c hangman.o -o client

hangman:
	@ gcc -I include/ src/hangman.c -c

clean:
	@ rm server -f
	@ rm client -f
	@ rm hangman.o -f