package net.landofrails.landofsignals.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.landofsignals.tile.TileSignalSO12;

public class BlockSignalSO12 extends BlockTypeEntity {
    public BlockSignalSO12(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileSignalSO12();
    }
}
