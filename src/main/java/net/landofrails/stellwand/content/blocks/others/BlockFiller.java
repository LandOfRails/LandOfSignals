package net.landofrails.stellwand.content.blocks.others;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.rendering.BlockFillerRenderEntity;

public class BlockFiller extends BlockTypeEntity {

	public BlockFiller() {
		super(LandOfSignals.MODID, "stellwand.blockfiller");
	}

	// TODO: Change to storage in the near future
	@Override
	protected BlockEntity constructBlockEntity() {
		return new BlockFillerRenderEntity();
	}

}
