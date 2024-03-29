package net.landofrails.landofsignals.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.api.contentpacks.v2.ContentPackException;
import net.landofrails.api.contentpacks.v2.deco.ContentPackDeco;
import net.landofrails.landofsignals.tile.TileDeco;
import net.landofrails.landofsignals.utils.Static;

import java.util.HashMap;
import java.util.Map;

public class BlockDeco extends BlockTypeEntity {

    private Map<String, ContentPackDeco> contentPackDeco = new HashMap<>();
    private String id;
    private int rot;

    public BlockDeco(String modID, String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileDeco(id, rot);
    }

    public void add(ContentPackDeco contentPackDeco) {
        if (!this.contentPackDeco.containsKey(contentPackDeco.getUniqueId())) {
            this.contentPackDeco.put(contentPackDeco.getUniqueId(), contentPackDeco);
        } else {
            throw new ContentPackException("There is already a Decoblock registered with this ID! ID: " + contentPackDeco.getUniqueId());
        }
    }

    public String getName(String uncheckedId) {
        if (!uncheckedId.contains(":")) {
            return "ID: " + uncheckedId + "; Click into air to refresh item!";
        }
        return contentPackDeco.get(checkIfMissing(uncheckedId)).getName();
    }

    private String checkIfMissing(String id) {
        if (contentPackDeco.containsKey(id)) return id;
        return Static.MISSING;
    }

    public Map<String, ContentPackDeco> getContentpackDeco() {
        return contentPackDeco;
    }

    public void setRot(int rot) {
        this.rot = rot;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRotationSteps(String itemId) {
        return contentPackDeco.get(checkIfMissing(itemId)).getRotationSteps();
    }

    public boolean isUTF8(String itemId) {
        return contentPackDeco.get(checkIfMissing(itemId)).isUTF8();
    }
}
