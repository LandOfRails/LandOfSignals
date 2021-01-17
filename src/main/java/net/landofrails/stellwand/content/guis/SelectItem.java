package net.landofrails.stellwand.content.guis;

import java.util.List;
import java.util.function.Consumer;

import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.helpers.ItemPickerGUI;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;

public class SelectItem implements IScreen {

	private static List<ItemStack> selection;
	private static Consumer<ItemStack> selectedItem;
	private static ItemStack current;
	private static GuiRegistry.GUI gui;

	public static void init(GuiRegistry.GUI gui) {
		SelectItem.gui = gui;
	}

	@Override
	public void init(IScreenBuilder screen) {

		ItemPickerGUI ip = new ItemPickerGUI(selection, (ItemStack item) -> {
			current = item;
			screen.close();
		});
		ip.show();

	}

	@Override
	public void onEnterKey(IScreenBuilder builder) {
		builder.close();
	}

	@Override
	public void onClose() {
		selectedItem.accept(current);
		selection = null;
		selectedItem = null;
		current = null;
	}

	@Override
	public void draw(IScreenBuilder builder) {
		// ItemPickerGUI has own renderer.
	}

	/**
	 * 
	 * Method to open this GUI with arguments
	 * 
	 * @param player
	 * @param selection
	 * @param selectedItem
	 */
	public void open(Player player, List<ItemStack> selection, Consumer<ItemStack> selectedItem) {
		SelectItem.selection = selection;
		SelectItem.selectedItem = selectedItem;
		gui.open(player);
	}

}
