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

import java.util.List;

public class ItemConnector extends CustomItem {

    TileSignalPart blockEntitySignalPart;
    TileSignalBox blockEntityBox;

    public ItemConnector(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.SIGNALS_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        if (world.isServer) {

            TileSignalPart tempPart = world.getBlockEntity(pos, TileSignalPart.class);
            TileSignalBox tempSignalBox = world.getBlockEntity(pos, TileSignalBox.class);

            if (tempPart == null && tempSignalBox == null) {
                return ClickResult.REJECTED;
            }

            if (tempPart != null && LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().containsKey(tempPart.getId())) {
                blockEntitySignalPart = tempPart;
                if (blockEntityBox == null)
                    player.sendMessage(PlayerMessage.direct("Pairing started with " + LOSBlocks.BLOCK_SIGNAL_PART.getName(blockEntitySignalPart.getId())));
            }
            if (tempSignalBox != null) {
                blockEntityBox = tempSignalBox;
                if (blockEntitySignalPart == null)
                    player.sendMessage(PlayerMessage.direct("Pairing started with signal box."));
            }

            if (blockEntitySignalPart != null && blockEntityBox != null) {
                blockEntityBox.setTileSignalPartPos(blockEntitySignalPart.getPos());
                player.sendMessage(PlayerMessage.direct("Box paired with " + LOSBlocks.BLOCK_SIGNAL_PART.getName(blockEntitySignalPart.getId())));

                blockEntitySignalPart = null;
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
            player.sendMessage(PlayerMessage.direct("Pairing canceled."));
        }
    }
}
