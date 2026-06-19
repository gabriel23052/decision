package com.gabriel2305.parser;

import com.gabriel2305.exceptions.ParserException;
import com.gabriel2305.storyteller.DecisionNode;
import com.gabriel2305.storyteller.HistoryExecutable;
import com.gabriel2305.storyteller.Option;
import com.gabriel2305.storyteller.TextNode;

import java.util.ArrayList;
import java.util.List;

public class FragmentParser {

    private int index = 0;
    private String actualNodeId;
    private String actualNodeType;
    private FragmentParserState state = FragmentParserState.IDLE;

    private final List<Fragment> fragments;
    private final List<HistoryExecutable> historyNodes = new ArrayList<>();

    public FragmentParser(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    private Fragment getActualFragment() {
        return fragments.get(index);
    }

    private boolean isAValidFragment(FragmentParserState targetState, FragmentType targetType) {
        return state == targetState && targetType == getActualFragment().type();
    }

    public List<HistoryExecutable> createHistory() {
        while (index < fragments.size()) {

            if (isAValidFragment(FragmentParserState.IDLE, FragmentType.OPENING_SQUARE_BRACKET)) {
                state = FragmentParserState.ID_DECLARATION;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.ID_DECLARATION, FragmentType.NODE_ID_DECLARATION)) {
                actualNodeId = getActualFragment().content();
                state = FragmentParserState.ID_DECLARED;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.ID_DECLARED, FragmentType.CLOSING_SQUARE_BRACKET)) {
                state = FragmentParserState.TYPE_DECLARATION;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.TYPE_DECLARATION, FragmentType.NODE_TYPE)) {
                actualNodeType = getActualFragment().content();
                state = FragmentParserState.TYPE_DECLARED;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.TYPE_DECLARED, FragmentType.OPENING_PARENTHESIS)) {
                state = FragmentParserState.NODE_PARAMS_DECLARATION;
                index++;
                nodeDelegator();
                continue;
            }

            throw new ParserException("Unexpected fragment: " + getActualFragment());
        }

        if (state != FragmentParserState.IDLE) {
            throw new ParserException("Unexpected end");
        }

        return historyNodes;
    }

    private void nodeDelegator() {
        if (actualNodeType.equals("text")) {
            handleTextNode();
            return;
        }
        if (actualNodeType.equals("decision")) {
            handleDecisionNode();
            return;
        }
        throw new ParserException("The node type \"" + actualNodeType + "\" doesn't exists");
    }

    private void handleTextNode() {
        String text = "";
        String gotoId = "";

        while (state != FragmentParserState.IDLE) {

            if (isAValidFragment(FragmentParserState.NODE_PARAMS_DECLARATION, FragmentType.OPENING_QUOTES)) {
                state = FragmentParserState.NODE_TEXT_DECLARATION;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_TEXT_DECLARATION, FragmentType.NODE_TEXT)) {
                text = getActualFragment().content();
                state = FragmentParserState.NODE_TEXT_DECLARED;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_TEXT_DECLARED, FragmentType.CLOSING_QUOTES)) {
                state = FragmentParserState.NODE_PARAM_TRANSITION;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_PARAM_TRANSITION, FragmentType.COMMA)) {
                state = FragmentParserState.NODE_GOTO_DECLARATION;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_GOTO_DECLARATION, FragmentType.NODE_ID_REF)) {
                gotoId = getActualFragment().content();
                state = FragmentParserState.NODE_GOTO_DECLARED;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_GOTO_DECLARED, FragmentType.CLOSING_PARENTHESIS)) {
                index++;
                state = FragmentParserState.IDLE;
                continue;
            }

            throw new ParserException("Unexpected fragment: " + getActualFragment());
        }
        historyNodes.add(new TextNode(actualNodeId, text, gotoId));
    }

    private void handleDecisionNode() {
        String text = "";
        List<Option> options = new ArrayList<>();

        while (state != FragmentParserState.IDLE) {

            if (isAValidFragment(FragmentParserState.NODE_PARAMS_DECLARATION, FragmentType.OPENING_QUOTES)) {
                state = FragmentParserState.NODE_TEXT_DECLARATION;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_TEXT_DECLARATION, FragmentType.NODE_TEXT)) {
                text = getActualFragment().content();
                state = FragmentParserState.NODE_TEXT_DECLARED;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_TEXT_DECLARED, FragmentType.CLOSING_QUOTES)) {
                state = FragmentParserState.NODE_PARAM_TRANSITION;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_PARAM_TRANSITION, FragmentType.COMMA)) {
                state = FragmentParserState.NODE_GOTO_DECLARATION;
                index++;
                handleDecisionOptions(options);
                continue;
            }

            throw new ParserException("Unexpected fragment: " + getActualFragment());
        }

        DecisionNode decisionNode = new DecisionNode(actualNodeId, text);
        options.forEach(decisionNode::addOption);
        historyNodes.add(decisionNode);
    }

    private void handleDecisionOptions(List<Option> optionsList) {

        String gotoIdBuffer = "";
        String textBuffer = "";

        while (state != FragmentParserState.IDLE) {

            if (isAValidFragment(FragmentParserState.NODE_GOTO_DECLARATION, FragmentType.NODE_ID_REF)) {
                gotoIdBuffer = getActualFragment().content();
                state = FragmentParserState.NODE_GOTO_DECLARED;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_GOTO_DECLARED, FragmentType.COMMA)) {
                state = FragmentParserState.NODE_PARAM_TRANSITION;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_PARAM_TRANSITION, FragmentType.OPENING_QUOTES)) {
                state = FragmentParserState.NODE_TEXT_DECLARATION;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_TEXT_DECLARATION, FragmentType.NODE_TEXT)) {
                textBuffer = getActualFragment().content();
                state = FragmentParserState.NODE_TEXT_DECLARED;
                index++;
                continue;
            }

            if (isAValidFragment(FragmentParserState.NODE_TEXT_DECLARED, FragmentType.CLOSING_QUOTES)) {
                state = FragmentParserState.NODE_PARAM_TRANSITION;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_PARAM_TRANSITION) {
                optionsList.add(new Option(textBuffer, gotoIdBuffer));
                textBuffer = "";
                gotoIdBuffer = "";

                if (getActualFragment().type() == FragmentType.COMMA) {
                    state = FragmentParserState.NODE_GOTO_DECLARATION;
                    index++;
                    continue;
                } else if (getActualFragment().type() == FragmentType.CLOSING_PARENTHESIS) {
                    state = FragmentParserState.IDLE;
                    index++;
                    continue;
                }
            }

            throw new ParserException("Unexpected fragment: " + getActualFragment() + " " + index);
        }
    }
}

