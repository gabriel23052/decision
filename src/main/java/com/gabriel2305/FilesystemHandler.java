package com.gabriel2305;

import com.gabriel2305.exceptions.FilesystemException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class FilesystemHandler {

    private final Map<String, Path> storiesMap = new HashMap<>();

    private Path getDataDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        String APP_NAME = "decision";

        if (os.contains("win")) {
            String appData = System.getenv("APPDATA");

            if (appData == null || appData.isEmpty()) {
                throw new FilesystemException("APPDATA environment variable not found");
            }

            return Path.of(appData, APP_NAME);
        }

        if (os.contains("linux")) {
            String xdgDataHome = System.getenv("XDG_DATA_HOME");

            if (xdgDataHome != null && !xdgDataHome.isBlank()) {
                return Path.of(xdgDataHome, APP_NAME);
            }

            return Path.of(System.getProperty("user.home"), ".local", "share", APP_NAME);
        }

        throw new FilesystemException("OS not supported: " + os);
    }

    public void readStoriesDirectory() throws FilesystemException {
        Path storiesPath = getDataDirectory();
        if (!Files.exists(storiesPath)) {
            try {
                Files.createDirectory(storiesPath);
                return;
            } catch (IOException e) {
                throw new FilesystemException("Failed while trying to create the stories directory");
            }
        }
        try (Stream<Path> filesInDirectory = Files.list(storiesPath)) {
            filesInDirectory
                    .filter(fileInDirectory -> fileInDirectory
                            .getFileName()
                            .toString()
                            .endsWith(".dhis")
                    ).
                    forEach(dhisFile -> storiesMap.put(
                            dhisFile
                                    .getFileName()
                                    .toString()
                                    .replaceAll(".dhis", "")
                            , dhisFile));
        } catch (IOException e) {
            throw new FilesystemException("Failed while trying to read the contents of stories directory");
        }
    }

    public String[] getAvailableStories() {
        return storiesMap.keySet().toArray(new String[0]);
    }

    public String getHistoryFileContent(String history) {
        if (!storiesMap.containsKey(history)) {
            throw new FilesystemException("History file does not exist");
        }
        try {
            return Files.readString(storiesMap.get(history));
        } catch (IOException e) {
            throw new FilesystemException("Failed while trying to read the history file content");
        }
    }
}
