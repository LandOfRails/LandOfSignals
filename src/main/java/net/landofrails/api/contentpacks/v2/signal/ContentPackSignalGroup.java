package net.landofrails.api.contentpacks.v2.signal;

import java.util.Map;

public class ContentPackSignalGroup {

    private String groupName;
    private Map<String, ContentPackSignalState> states;

    public ContentPackSignalGroup() {

    }

    public ContentPackSignalGroup(String groupName, Map<String, ContentPackSignalState> states) {
        this.groupName = groupName;
        this.states = states;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Map<String, ContentPackSignalState> getStates() {
        return states;
    }

    public void setStates(Map<String, ContentPackSignalState> states) {
        this.states = states;
    }
}