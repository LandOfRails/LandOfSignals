package net.landofrails.landofsignals.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.landofsignals.tile.TileTicketMachineDB;

public class BlockTicketMachineDB extends BlockTypeEntity {

    private float rot;

    public BlockTicketMachineDB(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileTicketMachineDB(rot);
    }

    public void setRot(final float rot) {
        this.rot = rot;
    }
}
