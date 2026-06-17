package com.gabriel2305;

import java.io.IOException;

public class Main {
    void main() throws IOException {
        UI.log("Starting application");
        Application application = new Application();
        application.start();
        UI.log("Finishing application");
    }
}

