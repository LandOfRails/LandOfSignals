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
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.blocks.BlockSignalLever;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;

import java.util.*;

public class ItemSignalLever extends CustomItem {

    private static final String MSG_HAS_ANIMATION = "message.landofsignals:customlevers.hasanimation";
    private static final String MSG_HAS_FLARES = "message.landofsignals:signalpart.hasflares";
    private static final String MSG_LOS_TRUE = "message.landofsignals:true";
    private static final String MSG_LOS_FALSE = "message.landofsignals:false";

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
        List<String> tooltips = new ArrayList<>();
        tooltips.add("Pack: " + LandOfSignals.MODID);
        tooltips.add("ID: item_signal_lever");

        Object[] hasAnimation = new Object[]{TextUtil.translate(MSG_LOS_TRUE)};
        // String has to be converted to array by us, build pipeline is not able to do it itself.
        tooltips.add(TextUtil.translate(MSG_HAS_ANIMATION, hasAnimation));

        Object[] hasFlaresRawText = new Object[]{TextUtil.translate(MSG_LOS_FALSE)};
        // String has to be converted to array by us, build pipeline is not able to do it itself.
        tooltips.add(TextUtil.translate(MSG_HAS_FLARES, hasFlaresRawText));

        return tooltips;
    }
}
