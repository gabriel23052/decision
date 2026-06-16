package com.gabriel2305;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class UI {

    private static final Scanner scanner = new Scanner(System.in);

    public static void error(Object object) {
        IO.println("[ERRO] " + object.toString());
    }

    public static void log(Object object) {
        IO.println("[LOG] " + object.toString());
    }

    public static int historySelection(String[] stories) {
        int historyIndex = 1;
        IO.println("* Selecione a história que deseja");
        IO.println("0 - Sair");
        for (String history : stories) {
            IO.println(historyIndex + " - " + history);
            historyIndex++;
        }
        while (true) {
            int option = UI.readInt("Digite o número da opção escolhida");
            if (option < 0 || option > stories.length) {
                UI.error("Opção inválida, tente novamente");
                continue;
            }
            return option;
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

}
