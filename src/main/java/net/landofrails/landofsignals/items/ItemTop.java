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
import net.landofrails.landofsignals.blocks.BlockTop;
import net.landofrails.landofsignals.tile.TileGround;
import net.landofrails.landofsignals.tile.TileMid;
import net.landofrails.landofsignals.utils.Static;

import java.util.Collections;
import java.util.List;

public class ItemTop extends CustomItem {
    private String blockName;

    public ItemTop(String modID, String name, String block) {
        super(modID, name);
        this.blockName = block;
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(LOSTabs.MAIN_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        float rot = -(Math.round(player.getRotationYawHead() / 10) * 10) + 180;
        TileMid midEntity = world.getBlockEntity(pos, TileMid.class);
        if (midEntity == null) {
            TileGround groundEntity = world.getBlockEntity(pos, TileGround.class);
            if (groundEntity != null) {
                rot = groundEntity.getBlockRotate();
            }
        } else {
            rot = midEntity.getBlockRotate();
        }
        BlockTop block = Static.listTopModels.get(blockName)._2();
        block.setBlock(blockName);
        block.setRot(rot);
        world.setBlock(pos.offset(facing), block);
        return ClickResult.ACCEPTED;
    }
}