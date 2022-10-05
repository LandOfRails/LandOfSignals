package net.landofrails.landofsignals.blocks;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.api.contentpacks.v2.signalbox.ContentPackSignalbox;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.utils.Static;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"java:S2387", "java:S1135"})
public class BlockSignalBox extends BlockTypeEntity {

    private final Map<String, ContentPackSignalbox> contentPackSignalboxes = new HashMap<>();
    private String id;
    private int rot;

    public BlockSignalBox(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileSignalBox(id, rot);
    }

    public void add(ContentPackSignalbox contentPackSignalbox) {
        if (!contentPackSignalboxes.containsKey(contentPackSignalbox.getUniqueId())) {
            contentPackSignalboxes.put(contentPackSignalbox.getUniqueId(), contentPackSignalbox);
        } else {
            //TODO: Add conflict info for user after he entered a world
            ModCore.error("There is already a Signalbox registered with this ID! ID: " + contentPackSignalbox.getUniqueId());
        }
    }

    public String getName(String uncheckedId) {
        if (!uncheckedId.contains(":")) {
            return "ID: " + uncheckedId + "; Click into air to refresh item!";
        }
        return contentPackSignalboxes.get(checkIfMissing(uncheckedId)).getName();
    }

    private String checkIfMissing(String id) {
        if (contentPackSignalboxes.containsKey(id)) return id;
        return Static.MISSING;
    }

    public Map<String, ContentPackSignalbox> getContentpackSignalboxes() {
        return contentPackSignalboxes;
    }

    public void setRot(int rot) {
        this.rot = rot;
    }

    public void setId(String id) {
        this.id = id;
    }
}
