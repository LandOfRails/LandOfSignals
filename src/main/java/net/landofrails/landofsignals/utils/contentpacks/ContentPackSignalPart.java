package net.landofrails.landofsignals.utils.contentpacks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ContentPackSignalPart {

    private String id;
    private String name;
    private String model;

    private float[] translation;
    private float[] itemTranslation;
    private float[] scaling;

    private List<String> states;

    private Map<String, List<ContentPackAnimation>> animations;

    public ContentPackSignalPart(String id, String name, String model, float[] translation, float[] itemTranslation, float[] scaling, List<String> states) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.translation = translation;
        this.itemTranslation = itemTranslation;
        this.scaling = scaling;
        this.states = states;
    }

    public ContentPackSignalPart(String id, String name, String model, float[] translation, float[] itemTranslation, float[] scaling, List<String> states, Map<String, List<ContentPackAnimation>> animations) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.translation = translation;
        this.itemTranslation = itemTranslation;
        this.scaling = scaling;
        this.states = states;
        this.animations = animations;
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

    public Map<String, List<ContentPackAnimation>> getAnimations() {
        return animations;
    }

    public void setAnimations(Map<String, List<ContentPackAnimation>> animations) {
        this.animations = animations;
    }

    public static ContentPackSignalPart fromJson(InputStream inputStream) {
        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read = 0;

        try {
            while ((read = inputStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            throw new ContentPackException("Cant read ContentPackSignalPart: " + e.getMessage());
        }

        String json = s.toString();
        Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, ContentPackSignalPart.class);
    }
}
