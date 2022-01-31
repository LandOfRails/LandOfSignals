package net.landofrails.stellwand.contentpacks.entries.buttonreceiver;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;

public class BlockButtonReceiverEntryBlock extends ContentPackEntryBlock {

    private Boolean wallMountable;

    public BlockButtonReceiverEntryBlock() {
        wallMountable = false;
    }

    public BlockButtonReceiverEntryBlock(float[] rotation, float[] translation, Boolean wallMountable) {
        super(rotation, translation);
        this.wallMountable = wallMountable;
    }

    public boolean getWallMountable() {
        if (wallMountable == null)
            wallMountable = false;
        return wallMountable;
    }

}
