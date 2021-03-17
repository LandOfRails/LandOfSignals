package net.landofrails.stellwand;

import cam72cam.mod.ModEvent;
import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.entities.rendering.BlockFillerRenderEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSenderRenderEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSignalRenderEntity;
import net.landofrails.stellwand.content.guis.CustomGuis;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.loader.Loader;
import net.landofrails.stellwand.content.network.CustomPackets;
import net.landofrails.stellwand.content.tabs.CustomTabs;

public class Stellwand {

	public static final String DOMAIN = "stellwand";

	private Stellwand() {

	}

	// Gets called before clientEvent and serverEvent
	public static void commonEvent(ModEvent event) {

		switch (event) {
			case CONSTRUCT :

				Loader.init();

				CustomGuis.register();
				CustomTabs.register();
				CustomItems.register();

				CustomPackets.register();

				break;
			case INITIALIZE :
			case SETUP :
				// Register overlays
			case RELOAD :
			case START :
			case FINALIZE :
				break;
		}

	}


	public static void clientEvent(ModEvent event) {

		switch (event) {
			case CONSTRUCT :

				CustomItems.registerRenderers();
				CustomBlocks.registerBlockRenderers();

				break;
			case INITIALIZE :
				break;
			case SETUP :
				// Register overlay
			case RELOAD :
			case START :
			case FINALIZE :
				break;
		}

	}

	public static void serverEvent(ModEvent event) {

		switch (event) {
			case CONSTRUCT :

				BlockSenderRenderEntity.check(false);
				BlockSignalRenderEntity.check(false);
				BlockFillerRenderEntity.check(false);

				break;
			case INITIALIZE :
			case SETUP :
				// Register overlay
			case RELOAD :
			case START :
			case FINALIZE :
				break;
		}

	}

}
