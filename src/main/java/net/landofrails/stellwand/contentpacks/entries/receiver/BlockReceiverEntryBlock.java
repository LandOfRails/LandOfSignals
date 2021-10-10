package net.landofrails.stellwand.contentpacks.entries.receiver;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;

public class BlockReceiverEntryBlock extends ContentPackEntryBlock {

    private Boolean wallMountable;

    public BlockReceiverEntryBlock() {
        wallMountable = false;
    }

    public BlockReceiverEntryBlock(float[] rotation, float[] translation, Boolean wallMountable) {
        super(rotation, translation);
        this.wallMountable = wallMountable;
    }

    public boolean getWallMountable() {
        if (wallMountable == null)
            wallMountable = false;
        return wallMountable;
    }

}
