package net.landofrails.stellwand.content.entities.storage;

import cam72cam.mod.ModCore;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.entities.function.BlockFillerFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockFillerRenderEntity;
import net.landofrails.stellwand.content.entities.storage.versionmapper.VersionMapper;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.filler.BlockFillerEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.contentpacks.types.DirectionType;
import net.landofrails.stellwand.utils.StellwandUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BlockFillerStorageEntity extends BlockFillerFunctionEntity {

    // Statics
    public static final String MISSING = "missing";

    protected static Map<String, OBJModel> models = new HashMap<>();
    protected static Map<String, OBJRender> renderers = new HashMap<>();
    protected static Map<String, float[]> rotations = new HashMap<>();
    protected static Map<String, float[]> translations = new HashMap<>();
    protected static Map<String, DirectionType[]> directionFrom = new HashMap<>();
    protected static Map<String, DirectionType[]> directionTo = new HashMap<>();

    // TagFields
    @TagField("contentPackBlockId")
    public String contentPackBlockId = MISSING;

    @TagField("blockRotation")
    public float blockRotation = 0f;

    @TagField("version")
    public int version = 1;

    // Subclasses
    @SuppressWarnings({"java:S1104"})
    public BlockFillerRenderEntity renderEntity;

    public BlockFillerStorageEntity() {
        renderEntity = new BlockFillerRenderEntity(this);
    }

    public String getContentPackBlockId() {
        return contentPackBlockId;
    }

    public void setContentBlockId(String contentPackBlockId) {
        this.contentPackBlockId = contentPackBlockId;
    }

    public static void prepare() {

        Stellwand.debug("Preparing Filler..");

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
        for (Entry<ContentPackEntry, String> entry : Content.getBlockFillers().entrySet()) {
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
                ContentPackEntryBlock block = cpe.getBlock();
                String objPath = cpe.getModel();
                Identifier id = new Identifier("stellwand", objPath);
                OBJModel m = new OBJModel(id, 0);
                models.put(blockId, m);
                rotations.put(blockId, block.getRotation());
                translations.put(blockId, block.getTranslation());
                BlockFillerEntry blockFillerEntry = (BlockFillerEntry) cpe;
                directionFrom.put(blockId, blockFillerEntry.getDirectionFrom());
                directionTo.put(blockId, blockFillerEntry.getDirectionTo());
            } catch (Exception e) {
                ModCore.Mod.error("Error while loading contentpack blocks:");
                ModCore.Mod.error("Block: %s", name);
                ModCore.Mod.error("Type: %s", type);
                StellwandUtils.printSuperclasses(cpeb);
                e.printStackTrace();
            }
        }

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

    /**
     * Releases the renderer in to the wild and frees the cache preventing a deadlock situation
     */
    public static void releaseRenderersIntoTheWild() {
        getRenderers().forEach((k, v) -> v.free());
        getRenderers().clear();
    }

    // Map old versions to newer ones
    @Override
    public void load(TagCompound nbt) {
        VersionMapper.checkMap(this.getClass(), nbt);
    }

}
