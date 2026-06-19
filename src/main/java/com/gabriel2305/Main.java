package com.gabriel2305;

import java.util.Scanner;

public class Main {
    void main() {
        try {
            Application application = new Application();
            application.start();
            System.out.println();
            System.out.println("Pressione Enter para sair...");
            new Scanner(System.in).nextLine();
        } catch (Exception e) {
            UI.error("Ocorreu uma falha inesperada: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

