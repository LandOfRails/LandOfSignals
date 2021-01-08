package net.landofrails.stellwand.content.tabs;

import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.ItemStack;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.items.CustomItems;

public class CustomTabs {

	private CustomTabs() {

	}

	public static final CreativeTab STELLWAND_TAB;

	static {
		STELLWAND_TAB = new CreativeTab(LandOfSignals.MODID + ".stellwand",
				() -> new ItemStack(CustomItems.ITEMCONNECTOR1, 1));
	}

}
