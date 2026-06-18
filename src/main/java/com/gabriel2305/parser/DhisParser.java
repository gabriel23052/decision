package com.gabriel2305.parser;

import com.gabriel2305.exceptions.ParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DhisParser {

    private final int ASCII_SPACE = 32;

    private final char[] dhisChars;
    private int index = 0;
    private final List<Fragment> fragments = new ArrayList<>();

    public DhisParser(String dhis) {
        this.dhisChars = dhis.toCharArray();
    }

    private char getActualChar() {
        try {
            return dhisChars[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParserException("Ended unexpectedly (position: " + index + ")");
        }
    }

    private String contentBufferToString(List<Character> buffer) {
        return buffer
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    public List<Fragment> createFragments() throws ParserException {
        while (index < dhisChars.length) {
            char actualChar = getActualChar();
            if (actualChar <= ASCII_SPACE) {
                index++;
                continue;
            }
            if (actualChar == '[') {
                fragments.add(new Fragment(FragmentType.OPENING_SQUARE_BRACKET));
                index++;
                createNodeIdDeclaration();
                continue;
            }
            if (actualChar == ']') {
                fragments.add(new Fragment(FragmentType.CLOSING_SQUARE_BRACKET));
                index++;
                continue;
            }
            if (actualChar == '(') {
                fragments.add(new Fragment(FragmentType.OPENING_PARENTHESIS));
                index++;
                handleNodeParams();
                continue;
            }
            if (actualChar == ')') {
                fragments.add(new Fragment(FragmentType.CLOSING_PARENTHESIS));
                index++;
                continue;
            }
            if (validateNodeTypeChar(actualChar)) {
                createNodeTypeDeclaration();
                continue;
            }
            throw new ParserException("Invalid char (position: " + index + ")");
        }
        return fragments;
    }

    private void createNodeIdDeclaration() {
        List<Character> contentBuffer = new ArrayList<>();
        while (getActualChar() != ']') {
            if (!validateNodeIdChar(getActualChar())) {
                throw new ParserException("Invalid char in NODE_ID_DECLARATION (position: " + index + ")");
            }
            contentBuffer.add(getActualChar());
            index++;
        }
        if (contentBuffer.isEmpty()) {
            throw new ParserException("Empty NODE_ID_DECLARATION (position: " + index + ")");
        }
        fragments.add(new Fragment(FragmentType.NODE_ID_DECLARATION, contentBufferToString(contentBuffer)));
    }

    private void createNodeIdRef() {
        List<Character> contentBuffer = new ArrayList<>();
        while (getActualChar() != ')' && getActualChar() != ',' && getActualChar() > ASCII_SPACE) {
            if (!validateNodeIdChar(getActualChar())) {
                throw new ParserException("Invalid char in NODE_ID_REF (position: " + index + ")");
            }
            contentBuffer.add(getActualChar());
            index++;
        }
        if (contentBuffer.isEmpty()) {
            throw new ParserException("Empty NODE_ID_REF (position: " + index + ")");
        }
        fragments.add(new Fragment(FragmentType.NODE_ID_REF, contentBufferToString(contentBuffer)));
    }

    private void createNodeTypeDeclaration() {
        List<Character> contentBuffer = new ArrayList<>();
        while (getActualChar() > ASCII_SPACE && getActualChar() != '(' && getActualChar() != ')') {
            if (!validateNodeTypeChar(getActualChar())) {
                throw new ParserException("Invalid char in NODE_TYPE (position: " + index + ")");
            }
            contentBuffer.add(getActualChar());
            index++;
        }
        if (contentBuffer.isEmpty()) {
            throw new ParserException("Empty NODE_TYPE (position: " + index + ")");
        }
        fragments.add(new Fragment(FragmentType.NODE_TEXT, contentBufferToString(contentBuffer)));
    }

    private void createNodeText() {
        List<Character> contentBuffer = new ArrayList<>();
        while (getActualChar() != '"') {
            contentBuffer.add(getActualChar());
            index++;
        }
        if (contentBuffer.isEmpty()) {
            throw new ParserException("Empty NODE_TEXT (position: " + index + ")");
        }
        fragments.add(new Fragment(FragmentType.NODE_TEXT, contentBufferToString(contentBuffer)));
    }

    private void handleNodeParams() {
        while (getActualChar() != ')') {
            if (getActualChar() <= ASCII_SPACE) {
                index++;
                continue;
            }
            if (getActualChar() == '"') {
                fragments.add(new Fragment(FragmentType.OPENING_QUOTES));
                index++;
                createNodeText();
                if (getActualChar() != '"') {
                    throw new ParserException("Text not closed (position: " + index + ")");
                }
                fragments.add(new Fragment(FragmentType.CLOSING_QUOTES));
                index++;
                continue;
            }
            if (getActualChar() == ',') {
                fragments.add(new Fragment(FragmentType.COMMA));
                index++;
                continue;
            }
            if (validateNodeIdChar(getActualChar())) {
                createNodeIdRef();
                continue;
            }
            throw new ParserException("Invalid node param (position: " + index + ")");
        }
    }

    private static boolean validateNodeTypeChar(char value) {
        return value >= 97 && value <= 122;
    }

    private static boolean validateNodeIdChar(char value) {
        return (value >= 48 && value <= 57) ||
                (value >= 65 && value <= 90) ||
                value == 95 ||
                (value >= 97 && value <= 122);
    }
}
