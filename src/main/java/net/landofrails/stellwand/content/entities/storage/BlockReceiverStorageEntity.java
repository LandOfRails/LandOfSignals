package net.landofrails.stellwand.content.entities.storage;

import cam72cam.mod.ModCore;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.entities.function.BlockReceiverFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockReceiverRenderEntity;
import net.landofrails.stellwand.content.entities.storage.versionmapper.VersionMapper;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.receiver.BlockReceiverEntryBlock;
import net.landofrails.stellwand.utils.StellwandUtils;

import java.util.HashMap;
import java.util.Map;

public class BlockReceiverStorageEntity extends BlockReceiverFunctionEntity {

    // Statics
    public static final String MISSING = "missing";
    protected static Map<String, OBJModel> models = new HashMap<>();
    protected static Map<String, OBJRender> renderers = new HashMap<>();
    protected static Map<String, float[]> rotations = new HashMap<>();
    protected static Map<String, float[]> translations = new HashMap<>();
    protected static Map<String, Boolean> wallMountable = new HashMap<>();

    // TagFields
    @TagField("contentPackBlockId")
    public String contentPackBlockId = MISSING;

    @TagField("blockRotation")
    public float blockRotation = 0;

    @TagField(value = "blockBottom")
    public Boolean wallMounted;

    @TagField("active")
    public Boolean active;

    @TagField("version")
    public int version = 1;

    // Variables
    private boolean marked = false;
    private float[] markedColor = new float[]{0, 0, 0};

    // Subclasses
    @SuppressWarnings({"java:S1104"})
    public BlockReceiverRenderEntity renderEntity;

    public BlockReceiverStorageEntity() {
        renderEntity = new BlockReceiverRenderEntity(this);
    }

    @SuppressWarnings("java:S3252")
    public static void prepare() {

        Stellwand.debug("Preparing Receivers..");

        try {
            Identifier id = new Identifier(Stellwand.DOMAIN, "models/block/others/blocknotfound/blocknotfound.obj");
            OBJModel m = new OBJModel(id, 0);
            models.put(MISSING, m);
            rotations.put(MISSING, new float[]{0, 0, 0});
            translations.put(MISSING, new float[]{0.5f, 0.5f, 0.5f});
            wallMountable.put(MISSING, false);
        } catch (Exception e) {
            ModCore.Mod.error("Error while loading blocknotfound.obj: %s", e.getMessage());
        }
        // Add contentpack stuff
        for (Map.Entry<ContentPackEntry, String> entry : Content.getBlockReceivers().entrySet()) {
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
                BlockReceiverEntryBlock block = cpe.getBlock(BlockReceiverEntryBlock.class);
                String objPath = cpe.getModel();
                Identifier id = new Identifier("stellwand", objPath);
                OBJModel m = new OBJModel(id, 0);
                models.put(blockId, m);
                rotations.put(blockId, block.getRotation());
                translations.put(blockId, block.getTranslation());
                wallMountable.put(blockId, block.getWallMountable());
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


    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        if (active == null)
            return false;
        return active;
    }

    public void setWallMounted(boolean wallMounted) {
        if (wallMountable.containsKey(contentPackBlockId) && wallMountable.get(contentPackBlockId).booleanValue())
            this.wallMounted = wallMounted;
    }

    @SuppressWarnings("java:S1125")
    public boolean getWallMounted() {
        return wallMounted != null ? wallMounted : false;
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

    // Map old versions to newer ones
    @Override
    public void load(TagCompound nbt) {
        VersionMapper.checkMap(this.getClass(), nbt);
    }

}
