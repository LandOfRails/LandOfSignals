package net.landofrails.api.contentpacks.v2.signal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.api.contentpacks.v2.ContentPackException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ContentPackSignal {

    private String name;
    private String id;
    private Integer rotationSteps;
    private Map<String, ContentPackSignalGroup> signals;
    private Map<String, Object> metadata;

    public ContentPackSignal() {

    }

    public ContentPackSignal(String name, String id, Integer rotationSteps, Map<String, ContentPackSignalGroup> signals, Map<String, Object> metadata) {
        this.name = name;
        this.id = id;
        this.rotationSteps = rotationSteps;
        this.signals = signals;
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

    public Map<String, ContentPackSignalGroup> getSignals() {
        return signals;
    }

    public void setSignals(Map<String, ContentPackSignalGroup> signals) {
        this.signals = signals;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
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
}