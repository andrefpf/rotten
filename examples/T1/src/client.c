#include <stdio.h>
#include <stdlib.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <poll.h>
#include <assert.h>

#include <hangman.h>
#include <client.h>

int ret, fd;

void startclient() {
	struct sockaddr_in addr;

	fd = socket(AF_INET, SOCK_STREAM, 0);
	addr.sin_family = AF_INET;
	addr.sin_addr.s_addr = inet_addr(SERVER_IP);
	addr.sin_port = htons(SERVER_PORT);

	if ((ret = connect(fd, (struct sockaddr *) &addr, sizeof(addr))) < 0) {
		perror("unable to connect");
		exit(1);
	}
}

void startgame() {
    char buffer[BUFFER_SIZE];
    char guess[BUFFER_SIZE];
    char sair[] = "sair";
    while (1) {
        recv(fd, buffer, BUFFER_SIZE, 0);
        printf("%s\n", buffer);
        fflush(stdout);

        if (!strcmp(buffer, sair)) {
            printf("Você saiu do jogo.\n");
            return;
        }

        fgets(guess, BUFFER_SIZE, stdin);
        guess[strcspn(guess, "\n")] = '\0';
        fflush(stdin);
        send(fd, guess, sizeof(guess), 0);
    }
    return;
}

int main() {
    startclient();
    startgame(fd);
    close(fd);

    return 0;
}