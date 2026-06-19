package com.gabriel2305.storyteller;

import com.gabriel2305.UI;
import com.gabriel2305.exceptions.StorytellerException;

import java.util.Map;

public class Storyteller {

    private Map<String, HistoryExecutable> historyMap = null;
    private final String FIRST_NODE_ID = "start";
    private String nextNodeId = FIRST_NODE_ID;
    private final String title;

    public Storyteller(String title) {
        this.title = title;
    }

    public void setHistoryMap(Map<String, HistoryExecutable> historyMap) {
        this.historyMap = historyMap;
    }

    public void start() {
        if (historyMap == null || historyMap.isEmpty()) {
            throw new StorytellerException("History map is empty");
        }
        validateHistoryMap();
        UI.historyHeader(title);
        historyLoop();
        UI.historyEnd();
    }

    private void historyLoop() {
        while (!nextNodeId.equals("end")) {
            HistoryExecutable actualNode = historyMap.get(nextNodeId);
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

    private void validateHistoryMap() throws StorytellerException {
        boolean endFound = false;
        if (!historyMap.containsKey("start")) {
            throw new StorytellerException("\"start\" node not found");
        }
        for (HistoryExecutable h : historyMap.values()) {
            String[] externalReferences = h.getExternalReferences();
            for (String externalReference : externalReferences) {
                if (!historyMap.containsKey(externalReference)) {
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
