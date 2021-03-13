package net.landofrails.landofsignals.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;

import java.util.Collections;
import java.util.List;

public class ItemSignalPart extends CustomItem {

    public ItemSignalPart(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(LOSTabs.HIDDEN_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        if (!LandOfSignalsUtils.canPlaceBlock(world, pos, facing, player)) return ClickResult.REJECTED;

        int rot = -(Math.round(player.getRotationYawHead() / 10) * 10) + 180;
        TileSignalPart tileSignalPart = world.getBlockEntity(pos, TileSignalPart.class);
        if (tileSignalPart != null && !player.isCrouching()) rot = tileSignalPart.getBlockRotate();
        LOSBlocks.BLOCK_SIGNAL_PART.setRot(rot);
        LOSBlocks.BLOCK_SIGNAL_PART.setId(player.getHeldItem(hand).getTagCompound().getString("itemId"));
        world.setBlock(pos.offset(facing), LOSBlocks.BLOCK_SIGNAL_PART);
        return ClickResult.ACCEPTED;
    }

    @Override
    public String getCustomName(ItemStack stack) {
        TagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("itemId")) return LOSBlocks.BLOCK_SIGNAL_PART.getName(tag.getString("itemId"));
        else return "Error missing tag \"itemId\" for ItemSignalPart";
    }
}
