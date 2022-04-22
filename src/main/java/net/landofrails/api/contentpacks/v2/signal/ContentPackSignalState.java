package net.landofrails.api.contentpacks.v2.signal;

import java.util.Map;

public class ContentPackSignalState {

    private String signalName;
    private Map<String, ContentPackSignalModel[]> models;

    public ContentPackSignalState() {

    }

    public ContentPackSignalState(String signalName, Map<String, ContentPackSignalModel[]> models) {
        this.signalName = signalName;
        this.models = models;
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    public Map<String, ContentPackSignalModel[]> getModels() {
        return models;
    }

    public void setModels(Map<String, ContentPackSignalModel[]> models) {
        this.models = models;
    }
}