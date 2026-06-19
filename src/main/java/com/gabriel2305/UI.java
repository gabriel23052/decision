package com.gabriel2305;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class UI {

    private static final int LINE_SIZE = 80;
    private static final Scanner scanner = new Scanner(System.in);

    public static void printLn(Object message) {
        String[] messageLines = message.toString().split("\\R");
        for (String line : messageLines) {
            IO.println(line.trim());
        }
    }
    public static void printLn(Object message, TextAlignment textAlignment) {
        String[] messageLines = message.toString().split("\\R");
        for (String line : messageLines) {
            String trimLine = line.trim();
            if (textAlignment == TextAlignment.LEFT) {
                IO.println(trimLine);
                return;
            }
            if (textAlignment == TextAlignment.RIGHT) {
                int spaces = Math.max(0, LINE_SIZE - line.length());
                IO.println(" ".repeat(spaces) + trimLine);
                return;
            }
            int spaces = Math.floorDiv(LINE_SIZE - trimLine.length(), 2);
            IO.println(" ".repeat(spaces) + trimLine);
        }

    }

    public static int readInt(String message) {
        while (true) {
            IO.print("> " + message + ": ");
            try {
                int read = scanner.nextInt();
                scanner.nextLine();
                return read;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                UI.error("O valor digitado é inválido, tente novamente");
            }
        }
    }

    public static void blankLine() {
        IO.println("");
    }

    public static void line() {
        IO.println("-".repeat(LINE_SIZE));
    }

    public static void error(Object object) {
        IO.println("[ERROR] " + object.toString());
    }

    public static void log(Object object) {
        IO.println("[LOG] " + object.toString());
    }

    public static void welcome() {
        UI.line();
        UI.printLn("Bem-vindo ao Decision", TextAlignment.CENTER);
        UI.blankLine();
        UI.printLn("Um game que permite contar histórias interativas diretamente no terminal, aqui");
        UI.printLn("você controla o destino digitando alguns números no seu teclado. Para começar,");
        UI.printLn("escolha uma das histórias. Todos os comandos do jogo consistem em digitar um");
        UI.printLn("número e apertar ENTER");
        UI.blankLine();
    }

    public static void goodbye() {
        UI.blankLine();
        UI.printLn("Obrigado por jogar o Decision", TextAlignment.CENTER);
        UI.printLn("Desenvolvido por Gabriel Pereira", TextAlignment.CENTER);
        UI.printLn("https://github.com/gabriel23052", TextAlignment.CENTER);
        UI.blankLine();
        UI.line();
    }

    public static int historySelection(String[] stories) {
        UI.printLn("Escolha a história", TextAlignment.CENTER);
        UI.blankLine();
        UI.printLn("0 - Sair do jogo");
        for (int i = 0; i < stories.length; i++) {
            UI.printLn((i + 1) + " - " + stories[i]);
        }
        UI.blankLine();
        while (true) {
            int option = UI.readInt("Digite a opção escolhida");
            if (option < 0 || option > stories.length) {
                UI.error("Opção inválida, tente novamente");
                continue;
            }
            return option;
        }
    }

    public static void historyHeader(String title) {
        UI.blankLine();
        UI.line();
        UI.blankLine();
        UI.printLn(title, TextAlignment.CENTER);
        UI.blankLine();
    }

    public static void historyEnd() {
        UI.blankLine();
        UI.printLn("FIM", TextAlignment.CENTER);
        UI.blankLine();
        UI.line();
    }

    public static void emptyStories() {
        UI.printLn("Ops, parece que não há nenhuma história no diretório ):", TextAlignment.CENTER);
        UI.printLn("Verifique as instruções do repositório do projeto para prosseguir:", TextAlignment.CENTER);
        UI.printLn("https://github.com/gabriel23052", TextAlignment.CENTER);
        UI.line();
    }
}
