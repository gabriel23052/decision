package com.gabriel2305.parser;

import com.gabriel2305.exceptions.ParserException;
import com.gabriel2305.storyteller.DecisionNode;
import com.gabriel2305.storyteller.HistoryExecutable;
import com.gabriel2305.storyteller.Option;
import com.gabriel2305.storyteller.TextNode;

import java.util.ArrayList;
import java.util.List;

public class FragmentParser {

    private FragmentParserState state = FragmentParserState.IDLE;
    private final List<Fragment> fragments;
    private final List<HistoryExecutable> historyNodes = new ArrayList<>();
    private int index = 0;
    private String actualNodeId;
    private String actualNodeType;

    public FragmentParser(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    private Fragment getActualFragment() {
        return fragments.get(index);
    }

    private boolean isThisFragment(FragmentType type) {
        return getActualFragment().type() == type;
    }

    public List<HistoryExecutable> createHistory() {
        while (index < fragments.size()) {

            if (state == FragmentParserState.IDLE
                    && isThisFragment(FragmentType.OPENING_SQUARE_BRACKET)) {
                state = FragmentParserState.ID_DECLARATION;
                index++;
                continue;
            }

            if (state == FragmentParserState.ID_DECLARATION
                    && isThisFragment(FragmentType.NODE_ID_DECLARATION)) {
                actualNodeId = getActualFragment().content();
                state = FragmentParserState.ID_DECLARED;
                index++;
                continue;
            }

            if (state == FragmentParserState.ID_DECLARED
                    && isThisFragment(FragmentType.CLOSING_SQUARE_BRACKET)) {
                state = FragmentParserState.TYPE_DECLARATION;
                index++;
                continue;
            }

            if (state == FragmentParserState.TYPE_DECLARATION
                    && isThisFragment(FragmentType.NODE_TYPE)) {
                actualNodeType = getActualFragment().content();
                state = FragmentParserState.TYPE_DECLARED;
                index++;
                continue;
            }

            if (state == FragmentParserState.TYPE_DECLARED
                    && isThisFragment(FragmentType.OPENING_PARENTHESIS)) {
                state = FragmentParserState.NODE_PARAMS_DECLARATION;
                index++;
                nodeParamsHandler();
                continue;
            }

            throw new ParserException("Unexpected fragment: " + getActualFragment());
        }

        if (state != FragmentParserState.IDLE) {
            throw new ParserException("Unexpected end");
        }
        return historyNodes;
    }

    private void nodeParamsHandler() {
        if (actualNodeType.equals("text")) {
            createTextNode();
            return;
        }
        if (actualNodeType.equals("decision")) {
            createDecisionNode();
            return;
        }
        throw new ParserException("The node type \"" + actualNodeType + "\" doesn't exists");
    }

    private void createTextNode() {
        String text = "";
        String gotoId = "";
        while (state != FragmentParserState.IDLE) {

            if (state == FragmentParserState.NODE_PARAMS_DECLARATION
                    && isThisFragment(FragmentType.OPENING_QUOTES)) {
                state = FragmentParserState.NODE_TEXT_DECLARATION;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_TEXT_DECLARATION
                    && isThisFragment(FragmentType.NODE_TEXT)) {
                text = getActualFragment().content();
                state = FragmentParserState.NODE_TEXT_DECLARED;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_TEXT_DECLARED
                    && isThisFragment(FragmentType.CLOSING_QUOTES)) {
                state = FragmentParserState.NODE_PARAM_TRANSITION;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_PARAM_TRANSITION
                    && isThisFragment(FragmentType.COMMA)) {
                state = FragmentParserState.NODE_GOTO_DECLARATION;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_GOTO_DECLARATION
                    && isThisFragment(FragmentType.NODE_ID_REF)) {
                gotoId = getActualFragment().content();
                state = FragmentParserState.NODE_GOTO_DECLARED;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_GOTO_DECLARED
                    && isThisFragment(FragmentType.CLOSING_PARENTHESIS)) {
                index++;
                state = FragmentParserState.IDLE;
                continue;
            }

            throw new ParserException("Unexpected fragment: " + getActualFragment());
        }
        historyNodes.add(new TextNode(actualNodeId, text, gotoId));
    }

    private void createDecisionNode() {
        String text = "";
        List<Option> options = new ArrayList<>();

        while (state != FragmentParserState.IDLE) {

            if (state == FragmentParserState.NODE_PARAMS_DECLARATION
                    && isThisFragment(FragmentType.OPENING_QUOTES)) {
                state = FragmentParserState.NODE_TEXT_DECLARATION;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_TEXT_DECLARATION
                    && isThisFragment(FragmentType.NODE_TEXT)) {
                text = getActualFragment().content();
                state = FragmentParserState.NODE_TEXT_DECLARED;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_TEXT_DECLARED
                    && isThisFragment(FragmentType.CLOSING_QUOTES)) {
                state = FragmentParserState.NODE_PARAM_TRANSITION;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_PARAM_TRANSITION
                    && isThisFragment(FragmentType.COMMA)) {
                state = FragmentParserState.NODE_GOTO_DECLARATION;
                index++;
                createOptions(options);
                continue;
            }

            throw new ParserException("Unexpected fragment: " + getActualFragment());
        }
        DecisionNode decisionNode = new DecisionNode(actualNodeId, text);
        options.forEach(decisionNode::addOption);
        historyNodes.add(decisionNode);
    }

    private void createOptions(List<Option> optionsList) {
        String gotoIdBuffer = "";
        String textBuffer = "";

        while(state != FragmentParserState.IDLE) {

            if (state == FragmentParserState.NODE_GOTO_DECLARATION
                    && isThisFragment(FragmentType.NODE_ID_REF)) {
                gotoIdBuffer = getActualFragment().content();
                state = FragmentParserState.NODE_GOTO_DECLARED;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_GOTO_DECLARED
                    && isThisFragment(FragmentType.COMMA)) {
                state = FragmentParserState.NODE_PARAM_TRANSITION;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_PARAM_TRANSITION
                    && isThisFragment(FragmentType.OPENING_QUOTES)) {
                state = FragmentParserState.NODE_TEXT_DECLARATION;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_TEXT_DECLARATION
                    && isThisFragment(FragmentType.NODE_TEXT)) {
                textBuffer = getActualFragment().content();
                state = FragmentParserState.NODE_TEXT_DECLARED;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_TEXT_DECLARED
                    && isThisFragment(FragmentType.CLOSING_QUOTES)) {
                state = FragmentParserState.NODE_PARAM_TRANSITION;
                index++;
                continue;
            }

            if (state == FragmentParserState.NODE_PARAM_TRANSITION) {
                optionsList.add(new Option(textBuffer, gotoIdBuffer));
                textBuffer = "";
                gotoIdBuffer = "";
                if (isThisFragment(FragmentType.COMMA)) {
                    state = FragmentParserState.NODE_GOTO_DECLARATION;
                    index++;
                    continue;
                } else if (isThisFragment(FragmentType.CLOSING_PARENTHESIS)) {
                    state = FragmentParserState.IDLE;
                    index++;
                    continue;
                }
            }

            throw new ParserException("Unexpected fragment: " + getActualFragment() + " " + index);
        }
    }

}

