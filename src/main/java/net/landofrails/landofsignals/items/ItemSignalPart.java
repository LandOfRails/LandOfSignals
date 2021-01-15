package net.landofrails.landofsignals.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.blocks.BlockSignalPart;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;
import net.landofrails.landofsignals.utils.Static;

import java.util.Collections;
import java.util.List;

public class ItemSignalPart extends CustomItem {

    private BlockSignalPart block;

    public ItemSignalPart(String name, BlockSignalPart block) {
        super(LandOfSignals.MODID, name);
        Static.itemSignalPartList.add(this);
        this.block = block;
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
        block.setRot(rot);
        block.setPos(pos.offset(facing));
        world.setBlock(pos.offset(facing), block);
        return ClickResult.ACCEPTED;
    }

    public BlockSignalPart getBlock() {
        return block;
    }
}
