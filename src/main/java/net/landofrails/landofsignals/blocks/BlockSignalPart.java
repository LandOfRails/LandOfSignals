package net.landofrails.landofsignals.blocks;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.math.Vec3d;
import net.landofrails.api.contentpacks.v1.ContentPackSignalPart;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.landofsignals.configs.LegacyMode;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.Static;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockSignalPart extends BlockTypeEntity {

    @Deprecated
    private Map<String, ContentPackSignalPart> signalParts = new HashMap<>();
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

    public String getPath_depr(String uncheckedId) {
        return signalParts.get(checkIfMissing_depr(uncheckedId)).getModel();
    }

    public Vec3d getTranslation_depr(String uncheckedId) {
        String id = checkIfMissing_depr(uncheckedId);
        float[] translation = signalParts.get(id).getTranslation();
        return new Vec3d(translation[0], translation[1], translation[2]);
    }

    public Vec3d getScaling_depr(String uncheckedId) {
        String id = checkIfMissing_depr(uncheckedId);
        float[] scaling = signalParts.get(id).getScaling();
        return new Vec3d(scaling[0], scaling[1], scaling[2]);
    }

    public Vec3d getItemScaling_depr(String uncheckedId) {
        String id = checkIfMissing_depr(uncheckedId);
        float[] scaling = signalParts.get(id).getItemScaling();
        return new Vec3d(scaling[0], scaling[1], scaling[2]);
    }

    public List<String> getStates_depr(String uncheckedId) {
        return signalParts.get(checkIfMissing_depr(uncheckedId)).getStates();
    }

    public String getId_depr(String uncheckedId) {
        return signalParts.get(checkIfMissing_depr(uncheckedId)).getId();
    }

    public Vec3d getItemTranslation_depr(String uncheckedId) {
        String id = checkIfMissing_depr(uncheckedId);
        float[] translation = signalParts.get(id).getItemTranslation();
        return new Vec3d(translation[0], translation[1], translation[2]);
    }

    public String getName_depr(String uncheckedId) {
        return signalParts.get(checkIfMissing_depr(uncheckedId)).getName();
    }

    public String getName(String uncheckedId) {
        return contentPackSignals.get(checkIfMissing(uncheckedId)).getName();
    }

    @Deprecated
    public void add_depr(ContentPackSignalPart contentPackSignalPart) {
        if (!signalParts.containsKey(contentPackSignalPart.getId())) {
            this.signalParts.put(contentPackSignalPart.getId(), contentPackSignalPart);
        } else {
            //TODO: Add conflict info for user after he entered a world
            ModCore.error("There is already a SignalPart registered with this ID! ID: " + contentPackSignalPart.getId());
        }
    }

    public void add(ContentPackSignal contentPackSignal) {
        if (!contentPackSignals.containsKey(contentPackSignal.getId())) {
            contentPackSignals.put(contentPackSignal.getId(), contentPackSignal);
        } else {
            //TODO: Add conflict info for user after he entered a world
            ModCore.error("There is already a SignalPart registered with this ID! ID: " + contentPackSignal.getId());
        }
    }

    public Map<String, ContentPackSignalPart> getSignalParts_depr() {
        return signalParts;
    }

    public Map<String, ContentPackSignal> getContentpackSignals() {
        return contentPackSignals;
    }

    /**
     * Replace with checkIfMissing(id)
     *
     * @param id
     * @return
     */
    private String checkIfMissing_depr(String id) {
        if (signalParts.containsKey(id)) return id;
        else return Static.MISSING;
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
