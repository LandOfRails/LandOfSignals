package net.landofrails.stellwand;

import cam72cam.mod.net.Packet;
import cam72cam.mod.net.PacketDirection;
import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.guis.CustomGuis;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.items.ItemBlockSignal;
import net.landofrails.stellwand.content.loader.Loader;
import net.landofrails.stellwand.content.network.ChangeHandHeldItem;
import net.landofrails.stellwand.content.tabs.CustomTabs;

public class Stellwand {

	private Stellwand() {

	}

	public static void commonEvent() {

		Loader.init();

		CustomGuis.register();
		CustomTabs.register();
		CustomItems.register();
		CustomBlocks.registerBlocks();

		Packet.register(ChangeHandHeldItem::new, PacketDirection.ClientToServer);
	}

	public static void clientEvent() {
		ItemBlockSignal.init();

		CustomItems.registerRenderers();
		CustomBlocks.registerItemRenderers();
		CustomBlocks.registerBlockRenderers();

	}

}
