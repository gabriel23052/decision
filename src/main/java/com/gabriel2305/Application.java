package com.gabriel2305;

import java.io.IOException;

public class Application {

    FilesystemHandler filesystemHandler = new FilesystemHandler();

    public Application() throws IOException {
    }

    public void start() {
        UI.log("Starting application");
        String[] availableStories = filesystemHandler.getAvailableStories().toArray(new String[0]);
        UI.welcome();
        UI.historyMenu(availableStories);
        UI.log("Finishing application");
    }

}

