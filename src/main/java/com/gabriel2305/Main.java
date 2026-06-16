package com.gabriel2305;

import java.io.IOException;

public class Main {
    void main() throws IOException {
        UI.error("Teste");
        UI.log("Teste");

        FilesystemHandler filesystemHandler = new FilesystemHandler();
        String[] availableStories = filesystemHandler
                .getAvailableStories()
                .toArray(new String[0]);

        int historySelectionResult = UI.historySelection(availableStories);
        if (historySelectionResult == 0) return;
        UI.log("Escolheu: " + availableStories[historySelectionResult - 1]);
    }
}

