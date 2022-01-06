package net.landofrails.stellwand.content.guis;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.helpers.ItemPickerGUI;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.ContentPack;
import net.landofrails.stellwand.contentpacks.types.EntryType;

import java.util.*;
import java.util.function.Consumer;

public class SelectItem implements IScreen {

    // Only initialized once
    private static final Map<EntryType, Map.Entry<ItemStack, Consumer<ItemStack>>> ENTRYTYPES = new HashMap<>();
    private static GuiRegistry.GUI gui;

    // Initialized every call
    private ItemStack current;
    private EntryType entryType;

    public static void init(GuiRegistry.GUI gui) {
        SelectItem.gui = gui;
    }

    public static void setEntryType(EntryType type, Map.Entry<ItemStack, Consumer<ItemStack>> defaultAndSelectedItem) {
        ENTRYTYPES.putIfAbsent(type, defaultAndSelectedItem);
    }

    @Override
    public void init(IScreenBuilder screen) {
        ItemStack itemStack = MinecraftClient.getPlayer().getHeldItem(Player.Hand.PRIMARY);
        this.entryType = EntryType.getEntryTypeFromItem(itemStack);
        generateContentPackButtons(screen, Content.getContentPacksFor(entryType));
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        builder.close();
    }

    @Override
    public void onClose() {
        if (current != null)
            ENTRYTYPES.get(entryType).getValue().accept(current);
        entryType = null;
    }

    @Override
    public void draw(IScreenBuilder builder) {
        // ItemPickerGUI has own renderer.
    }

    private List<ItemStack> getItemStackForContentPack(ContentPack pack, EntryType type) {
        List<ItemStack> items = new ArrayList<>();
        Content.getBlocks(pack, type).forEach((cpe, value) -> {
            ItemStack is = new ItemStack(type.getCustomItem(), 1);
            TagCompound tag = is.getTagCompound();
            tag.setString("itemId", cpe.getBlockId(value));
            is.setTagCompound(tag);
            items.add(is);
        });
        if (items.isEmpty())
            items.add(ENTRYTYPES.get(entryType).getKey());
        return items;
    }

    private void generateContentPackButtons(IScreenBuilder screen, Collection<ContentPack> contentPacks) {
        int index = 0;
        for (ContentPack contentPack : contentPacks) {
            int height = 20;
            int width = 200;
            int x = -width / 2;
            int marginY = 5;
            int offsetY = -50;
            int y = ((height + marginY) * index) + offsetY;

            new Button(screen, x, y, width, height, contentPack.getButtonName()) {
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

            index++;
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
    public static void open(Player player, EntryType type, ItemStack defaultItem, Consumer<ItemStack> selectedItem) {
        setEntryType(type, new AbstractMap.SimpleEntry<>(defaultItem, selectedItem));
        gui.open(player);
    }

}
