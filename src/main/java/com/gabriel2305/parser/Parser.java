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
        while(index < historyCharArr.length) {
            char actualChar = getActualChar();
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
            if (actualChar <= ASCII_SPACE) {
                index++;
                continue;
            }
            if(validateNodeIdNameChar(actualChar)) {
                parseNodeTypeDeclaration();
                continue;
            }
            throw new ParserException("Invalid char (position: " + index + ")");
        }
        UI.log(fragmentList);
    }

    private char getActualChar() {
        return historyCharArr[index];
    }

    private void parseNodeIdDeclaration() {
        List<Character> contentBuffer = new ArrayList<>();
        while(getActualChar() != ']') {
            if (!validateNodeIdNameChar(getActualChar())) {
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
        fragmentList.add(new Fragment(FragmentType.NODE_ID, content));
    }

    private void parseNodeTypeDeclaration() {
        List<Character> contentBuffer = new ArrayList<>();
        while(getActualChar() > ASCII_SPACE) {
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

    private static boolean validateNodeTypeChar(char value) {
        return value >= 97 && value <= 122;
    }

    private static boolean validateNodeIdNameChar(char value) {
        return (value >= 48 && value <= 57) ||
                (value >= 65 && value <= 90) ||
                value == 95 ||
                (value >= 97 && value <= 122);
    }
}
