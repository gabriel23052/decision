package com.gabriel2305;

import com.gabriel2305.exceptions.FilesystemException;
import com.gabriel2305.exceptions.ParserException;
import com.gabriel2305.parser.DhisParser;
import com.gabriel2305.parser.Fragment;
import com.gabriel2305.parser.FragmentParser;
import com.gabriel2305.storyteller.HistoryExecutable;
import com.gabriel2305.storyteller.Storyteller;

import java.util.Map;

public class Application {

    private final FilesystemHandler filesystemHandler = new FilesystemHandler();

    public void start() {
        String[] availableStories = getAvailableStories();

        UI.welcome();

        if (availableStories.length == 0) {
            UI.emptyStories();
            UI.goodbye();
            return;
        }

        int option = UI.historySelection(availableStories);
        if (option == 0) {
            UI.goodbye();
            return;
        }

        String title = availableStories[option - 1];
        String dhis = filesystemHandler.getHistoryFileContent(title);


        Map<String, HistoryExecutable> historyMap = compileHistory(dhis);

        Storyteller storyteller = new Storyteller();
        storyteller.setHistoryMap(historyMap);
        storyteller.start();

        UI.goodbye();
    }

    private String[] getAvailableStories() {
        try {
            filesystemHandler.readStoriesDirectory();
            return filesystemHandler.getAvailableStories();
        } catch (FilesystemException e) {
            UI.error(e.getMessage());
            System.exit(0);
            return null;
        }
    }

    private Map<String, HistoryExecutable> compileHistory(String dhis) {
        DhisParser dhisParser = new DhisParser();
        FragmentParser fragmentParser = new FragmentParser();
        try {
            dhisParser.setDhis(dhis);
            Fragment[] fragments = dhisParser.createFragments();
            fragmentParser.setFragments(fragments);
            return fragmentParser.createHistoryMap();
        } catch (ParserException e) {
            UI.error(e.getMessage());
            System.exit(0);
            return null;
        }
    }
}

