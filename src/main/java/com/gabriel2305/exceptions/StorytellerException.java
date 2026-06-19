package com.gabriel2305.exceptions;

public class StorytellerException extends RuntimeException {
    public StorytellerException(String message) {
        super("STORYTELLER: " + message);
    }
}
