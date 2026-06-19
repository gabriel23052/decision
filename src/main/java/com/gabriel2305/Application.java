package com.gabriel2305;

import com.gabriel2305.exceptions.FilesystemException;
import com.gabriel2305.exceptions.ParserException;
import com.gabriel2305.exceptions.StorytellerException;
import com.gabriel2305.parser.DhisParser;
import com.gabriel2305.parser.Fragment;
import com.gabriel2305.parser.FragmentParser;
import com.gabriel2305.storyteller.HistoryExecutable;
import com.gabriel2305.storyteller.Storyteller;

import java.util.Map;

public class Application {

    public void start() {
        FilesystemHandler filesystemHandler = new FilesystemHandler();
        String[] availableStories = new String[0];
        DhisParser dhisParser = new DhisParser();
        FragmentParser fragmentParser = new FragmentParser();
        Storyteller storyteller = new Storyteller();

        try {
            filesystemHandler.readStoriesDirectory();
            availableStories = filesystemHandler.getAvailableStories();
        } catch (FilesystemException e) {
            UI.error(e.getMessage());
        }

        UI.welcome();

        if (availableStories.length == 0) {
            UI.emptyStories();
            return;
        }

        int menuOption = UI.historyMenu(availableStories);

        if (menuOption == 0) {
            UI.goodbye();
            return;
        }

        String dhisFile = filesystemHandler.getHistoryFileContent(availableStories[menuOption - 1]);
        dhisParser.setDhis(dhisFile);
        Fragment[] fragments = new Fragment[0];
        try {
            fragments = dhisParser.createFragments();
        } catch (ParserException e) {
            UI.error(e.getMessage());
        }

        fragmentParser.setFragments(fragments);
        Map<String, HistoryExecutable> historyMap = null;
        try {
            historyMap = fragmentParser.createHistoryMap();
        } catch (ParserException e) {
            UI.error(e.getMessage());
        }

        storyteller.setHistoryMap(historyMap);
        try {
            storyteller.start();
        } catch (StorytellerException e) {
            UI.error(e.getMessage());
        }

        UI.goodbye();
    }
}

