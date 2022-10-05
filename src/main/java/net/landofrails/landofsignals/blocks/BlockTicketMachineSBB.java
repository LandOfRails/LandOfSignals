package net.landofrails.landofsignals.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.landofsignals.tile.TileTicketMachineSBB;

public class BlockTicketMachineSBB extends BlockTypeEntity {

    private float rot;

    public BlockTicketMachineSBB(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileTicketMachineSBB(rot);
    }

    public void setRot(final float rot) {
        this.rot = rot;
    }

}
