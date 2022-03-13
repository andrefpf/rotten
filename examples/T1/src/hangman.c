#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include <stdio.h>

#include <hangman.h>


struct Hangman * create_game() {
    struct Hangman * hangman = malloc(sizeof(struct Hangman));
    hangman->mistakes = 0;
    choose_word(hangman->word);
    fill_gaps(hangman->guess, hangman->word);  
    return hangman;  
}

int evaluate(char c, struct Hangman * hangman) {
    if (belongs(c, hangman->word)) {
        complete_word(c, hangman->guess, hangman->word);

        if (strcmp(hangman->guess, hangman->word))
            return CORRECT;
        else 
            return WIN;

    } else {
        hangman->mistakes++;

        if (hangman->mistakes >= NUM_BODY_PARTS)
            return LOSE;
        else
            return WRONG;
    }
}

int get_body_part(struct Hangman * hangman) {
    return hangman->mistakes;
}

void choose_word(char * word) {
    int opt = random() % N_WORDS;
    strcpy(word, RANDOM_WORDS[opt]);
}

void fill_gaps(char * guess, char * word) {
    int size = strlen(word);
    char c;

    for (int i = 0; i < size; i++) {
        c = word[i];

        if ('A' <= c && c <= 'z') {
            guess[i] = '_';
        } else {
            guess[i] = c;
        }
    }

    guess[size] = '\0'; // end char
}

int belongs(char c, char * word) {
    for (int i = 0; i < strlen(word); i++) {
        if (tolower(word[i]) == tolower(c)) {
            return 1;
        }
    }
    return 0;
}

int complete_word(char c, char * guess, char * word) {
    for (int i = 0; i < strlen(word); i++) {
        if (tolower(word[i]) == tolower(c)) {
            guess[i] = word[i];
        }
    }
}