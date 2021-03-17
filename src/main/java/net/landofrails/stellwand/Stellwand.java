package net.landofrails.stellwand;

import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.guis.CustomGuis;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.loader.Loader;
import net.landofrails.stellwand.content.network.CustomPackets;
import net.landofrails.stellwand.content.tabs.CustomTabs;

public class Stellwand {

	public static final String DOMAIN = "stellwand";

	private Stellwand() {

	}

	public static void commonEvent() {

		Loader.init();

		CustomGuis.register();
		CustomTabs.register();
		CustomItems.register();

		CustomPackets.register();

	}

	public static void clientEvent() {

		CustomItems.registerRenderers();
		CustomBlocks.registerBlockRenderers();

	}

}
