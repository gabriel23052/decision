package com.gabriel2305.exceptions;

public class ParserException extends RuntimeException {
    public ParserException(String message) {
        super("PARSER: " + message);
    }
}
