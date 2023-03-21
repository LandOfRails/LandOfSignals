package net.landofrails.landofsignals.blocks;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackComplexSignal;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackSignalGroup;
import net.landofrails.landofsignals.tile.TileComplexSignal;
import net.landofrails.landofsignals.utils.Static;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"java:S2387", "java:S1135"})
public class BlockComplexSignal extends BlockTypeEntity {

    private final Map<String, ContentPackComplexSignal> contentPackComplexSignals = new HashMap<>();
    private String id;
    private int rot;

    public BlockComplexSignal(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileComplexSignal(id, rot);
    }

    public void setRot(final int rot) {
        this.rot = rot;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName(final String uncheckedId) {
        if (!uncheckedId.contains(":")) {
            return "ID: " + uncheckedId + "; Click into air to refresh item!";
        }
        return contentPackComplexSignals.get(checkIfMissing(uncheckedId)).getName();
    }

    public void add(final ContentPackComplexSignal contentPackComplexSignal) {
        if (!contentPackComplexSignals.containsKey(contentPackComplexSignal.getUniqueId())) {
            contentPackComplexSignals.put(contentPackComplexSignal.getUniqueId(), contentPackComplexSignal);
        } else {
            //TODO: Add conflict info for user after he entered a world
            ModCore.error("There is already a ComplexSignal registered with this ID! ID: " + contentPackComplexSignal.getUniqueId());
        }
    }

    public Map<String, ContentPackComplexSignal> getContentpackComplexSignals() {
        return contentPackComplexSignals;
    }

    private String checkIfMissing(final String id) {
        if (contentPackComplexSignals.containsKey(id)) return id;
        else return Static.MISSING;
    }

    public Map<String, ContentPackSignalGroup> getAllGroupStates(String id) {
        if (!contentPackComplexSignals.containsKey(id)) return Collections.emptyMap();

        return contentPackComplexSignals.get(id).getSignals();
    }

    public float getRotationSteps(String id) {
        return contentPackComplexSignals.get(id).getRotationSteps();
    }

    public boolean isUTF8(String id) {
        return contentPackComplexSignals.get(checkIfMissing(id)).isUTF8();
    }
}
