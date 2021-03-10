package net.landofrails.landofsignals.utils.contentpacks;

import java.util.List;

public class ContentPackSignalPart {

    private String id;
    private String name;
    private String model;

    private float[] translation;
    private float[] itemTranslation;
    private float[] scaling;

    private List<String> states;

    public ContentPackSignalPart(String id, String name, String model, float[] translation, float[] itemTranslation, float[] scaling, List<String> states) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.translation = translation;
        this.itemTranslation = itemTranslation;
        this.scaling = scaling;
        this.states = states;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float[] getTranslation() {
        return translation;
    }

    public void setTranslation(float[] translation) {
        this.translation = translation;
    }

    public float[] getItemTranslation() {
        return itemTranslation;
    }

    public void setItemTranslation(float[] itemTranslation) {
        this.itemTranslation = itemTranslation;
    }

    public float[] getScaling() {
        return scaling;
    }

    public void setScaling(float[] scaling) {
        this.scaling = scaling;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }
}
