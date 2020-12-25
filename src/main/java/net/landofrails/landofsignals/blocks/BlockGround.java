package net.landofrails.landofsignals.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.landofsignals.tile.TileGround;

public class BlockGround extends BlockTypeEntity {

    private float rot;
    private String block;

    public BlockGround(String modID, String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileGround(rot, block);
    }

    public void setRot(float rot) {
        this.rot = rot;
    }

    public void setBlock(String block) {
        this.block = block;
    }
    
}
