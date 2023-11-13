package net.landofrails.landofsignals.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.text.TextUtil;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.blocks.BlockSignalLever;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ItemSignalLever extends CustomItem {

    private static final String MSG_W_ANIMATION = "message.landofsignals:with.animation";

    public ItemSignalLever(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.ASSETS_TAB);
    }

    @Override
    public ClickResult onClickBlock(final Player player, final World world, final Vec3i pos, final Player.Hand hand, final Facing facing, final Vec3d inBlockPos) {
        final Optional<Vec3i> target = LandOfSignalsUtils.canPlaceBlock(world, pos, facing, player);
        if (!target.isPresent()) return ClickResult.REJECTED;

        final BlockSignalLever block = LOSBlocks.BLOCK_SIGNAL_LEVER;
        block.setRot(-player.getRotationYawHead() + 180);
        world.setBlock(target.get(), block);
        return ClickResult.ACCEPTED;
    }

    @Override
    public List<String> getTooltip(ItemStack itemStack) {
        return Collections.singletonList(TextUtil.translate(MSG_W_ANIMATION));
    }
}
