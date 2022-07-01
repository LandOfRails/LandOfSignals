package net.landofrails.api.contentpacks.v2.parent;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;

@SuppressWarnings({"java:S100", "java:S116", "java:S117"})
public class ContentPackReferences {

    private Map<String, float[]> translationRefs;
    private Map<String, float[]> rotationRefs;
    private Map<String, float[]> scalingRefs;

    private Map<String, String[]> textureRefs;
    private Map<String, String[]> obj_groupRefs;

    private Map<String, ContentPackItem> contentPackItemRefs;
    private Map<String, ContentPackBlock> contentPackBlockRefs;

    public ContentPackReferences() {

    }

    public ContentPackReferences(Map<String, float[]> translationRefs, Map<String, float[]> rotationRefs, Map<String, float[]> scalingRefs, Map<String, String[]> textureRefs, Map<String, String[]> obj_groupRefs, Map<String, ContentPackItem> contentPackItemRefs, Map<String, ContentPackBlock> contentPackBlockRefs) {
        this.translationRefs = translationRefs;
        this.rotationRefs = rotationRefs;
        this.scalingRefs = scalingRefs;
        this.textureRefs = textureRefs;
        this.obj_groupRefs = obj_groupRefs;
        this.contentPackItemRefs = contentPackItemRefs;
        this.contentPackBlockRefs = contentPackBlockRefs;
    }

    public Map<String, float[]> getTranslationRefs() {
        return translationRefs;
    }

    public void setTranslationRefs(Map<String, float[]> translationRefs) {
        this.translationRefs = translationRefs;
    }

    public Map<String, float[]> getRotationRefs() {
        return rotationRefs;
    }

    public void setRotationRefs(Map<String, float[]> rotationRefs) {
        this.rotationRefs = rotationRefs;
    }

    public Map<String, float[]> getScalingRefs() {
        return scalingRefs;
    }

    public void setScalingRefs(Map<String, float[]> scalingRefs) {
        this.scalingRefs = scalingRefs;
    }

    public Map<String, String[]> getTextureRefs() {
        return textureRefs;
    }

    public void setTextureRefs(Map<String, String[]> textureRefs) {
        this.textureRefs = textureRefs;
    }

    public Map<String, String[]> getObj_groupRefs() {
        return obj_groupRefs;
    }

    public void setObj_groupRefs(Map<String, String[]> obj_groupRefs) {
        this.obj_groupRefs = obj_groupRefs;
    }

    public Map<String, ContentPackItem> getContentPackItemRefs() {
        return contentPackItemRefs;
    }

    public void setContentPackItemRefs(Map<String, ContentPackItem> contentPackItemRefs) {
        this.contentPackItemRefs = contentPackItemRefs;
    }

    public Map<String, ContentPackBlock> getContentPackBlockRefs() {
        return contentPackBlockRefs;
    }

    public void setContentPackBlockRefs(Map<String, ContentPackBlock> contentPackBlockRefs) {
        this.contentPackBlockRefs = contentPackBlockRefs;
    }

    public void validate(Consumer<String> invalid) {

        defaultMissing(invalid);

        clampArray(rotationRefs.entrySet(), -360.0f, 360.0f);
        clampArray(scalingRefs.entrySet(), .1f, 25f);
    }

    public void defaultMissing(Consumer<String> invalid) {

        StringJoiner stringJoiner = new StringJoiner(",", "[", "]");

        if (translationRefs == null) {
            translationRefs = Collections.emptyMap();
        }
        defaultMissingFloats(translationRefs.entrySet(), new float[]{.5f, .5f, .5f}, "translationRefs", stringJoiner);

        if (rotationRefs == null) {
            rotationRefs = Collections.emptyMap();
        }
        defaultMissingFloats(rotationRefs.entrySet(), new float[]{0, 0, 0}, "rotationRefs", stringJoiner);


        if (scalingRefs == null) {
            scalingRefs = Collections.emptyMap();
        }
        defaultMissingFloats(scalingRefs.entrySet(), new float[]{.7f, .7f, .7f}, "scalingRefs", stringJoiner);

        if (obj_groupRefs == null) {
            obj_groupRefs = Collections.emptyMap();
        }

        if (textureRefs == null) {
            textureRefs = Collections.emptyMap();
        }

        if (contentPackItemRefs == null) {
            contentPackItemRefs = Collections.emptyMap();
        }

        if (contentPackBlockRefs == null) {
            contentPackBlockRefs = Collections.emptyMap();
        }

        if (stringJoiner.length() > 2) {
            invalid.accept("item: " + stringJoiner.toString());
        }

    }

    private void defaultMissingFloats(Set<Map.Entry<String, float[]>> floatMap, float[] defaultFloats, String field, StringJoiner parentJoiner) {
        StringJoiner refJoiner = new StringJoiner(",", "[", "]");
        if (!floatMap.isEmpty()) {
            for (Map.Entry<String, float[]> entry : floatMap) {
                if (entry.getValue() == null || entry.getValue().length == 0) {
                    entry.setValue(defaultFloats);
                } else if (entry.getValue().length != 3) {
                    refJoiner.add(entry.getKey() + "[" + toString(entry.getValue()) + "] should contain exactly " + defaultFloats.length + " values!");
                }
            }
        }
        if (refJoiner.length() > 2) {
            parentJoiner.add(field + "[" + refJoiner + "]");
        }
    }

    private String toString(float[] arr) {
        StringBuilder txt = new StringBuilder("[");
        for (float v : arr)
            txt.append(txt.length() == 1 ? "" + v : ", " + v);
        txt.append("]");
        return txt.toString();
    }

    /**
     * TODO Does this work?
     *
     * @param floatMap
     * @param min
     * @param max
     */
    private void clampArray(Set<Map.Entry<String, float[]>> floatMap, float min, float max) {
        for (Map.Entry<String, float[]> entry : floatMap) {
            if (entry.getValue() != null && entry.getValue().length == 0) {
                clampArray(entry.getValue(), min, max);
            }
        }
    }

    private void clampArray(float[] floatArray, float min, float max) {
        for (int index = 0; index < floatArray.length; index++)
            floatArray[index] = Math.max(Math.min(floatArray[index], max), min);
    }

    /* Getters */
    public float[] getTranslationOrElse(String id, float[] defaultTranslation) {
        return translationRefs.getOrDefault(id, defaultTranslation);
    }

    public float[] getRotationOrElse(String id, float[] defaultRotation) {
        return rotationRefs.getOrDefault(id, defaultRotation);
    }

    public float[] getScalingOrElse(String id, float[] defaultScaling) {
        return scalingRefs.getOrDefault(id, defaultScaling);
    }

    public String[] getTexturesOrElse(String id, String[] defaultTextures) {
        return textureRefs.getOrDefault(id, defaultTextures);
    }

    public String[] getObj_groupsOrElse(String id, String[] defaultObj_groups) {
        return obj_groupRefs.getOrDefault(id, defaultObj_groups);
    }

    public ContentPackItem getContentPackItemOrElse(String id, ContentPackItem defaultItem) {
        return contentPackItemRefs.getOrDefault(id, defaultItem);
    }

    public ContentPackBlock getContentPackBlockOrElse(String id, ContentPackBlock defaultBlock) {
        return contentPackBlockRefs.getOrDefault(id, defaultBlock);
    }

}
