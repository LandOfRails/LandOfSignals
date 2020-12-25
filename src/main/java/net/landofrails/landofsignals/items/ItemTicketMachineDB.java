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
import net.landofrails.landofsignals.blocks.BlockTicketMachineDB;

import java.util.Collections;
import java.util.List;

public class ItemTicketMachineDB extends CustomItem {
    public ItemTicketMachineDB(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(LOSTabs.MAIN_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        BlockTicketMachineDB block = LOSBlocks.BLOCK_TICKET_MACHINE_DB;
        block.setRot(-(Math.round(player.getRotationYawHead() / 10) * 10) + 180);
        world.setBlock(pos.offset(facing), block);
        return ClickResult.ACCEPTED;
    }
}
