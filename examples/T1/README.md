# T1 - Jogo da Forca 

## ALUNOS 
  André Filipe da Silva Fernandes - 19100515
  José Luis de Souza - 

## INTRODUÇÃO
  Neste trabalho implementamos um servidor e um cliente para executar um jogo da forca na linguagem C, utilizando apenas sockets nativos do sistema

## DETALHES DE IMPLEMENTAÇÃO
  Na implementação deste projeto usamos 3 arquivos: server.c, responsável por gerenciar o servidor; client.c, responsável por gerenciar a comunicação do cliente; e hangman.c, que contêm as funções e estruturas necessárias para o funcionamento do jogo.

  O servidor usa a chamada poll para gerenciar a conexão entre os clientes, e novas conexões são gerenciadas pela função handle_pollin. A cada novo cliente que conecta, o servidor cria uma nova struct Hangman, que é inicializada com uma palavra aleatória da lista de palavras. A posição do novo cliente é alocada em algum espaço livre do vetor "game" através da função "add_connection".

  O jogo é executado efetivamente no servidor, na função game_loop, que recebe o número do socket, a mensagem enviada pelo jogador, e o índice desse jogador nos arrays auxiliares. Com essas informações o servidor avalia a entrada, gera uma string para ser printada no cliente e envia para ele. 

  O código no lado do cliente é simples e apenas implementa a comunicação com o servidor que recebe o input do usuário, envia para o servidor e printa a mensagem recebida, que será a interface de usuário. 

  É possível alterar informações como a porta onde o servidor vai ser criado, a quantidade máxima de jogadores, o tamanho dos buffers, e o endereço do servidor ao qual deseja conectar nos arquivos server.h e client.h alterando os defines.

  server.h
  ```
    #ifndef SERVER_H
    #define SERVER_H

    #define PORT (6667)

    #define MAX_PLAYERS (10)
    #define POLL_LENGHT (MAX_PLAYERS + 1)

    #define BUFFER_SIZE (1024)


    #endif
  ```

  client.h
  ```
    #ifndef SERVER_H
    #define SERVER_H

    #define SERVER_IP ("127.0.0.1")
    #define SERVER_PORT (6667)

    #define BUFFER_SIZE (1024)

    #endif
  ```


## LIMITAÇÕES
  Apesar de não termos presenciado nenhum erro, nosso código pode estar sujeito a condições de corrida ao alocar um novo espaço para conexão no array. O código carece de testes automatizados que auxiliariam a encontrar possíveis problemas e a refatorar alguns trechos que não contam com as melhores práticas de programação possíveis. Em termos de projeto, nosso programa possui um número pré definido de clientes, e não escalona de acordo com a demanda. 