package com.gabriel2305;

import com.gabriel2305.exceptions.FilesystemException;
import com.gabriel2305.exceptions.ParserException;
import com.gabriel2305.parser.DhisParser;
import com.gabriel2305.parser.Fragment;
import com.gabriel2305.parser.FragmentParser;
import com.gabriel2305.storyteller.StoryExecutable;
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

        int option = UI.storySelection(availableStories);
        if (option == 0) {
            UI.goodbye();
            return;
        }

        String title = availableStories[option - 1];
        String dhis = filesystemHandler.getDhisFileContent(title);


        Map<String, StoryExecutable> storyMap = compileStory(dhis);

        Storyteller storyteller = new Storyteller(title);
        storyteller.setStoryMap(storyMap);
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

    private Map<String, StoryExecutable> compileStory(String dhis) {
        DhisParser dhisParser = new DhisParser();
        FragmentParser fragmentParser = new FragmentParser();
        try {
            dhisParser.setDhis(dhis);
            Fragment[] fragments = dhisParser.createFragments();
            fragmentParser.setFragments(fragments);
            return fragmentParser.createStoryMap();
        } catch (ParserException e) {
            UI.error(e.getMessage());
            System.exit(0);
            return null;
        }
    }
}

