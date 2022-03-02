package net.landofrails.landofsignals.blocks;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.math.Vec3d;
import net.landofrails.landofsignals.tile.TileSignPart;
import net.landofrails.landofsignals.utils.Static;
import net.landofrails.landofsignals.utils.contentpacks.ContentPackSignPart;

import java.util.HashMap;
import java.util.Map;

public class BlockSignPart extends BlockTypeEntity {

    private Map<String, ContentPackSignPart> signs = new HashMap<>();
    private String id;
    private int rot;

    public BlockSignPart(String modID, String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileSignPart(id, rot);
    }

    public void setRot(int rot) {
        this.rot = rot;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath(String uncheckedId) {
        return signs.get(checkIfMissing(uncheckedId)).getModel();
    }

    public Vec3d getTranslation(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        float[] translation = signs.get(id).getTranslation();
        return new Vec3d(translation[0], translation[1], translation[2]);
    }

    public Vec3d getScaling(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        float[] scaling = signs.get(id).getScaling();
        return new Vec3d(scaling[0], scaling[1], scaling[2]);
    }

    public Vec3d getItemScaling(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        float[] scaling = signs.get(id).getItemScaling();
        return new Vec3d(scaling[0], scaling[1], scaling[2]);
    }

    public String getId(String uncheckedId) {
        return signs.get(checkIfMissing(uncheckedId)).getId();
    }

    public Vec3d getItemTranslation(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        float[] translation = signs.get(id).getItemTranslation();
        return new Vec3d(translation[0], translation[1], translation[2]);
    }

    public String getName(String uncheckedId) {
        return signs.get(checkIfMissing(uncheckedId)).getName();
    }

    public void add(ContentPackSignPart contentPackSign) {
        if (!signs.containsKey(contentPackSign.getId())) {
            this.signs.put(contentPackSign.getId(), contentPackSign);
        } else {
            //TODO: Add conflict info for user after he entered a world
            ModCore.error("There is already a SignPart registered with this ID! ID: " + contentPackSign.getId());
        }
    }

    public Map<String, ContentPackSignPart> getSignParts() {
        return signs;
    }

    public String[] getRenderGroups(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        return signs.get(id).getRenderGroups();
    }

    private String checkIfMissing(String id) {
        if (signs.containsKey(id)) return id;
        else return Static.MISSING;
    }
}
