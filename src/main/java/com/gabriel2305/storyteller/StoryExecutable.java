package com.gabriel2305.storyteller;

public interface StoryExecutable {

    void execute();
    String getId();
    String[] getExternalReferences();
    String getNextNodeId();

}
