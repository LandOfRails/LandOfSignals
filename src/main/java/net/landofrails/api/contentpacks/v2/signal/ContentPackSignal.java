package net.landofrails.api.contentpacks.v2.signal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.api.contentpacks.v2.ContentPackException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class ContentPackSignal {

    private String name;
    private String id;
    private Integer rotationSteps;
    // objPath : objProperties
    private Map<String, ContentPackSignalModel[]> base;
    // groupId : group
    private Map<String, ContentPackSignalGroup> signals;
    // groupId : state
    private Map<String, String> itemGroupStates;
    // metadataId : data
    private Map<String, ?> metadata;

    public ContentPackSignal() {

    }

    public ContentPackSignal(String name, String id, Integer rotationSteps, Map<String, ContentPackSignalModel[]> base, Map<String, ContentPackSignalGroup> signals, Map<String, String> itemGroupStates, Map<String, Object> metadata) {
        this.name = name;
        this.id = id;
        this.rotationSteps = rotationSteps;
        this.base = base;
        this.signals = signals;
        this.itemGroupStates = itemGroupStates;
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRotationSteps() {
        return rotationSteps;
    }

    public void setRotationSteps(Integer rotationSteps) {
        this.rotationSteps = rotationSteps;
    }

    public Map<String, ContentPackSignalModel[]> getBase() {
        return base;
    }

    public void setBase(Map<String, ContentPackSignalModel[]> base) {
        this.base = base;
    }

    public Map<String, ContentPackSignalGroup> getSignals() {
        return signals;
    }

    public void setSignals(Map<String, ContentPackSignalGroup> signals) {
        this.signals = signals;
    }

    public Map<String, String> getItemGroupStates() {
        return itemGroupStates;
    }

    public void setItemGroupStates(Map<String, String> itemGroupStates) {
        this.itemGroupStates = itemGroupStates;
    }

    public Map<String, ?> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, ?> metadata) {
        this.metadata = metadata;
    }

    public static ContentPackSignal fromJson(InputStream inputStream) {
        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read;

        try {
            while ((read = inputStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            throw new ContentPackException("Cant read ContentPackSignal: " + e.getMessage());
        }

        String json = s.toString();
        Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, ContentPackSignal.class);
    }

    public void validate(Consumer<String> invalid) {

        defaultMissing();

        StringJoiner joiner = new StringJoiner(",", "[", "]");
        if (name == null)
            joiner.add("name");
        if (id == null)
            joiner.add("id");
        if (signals == null || signals.isEmpty())
            joiner.add("signals");
        if (joiner.length() > 2) {
            invalid.accept(joiner.toString());
        } else if (signals != null) {
            for (Map.Entry<String, ContentPackSignalGroup> signalGroupEntry : signals.entrySet()) {
                ContentPackSignalGroup signalGroup = signalGroupEntry.getValue();
                Consumer<String> signalConsumer = text -> invalid.accept(signalGroupEntry.getKey() + ": [" + text + "]");
                signalGroup.validate(signalConsumer);
            }
        }
    }

    private void defaultMissing() {
        if (rotationSteps == null) {
            rotationSteps = 10;
        } else {
            rotationSteps = Math.min(Math.max(10, rotationSteps), 90);
        }

        if (metadata == null) {
            metadata = new HashMap<>();
        }

        if (base == null) {
            base = new HashMap<>();
        }

        if (itemGroupStates == null) {
            itemGroupStates = new HashMap<>();
        }

        if (itemGroupStates.size() != signals.size()) {
            signals.forEach((groupId, group) -> itemGroupStates.putIfAbsent(groupId, group.getStates().keySet().iterator().next()));
        }

    }

}