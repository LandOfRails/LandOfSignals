package net.landofrails.stellwand.content.blocks;

import cam72cam.mod.render.BlockRender;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.blocks.others.BlockFiller;
import net.landofrails.stellwand.content.blocks.others.BlockSender;
import net.landofrails.stellwand.content.blocks.others.BlockSignal;
import net.landofrails.stellwand.content.entities.rendering.BlockFillerRenderEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSenderRenderEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSignalRenderEntity;
import net.landofrails.stellwand.content.entities.storage.BlockFillerStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;

public class CustomBlocks {

	private CustomBlocks() {

	}

	public static final BlockFiller BLOCKFILLER = new BlockFiller();
	public static final BlockSender BLOCKSENDER = new BlockSender();
	public static final BlockSignal BLOCKSIGNAL = new BlockSignal();

	public static void init() {
		// WICHTIG: Triggert das Laden der Blöcke.
	}

	public static void registerBlockRenderers() {
		Stellwand.debug("Registering blocks");
		BlockRender.register(BLOCKFILLER, BlockFillerRenderEntity::render, BlockFillerStorageEntity.class);
		BlockRender.register(BLOCKSIGNAL, BlockSignalRenderEntity::render,
				BlockSignalStorageEntity.class);
		BlockRender.register(BLOCKSENDER, BlockSenderRenderEntity::render,
				BlockSenderStorageEntity.class);

	}

}
