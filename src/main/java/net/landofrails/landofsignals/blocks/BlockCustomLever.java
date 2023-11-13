package net.landofrails.landofsignals.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.api.contentpacks.v2.ContentPackException;
import net.landofrails.api.contentpacks.v2.lever.ContentPackLever;
import net.landofrails.landofsignals.tile.TileCustomLever;
import net.landofrails.landofsignals.utils.Static;

import java.util.HashMap;
import java.util.Map;

public class BlockCustomLever extends BlockTypeEntity {

    private Map<String, ContentPackLever> contentPackCustomLever = new HashMap<>();
    @SuppressWarnings("java:S2387")
    private String id;
    private int rot;

    public BlockCustomLever(String modID, String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileCustomLever(id, rot);
    }

    public void add(ContentPackLever contentPackLever) {
        if (!this.contentPackCustomLever.containsKey(contentPackLever.getUniqueId())) {
            this.contentPackCustomLever.put(contentPackLever.getUniqueId(), contentPackLever);
        } else {
            throw new ContentPackException("There is already a custom lever registered with this ID! ID: " + contentPackLever.getUniqueId());
        }
    }

    public String getName(String uncheckedId) {
        return contentPackCustomLever.get(checkIfMissing(uncheckedId)).getName();
    }

    private String checkIfMissing(String id) {
        if (contentPackCustomLever.containsKey(id)) return id;
        return Static.MISSING;
    }

    public Map<String, ContentPackLever> getContentpackLever() {
        return contentPackCustomLever;
    }

    public void setRot(int rot) {
        this.rot = rot;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRotationSteps(String itemId) {
        return contentPackCustomLever.get(checkIfMissing(itemId)).getRotationSteps();
    }

    public boolean isUTF8(String itemId) {
        return contentPackCustomLever.get(checkIfMissing(itemId)).isUTF8();
    }
}
