package net.landofrails.landofsignals.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileTop;

import java.util.Collections;
import java.util.List;

public class ItemConnector extends CustomItem {

    TileTop blockEntityTop;
    TileSignalBox blockEntityBox;

    public ItemConnector(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(LOSTabs.MAIN_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        if (world.isServer) {
            if (blockEntityTop == null) {
                blockEntityTop = world.getBlockEntity(pos, TileTop.class);
            }
            if (blockEntityBox == null) {
                blockEntityBox = world.getBlockEntity(pos, TileSignalBox.class);
            }
            if (blockEntityBox != null && blockEntityTop != null) {
                blockEntityBox.setUUID(blockEntityTop.getUUID());
                blockEntityTop = null;
                blockEntityBox = null;
                player.sendMessage(PlayerMessage.direct("Signal paired."));
                return ClickResult.ACCEPTED;
            } else if (blockEntityTop != null) {
                player.sendMessage(PlayerMessage.direct("Pairing started with " + blockEntityTop.getBlock()));
                return ClickResult.ACCEPTED;
            } else if (blockEntityBox != null) {
                player.sendMessage(PlayerMessage.direct("Pairing started with SignalBox"));
                return ClickResult.ACCEPTED;
            } else {
                return ClickResult.REJECTED;
            }
        }
        return ClickResult.REJECTED;
    }
}
