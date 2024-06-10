package net.landofrails.api.contentpacks.v2.sign;

import com.google.gson.Gson;
import net.landofrails.api.contentpacks.v2.ContentPack;
import net.landofrails.api.contentpacks.v2.ContentPackException;
import net.landofrails.api.contentpacks.v2.flares.Flare;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.parent.ContentPackReferences;
import net.landofrails.landofsignals.LOSTabs;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ContentPackSign {

    private static final Gson GSON = new Gson();

    private String name;
    private String id;
    private Float rotationSteps;
    private String creativeTab;
    private Boolean writeable;
    // objPath : objProperties
    private Map<String, ContentPackModel[]> base;
    private Flare[] flares;
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

    public Flare[] getFlares() {
        return flares;
    }

    public void setFlares(Flare[] flares) {
        this.flares = flares;
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

    public Map<String, ContentPackModel[]> getBase() {
        return base;
    }

    public void setBase(Map<String, ContentPackModel[]> base) {
        this.base = base;
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

    public static ContentPackSign fromJson(InputStream inputStream) {
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
        return GSON.fromJson(json, ContentPackSign.class);
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
        } else if (!base.isEmpty()) {
            if (!"MISSING".equalsIgnoreCase(id)) {
                uniqueId = contentPack.getId() + ":" + id;
            } else {
                uniqueId = id;
            }

            for (Map.Entry<String, ContentPackModel[]> signModelEntry : base.entrySet()) {

                Consumer<String> signConsumer = text -> invalid.accept(signModelEntry.getKey() + ": [" + text + "]");
                Stream.of(signModelEntry.getValue()).forEach(model -> model.validate(signConsumer, references));
            }
        }
        if(Arrays.stream(flares).anyMatch(flare -> flare.getObjPath() == null || base.get(flare.getObjPath()).length == 0)){
            invalid.accept("Unable to determine obj path for the flares, add/check obj paths");
        }

        if (objTextures.isEmpty()) {
            for (Map.Entry<String, ContentPackModel[]> modelEntry : base.entrySet()) {
                for (ContentPackModel model : modelEntry.getValue()) {
                    String objPath = modelEntry.getKey();
                    objTextures.putIfAbsent(objPath, new HashSet<>());
                    Set<String> value = objTextures.get(objPath);
                    value.add("");
                    value.add(model.getTextures());
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
            creativeTab = LOSTabs.SIGNS_TAB;
        }

        if (metadata == null) {
            metadata = new HashMap<>();
        }

        if (base == null) {
            base = new HashMap<>();
        }

        if (objTextures == null) {
            objTextures = new HashMap<>();
        }

        if(flares == null){
            flares = new Flare[0];
        }

        List<String> baseList = new ArrayList<>(base.keySet());
        for(Flare flare : flares){
            String firstEntryKey = baseList.get(flare.getObjPathIndex());
            if(flare.getObjPath() == null){
                flare.setObjPath(firstEntryKey);
            }
            if(flare.getObjGroups() == null){
                String[] groups = base.get(flare.getObjPath())[flare.getObjPathIndex()].getObj_groups();
                if(groups == null)
                    groups = new String[0];
                flare.setObjGroups(groups);
            }
        }

    }

    public void setUTF8(boolean isUTF8) {
        this.isUTF8 = isUTF8;
    }

    public boolean isUTF8() {
        return isUTF8;
    }
}
