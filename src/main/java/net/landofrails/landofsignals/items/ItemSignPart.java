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
import net.landofrails.landofsignals.tile.TileSignPart;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;
import net.landofrails.landofsignals.utils.Static;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ItemSignPart extends CustomItem {

    private static final String ITEMIDKEY = "itemId";
    private static final String MSG_LOS_SIGNS_WRITEABLE = "message.landogsignals:signs.writeable";
    private static final String MSG_LOS_TRUE = "message.landofsignals:true";
    private static final String MSG_LOS_FALSE = "message.landofsignals:false";

    public ItemSignPart(String modID, String name) {
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
        float rotationSteps = LOSBlocks.BLOCK_SIGN_PART.getRotationSteps(itemId);
        int rot = (int) (-(Math.round(player.getRotationYawHead() / rotationSteps) * rotationSteps) + 180);
        TileSignPart tileSignPartPart = world.getBlockEntity(pos, TileSignPart.class);
        if (tileSignPartPart != null && !player.isCrouching()) rot = tileSignPartPart.getBlockRotate();
        LOSBlocks.BLOCK_SIGN_PART.setRot(rot);
        LOSBlocks.BLOCK_SIGN_PART.setId(player.getHeldItem(hand).getTagCompound().getString(ITEMIDKEY));
        world.setBlock(target.get(), LOSBlocks.BLOCK_SIGN_PART);
        return ClickResult.ACCEPTED;
    }

    @Override
    public String getCustomName(ItemStack stack) {
        TagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey(ITEMIDKEY)) return LOSBlocks.BLOCK_SIGN_PART.getName(tag.getString(ITEMIDKEY));
        else return "Error missing tag \"itemId\" for ItemSignPart";
    }

    @Override
    public List<ItemStack> getItemVariants(CreativeTab creativeTab) {
        List<ItemStack> itemStackList = new ArrayList<>();

        if (creativeTab == null || creativeTab.equals(LOSTabs.get(LOSTabs.SIGNS_TAB))) {
            for (String id : LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().keySet()) {
                if (!id.equals(Static.MISSING)) {
                    ItemStack is = new ItemStack(LOSItems.ITEM_SIGN_PART, 1);
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

            boolean writeable = LOSBlocks.BLOCK_SIGN_PART.isWritable(itemId);
            String writeableRawText = TextUtil.translate(writeable ? MSG_LOS_TRUE : MSG_LOS_FALSE);
            tooltips.add(TextUtil.translate(MSG_LOS_SIGNS_WRITEABLE, new Object[]{writeableRawText}));
        }
        return tooltips;
    }
}
