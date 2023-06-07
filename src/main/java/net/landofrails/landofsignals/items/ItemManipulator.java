package net.landofrails.landofsignals.items;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.packet.GuiItemManipulatorToClient;
import net.landofrails.landofsignals.utils.IManipulate;

import java.util.List;

@SuppressWarnings("java:S1117")
public class ItemManipulator extends CustomItem {

    public ItemManipulator(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.ASSETS_TAB);
    }

    @Override
    @SuppressWarnings({"java:S2696", "java:S1134"})
    public ClickResult onClickBlock(final Player player, final World world, final Vec3i pos, final Player.Hand hand, final Facing facing, final Vec3d inBlockPos) {
        BlockEntity block = world.getBlockEntity(pos, BlockEntity.class);

        if (block instanceof IManipulate && world.isServer) {
            new GuiItemManipulatorToClient(block).sendToPlayer(player);
            return ClickResult.ACCEPTED;
        }
        return ClickResult.REJECTED;
    }

}
