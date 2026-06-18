package com.gabriel2305;

import com.gabriel2305.exceptions.ParserException;
import com.gabriel2305.parser.DhisParser;
import com.gabriel2305.parser.Fragment;

import java.io.IOException;
import java.util.List;

public class Application {

    public void start() throws IOException {
        FilesystemHandler filesystemHandler = new FilesystemHandler();
        String[] availableStories = filesystemHandler.getAvailableStories().toArray(new String[0]);

        UI.welcome();
        int option = UI.historyMenu(availableStories);

        String selectedHistory = availableStories[option - 1];
        UI.log("Selected: " + selectedHistory);
        UI.log("Trying create fragments");

        DhisParser dhisParser = new DhisParser(filesystemHandler.getHistoryFileContent(selectedHistory));
        try {
            List<Fragment> fragments = dhisParser.createFragments();
            UI.log(fragments);
        } catch (ParserException e) {
            UI.error(e.getMessage());
        }
    }
}

