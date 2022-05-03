package net.landofrails.api.contentpacks.v2.signal;

import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;

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

    public void validate(Consumer<String> invalid) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        if (states == null) {
            joiner.add("states");
            invalid.accept(joiner.toString());
        } else {
            for (Map.Entry<String, ContentPackSignalState> signalStateEntry : states.entrySet()) {
                ContentPackSignalState signalState = signalStateEntry.getValue();
                signalState.validate(invalid);
            }
        }

    }
}