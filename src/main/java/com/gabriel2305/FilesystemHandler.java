package com.gabriel2305;

import com.gabriel2305.exceptions.FilesystemException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public final class FilesystemHandler {

    private final Path STORIES_PATH = Path.of(System.getenv("APPDATA"), "decision");

    private final Map<String, Path> storiesMap = new HashMap<>();

    FilesystemHandler() throws IOException {
        if (!Files.exists(STORIES_PATH)) {
            Files.createDirectory(STORIES_PATH);
            return;
        }
        try (Stream<Path> filesInDirectory = Files.list(STORIES_PATH)) {
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
        }
    }

    public Set<String> getAvailableStories() {
        return storiesMap.keySet();
    }

    public String getHistoryFileContent(String history) throws FilesystemException, IOException {
        if (!storiesMap.containsKey(history)) throw new FilesystemException("History file does not exist");
        return Files.readString(storiesMap.get(history));
    }
}
