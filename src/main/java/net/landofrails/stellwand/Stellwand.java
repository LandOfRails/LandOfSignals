package net.landofrails.stellwand;

import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.tabs.CustomTabs;

public class Stellwand {

	private Stellwand() {

	}

	public static void commonEvent() {
		CustomTabs.register();
		CustomItems.register();
		CustomBlocks.registerBlocks();
	}

	public static void clientEvent() {
		CustomItems.registerRenderers();
		CustomBlocks.registerItemRenderers();
		CustomBlocks.registerBlockRenderers();
	}

}
