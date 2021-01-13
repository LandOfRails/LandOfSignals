package net.landofrails.stellwand.content.blocks.others;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.BlockSignalEntity;

public class BlockSignal extends BlockTypeEntity {

	public BlockSignal() {
		super(LandOfSignals.MODID, "blocksignal");
	}

	@Override
	protected BlockEntity constructBlockEntity() {
		return new BlockSignalEntity();
	}

}
