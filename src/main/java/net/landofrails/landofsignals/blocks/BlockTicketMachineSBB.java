package net.landofrails.landofsignals.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.landofsignals.tile.TileTicketMachineSBB;

public class BlockTicketMachineSBB extends BlockTypeEntity {

    private float rot;

    public BlockTicketMachineSBB(String modID, String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileTicketMachineSBB(rot);
    }

    public void setRot(float rot) {
        this.rot = rot;
    }

}
