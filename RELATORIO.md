# Membros   
- André Filipe da Silva Fernandes
- José Luiz de Souza 

# Detalhes da Aplicação
A aplicação tem por objetivo funcionar de maneira semelhante a um cliente de bit torrent por CLI. Evidentemente é um cliente rudimentar e que efetivamente usa protocolos de comunicação diferentes, mas a utilização deve ser parecida.

A aplicação é capaz de fazer seed de um arquivo qualquer, nesse momento o programa gera um link e o arquivo está pronto para ser baixado em quaisquer outras instâncias do programa. 

Para baixar algum arquivo é necessário usar o link associado a ele. A partir disso o agente faz uma requisição ao grupo e caso algum dos agentes possua o arquivo desejado envia diretamente a quem solicitou. O agente que solicitou é quem faz o controle para que apenas uma cópia seja utilizada.

A classe FileTransfer gerencia toda a troca de mensagens, arquivos e é a principal interface com a biblioteca jgroups.

A classe RottenClient é uma classe filha de FileTransfer e implementa as funções publicas de mais alto nível, como download, seed, remove seed e possui as estruturas de dados para gerenciar esse nível da aplicação.

A classe Envelope armazena os conteúdos que serão transmitidos pela rede, pois pode ser serializada. Esses conteúdos podem ser tanto arquivos quanto requisições. 

A classe Frontend implementa toda a interação com o usuário.

# Estudo de Caso 
O programa foi testado transmitindo arquivos de texto, imagens, arquivos jar e alguns outros entre dois computadores executando em lan, cada um executando duas instâncias da aplicação. 
Alguns dos exemplos utilizados estão na pasta examples.
