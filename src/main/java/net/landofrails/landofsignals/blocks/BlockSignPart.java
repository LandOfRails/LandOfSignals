package net.landofrails.landofsignals.blocks;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.math.Vec3d;
import net.landofrails.api.contentpacks.v1.ContentPackSignObject;
import net.landofrails.api.contentpacks.v1.ContentPackSignPart;
import net.landofrails.landofsignals.tile.TileSignPart;
import net.landofrails.landofsignals.utils.Static;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlockSignPart extends BlockTypeEntity {

    private static final Function<Map.Entry<Integer, ?>, Integer> INTKEY = Map.Entry::getKey;
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

    public Map<Integer, String> getPath(String uncheckedId) {
        return signs.get(checkIfMissing(uncheckedId)).getModel();
    }

    public Map<Integer, Vec3d> getTranslation(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        Map<Integer, float[]> translationMap = signs.get(id).getTranslation();
        return translationMap.entrySet().stream().collect(Collectors.toMap(INTKEY, translation -> new Vec3d(translation.getValue()[0], translation.getValue()[1], translation.getValue()[2])));
    }

    public Map<Integer, Vec3d> getScaling(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        Map<Integer, float[]> scalingMap = signs.get(id).getScaling();
        return scalingMap.entrySet().stream().collect(Collectors.toMap(INTKEY, scaling -> new Vec3d(scaling.getValue()[0], scaling.getValue()[1], scaling.getValue()[2])));
    }

    public Map<Integer, Vec3d> getItemScaling(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        Map<Integer, float[]> itemScalingMap = signs.get(id).getItemScaling();
        return itemScalingMap.entrySet().stream().collect(Collectors.toMap(INTKEY, itemScaling -> new Vec3d(itemScaling.getValue()[0], itemScaling.getValue()[1], itemScaling.getValue()[2])));
    }

    public String getId(String uncheckedId) {
        return signs.get(checkIfMissing(uncheckedId)).getId();
    }

    public Map<Integer, Vec3d> getItemTranslation(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        Map<Integer, float[]> itemTranslationMap = signs.get(id).getItemTranslation();
        return itemTranslationMap.entrySet().stream().collect(Collectors.toMap(INTKEY, itemTranslation -> new Vec3d(itemTranslation.getValue()[0], itemTranslation.getValue()[1], itemTranslation.getValue()[2])));
    }

    public String getName(String uncheckedId) {
        return signs.get(checkIfMissing(uncheckedId)).getName();
    }

    @Deprecated
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

    public Map<Integer, String[]> getRenderGroups(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        return signs.get(id).getRenderGroups();
    }

    public Map<Integer, String> getTexture(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        return signs.get(id).getTexture();
    }

    public Map<Integer, ContentPackSignObject> getSignObjects(String uncheckedId) {
        String id = checkIfMissing(uncheckedId);
        return signs.get(id).getSignObjects();
    }

    private String checkIfMissing(String id) {
        if (signs.containsKey(id)) return id;
        else return Static.MISSING;
    }
}
