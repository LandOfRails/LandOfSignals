package net.landofrails.stellwand.content.blocks.others;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.block.Material;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;

public class BlockSender extends BlockTypeEntity {

	public BlockSender() {
		super(LandOfSignals.MODID, "stellwand.blocksender");
	}

	@Override
	protected BlockEntity constructBlockEntity() {
		return new BlockSenderStorageEntity();
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