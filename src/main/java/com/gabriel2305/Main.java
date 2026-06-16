package com.gabriel2305;

import java.io.IOException;

public class Main {
    void main() throws IOException {
        FilesystemHandler filesystemHandler = new FilesystemHandler();
        filesystemHandler.getAvailableStories().forEach(IO::println);
        System.out.println(filesystemHandler.getHistoryFileContent("example1"));
        System.out.println(filesystemHandler.getHistoryFileContent("example2"));
    }
}

