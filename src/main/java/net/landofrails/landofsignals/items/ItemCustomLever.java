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
import net.landofrails.landofsignals.tile.TileCustomLever;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;
import net.landofrails.landofsignals.utils.Static;

import java.util.*;

public class ItemCustomLever extends CustomItem {

    private static final Map<String, List<String>> cacheTooltips = new HashMap<>();

    private static final String ITEMIDKEY = "itemId";
    private static final String MSG_NOT_UTF8 = "message.landofsignals:non.utf.eight.items";
    private static final String MSG_HAS_FLARES = "message.landofsignals:signalpart.hasflares";
    private static final String MSG_HAS_ANIMATION = "message.landofsignals:customlevers.hasanimation";
    private static final String MSG_LOS_TRUE = "message.landofsignals:true";
    private static final String MSG_LOS_FALSE = "message.landofsignals:false";

    public ItemCustomLever(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.ASSETS_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        Optional<Vec3i> target = LandOfSignalsUtils.canPlaceBlock(world, pos, facing, player);
        if (!target.isPresent()) return ClickResult.REJECTED;

        String itemId = player.getHeldItem(hand).getTagCompound().getString(ITEMIDKEY);

        if(!LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().containsKey(itemId)) return ClickResult.REJECTED;

        float rotationSteps = LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(itemId).getRotationSteps();
        int rot = (int) (-(Math.round(player.getRotationYawHead() / rotationSteps) * rotationSteps) + 180);

        TileCustomLever tileCustomLever = world.getBlockEntity(pos, TileCustomLever.class);
        if (tileCustomLever != null && !player.isCrouching()) rot = tileCustomLever.getBlockRotate();
        LOSBlocks.BLOCK_CUSTOM_LEVER.setRot(rot);
        LOSBlocks.BLOCK_CUSTOM_LEVER.setId(player.getHeldItem(hand).getTagCompound().getString(ITEMIDKEY));
        world.setBlock(target.get(), LOSBlocks.BLOCK_CUSTOM_LEVER);

        return ClickResult.ACCEPTED;
    }

    @Override
    public String getCustomName(ItemStack stack) {
        TagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey(ITEMIDKEY)) return LOSBlocks.BLOCK_CUSTOM_LEVER.getName(tag.getString(ITEMIDKEY));
        else return "Error missing tag \"itemId\" for ItemCustomLever";
    }

    @Override
    public List<ItemStack> getItemVariants(CreativeTab creativeTab) {
        List<ItemStack> itemStackList = new ArrayList<>();

        if (creativeTab == null || creativeTab.equals(LOSTabs.get(LOSTabs.ASSETS_TAB))) {
            for (String id : LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().keySet()) {
                if (!id.equals(Static.MISSING)) {
                    ItemStack is = new ItemStack(LOSItems.ITEM_CUSTOM_LEVER, 1);
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
        if(itemId == null){
            return new ArrayList<>();
        }
        if(!cacheTooltips.containsKey(itemId)){
            cacheTooltip(itemId);
        }
        return cacheTooltips.get(itemId);
    }

    private void cacheTooltip(String itemId) {
        List<String> tooltips = new ArrayList<>();
        String delimiter = ":";
        if (itemId.split(delimiter).length == 2) {
            tooltips.add("Pack: " + itemId.split(delimiter)[0]);
            tooltips.add("ID: " + itemId.split(delimiter)[1]);
        } else {
            tooltips.add("ID: " + itemId);
        }

        Object[] hasAnimation = new Object[]{TextUtil.translate(MSG_LOS_FALSE)};
        // String has to be converted to array by us, build pipeline is not able to do it itself.
        tooltips.add(TextUtil.translate(MSG_HAS_ANIMATION, hasAnimation));

        boolean hasFlares = LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(itemId).getFlares().length > 0;
        Object[] hasFlaresRawText = new Object[]{TextUtil.translate(hasFlares ? MSG_LOS_TRUE : MSG_LOS_FALSE)};
        // String has to be converted to array by us, build pipeline is not able to do it itself.
        tooltips.add(TextUtil.translate(MSG_HAS_FLARES, hasFlaresRawText));

        boolean isUTF8 = LOSBlocks.BLOCK_CUSTOM_LEVER.isUTF8(itemId);
        if (!isUTF8) {
            tooltips.add("");
            tooltips.add(TextUtil.translate(MSG_NOT_UTF8));
        }

        cacheTooltips.put(itemId, tooltips);
    }
}
