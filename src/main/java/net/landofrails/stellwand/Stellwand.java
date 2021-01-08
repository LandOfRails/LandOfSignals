package net.landofrails.stellwand;

import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.items.CustomItems;

public class Stellwand {

	private Stellwand() {

	}

	public static void commonEvent() {
		CustomItems.register();
		CustomBlocks.registerBlocks();
	}

	public static void clientEvent() {
		CustomItems.registerRenderers();
		CustomBlocks.registerItemRenderers();
		CustomBlocks.registerBlockRenderers();
	}

}
