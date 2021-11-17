package net.landofrails.stellwand.content.tabs;

import java.util.Iterator;
import java.util.Map.Entry;

import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;

public class CustomTabs {

	private CustomTabs() {

	}

	@SuppressWarnings("java:S3008")
	public static CreativeTab STELLWAND_TAB;
	@SuppressWarnings("java:S3008")
	public static CreativeTab HIDDEN_TAB;

	public static void register() {
		STELLWAND_TAB = new CreativeTab(LandOfSignals.MODID + ".stellwand", () -> {

			Iterator<Entry<ContentPackEntry, String>> it = Content.getBlockSignals().entrySet().iterator();
			Entry<ContentPackEntry, String> entry = it.next();

			ContentPackEntry cpe = entry.getKey();
			ItemStack is = new ItemStack(CustomItems.ITEMBLOCKSIGNAL, 1);
			TagCompound tag = is.getTagCompound();
			tag.setString("itemId", cpe.getBlockId(entry.getValue()));
			is.setTagCompound(tag);

			return is;

		});
	}

}
