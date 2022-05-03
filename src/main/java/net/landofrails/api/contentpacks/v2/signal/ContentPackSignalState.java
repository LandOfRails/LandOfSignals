package net.landofrails.api.contentpacks.v2.signal;

import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;

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

    public void validate(Consumer<String> invalid) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");

        if (signalName == null || signalName.isEmpty())
            joiner.add("signalName");
        if (models == null || models.isEmpty() || models.containsKey(null))
            joiner.add("models");
        if (joiner.length() > 2) {
            invalid.accept(joiner.toString());
        } else if (models != null) {
            for (Map.Entry<String, ContentPackSignalModel[]> signalModelEntry : models.entrySet()) {
                String objPath = signalModelEntry.getKey();
                ContentPackSignalModel[] modelArray = signalModelEntry.getValue();
                for (ContentPackSignalModel model : modelArray) {
                    Consumer<String> modelConsumer = text -> invalid.accept(objPath + ": [" + text + "]");
                    model.validate(modelConsumer);
                }
            }
        }

    }
}