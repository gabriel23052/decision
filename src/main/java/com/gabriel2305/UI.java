package com.gabriel2305;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class UI {

    private static final int LINE_SIZE = 80;
    private static final Scanner scanner = new Scanner(System.in);

    public static void printLn(Object message) {
        IO.println(message.toString());
    }
    public static void printLn(Object message, TextAlignment textAlignment) {
        String messageStr = message.toString();
        if (textAlignment == TextAlignment.LEFT) {
            IO.println(messageStr);
            return;
        }
        if (textAlignment == TextAlignment.RIGHT) {
            int spaces = Math.max(0, LINE_SIZE - messageStr.length());
            IO.println(" ".repeat(spaces) + messageStr);
            return;
        }
        int spaces = Math.floorDiv(LINE_SIZE - messageStr.length(), 2);
        IO.println(" ".repeat(spaces) + messageStr);
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
        IO.println("[ERRO] " + object.toString());
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

    public static int historyMenu(String[] stories) {
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
}
