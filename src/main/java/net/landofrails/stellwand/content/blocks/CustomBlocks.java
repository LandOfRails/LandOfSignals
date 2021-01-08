package net.landofrails.stellwand.content.blocks;

import cam72cam.mod.render.BlockRender;
import net.landofrails.stellwand.content.blocks.others.BlockFiller;
import net.landofrails.stellwand.utils.UselessEntity;

public class CustomBlocks {

	public static final BlockFiller BLOCKFILLER = new BlockFiller();

	public static void register() {
		// Cant register diffrent block types
	}

	public static void registerRenderers() {
		BlockRender.register(BLOCKFILLER, BLOCKFILLER::render, UselessEntity.class);
	}

}
