package net.landofrails.landofsignals.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
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
    public ItemSignalLever(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(LOSTabs.ASSETS_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        Optional<Vec3i> target = LandOfSignalsUtils.canPlaceBlock(world, pos, facing, player);
        if (!target.isPresent()) return ClickResult.REJECTED;

        BlockSignalLever block = LOSBlocks.BLOCK_SIGNAL_LEVER;
        block.setRot(-player.getRotationYawHead() + 180);
        world.setBlock(target.get(), block);
        return ClickResult.ACCEPTED;
    }
}
