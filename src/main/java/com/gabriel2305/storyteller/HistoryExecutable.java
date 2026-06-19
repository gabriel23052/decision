package com.gabriel2305.storyteller;

public interface HistoryExecutable {

    void execute();
    String getId();
    String[] getExternalReferences();
    String getNextNodeId();

}
