package net.landofrails.landofsignals.blocks;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.Static;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"java:S2387", "java:S1135"})
public class BlockSignalPart extends BlockTypeEntity {

    private final Map<String, ContentPackSignal> contentPackSignals = new HashMap<>();
    private String id;
    private int rot;

    public BlockSignalPart(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileSignalPart(id, rot);
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
        return contentPackSignals.get(checkIfMissing(uncheckedId)).getName();
    }

    public void add(final ContentPackSignal contentPackSignal) {
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

    private String checkIfMissing(final String id) {
        if (contentPackSignals.containsKey(id)) return id;
        else return Static.MISSING;
    }

    public boolean isOldContentPack(String id) {
        return contentPackSignals.containsKey(id) &&
                contentPackSignals.get(id).getMetadata().containsKey("addonversion") &&
                (Integer) contentPackSignals.get(id).getMetadata().get("addonversion") != 2;
    }

    public String[] getAllStates(String id) {
        if (!contentPackSignals.containsKey(id)) return new String[0];

        return contentPackSignals.get(id).getStates();
    }

    public float getRotationSteps(String id) {
        return contentPackSignals.get(id).getRotationSteps();
    }

    public boolean isUTF8(String id) {
        return contentPackSignals.get(checkIfMissing(id)).isUTF8();
    }
}
