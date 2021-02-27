package net.landofrails.stellwand.content.tabs;

import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.ItemStack;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.items.CustomItems;

public class CustomTabs {

	private CustomTabs() {

	}

	@SuppressWarnings("java:S3008")
	public static CreativeTab STELLWAND_TAB;
	@SuppressWarnings("java:S3008")
	public static CreativeTab HIDDEN_TAB;

	public static void register() {
		STELLWAND_TAB = new CreativeTab(LandOfSignals.MODID + ".stellwand",
				() -> new ItemStack(CustomItems.ITEMCONNECTOR1, 1));
		HIDDEN_TAB = new CreativeTab(null);
	}

}
