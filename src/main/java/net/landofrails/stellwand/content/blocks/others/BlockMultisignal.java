package net.landofrails.stellwand.content.blocks.others;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.block.Material;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.storage.BlockMultisignalStorageEntity;

public class BlockMultisignal extends BlockTypeEntity {

    public BlockMultisignal() {
        super(LandOfSignals.MODID, "stellwand.blockmultisignal");
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new BlockMultisignalStorageEntity();
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
