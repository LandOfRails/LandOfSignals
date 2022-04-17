package net.landofrails.landofsignals.blocks;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.math.Vec3d;
import net.landofrails.api.contentpacks.v1.ContentPackAnimation;
import net.landofrails.api.contentpacks.v1.ContentPackSignalPart;
import net.landofrails.landofsignals.tile.TileSignalPartAnimated;
import net.landofrails.landofsignals.utils.Static;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockSignalPartAnimated extends BlockTypeEntity {

    private Map<String, ContentPackSignalPart> signalParts = new HashMap<>();
    private String id;
    private int rot;

    public BlockSignalPartAnimated(String modID, String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileSignalPartAnimated(id, rot);
    }

    public void setRot(int rot) {
        this.rot = rot;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath(String uncheckedId) {
        return signalParts.get(checkIfMissing(uncheckedId)).getModel();
    }

    public Vec3d getTranslation(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        float[] translation = signalParts.get(id).getTranslation();
        return new Vec3d(translation[0], translation[1], translation[2]);
    }

    public Vec3d getScaling(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        float[] scaling = signalParts.get(id).getScaling();
        return new Vec3d(scaling[0], scaling[1], scaling[2]);
    }

    public List<String> getStates(String uncheckedId) {
        return signalParts.get(checkIfMissing(uncheckedId)).getStates();
    }

    public String getId(String uncheckedId) {
        return signalParts.get(checkIfMissing(uncheckedId)).getId();
    }

    public Vec3d getItemTranslation(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        float[] translation = signalParts.get(id).getItemTranslation();
        return new Vec3d(translation[0], translation[1], translation[2]);
    }

    public String getName(String uncheckedId) {
        return signalParts.get(checkIfMissing(uncheckedId)).getName();
    }

    public List<ContentPackAnimation> getAnimation(String uncheckedId, String animation) {
        return signalParts.get(checkIfMissing(uncheckedId)).getAnimations().get(animation);
    }

    public Map<String, List<ContentPackAnimation>> getAniamtions(String uncheckedId) {
        return signalParts.get(checkIfMissing(uncheckedId)).getAnimations();
    }

    public void add(ContentPackSignalPart contentPackSignalPart) {
        if (!signalParts.containsKey(contentPackSignalPart.getId())) {
            this.signalParts.put(contentPackSignalPart.getId(), contentPackSignalPart);
        } else {
            //TODO: Add conflict info for user after he entered a world
            ModCore.error("There is already a SignalPart registered with this ID! ID: " + contentPackSignalPart.getId());
        }
    }

    public Map<String, ContentPackSignalPart> getSignalParts() {
        return signalParts;
    }

    private String checkIfMissing(String id) {
        if (signalParts.containsKey(id)) return id;
        else return Static.MISSING;
    }
}
