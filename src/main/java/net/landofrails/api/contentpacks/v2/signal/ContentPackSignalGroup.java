package net.landofrails.api.contentpacks.v2.signal;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class ContentPackSignalGroup {

    private String groupName;
    private LinkedHashMap<String, ContentPackSignalState> states;

    public ContentPackSignalGroup() {

    }

    @SuppressWarnings("java:S1319")
    public ContentPackSignalGroup(String groupName, LinkedHashMap<String, ContentPackSignalState> states) {
        this.groupName = groupName;
        this.states = states;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @SuppressWarnings("java:S1319")
    public LinkedHashMap<String, ContentPackSignalState> getStates() {
        return states;
    }

    @SuppressWarnings("java:S1319")
    public void setStates(LinkedHashMap<String, ContentPackSignalState> states) {
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
                Consumer<String> groupConsumer = text -> invalid.accept(signalStateEntry.getKey() + ": [" + text + "]");
                signalState.validate(groupConsumer);
            }
        }

    }
}