package com.gabriel2305.parser;

import com.gabriel2305.UI;
import com.gabriel2305.exceptions.ParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

    private final int ASCII_SPACE = 32;

    private final char[] historyCharArr;
    private int index = 0;
    private final List<Fragment> fragmentList = new ArrayList<>();

    public Parser(String historyStr) {
        this.historyCharArr = historyStr.toCharArray();
    }

    public void createFragments() throws ParserException {
        while (index < historyCharArr.length) {
            char actualChar = getActualChar();
            if (actualChar <= ASCII_SPACE) {
                index++;
                continue;
            }
            if (actualChar == '[') {
                fragmentList.add(new Fragment(FragmentType.OPENING_SQUARE_BRACKET));
                index++;
                parseNodeIdDeclaration();
                continue;
            }
            if (actualChar == ']') {
                fragmentList.add(new Fragment(FragmentType.CLOSING_SQUARE_BRACKET));
                index++;
                continue;
            }
            if (actualChar == '(') {
                fragmentList.add(new Fragment(FragmentType.OPENING_PARENTHESIS));
                index++;
                parseNodeParams();
                continue;
            }
            if (actualChar == ')') {
                fragmentList.add(new Fragment(FragmentType.CLOSING_PARENTHESIS));
                index++;
                continue;
            }
            if (validateNodeTypeChar(actualChar)) {
                parseNodeTypeDeclaration();
                continue;
            }
            throw new ParserException("Invalid char (position: " + index + ")");
        }
        UI.log(fragmentList);
    }

    private char getActualChar() {
        try {
            return historyCharArr[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParserException("Ended unexpectedly");
        }
    }

    private void parseNodeIdDeclaration() {
        List<Character> contentBuffer = new ArrayList<>();
        while (getActualChar() != ']') {
            if (!validateNodeIdChar(getActualChar())) {
                throw new ParserException("Invalid char in NODE_ID (position: " + index + ")");
            }
            contentBuffer.add(getActualChar());
            index++;
        }
        if (contentBuffer.isEmpty()) {
            throw new ParserException("Empty NODE_ID (position: " + index + ")");
        }
        String content = contentBuffer
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        fragmentList.add(new Fragment(FragmentType.NODE_ID_DECLARATION, content));
    }

    private void parseNodeIdRef() {
        List<Character> contentBuffer = new ArrayList<>();
        while (getActualChar() != ')' && getActualChar() != ',' && getActualChar() > ASCII_SPACE) {
            if (!validateNodeIdChar(getActualChar())) {
                throw new ParserException("Invalid char in NODE_ID (position: " + index + ")");
            }
            contentBuffer.add(getActualChar());
            index++;
        }
        if (contentBuffer.isEmpty()) {
            throw new ParserException("Empty NODE_ID (position: " + index + ")");
        }
        String content = contentBuffer
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        fragmentList.add(new Fragment(FragmentType.NODE_ID_REF, content));
    }

    private void parseNodeTypeDeclaration() {
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
        String content = contentBuffer
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        fragmentList.add(new Fragment(FragmentType.NODE_TYPE, content));
    }

    private void parseNodeParams() {
        while (getActualChar() != ')') {
            if (getActualChar() <= ASCII_SPACE) {
                index++;
                continue;
            }
            if (getActualChar() == '"') {
                fragmentList.add(new Fragment(FragmentType.OPENING_QUOTES));
                index++;
                parseNodeText();
                if (getActualChar() != '"') {
                    throw new ParserException("Text not closed (position: " + index + ")");
                }
                fragmentList.add(new Fragment(FragmentType.CLOSING_QUOTES));
                index++;
                continue;
            }
            if (getActualChar() == ',') {
                fragmentList.add(new Fragment(FragmentType.COMMA));
                index++;
                continue;
            }
            if (validateNodeIdChar(getActualChar())) {
                parseNodeIdRef();
                continue;
            }
            throw new ParserException("Invalid node param (position: " + index + ")");
        }
    }

    private void parseNodeText() {
        List<Character> contentBuffer = new ArrayList<>();
        while (getActualChar() != '"') {
            contentBuffer.add(getActualChar());
            index++;
        }
        if (contentBuffer.isEmpty()) {
            throw new ParserException("Empty NODE_TEXT (position: " + index + ")");
        }
        String content = contentBuffer
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        fragmentList.add(new Fragment(FragmentType.NODE_TEXT, content));
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
