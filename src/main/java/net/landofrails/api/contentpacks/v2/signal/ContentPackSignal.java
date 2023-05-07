package net.landofrails.api.contentpacks.v2.signal;

import com.google.gson.Gson;
import net.landofrails.api.contentpacks.v2.ContentPack;
import net.landofrails.api.contentpacks.v2.ContentPackException;
import net.landofrails.landofsignals.LOSTabs;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

public class ContentPackSignal {

    private static final Gson GSON = new Gson();

    private String name;
    private String id;
    private Float rotationSteps;
    private String creativeTab;
    private String model;
    private Boolean useBase;
    private String base;
    private String[] states;
    private String itemState;

    private float[] translation;
    private float[] itemTranslation;
    private float[] scaling;
    private float[] itemScaling;

    // metadataId : data
    private Map<String, Object> metadata;

    // Processed data
    private String uniqueId;
    private Boolean isUTF8;

    public ContentPackSignal() {

    }

    @SuppressWarnings("java:S107")
    public ContentPackSignal(String name, String id, Float rotationSteps, String creativeTab, String model, Boolean useBase, String base, String[] states, String itemState, float[] translation, float[] itemTranslation, float[] scaling, float[] itemScaling) {
        this.name = name;
        this.id = id;
        this.rotationSteps = rotationSteps;
        this.creativeTab = creativeTab;
        this.model = model;
        this.useBase = useBase;
        this.base = base;
        this.states = states;
        this.itemState = itemState;
        this.translation = translation;
        this.itemTranslation = itemTranslation;
        this.scaling = scaling;
        this.itemScaling = itemScaling;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getRotationSteps() {
        return rotationSteps;
    }

    public void setRotationSteps(Float rotationSteps) {
        this.rotationSteps = rotationSteps;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public void setCreativeTab(String creativeTab) {
        this.creativeTab = creativeTab;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getUseBase() {
        return useBase;
    }

    public void setUseBase(Boolean useBase) {
        this.useBase = useBase;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String[] getStates() {
        return states.clone();
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public String getItemState() {
        return itemState;
    }

    public void setItemGroupStates(String itemState) {
        this.itemState = itemState;
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

    public float[] getItemScaling() {
        return itemScaling;
    }

    public void setItemScaling(float[] itemScaling) {
        this.itemScaling = itemScaling;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getUniqueId() {
        return uniqueId;
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
        return GSON.fromJson(json, ContentPackSignal.class);
    }

    public void validate(Consumer<String> invalid, ContentPack contentPack) {

        defaultMissing();

        StringJoiner joiner = new StringJoiner(",", "[", "]");
        if (name == null)
            joiner.add("name");
        if (id == null)
            joiner.add("id");
        if (model == null)
            joiner.add("model");
        if (states == null || states.length == 0)
            joiner.add("signals");
        if (isUTF8 == null)
            joiner.add("isUTF8");
        if (joiner.length() > 2) {
            invalid.accept(joiner.toString());
        } else {

            if (!"MISSING".equalsIgnoreCase(id)) {
                uniqueId = contentPack.getId() + ":" + id;
            } else {
                uniqueId = id;
            }

        }

    }

    private void defaultMissing() {

        if (states == null || Arrays.stream(states).noneMatch(String::isEmpty)) {
            String[] tempStates = new String[1 + (this.states != null ? this.states.length : 0)];
            tempStates[0] = "";
            if (this.states != null && this.states.length > 0) {
                System.arraycopy(this.states, 0, tempStates, 1, this.states.length);
            }
            this.states = tempStates;
        }

        if (rotationSteps == null) {
            rotationSteps = 10f;
        } else {
            rotationSteps = Math.min(Math.max(10, rotationSteps), 90);
        }

        if (creativeTab == null) {
            creativeTab = LOSTabs.SIGNALS_TAB;
        }

        if (useBase == null) {
            useBase = false;
        }

        if (metadata == null) {
            metadata = new HashMap<>();
        }

        if (itemState == null && states.length > 0) {
            itemState = states[0];
        }

        if (translation == null) {
            translation = new float[]{0, 0, 0};
        }

        if (itemTranslation == null) {
            itemTranslation = new float[]{0, 0, 0};
        }

        if (scaling == null) {
            scaling = new float[]{1, 1, 1};
        }

        if (itemScaling == null) {
            itemScaling = new float[]{1, 1, 1};
        }

    }

    public boolean isUTF8() {
        return isUTF8;
    }

    public void setUTF8(boolean isUTF8) {
        this.isUTF8 = isUTF8;
    }
}