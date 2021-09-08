package net.landofrails.stellwand.content.entities.storage;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.entities.function.BlockMultisignalFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockMultisignalRenderEntity;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.multisignal.BlockMultisignalEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.utils.StellwandUtils;
import net.landofrails.stellwand.utils.mapper.MapStringStringMapper;

import java.util.*;

public class BlockMultisignalStorageEntity extends BlockMultisignalFunctionEntity {

    // Statics
    public static final String MISSING = "missing";
    protected static Map<String, OBJModel> models = new HashMap<>();
    protected static Map<String, OBJRender> renderers = new HashMap<>();
    protected static Map<String, float[]> rotations = new HashMap<>();
    protected static Map<String, float[]> translations = new HashMap<>();
    protected static Map<String, Map<String, Map<String, String>>> possibleModes = new LinkedHashMap<>();

    // TagFields
    @TagField("contentPackBlockId")
    public String contentPackBlockId = MISSING;

    @TagField("blockRotation")
    public float blockRotation = 0;

    // Signalgroup, Signalname
    @TagField(value = "displayModes", mapper = MapStringStringMapper.class)
    public Map<String, String> displayModes;

    // Variables
    // Sender, (Signalgroup, Signalname)
    @SuppressWarnings("java:S1104")
    public Map<Vec3i, Map<String, String>> senderModeslist = new HashMap<>();

    private static Map<String, LinkedList<String>> modeGroups = new HashMap<>();

    private boolean marked = false;
    private float[] markedColor = new float[]{0, 0, 0};

    // Subclasses
    private BlockMultisignalRenderEntity renderEntity;

    public BlockMultisignalStorageEntity() {
        renderEntity = new BlockMultisignalRenderEntity(this);
    }

    public BlockMultisignalRenderEntity getRenderEntity() {
        return renderEntity;
    }

    public static void prepare() {

        Stellwand.debug("Preparing Multisignals..");

        try {
            Identifier id = new Identifier(Stellwand.DOMAIN, "models/block/others/blocknotfound/blocknotfound.obj");
            OBJModel m = new OBJModel(id, 0);
            models.put(MISSING, m);
            rotations.put(MISSING, new float[]{0, 0, 0});
            translations.put(MISSING, new float[]{0.5f, 0.5f, 0.5f});
        } catch (Exception e) {
            ModCore.Mod.error("Error while loading blocknotfound.obj: %s", e.getMessage());
        }
        // Add contentpack stuff
        for (Map.Entry<ContentPackEntry, String> entry : Content.getBlockMultisignals().entrySet()) {
            String name = null;
            String type = null;
            ContentPackEntryBlock cpeb = null;
            try {
                ContentPackEntry cpe = entry.getKey();
                String packId = entry.getValue();
                String blockId = cpe.getBlockId(packId);
                cpeb = entry.getKey().getBlock();
                name = blockId;
                type = cpe.getType() != null ? cpe.getType().name() : "null";
                BlockMultisignalEntryBlock block = cpe.getBlock(BlockMultisignalEntryBlock.class);
                String objPath = cpe.getModel();
                Identifier id = new Identifier("stellwand", objPath);
                OBJModel m = new OBJModel(id, 0);
                models.put(blockId, m);
                rotations.put(blockId, block.getRotation());
                translations.put(blockId, block.getTranslation());
                possibleModes.put(blockId, new LinkedHashMap<>(block.getModesList()));
                modeGroups.put(blockId, block.getModeGroups());
            } catch (Exception e) {
                ModCore.Mod.error("Error while loading contentpack blocks:");
                ModCore.Mod.error("Block: %s", name);
                ModCore.Mod.error("Type: %s", type);
                StellwandUtils.printSuperclasses(cpeb);
                e.printStackTrace();
            }
        }
    }

    public String getContentPackBlockId() {
        return contentPackBlockId;
    }

    public void setContentBlockId(String contentPackBlockId) {
        this.contentPackBlockId = contentPackBlockId;
    }

    public Map<String, Map<String, String>> getPossibleModes() {
        if (possibleModes.containsKey(contentPackBlockId))
            return possibleModes.get(contentPackBlockId);
        return new LinkedHashMap<>();
    }

    public LinkedList<String> getModeGroups() {
        if (modeGroups.containsKey(contentPackBlockId))
            return modeGroups.get(contentPackBlockId);
        return new LinkedList<>();
    }

    public void setModes(Map<String, String> modes) {
        this.displayModes = modes;
    }

    public Map<String, String> getDisplayModes() {

        if ((displayModes == null || displayModes.isEmpty()) && possibleModes.containsKey(contentPackBlockId)) {
            displayModes = new HashMap<>();

            getPossibleModes().forEach((signalGroup, modes) -> {
                Iterator<Map.Entry<String, String>> it = modes.entrySet().iterator();
                if (it.hasNext())
                    displayModes.put(signalGroup, it.next().getValue());
            });

        }

        return displayModes;
    }

    @SuppressWarnings("java:S1751")
    public void updateSignalModes() {

        Map<String, String> actualModes = new HashMap<>();

        getPossibleModes().forEach((signalGroup, modes) -> {
            String actualMode = "";

            for (Map.Entry<String, String> entry : modes.entrySet()) {
                actualMode = entry.getValue();
                break;
            }
            for (String possibleMode : modes.values()) {
                for (Map.Entry<Vec3i, Map<String, String>> entry2 : senderModeslist.entrySet()) {
                    Map<String, String> signalGroupSignalMap = entry2.getValue();
                    if (signalGroupSignalMap.containsKey(signalGroup) && possibleMode.equals(signalGroupSignalMap.get(signalGroup))) {
                        actualMode = possibleMode;
                    }
                }
            }

            for (String possibleMode : modes.values())
                if (senderModeslist.get(getPos()) != null && senderModeslist.get(getPos()).containsValue(possibleMode))
                    actualMode = possibleMode;

            actualModes.put(signalGroup, actualMode);
        });

        setModes(actualModes);

    }

    // Getters
    public static Map<String, OBJModel> getModels() {
        return models;
    }

    public static Map<String, OBJRender> getRenderers() {
        return renderers;
    }

    public static Map<String, float[]> getRotations() {
        return rotations;
    }

    public static Map<String, float[]> getTranslations() {
        return translations;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked, float[] color) {
        this.marked = marked;
        this.markedColor = color;
    }

    public float[] getMarkedColor() {
        return markedColor;
    }

}
