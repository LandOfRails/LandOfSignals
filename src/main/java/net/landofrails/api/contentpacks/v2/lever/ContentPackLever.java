package net.landofrails.api.contentpacks.v2.lever;

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
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

        Predicate<String> isActive = "active"::equalsIgnoreCase;
        Predicate<String> isInactive = "inactive"::equalsIgnoreCase;

        if(Arrays.stream(flares).anyMatch(flare -> flare.getObjPath() == null)){
            invalid.accept("Unable to determine obj path for the flares, add/check obj paths");
        }else if(Arrays.stream(flares).anyMatch(flare -> flare.getStates().length != 1 && !(flare.isAlwaysOn() && flare.getStates().length == 0))){
            invalid.accept("Flare needs to have (only) one of the two states: \"active\" or \"inactive\". Exception: alwaysOn = true");
        }else if(Arrays.stream(flares).anyMatch(flare -> Arrays.stream(flare.getStates()).anyMatch(state -> !isActive.test(state) && !isInactive.test(state)))){
            invalid.accept("Flare needs to have (only) one of the two states: \"active\" or \"inactive\". Exception: alwaysOn = true");
        }

        if (objTextures.isEmpty()) {
            for (Map.Entry<String, ContentPackModel[]> modelEntry : active.entrySet()) {
                for (ContentPackModel model : modelEntry.getValue()) {
                    String objPath = modelEntry.getKey();
                    objTextures.putIfAbsent(objPath, new HashSet<>());
                    Set<String> value = objTextures.get(objPath);
                    value.add("");
                    value.add(model.getTextures());
                }
            }

            for (Map.Entry<String, ContentPackModel[]> modelEntry : inactive.entrySet()) {
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

        if(flares == null){
            flares = new Flare[0];
        }

        List<Flare> splitFlares = new ArrayList<>();
        for(Flare flare : flares){
            if(flare.isAlwaysOn() && flare.getObjPath() == null && active.size() == 1 && inactive.size() == 1){
                String firstActiveEntryKey = active.keySet().iterator().next();
                String firstInactiveEntryKey = inactive.keySet().iterator().next();
                if(!firstActiveEntryKey.equalsIgnoreCase(firstInactiveEntryKey)){
                    // active flare
                    String activeFirstEntryKey = active.keySet().iterator().next();
                    flare.setObjPath(activeFirstEntryKey);
                    String[] activeGroups = active.get(activeFirstEntryKey)[0].getObj_groups();
                    if(activeGroups == null)
                        activeGroups = new String[0];
                    flare.setObjGroups(activeGroups);
                    flare.setAlwaysOn(false);
                    flare.setStates(new String[]{"active"});

                    // inactive flare
                    Flare splitFlare = new Flare(flare);
                    String inactiveFirstEntryKey = inactive.keySet().iterator().next();
                    splitFlare.setObjPath(inactiveFirstEntryKey);
                    String[] inactiveGroups = inactive.get(inactiveFirstEntryKey)[0].getObj_groups();
                    if(inactiveGroups == null)
                        inactiveGroups = new String[0];
                    splitFlare.setObjGroups(inactiveGroups);
                    splitFlare.setStates(new String[]{"inactive"});
                    splitFlares.add(splitFlare);
                }else{
                    if(flare.getObjPath() == null && active.size() == 1){
                        String firstEntryKey = active.keySet().iterator().next();
                        flare.setObjPath(firstEntryKey);
                        String[] groups = active.get(firstEntryKey)[0].getObj_groups();
                        if(groups == null)
                            groups = new String[0];
                        flare.setObjGroups(groups);
                    }
                }
            }else if(flare.getStates().length == 1 && "active".equalsIgnoreCase(flare.getStates()[0])){
                if(flare.getObjPath() == null && active.size() == 1){
                    String firstEntryKey = active.keySet().iterator().next();
                    flare.setObjPath(firstEntryKey);
                    String[] groups = active.get(firstEntryKey)[0].getObj_groups();
                    if(groups == null)
                        groups = new String[0];
                    flare.setObjGroups(groups);
                }
            }else if(flare.getStates().length == 1 && "inactive".equalsIgnoreCase(flare.getStates()[0]) && flare.getObjPath() == null && inactive.size() == 1){
                String firstEntryKey = inactive.keySet().iterator().next();
                flare.setObjPath(firstEntryKey);
                String[] groups = inactive.get(firstEntryKey)[0].getObj_groups();
                if(groups == null)
                    groups = new String[0];
                flare.setObjGroups(groups);
            }
        }

        if(!splitFlares.isEmpty()) {
            List<Flare> newFlares = Arrays.stream(flares)
                    .collect(Collectors.toCollection(ArrayList::new));
            newFlares.addAll(splitFlares);
            flares = newFlares.toArray(new Flare[0]);
        }


    }

    public void setUTF8(boolean isUTF8) {
        this.isUTF8 = isUTF8;
    }

    public boolean isUTF8() {
        return isUTF8;
    }
}
