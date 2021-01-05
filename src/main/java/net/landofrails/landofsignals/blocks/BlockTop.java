package net.landofrails.landofsignals.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.math.Vec3i;
import net.landofrails.landofsignals.tile.TileTop;

public class BlockTop extends BlockTypeEntity {

    private float rot;
    private String block;
    private Vec3i pos;

    public BlockTop(String modID, String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileTop(rot, block, pos);
    }

    public void setRot(float rot) {
        this.rot = rot;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public void setPos(Vec3i pos) {
        this.pos = pos;
    }

}
