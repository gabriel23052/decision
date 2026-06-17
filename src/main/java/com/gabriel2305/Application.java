package com.gabriel2305;

import com.gabriel2305.exceptions.ParserException;
import com.gabriel2305.parser.Parser;

import java.io.IOException;

public class Application {

    public void start() throws IOException {
        FilesystemHandler filesystemHandler = new FilesystemHandler();
        String[] availableStories = filesystemHandler.getAvailableStories().toArray(new String[0]);

        UI.welcome();
        int option = UI.historyMenu(availableStories);

        String selectedHistory = availableStories[option - 1];
        UI.log("Selected: " + selectedHistory);
        UI.log("Trying create fragments");

        Parser parser = new Parser(filesystemHandler.getHistoryFileContent(selectedHistory));
        try {
            parser.createFragments();
        } catch (ParserException e) {
            UI.error(e.getMessage());
        }
    }
}

