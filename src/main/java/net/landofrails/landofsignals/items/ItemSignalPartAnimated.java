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
import java.util.Optional;

public class ItemSignalPartAnimated extends CustomItem {

    public ItemSignalPartAnimated(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(LOSTabs.HIDDEN_TAB);
    }

    @Override
    public ClickResult onClickBlock(final Player player, final World world, final Vec3i pos, final Player.Hand hand, final Facing facing, final Vec3d inBlockPos) {
        final Optional<Vec3i> target = LandOfSignalsUtils.canPlaceBlock(world, pos, facing, player);
        if (!target.isPresent()) return ClickResult.REJECTED;

        int rot = -(Math.round(player.getRotationYawHead() / 10) * 10) + 180;
        final TileSignalPart tileSignalPart = world.getBlockEntity(pos, TileSignalPart.class);
        if (tileSignalPart != null && !player.isCrouching()) rot = tileSignalPart.getBlockRotate();
        LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.setRot(rot);
        LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.setId(player.getHeldItem(hand).getTagCompound().getString("itemId"));
        world.setBlock(target.get(), LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED);
        return ClickResult.ACCEPTED;
    }

    @Override
    public String getCustomName(final ItemStack stack) {
        final TagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("itemId"))
            return LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getName(tag.getString("itemId"));
        else return "Error missing tag \"itemId\" for ItemSignalPartAnimated";
    }
}
