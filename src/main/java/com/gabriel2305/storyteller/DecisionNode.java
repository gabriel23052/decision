package com.gabriel2305.storyteller;

import java.util.ArrayList;
import java.util.List;

public class DecisionNode implements HistoryExecutable {

    private final String id;
    private final String text;
    private final List<Option> options = new ArrayList<>();

    public DecisionNode(String id, String text) {
        this.text = text;
        this.id = id;
    }

    @Override
    public void execute() {

    }

    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public String[] getExternalReferences() {
        return options
                .stream()
                .map(Option::gotoId)
                .toArray(String[]::new);
    }

    @Override
    public String toString() {
        return "Decision(\"" + text.substring(0, Math.min(text.length(), 8)) + "...\")";
    }

    @Override
    public String getId() {
        return id;
    }


}
