package net.landofrails.api.contentpacks.v2.complexsignal;

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
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ContentPackComplexSignal {

    private static final Gson GSON = new Gson();

    private String name;
    private String id;
    private Float rotationSteps;
    private String creativeTab;
    // objPath : objProperties
    private Map<String, ContentPackModel[]> base;
    // groupId : group
    private Map<String, ContentPackSignalGroup> signals;
    // groupId : state
    private Map<String, String> itemGroupStates;
    private ContentPackReferences references;
    // metadataId : data
    private Map<String, Object> metadata;
    private Flare[] flares;
    // Processed data
    private Map<String, Set<String>> objTextures;
    private String uniqueId;
    private Boolean isUTF8;


    public ContentPackComplexSignal() {

    }

    @SuppressWarnings("java:S107")
    public ContentPackComplexSignal(String name, String id, Float rotationSteps, String creativeTab, Map<String, ContentPackModel[]> base, Map<String, ContentPackSignalGroup> signals, Map<String, String> itemGroupStates, ContentPackReferences references, Map<String, Object> metadata) {
        this.name = name;
        this.id = id;
        this.rotationSteps = rotationSteps;
        this.creativeTab = creativeTab;
        this.base = base;
        this.signals = signals;
        this.itemGroupStates = itemGroupStates;
        this.references = references;
        this.metadata = metadata;
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

    public Map<String, ContentPackModel[]> getBase() {
        return base;
    }

    public void setBase(Map<String, ContentPackModel[]> base) {
        this.base = base;
    }

    public Map<String, ContentPackSignalGroup> getSignals() {
        return signals;
    }

    public void setSignals(Map<String, ContentPackSignalGroup> signals) {
        this.signals = signals;
    }

    public Map<String, String> getItemGroupStates() {
        return itemGroupStates;
    }

    public void setItemGroupStates(Map<String, String> itemGroupStates) {
        this.itemGroupStates = itemGroupStates;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Flare[] getFlares() {
        return flares;
    }

    public void setFlares(Flare[] flares) {
        this.flares = flares;
    }

    public Map<String, Set<String>> getObjTextures() {
        return objTextures;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public static ContentPackComplexSignal fromJson(InputStream inputStream) {
        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read;

        try {
            while ((read = inputStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            throw new ContentPackException("Cant read ContentPackComplexSignal: " + e.getMessage());
        }

        String json = s.toString();
        return GSON.fromJson(json, ContentPackComplexSignal.class);
    }

    public void validate(Consumer<String> invalid, ContentPack contentPack) {

        if (references == null) {
            references = new ContentPackReferences();
        }
        Consumer<String> referencesConsumer = text -> invalid.accept("references" + ": [" + text + "]");
        references.validate(referencesConsumer);

        StringJoiner joiner = new StringJoiner(",", "[", "]");
        if (name == null)
            joiner.add("name");
        if (id == null)
            joiner.add("id");
        if (signals == null || signals.isEmpty())
            joiner.add("signals");
        if (isUTF8 == null)
            joiner.add("isUTF8");
        if (joiner.length() > 2) {
            invalid.accept(joiner.toString());
        } else {

            defaultMissing();

            if(Arrays.stream(flares).anyMatch(flare -> flare.isAlwaysOn() && base.isEmpty())){
                invalid.accept("Flare alwaysOn and signal without base, either specify a groupId or add a base model");
            }else if(Arrays.stream(flares).anyMatch(flare -> flare.isAlwaysOn() && flare.getObjPath() == null && base.size() > 1)){
                invalid.accept("Flare alwaysOn and multiple entries in base, please specify an objPath");
            }else if(Arrays.stream(flares).anyMatch(flare -> flare.isAlwaysOn() && !base.containsKey(flare.getObjPath()))){
                invalid.accept("Flare specifies objPath that is not found in base.");
            }else if(Arrays.stream(flares).anyMatch(flare -> !flare.isAlwaysOn() && !signals.containsKey(flare.getGroupId()))){
                invalid.accept("Flare specifies groupId that is not found in signals.");
            }else if(
                Arrays.stream(flares).filter(flare -> !flare.isAlwaysOn() && flare.getObjPath() == null).anyMatch(flare ->
                        Arrays.stream(flare.getStates()).anyMatch(state -> signals.get(flare.getGroupId()).getStates().get(state).getModels().size() > 1)
                )
            ){
                invalid.accept("Flare specifies no objPath but has multiple available entries.");
            }


            if (!"MISSING".equalsIgnoreCase(id)) {
                uniqueId = contentPack.getId() + ":" + id;
            } else {
                uniqueId = id;
            }

            if (signals != null) {

                for (Map.Entry<String, ContentPackSignalGroup> signalGroupEntry : signals.entrySet()) {
                    ContentPackSignalGroup signalGroup = signalGroupEntry.getValue();
                    Consumer<String> signalConsumer = text -> invalid.accept(signalGroupEntry.getKey() + ": [" + text + "]");
                    signalGroup.validate(signalConsumer, references);
                }
            }
            if (!base.isEmpty()) {
                for (Map.Entry<String, ContentPackModel[]> signalModelEntry : base.entrySet()) {
                    Consumer<String> signalConsumer = text -> invalid.accept(signalModelEntry.getKey() + ": [" + text + "]");
                    Stream.of(signalModelEntry.getValue()).forEach(model -> model.validate(signalConsumer, references));
                }
            }
            if (objTextures.isEmpty()) {
                initObjTextures();
            }
        }

    }

    private void initObjTextures() {
        for (ContentPackSignalGroup group : signals.values()) {
            for (ContentPackSignalState state : group.getStates().values()) {
                for (Map.Entry<String, ContentPackModel[]> modelEntry : state.getModels().entrySet()) {
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

    @SuppressWarnings("java:S6541")
    private void defaultMissing() {

        if (rotationSteps == null) {
            rotationSteps = 10f;
        } else {
            rotationSteps = Math.min(Math.max(10, rotationSteps), 90);
        }

        if (creativeTab == null) {
            creativeTab = LOSTabs.SIGNALS_TAB;
        }

        if (metadata == null) {
            metadata = new HashMap<>();
        }

        if (base == null) {
            base = new HashMap<>();
        }

        if (itemGroupStates == null) {
            itemGroupStates = new HashMap<>();
        }

        if (itemGroupStates.size() != signals.size()) {

            Function<ContentPackSignalGroup, String> nullDefault = group -> {
                String stateId = group.getStates().keySet().iterator().next();
                if (stateId == null) {
                    if (group.getStates().containsKey("default")) {
                        throw new ContentPackException("There is a null and a \"default\" state. Sorry, this will not work. :(");
                    }
                    stateId = "default";
                }
                return stateId;
            };
            signals.forEach((groupId, group) -> itemGroupStates.putIfAbsent(groupId, nullDefault.apply(group)));
        }

        if (objTextures == null) {
            objTextures = new HashMap<>();
        }

        if(flares == null){
            flares = new Flare[0];
        }

        Arrays.stream(flares).forEach(Flare::validate);

        for(Flare flare : flares){
            if(flare.isAlwaysOn() && flare.getGroupId() == null && flare.getObjPath() == null && base.size() == 1) {
                String firstEntryKey = base.keySet().iterator().next();
                flare.setObjPath(firstEntryKey);
                String[] groups = base.get(firstEntryKey)[0].getObj_groups();
                if (groups == null)
                    groups = new String[0];
                flare.setObjGroups(groups);
            }else if(flare.getGroupId() != null && flare.getObjPath() == null){
                ContentPackSignalState firstState = signals.get(flare.getGroupId()).getStates().get(flare.getStates()[0]);
                if(firstState.getModels().size() == 1){
                    String firstObjPath = firstState.getModels().keySet().iterator().next();
                    String[] firstObjGroups = firstState.getModels().get(firstObjPath)[flare.getObjPathIndex()].getObj_groups();
                    if(
                        Arrays.stream(flare.getStates()).allMatch(stateKey -> {
                            ContentPackSignalState state = signals.get(flare.getGroupId()).getStates().get(stateKey);
                            String stateObjPath = state.getModels().keySet().iterator().next();
                            return state.getModels().size() == 1 &&
                                    state.getModels().get(stateObjPath).length == 1 &&
                                    firstObjPath.equalsIgnoreCase(stateObjPath) &&
                                    Arrays.deepEquals(firstObjGroups, state.getModels().get(stateObjPath)[0].getObj_groups());
                        })
                    ){
                        flare.setObjPath(firstObjPath);
                        flare.setObjGroups(firstObjGroups == null ? new String[0] : firstObjGroups);
                    }
                }
            }
        }
    }

    public boolean isUTF8() {
        return isUTF8;
    }

    public void setUTF8(boolean isUTF8) {
        this.isUTF8 = isUTF8;
    }
}