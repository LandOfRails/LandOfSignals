package net.landofrails.landofsignals.blocks;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.landofsignals.configs.LegacyMode;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.Static;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BlockSignalPart extends BlockTypeEntity {

    private Map<String, ContentPackSignal> contentPackSignals = new HashMap<>();
    private String id;
    private int rot;
    private LegacyMode legacyMode;

    public BlockSignalPart(String modID, String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileSignalPart(id, rot, legacyMode);
    }

    public void setRot(int rot) {
        this.rot = rot;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLegacyMode(LegacyMode legacyMode) {
        this.legacyMode = legacyMode;
    }

    public String getName(String uncheckedId) {
        if (!uncheckedId.contains(":")) {
            return "ID: " + uncheckedId + "; Click into air to refresh item!";
        }
        return contentPackSignals.get(checkIfMissing(uncheckedId)).getName();
    }

    public void add(ContentPackSignal contentPackSignal) {
        if (!contentPackSignals.containsKey(contentPackSignal.getUniqueId())) {
            contentPackSignals.put(contentPackSignal.getUniqueId(), contentPackSignal);
        } else {
            //TODO: Add conflict info for user after he entered a world
            ModCore.error("There is already a SignalPart registered with this ID! ID: " + contentPackSignal.getUniqueId());
        }
    }

    public Map<String, ContentPackSignal> getContentpackSignals() {
        return contentPackSignals;
    }

    private String checkIfMissing(String id) {
        if (contentPackSignals.containsKey(id)) return id;
        else return Static.MISSING;
    }

    public boolean isOldContentPack(String id) {
        return contentPackSignals.containsKey(id) &&
                contentPackSignals.get(id).getMetadata().containsKey("addonversion") &&
                (Integer) contentPackSignals.get(id).getMetadata().get("addonversion") != 2;
    }

    public Map<String, ContentPackSignalGroup> getAllGroupStates(String id) {
        if (!contentPackSignals.containsKey(id)) return Collections.emptyMap();

        return contentPackSignals.get(id).getSignals();
    }

    public float getRotationSteps(String id) {
        return contentPackSignals.get(id).getRotationSteps();
    }
}
