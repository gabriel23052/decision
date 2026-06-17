package com.gabriel2305.parser;

import com.gabriel2305.UI;
import com.gabriel2305.exceptions.ParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

    private final char[] historyCharArr;

    public Parser(String historyStr) {
        this.historyCharArr = historyStr.toCharArray();
    }

    public void createFragments() throws ParserException {

        boolean insideNodeIdDeclaration = false;
        List<Fragment> fragmentList = new ArrayList<>();
        List<Character> contentBuffer = new ArrayList<>();

        for (int i = 0; i < historyCharArr.length; i++) {
            char actualChar = historyCharArr[i];

            if (actualChar == '[') {
                insideNodeIdDeclaration = true;
                fragmentList.add(new Fragment(FragmentType.OPENING_SQUARE_BRACKET));
                continue;
            }

            if (insideNodeIdDeclaration) {
                if (actualChar == ']') {
                    String content = contentBuffer
                            .stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining());
                    fragmentList.add(new Fragment(FragmentType.NODE_ID, content));
                    fragmentList.add(new Fragment(FragmentType.CLOSING_SQUARE_BRACKET));
                    insideNodeIdDeclaration = false;
                    contentBuffer.clear();
                    continue;
                }
                if (!validateNodeIdName(actualChar)) {
                    throw new ParserException("Invalid char in NODE_ID (position: " + i + ")");
                }
                contentBuffer.add(actualChar);
                continue;
            }

            if (actualChar <= 32) {
                continue;
            }

            throw new ParserException("Invalid char (position: " + i + ")");
        }
        UI.log(fragmentList);
    }

    private boolean validateNodeIdName(char value) {
        return (value >= 48 && value <= 57) ||
                (value >= 65 && value <= 90) ||
                value == 95 ||
                (value >= 97 && value <= 122);
    }
}
