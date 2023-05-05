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
import net.landofrails.landofsignals.tile.TileDeco;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;
import net.landofrails.landofsignals.utils.Static;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ItemDeco extends CustomItem {
    private static final String ITEMIDKEY = "itemId";
    private static final String MSG_NOT_UTF8 = "message.landofsignals:non.utf.eight.items";

    public ItemDeco(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.HIDDEN_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        Optional<Vec3i> target = LandOfSignalsUtils.canPlaceBlock(world, pos, facing, player);
        if (!target.isPresent()) return ClickResult.REJECTED;

        String itemId = player.getHeldItem(hand).getTagCompound().getString(ITEMIDKEY);

        if(!LOSBlocks.BLOCK_DECO.getContentpackDeco().containsKey(itemId)) return ClickResult.REJECTED;

        float rotationSteps = LOSBlocks.BLOCK_DECO.getRotationSteps(itemId);
        int rot = (int) (-(Math.round(player.getRotationYawHead() / rotationSteps) * rotationSteps) + 180);

        TileDeco tileDeco = world.getBlockEntity(pos, TileDeco.class);
        if (tileDeco != null && !player.isCrouching()) rot = tileDeco.getBlockRotate();
        LOSBlocks.BLOCK_DECO.setRot(rot);
        LOSBlocks.BLOCK_DECO.setId(player.getHeldItem(hand).getTagCompound().getString(ITEMIDKEY));
        world.setBlock(target.get(), LOSBlocks.BLOCK_DECO);
        return ClickResult.ACCEPTED;
    }

    @Override
    public String getCustomName(ItemStack stack) {
        TagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey(ITEMIDKEY)) return LOSBlocks.BLOCK_DECO.getName(tag.getString(ITEMIDKEY));
        else return "Error missing tag \"itemId\" for ItemSignalBox";
    }

    @Override
    public List<ItemStack> getItemVariants(CreativeTab creativeTab) {
        List<ItemStack> itemStackList = new ArrayList<>();

        if (creativeTab == null || creativeTab.equals(LOSTabs.get(LOSTabs.ASSETS_TAB))) {
            for (String id : LOSBlocks.BLOCK_DECO.getContentpackDeco().keySet()) {
                if (!id.equals(Static.MISSING)) {
                    ItemStack is = new ItemStack(LOSItems.ITEM_DECO, 1);
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

            boolean isUTF8 = LOSBlocks.BLOCK_DECO.isUTF8(itemId);
            if (!isUTF8) {
                tooltips.add("");
                tooltips.add(TextUtil.translate(MSG_NOT_UTF8));
            }

        }
        return tooltips;
    }
}
