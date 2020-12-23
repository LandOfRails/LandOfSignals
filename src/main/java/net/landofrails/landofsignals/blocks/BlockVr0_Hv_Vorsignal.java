package net.landofrails.landofsignals.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.landofsignals.tile.TileVr0_Hv_Vorsignal;

public class BlockVr0_Hv_Vorsignal extends BlockTypeEntity {

    private float rot;

    public BlockVr0_Hv_Vorsignal(String modID, String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileVr0_Hv_Vorsignal(rot);
    }

    public void setRot(float rot) {
        this.rot = rot;
    }
}
