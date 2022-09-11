package net.landofrails.landofsignals.blocks;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.api.contentpacks.v2.sign.ContentPackSign;
import net.landofrails.landofsignals.tile.TileSignPart;
import net.landofrails.landofsignals.utils.Static;

import java.util.HashMap;
import java.util.Map;

public class BlockSignPart extends BlockTypeEntity {

    private Map<String, ContentPackSign> contentPackSigns = new HashMap<>();
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

    public String getName(String uncheckedId) {
        return contentPackSigns.get(checkIfMissing(uncheckedId)).getName();
    }

    public void add(ContentPackSign contentPackSign) {
        if (!contentPackSigns.containsKey(contentPackSign.getUniqueId())) {
            this.contentPackSigns.put(contentPackSign.getUniqueId(), contentPackSign);
        } else {
            //TODO: Add conflict info for user after he entered a world
            ModCore.error("There is already a SignPart registered with this ID! ID: " + contentPackSign.getId());
        }
    }

    public Map<String, ContentPackSign> getContentpackSigns() {
        return contentPackSigns;
    }

    private String checkIfMissing(String id) {
        if (contentPackSigns.containsKey(id)) return id;
        else return Static.MISSING;
    }

    public boolean isWritable(String id) {
        if (!contentPackSigns.containsKey(id)) return false;
        else return contentPackSigns.get(id).isWriteable();
    }
}
