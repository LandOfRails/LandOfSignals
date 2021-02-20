package net.landofrails.stellwand.content.blocks.others;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.rendering.BlockSignalRenderEntity;

public class BlockSignal extends BlockTypeEntity {

	public BlockSignal() {
		super(LandOfSignals.MODID, "stellwand.blocksignal");
	}

	// TODO: Change to storage in the near future
	@Override
	protected BlockEntity constructBlockEntity() {
		return new BlockSignalRenderEntity();
	}

}
