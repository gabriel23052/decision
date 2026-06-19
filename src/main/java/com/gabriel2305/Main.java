package com.gabriel2305;

public class Main {
    void main() {
        try {
            UI.log("Starting application");
            Application application = new Application();
            application.start();
            UI.log("Finishing application");
        } catch (Exception e) {
            UI.error("Ocorreu uma falha inesperada: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

