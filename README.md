# Decision

Um jogo de terminal feito em Java para executar histórias interativas.

O projeto usa arquivos `.dhis`, uma sintaxe própria criada para descrever nós de história, textos, decisões e caminhos possíveis. A ideia principal é separar a história do código Java: o programa lê o arquivo, interpreta sua estrutura e executa a narrativa no terminal.

## Tecnologias

* Java
* Gradle
* JUnit

## Sobre o projeto

Este projeto foi desenvolvido como estudo de Java, com foco em:

* leitura de arquivos;
* parsing de texto;
* validação de sintaxe;
* tratamento de erros;
* organização de código com Gradle.
* teste unitários com JUnit

## Exemplo de história `.dhis`

```dhis
[start] text (
    "Você acorda em uma sala escura.
    Há duas portas na sua frente.",
    escolha
)

[escolha] decision (
    "O que você faz?",
    portaEsquerda, "abrir a porta da esquerda",
    portaDireita, "abrir a porta da direita"
)

[portaEsquerda] text (
    "A porta leva para fora.
    Você conseguiu escapar.",
    end
)

[portaDireita] text (
    "A porta estava trancada.
    Talvez fosse melhor ter escolhido outro caminho.",
    end
)
```

## Como funciona

Cada história é formada por nós.

Um nó pode ser, por exemplo:

* `text`: mostra um texto e leva para outro nó;
* `decision`: mostra uma decisão e permite escolher entre caminhos diferentes.

O programa valida pontos importantes da história, como:

* existência do nó inicial `start`;
* existência do nó final `end`;
* referências para nós inexistentes.

## Como executar

Clone o repositório:

```bash
git clone <url-do-repositorio>
```

Entre na pasta do projeto:

```bash
cd <nome-do-projeto>
```

Execute com Gradle:

```bash
./gradlew run
```

No Windows:

```bash
gradlew.bat run
```

## Histórias

O jogo procura os arquivos de história no diretório AppData do usuário no Windows.
Para facilitar os testes, o repositório inclui algumas histórias de exemplo na pasta
`examples`.

No Windows, você pode executar o script abaixo para copiar automaticamente essas
histórias para o diretório esperado pelo jogo:

```bash
examples/copyToAppdata.bat
```

## Gerar distribuição

Para gerar uma distribuição executável com os scripts do Gradle:

```bash
./gradlew installDist
```

Os arquivos serão gerados em:

```text
build/install/
```

Também é possível gerar um `.zip` com:

```bash
./gradlew distZip
```


## Status

Projeto em desenvolvimento.

A versão atual já permite carregar histórias, interpretar arquivos `.dhis`, validar referências e executar narrativas interativas pelo terminal.
