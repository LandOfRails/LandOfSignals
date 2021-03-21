package net.landofrails.stellwand.content.blocks.others;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.storage.BlockFillerStorageEntity;

public class BlockFiller extends BlockTypeEntity {

	public BlockFiller() {
		super(LandOfSignals.MODID, "stellwand.blockfiller");
	}

	@Override
	protected BlockEntity constructBlockEntity() {
		return new BlockFillerStorageEntity();
	}

}
