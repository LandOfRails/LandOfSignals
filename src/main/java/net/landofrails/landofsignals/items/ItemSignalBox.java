package net.landofrails.landofsignals.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.text.TextUtil;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;
import net.landofrails.landofsignals.utils.Static;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ItemSignalBox extends CustomItem {
    private static final String ITEMIDKEY = "itemId";
    private static final String MSG_NOT_UTF8 = "message.landofsignals:non.utf.eight.items";

    public ItemSignalBox(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.HIDDEN_TAB);
    }

    @Override
    public ClickResult onClickBlock(final Player player, final World world, final Vec3i pos, final Player.Hand hand, final Facing facing, final Vec3d inBlockPos) {
        final Optional<Vec3i> target = LandOfSignalsUtils.canPlaceBlock(world, pos, facing, player);
        if (!target.isPresent()) return ClickResult.REJECTED;

        int rot = -(Math.round(player.getRotationYawHead() / 10) * 10) + 180;
        TileSignalBox tileSignalBox = world.getBlockEntity(pos, TileSignalBox.class);
        if (tileSignalBox != null && !player.isCrouching()) rot = tileSignalBox.getBlockRotate();
        LOSBlocks.BLOCK_SIGNAL_BOX.setRot(rot);

        String itemId = player.getHeldItem(hand).getTagCompound().getString(ITEMIDKEY);

        if(!LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().containsKey(itemId)) return ClickResult.REJECTED;

        LOSBlocks.BLOCK_SIGNAL_BOX.setId(itemId);
        world.setBlock(target.get(), LOSBlocks.BLOCK_SIGNAL_BOX);
        return ClickResult.ACCEPTED;
    }

    @Override
    public String getCustomName(ItemStack stack) {
        TagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey(ITEMIDKEY)) return LOSBlocks.BLOCK_SIGNAL_BOX.getName(tag.getString(ITEMIDKEY));
        else return "Error missing tag \"itemId\" for ItemSignalBox";
    }

    @Override
    public List<ItemStack> getItemVariants(CreativeTab creativeTab) {
        List<ItemStack> itemStackList = new ArrayList<>();

        if (creativeTab == null || creativeTab.equals(LOSTabs.get(LOSTabs.SIGNALS_TAB))) {
            for (String id : LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().keySet()) {
                if (!id.equals(Static.MISSING)) {
                    ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_BOX, 1);
                    TagCompound tag = is.getTagCompound();
                    tag.setString(ITEMIDKEY, id);
                    is.setTagCompound(tag);
                    itemStackList.add(is);
                }
            }
        }

        itemStackList.sort(Comparator.comparing(LandOfSignalsUtils::getUniqueIdOfItemStack));

        return itemStackList;
    }

    @Override
    public List<String> getTooltip(ItemStack itemStack) {
        String itemId = itemStack.getTagCompound().getString(ITEMIDKEY);
        List<String> tooltips = new ArrayList<>();
        if (itemId != null) {
            String delimiter = ":";
            if (itemId.split(delimiter).length == 2) {
                tooltips.add("Pack: " + itemId.split(delimiter)[0]);
                tooltips.add("ID: " + itemId.split(delimiter)[1]);
            } else {
                tooltips.add("ID: " + itemId);
            }

            boolean isUTF8 = LOSBlocks.BLOCK_SIGNAL_BOX.isUTF8(itemId);
            if (!isUTF8) {
                tooltips.add("");
                tooltips.add(TextUtil.translate(MSG_NOT_UTF8));
            }

        }
        return tooltips;
    }
}
