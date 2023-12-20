package net.landofrails.api.contentpacks.v2.lever;

import com.google.gson.Gson;
import net.landofrails.api.contentpacks.v2.ContentPack;
import net.landofrails.api.contentpacks.v2.ContentPackException;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.parent.ContentPackReferences;
import net.landofrails.landofsignals.LOSTabs;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ContentPackLever {
    private static final Gson GSON = new Gson();

    private String name;
    private String id;
    private Float rotationSteps;
    private String creativeTab;
    private Boolean writeable;
    // objPath : objProperties
    private Map<String, ContentPackModel[]> active;
    // objPath : objProperties
    private Map<String, ContentPackModel[]> inactive;
    private ContentPackReferences references;
    // metadataId : data
    private Map<String, Object> metadata;

    // Processed data
    private Map<String, Set<String>> objTextures;
    private String uniqueId;
    private Boolean isUTF8;

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

    public boolean isWriteable() {
        return Boolean.TRUE.equals(writeable);
    }

    public void setWriteable(Boolean writeable) {
        this.writeable = writeable;
    }

    public Map<String, ContentPackModel[]> getActive() {
        return active;
    }

    public void setActive(Map<String, ContentPackModel[]> active) {
        this.active = active;
    }

    public Map<String, ContentPackModel[]> getInactive() {
        return inactive;
    }

    public void setInactive(Map<String, ContentPackModel[]> inactive) {
        this.inactive = inactive;
    }

    public ContentPackReferences getReferences() {
        return references;
    }

    public void setReferences(ContentPackReferences references) {
        this.references = references;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Set<String>> getObjTextures() {
        return objTextures;
    }

    public void setObjTextures(Map<String, Set<String>> objTextures) {
        this.objTextures = objTextures;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public static ContentPackLever fromJson(InputStream inputStream) {
        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read;

        try {
            while ((read = inputStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            throw new ContentPackException("Cant read ContentPackSign: " + e.getMessage());
        }

        String json = s.toString();
        return GSON.fromJson(json, ContentPackLever.class);
    }

    public void validate(Consumer<String> invalid, ContentPack contentPack) {

        if (references == null) {
            references = new ContentPackReferences();
        }
        Consumer<String> referencesConsumer = text -> invalid.accept("references" + ": [" + text + "]");
        references.validate(referencesConsumer);

        defaultMissing();

        StringJoiner joiner = new StringJoiner(",", "[", "]");
        if (name == null)
            joiner.add("name");
        if (id == null)
            joiner.add("id");
        if (isUTF8 == null)
            joiner.add("isUTF8");
        if (joiner.length() > 2) {
            invalid.accept(joiner.toString());
        } else if (!active.isEmpty() && !inactive.isEmpty()) {
            if (!"MISSING".equalsIgnoreCase(id)) {
                uniqueId = contentPack.getId() + ":" + id;
            } else {
                uniqueId = id;
            }

            for (Map.Entry<String, ContentPackModel[]> activeModelEntry : active.entrySet()) {

                Consumer<String> activeConsumer = text -> invalid.accept(activeModelEntry.getKey() + ": [" + text + "]");
                Stream.of(activeModelEntry.getValue()).forEach(model -> model.validate(activeConsumer, references));
            }

            for (Map.Entry<String, ContentPackModel[]> inactiveModelEntry : inactive.entrySet()) {

                Consumer<String> inactiveConsumer = text -> invalid.accept(inactiveModelEntry.getKey() + ": [" + text + "]");
                Stream.of(inactiveModelEntry.getValue()).forEach(model -> model.validate(inactiveConsumer, references));
            }
        }

        if (objTextures.isEmpty()) {
            for (Map.Entry<String, ContentPackModel[]> modelEntry : active.entrySet()) {
                for (ContentPackModel model : modelEntry.getValue()) {
                    String objPath = modelEntry.getKey();
                    objTextures.putIfAbsent(objPath, new HashSet<>());
                    objTextures.computeIfPresent(objPath, (key, value) -> {
                        value.addAll(Arrays.asList(model.getTextures()));
                        return value;
                    });
                }
            }

            for (Map.Entry<String, ContentPackModel[]> modelEntry : inactive.entrySet()) {
                for (ContentPackModel model : modelEntry.getValue()) {
                    String objPath = modelEntry.getKey();
                    objTextures.putIfAbsent(objPath, new HashSet<>());
                    objTextures.computeIfPresent(objPath, (key, value) -> {
                        value.addAll(Arrays.asList(model.getTextures()));
                        return value;
                    });
                }
            }
        }

    }

    private void defaultMissing() {

        if (rotationSteps == null) {
            rotationSteps = 10f;
        } else {
            rotationSteps = Math.min(Math.max(10, rotationSteps), 90);
        }

        if (creativeTab == null) {
            creativeTab = LOSTabs.ASSETS_TAB;
        }

        if (metadata == null) {
            metadata = new HashMap<>();
        }

        if (active == null) {
            active = new HashMap<>();
        }

        if (inactive == null) {
            inactive = new HashMap<>();
        }

        if (objTextures == null) {
            objTextures = new HashMap<>();
        }
    }

    public void setUTF8(boolean isUTF8) {
        this.isUTF8 = isUTF8;
    }

    public boolean isUTF8() {
        return isUTF8;
    }
}
