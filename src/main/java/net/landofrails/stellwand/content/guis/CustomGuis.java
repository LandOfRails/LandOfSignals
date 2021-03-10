package net.landofrails.stellwand.content.guis;

import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;

public class CustomGuis {

	public static GuiRegistry.BlockGUI selectSenderModes;

	private CustomGuis() {

	}

	public static void register() {
		// @formatter:off
		GuiRegistry.GUI selectItemGui = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "selectitemgui"), SelectItem::new);
		SelectItem.init(selectItemGui);
		selectSenderModes = GuiRegistry.registerBlock(BlockSenderStorageEntity.class, SelectSenderModes::new);
		// @formatter:on
	}

}
