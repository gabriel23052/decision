package com.gabriel2305.storyteller;

import com.gabriel2305.UI;

import java.util.ArrayList;
import java.util.List;

public class DecisionNode implements HistoryExecutable {

    private final String id;
    private final String text;
    private final List<Option> options = new ArrayList<>();
    private String nextNode = null;

    public DecisionNode(String id, String text) {
        this.text = text;
        this.id = id;
    }

    @Override
    public void execute() {
        int option = 0;
        UI.printLn(text);
        UI.blankLine();
        for (int i = 0; i < options.size(); i++) {
            UI.printLn((i + 1) + " - " + options.get(i).text());
        }
        UI.blankLine();
        while(true) {
            option = UI.readInt("Escolha");
            int index = option - 1;
            if (index < 0 || index >= options.size()) {
                UI.error("Opção inválida, tente novamente");
                continue;
            }
            break;
        }
        Option chosenOption = options.get(option - 1);
        nextNode = chosenOption.gotoId();
        UI.printLn("Você escolheu: " + chosenOption.text());
    }

    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public String getNextNodeId() {
        return nextNode;
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
