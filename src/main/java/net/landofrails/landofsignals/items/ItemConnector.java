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
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileSignalPart;

import java.util.Collections;
import java.util.List;

public class ItemConnector extends CustomItem {

    TileSignalPart blockEntitySignalPart;
    TileSignalBox blockEntityBox;

    public ItemConnector(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(LOSTabs.SIGNALS_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        if (world.isServer) {
            if (blockEntitySignalPart == null) blockEntitySignalPart = world.getBlockEntity(pos, TileSignalPart.class);
            if (blockEntityBox == null) blockEntityBox = world.getBlockEntity(pos, TileSignalBox.class);
            if (blockEntityBox != null && blockEntitySignalPart != null) {
                if (LOSBlocks.BLOCK_SIGNAL_PART.getStates(blockEntitySignalPart.getId()).size() <= 1) {
                    blockEntitySignalPart = null;
                    return ClickResult.REJECTED;
                }
                blockEntityBox.setTileSignalPartPos(blockEntitySignalPart.getPos());
                blockEntitySignalPart = null;
                blockEntityBox = null;
                player.sendMessage(PlayerMessage.direct("Signal paired."));
                return ClickResult.ACCEPTED;
            } else if (blockEntitySignalPart != null) {
                if (LOSBlocks.BLOCK_SIGNAL_PART.getStates(blockEntitySignalPart.getId()).size() <= 1) {
                    blockEntitySignalPart = null;
                    return ClickResult.REJECTED;
                }
                player.sendMessage(PlayerMessage.direct("Pairing started with " + LOSBlocks.BLOCK_SIGNAL_PART.getName(blockEntitySignalPart.getId())));
                return ClickResult.ACCEPTED;
            } else if (blockEntityBox != null) {
                player.sendMessage(PlayerMessage.direct("Pairing started with SignalBox"));
                return ClickResult.ACCEPTED;
            }
        }
        return ClickResult.REJECTED;
    }

    @Override
    public void onClickAir(Player player, World world, Player.Hand hand) {
        if (world.isServer && player.isCrouching()) {
            blockEntityBox = null;
            blockEntitySignalPart = null;
            player.sendMessage(PlayerMessage.direct("Pairing canceled."));
        }
    }
}
