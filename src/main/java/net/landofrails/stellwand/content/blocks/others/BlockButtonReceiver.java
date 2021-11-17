package net.landofrails.stellwand.content.blocks.others;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.block.Material;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.storage.BlockButtonReceiverStorageEntity;

public class BlockButtonReceiver extends BlockTypeEntity {

    public BlockButtonReceiver() {
        super(LandOfSignals.MODID, "stellwand.blockbuttonreceiver");
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new BlockButtonReceiverStorageEntity();
    }

    @Override
    public boolean isConnectable() {
        return true;
    }

    @Override
    public Material getMaterial() {
        return Material.METAL;
    }
}
