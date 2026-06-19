package com.gabriel2305;

import com.gabriel2305.exceptions.FilesystemException;

public class Application {

    public void start() {
        FilesystemHandler filesystemHandler = new FilesystemHandler();
        String[] availableStories = new String[0];
        try {
            filesystemHandler.readStoriesDirectory();
            availableStories = filesystemHandler.getAvailableStories();
        } catch (FilesystemException e) {
            UI.error(e.getMessage());
        }

        UI.welcome();
        int menuOption = UI.historyMenu(availableStories);

        if (menuOption == 0) {
            return;
        }

        String dhisFile = filesystemHandler.getHistoryFileContent(availableStories[menuOption - 1]);
        UI.log(dhisFile);
    }
}

