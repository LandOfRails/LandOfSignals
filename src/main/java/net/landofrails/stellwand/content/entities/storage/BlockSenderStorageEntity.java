package net.landofrails.stellwand.content.entities.storage;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.entities.function.BlockSenderFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSenderRenderEntity;
import net.landofrails.stellwand.content.entities.storage.versionmapper.VersionMapper;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.sender.BlockSenderEntry;
import net.landofrails.stellwand.contentpacks.types.DirectionType;
import net.landofrails.stellwand.utils.StellwandUtils;
import net.landofrails.stellwand.utils.compact.SignalContainer;
import net.landofrails.stellwand.utils.mapper.EmptyStringMapper;
import net.landofrails.stellwand.utils.mapper.Vec3iListMapper;

import java.util.*;
import java.util.Map.Entry;

public class BlockSenderStorageEntity extends BlockSenderFunctionEntity {

    // Statics
    public static final String MISSING = "missing";

    protected static Map<String, OBJModel> models = new HashMap<>();
    protected static Map<String, float[]> rotations = new HashMap<>();
    protected static Map<String, float[]> translations = new HashMap<>();
    protected static Map<String, DirectionType[]> directionFrom = new HashMap<>();
    protected static Map<String, DirectionType[]> directionTo = new HashMap<>();

    // TagFields
    @TagField("version")
    public int version = 2;

    @TagField("contentPackBlockId")
    public String contentPackBlockId = MISSING;

    @TagField("blockRotation")
    public float blockRotation = 0;

    @TagField(value = "signals", typeHint = Vec3i.class, mapper = Vec3iListMapper.class)
    public List<Vec3i> signals = new ArrayList<>();

    @TagField(value = "signalGroup", mapper = EmptyStringMapper.class)
    public String signalGroup;

    @TagField(value = "modePowerOff", mapper = EmptyStringMapper.class)
    public String modePowerOff;
    @TagField(value = "modePowerOn", mapper = EmptyStringMapper.class)
    public String modePowerOn;

    @TagField("hasPower")
    public boolean hasPower = false;

    // Variables
    private SignalContainer<BlockEntity> signalEntity;

    // Subclasses
    @SuppressWarnings("java:S1104")
    public BlockSenderRenderEntity renderEntity;

    public BlockSenderStorageEntity() {
        renderEntity = new BlockSenderRenderEntity(this);
    }

    public static void prepare() {

        Stellwand.debug("Preparing Sender..");

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
        for (Entry<ContentPackEntry, String> entry : Content.getBlockSenders().entrySet()) {
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
                BlockSenderEntry blockSenderEntry = (BlockSenderEntry) cpe;
                directionFrom.put(blockId, blockSenderEntry.getDirectionFrom());
                directionTo.put(blockId, blockSenderEntry.getDirectionTo());
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
    public String getContentPackBlockId() {
        return contentPackBlockId;
    }

    public void setContentBlockId(String contentPackBlockId) {
        this.contentPackBlockId = contentPackBlockId;
    }

    public boolean isCompatible(SignalContainer<?> otherSignal) {
        if (signals.isEmpty())
            return true;

        Iterator<Vec3i> signalIterator = signals.iterator();
        SignalContainer<?> signalContainer = null;

        do {
            Vec3i signalPos = signalIterator.next();
            if (SignalContainer.isSignal(getWorld(), signalPos)) {
                signalContainer = SignalContainer.of(getWorld(), signalPos);
            }
        } while (signalContainer == null && signalIterator.hasNext());

        if (signalContainer == null)
            return true;
        return signalContainer.isCompatible(otherSignal);
    }

    public void setSignal(SignalContainer<BlockEntity> signalEntity) {
        this.signalEntity = signalEntity;
    }

    public Vec3i getFirstSignal() {
        refreshSignals();
        for (Vec3i pos : this.signals)
            if (SignalContainer.isSignal(getWorld(), pos))
                return pos;
        return null;
    }

    public void refreshSignals() {
        this.signals.forEach(signal -> getWorld().keepLoaded(signal));
        this.signals.removeIf(vec3i -> !SignalContainer.isSignal(getWorld(), vec3i) && getWorld().isBlockLoaded(vec3i));
    }

    public SignalContainer<BlockEntity> getSignal() {
        return this.signalEntity;
    }

    // Getters
    public static Map<String, OBJModel> getModels() {
        return models;
    }

    public static Map<String, float[]> getRotations() {
        return rotations;
    }

    public static Map<String, float[]> getTranslations() {
        return translations;
    }

    // Map old versions to newer ones
    @Override
    public void load(TagCompound nbt) {
        VersionMapper.checkMap(this.getClass(), nbt);
    }
}
