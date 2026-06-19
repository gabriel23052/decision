package com.gabriel2305.storyteller;

import com.gabriel2305.UI;

public class TextNode implements StoryExecutable {

    private final String id;
    private final String text;
    private final String gotoId;

    public TextNode(String id, String text, String gotoId) {
        this.id = id;
        this.text = text;
        this.gotoId = gotoId;
    }

    @Override
    public void execute() {
        UI.printLn(text);
    }

    @Override
    public String[] getExternalReferences() {
        return new String[] { gotoId };
    }

    @Override
    public String getNextNodeId() {
        return gotoId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TextNode(\"" + text.substring(0, Math.min(text.length(), 8)) + "...\")";
    }
}
