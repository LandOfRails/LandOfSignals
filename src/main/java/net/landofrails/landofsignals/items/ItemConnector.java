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
import net.landofrails.landofsignals.tile.TileSignalPartAnimated;

import java.util.Collections;
import java.util.List;

public class ItemConnector extends CustomItem {

    TileSignalPart blockEntitySignalPart;
    TileSignalPartAnimated blockEntityAnimatedPart;
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

            TileSignalPart tempPart = world.getBlockEntity(pos, TileSignalPart.class);
            TileSignalPartAnimated tempAnimatedPart = world.getBlockEntity(pos, TileSignalPartAnimated.class);
            TileSignalBox tempSignalBox = world.getBlockEntity(pos, TileSignalBox.class);

            if (tempPart == null && tempAnimatedPart == null && tempSignalBox == null) {
                return ClickResult.REJECTED;
            }

            if (tempPart != null && LOSBlocks.BLOCK_SIGNAL_PART.getStates(tempPart.getId()).size() > 1) {
                blockEntityAnimatedPart = null;
                blockEntitySignalPart = tempPart;
                if (blockEntityBox == null)
                    player.sendMessage(PlayerMessage.direct("Pairing started with " + LOSBlocks.BLOCK_SIGNAL_PART.getName(blockEntitySignalPart.getId())));
            }
            if (tempAnimatedPart != null) {
                blockEntitySignalPart = null;
                blockEntityAnimatedPart = tempAnimatedPart;
                if (blockEntityBox == null)
                    player.sendMessage(PlayerMessage.direct("Pairing started with " + LOSBlocks.BLOCK_SIGNAL_PART.getName(blockEntityAnimatedPart.getId())));
            }
            if (tempSignalBox != null) {
                blockEntityBox = tempSignalBox;
                if (blockEntitySignalPart == null && blockEntityAnimatedPart == null)
                    player.sendMessage(PlayerMessage.direct("Pairing started with signal box."));
            }

            if ((blockEntitySignalPart != null || blockEntityAnimatedPart != null) && blockEntityBox != null) {
                if (blockEntitySignalPart != null) {
                    blockEntityBox.setTileSignalPartPos(blockEntitySignalPart.getPos());
                    player.sendMessage(PlayerMessage.direct("Box paired with " + LOSBlocks.BLOCK_SIGNAL_PART.getName(blockEntitySignalPart.getId())));
                } else if (blockEntityAnimatedPart != null) {
                    blockEntityBox.setTileSignalPartPos(blockEntityAnimatedPart.getPos());
                    player.sendMessage(PlayerMessage.direct("Box paired with " + LOSBlocks.BLOCK_SIGNAL_PART.getName(blockEntityAnimatedPart.getId())));
                }


                blockEntitySignalPart = null;
                blockEntityAnimatedPart = null;
                blockEntityBox = null;
                return ClickResult.ACCEPTED;
            }
        }
        return ClickResult.ACCEPTED;
    }

    @Override
    public void onClickAir(Player player, World world, Player.Hand hand) {
        if (world.isServer && player.isCrouching()) {
            blockEntityBox = null;
            blockEntitySignalPart = null;
            blockEntityAnimatedPart = null;
            player.sendMessage(PlayerMessage.direct("Pairing canceled."));
        }
    }
}
