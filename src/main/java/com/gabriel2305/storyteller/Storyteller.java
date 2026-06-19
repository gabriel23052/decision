package com.gabriel2305.storyteller;

import com.gabriel2305.UI;
import com.gabriel2305.exceptions.StorytellerException;

import java.util.Map;

public class Storyteller {

    private Map<String, StoryExecutable> storyMap = null;
    private final String FIRST_NODE_ID = "start";
    private String nextNodeId = FIRST_NODE_ID;
    private final String title;

    public Storyteller(String title) {
        this.title = title;
    }

    public void setStoryMap(Map<String, StoryExecutable> storyMap) {
        this.storyMap = storyMap;
    }

    public void start() {
        if (storyMap == null || storyMap.isEmpty()) {
            throw new StorytellerException("Story map is empty");
        }
        validateStoryMap();
        UI.storyHeader(title);
        storyLoop();
        UI.storyEnd();
    }

    private void storyLoop() {
        while (!nextNodeId.equals("end")) {
            StoryExecutable actualNode = storyMap.get(nextNodeId);
            if (actualNode == null) {
                throw new StorytellerException("Node not found: " + nextNodeId);
            }
            actualNode.execute();
            UI.blankLine();
            String next = actualNode.getNextNodeId();
            if (next == null) {
                throw new StorytellerException("Node returned null to nextNodeId");
            }
            nextNodeId = next;
        }
    }

    private void validateStoryMap() throws StorytellerException {
        boolean endFound = false;
        if (!storyMap.containsKey("start")) {
            throw new StorytellerException("\"start\" node not found");
        }
        for (StoryExecutable h : storyMap.values()) {
            String[] externalReferences = h.getExternalReferences();
            for (String externalReference : externalReferences) {
                if (!storyMap.containsKey(externalReference)) {
                    if ("end".equals(externalReference)) {
                        endFound = true;
                        continue;
                    }
                    throw new StorytellerException("Reference to non-existent node (" + externalReference + ")");
                }
            }
        }
        if (!endFound) {
            throw new StorytellerException("Reference to end not found");
        }
    }
}
