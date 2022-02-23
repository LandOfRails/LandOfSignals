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

    private float[] itemScaling;

    private List<String> states;

    private Map<String, List<ContentPackAnimation>> animations;

    public ContentPackSignalPart(final String id, final String name, final String model, final float[] translation, final float[] itemTranslation, final float[] scaling, final List<String> states) {
        this(id, name, model, translation, itemTranslation, scaling, scaling, states);
    }

    public ContentPackSignalPart(final String id, final String name, final String model, final float[] translation, final float[] itemTranslation, final float[] scaling, final float[] itemScaling, final List<String> states) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.translation = translation;
        this.itemTranslation = itemTranslation;
        this.scaling = scaling;
        this.itemScaling = itemScaling;
        this.states = states;
    }

    public ContentPackSignalPart(final String id, final String name, final String model, final float[] translation, final float[] itemTranslation, final float[] scaling, final List<String> states, final Map<String, List<ContentPackAnimation>> animations) {
        this(id, name, model, translation, itemTranslation, scaling, scaling, states);
        this.animations = animations;
    }

    public ContentPackSignalPart(final String id, final String name, final String model, final float[] translation, final float[] itemTranslation, final float[] scaling, final float[] itemScaling, final List<String> states, final Map<String, List<ContentPackAnimation>> animations) {
        this(id, name, model, translation, itemTranslation, scaling, itemScaling, states);
        this.animations = animations;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public float[] getTranslation() {
        return translation;
    }

    public void setTranslation(final float[] translation) {
        this.translation = translation;
    }

    public float[] getItemTranslation() {
        return itemTranslation;
    }

    public void setItemTranslation(final float[] itemTranslation) {
        this.itemTranslation = itemTranslation;
    }

    public float[] getScaling() {
        return scaling;
    }

    public void setScaling(final float[] scaling) {
        this.scaling = scaling;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(final List<String> states) {
        this.states = states;
    }

    public float[] getItemScaling() {
        if (itemScaling == null) {
            if (scaling != null) {
                itemScaling = scaling;
            } else {
                // Emergency value
                itemScaling = new float[]{1, 1, 1};
            }
        }
        return itemScaling;
    }

    public void setItemScaling(final float[] itemScaling) {
        this.itemScaling = itemScaling;
    }

    public Map<String, List<ContentPackAnimation>> getAnimations() {
        return animations;
    }

    public void setAnimations(final Map<String, List<ContentPackAnimation>> animations) {
        this.animations = animations;
    }

    public static ContentPackSignalPart fromJson(final InputStream inputStream) {
        final StringBuilder s = new StringBuilder();
        final byte[] buffer = new byte[1024];
        int read = 0;

        try {
            while ((read = inputStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (final IOException e) {
            throw new ContentPackException("Cant read ContentPackSignalPart: " + e.getMessage());
        }

        final String json = s.toString();
        final Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, ContentPackSignalPart.class);
    }
}
