package net.landofrails.stellwand.content.guis;

import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.helpers.ItemPickerGUI;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.ContentPack;
import net.landofrails.stellwand.contentpacks.types.EntryType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class SelectItem implements IScreen {

    // Only initialized once
    private static GuiRegistry.GUI gui;

    // Initialized every call
    private final List<Button> contentPackButtons = new LinkedList<>();
    private Consumer<ItemStack> selectedItem;
    private ItemStack current = null;
    private EntryType entryType;
    private ItemStack defaultItem;

    public static void init(GuiRegistry.GUI gui) {
        SelectItem.gui = gui;
    }

    @Override
    public void init(IScreenBuilder screen) {

        generateContentPackButtons(screen, Content.getContentPacksFor(entryType));

    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        builder.close();
    }

    @Override
    public void onClose() {
        if (current != null)
            selectedItem.accept(current);
        // Reset variables
        contentPackButtons.clear();
        selectedItem = null;
        entryType = null;
        current = null;
        defaultItem = null;
    }

    @Override
    public void draw(IScreenBuilder builder) {
        // ItemPickerGUI has own renderer.
    }

    private List<ItemStack> getItemStackForContentPack(ContentPack pack, EntryType type) {
        List<ItemStack> items = new ArrayList<>();
        Content.getBlocks(pack, type).forEach((cpe, value) -> {
            ItemStack is = new ItemStack(getCustomItemForEntryType(Objects.requireNonNull(type)), 1);
            TagCompound tag = is.getTagCompound();
            tag.setString("itemId", cpe.getBlockId(value));
            is.setTagCompound(tag);
            items.add(is);
        });
        if (items.isEmpty())
            items.add(defaultItem);
        return items;
    }

    private CustomItem getCustomItemForEntryType(EntryType type) {
        switch (type) {
            case BLOCKMULTISIGNAL:
                return CustomItems.ITEMBLOCKMULTISIGNAL;
            case BLOCKSENDER:
                return CustomItems.ITEMBLOCKSENDER;
            case BLOCKSIGNAL:
                return CustomItems.ITEMBLOCKSIGNAL;
            case BLOCKFILLER:
            default:
                return CustomItems.ITEMBLOCKFILLER;
        }
    }

    private void generateContentPackButtons(IScreenBuilder screen, List<ContentPack> contentPacks) {
        for (int i = 0; i < contentPacks.size(); i++) {
            ContentPack contentPack = contentPacks.get(i);
            int height = 20;
            int width = 200;
            int x = -width / 2;
            int marginY = 5;
            int offsetY = -50;
            int y = ((height + marginY) * i) + offsetY;
            Button button = new Button(screen, x, y, width, height, contentPack.getButtonName()) {
                @Override
                public void onClick(Player.Hand hand) {
                    List<ItemStack> selection = getItemStackForContentPack(contentPack, entryType);
                    ItemPickerGUI ip = new ItemPickerGUI(selection, (ItemStack item) -> {
                        current = item;
                        screen.close();
                    });
                    ip.show();
                }
            };
            contentPackButtons.add(button);
        }
    }

    /**
     * Method to open this GUI with arguments
     *
     * @param player       The player
     * @param type         Type of block
     * @param defaultItem  Default item if no items are found
     * @param selectedItem Consumer for returning itemstack
     */
    public void open(Player player, EntryType type, ItemStack defaultItem, Consumer<ItemStack> selectedItem) {
        this.selectedItem = selectedItem;
        entryType = type;
        this.defaultItem = defaultItem;
        gui.open(player);
    }

}
