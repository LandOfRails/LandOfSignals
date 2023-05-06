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
import net.landofrails.landofsignals.tile.TileComplexSignal;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileSignalPart;

import java.util.List;

public class ItemConnector extends CustomItem {

    TileSignalPart blockEntitySignalPart;
    TileComplexSignal blockEntityComplexSignal;
    TileSignalBox blockEntityBox;

    public ItemConnector(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.SIGNALS_TAB);
    }

    @Override
    public ClickResult onClickBlock(final Player player, final World world, final Vec3i pos, final Player.Hand hand, final Facing facing, final Vec3d inBlockPos) {
        if (world.isServer) {

            final TileSignalPart tempSignalPart = world.getBlockEntity(pos, TileSignalPart.class);
            final TileComplexSignal tempComplexSignal = world.getBlockEntity(pos, TileComplexSignal.class);
            final TileSignalBox tempSignalBox = world.getBlockEntity(pos, TileSignalBox.class);

            if (tempSignalPart == null && tempComplexSignal == null && tempSignalBox == null) {
                return ClickResult.REJECTED;
            }

            if (tempSignalPart != null && LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().containsKey(tempSignalPart.getId())) {
                blockEntitySignalPart = tempSignalPart;
                if (blockEntityBox == null)
                    player.sendMessage(PlayerMessage.direct("Pairing started with " + LOSBlocks.BLOCK_SIGNAL_PART.getName(blockEntitySignalPart.getId())));
            } else if (tempComplexSignal != null && LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().containsKey(tempComplexSignal.getId())) {
                blockEntityComplexSignal = tempComplexSignal;
                if (blockEntityBox == null)
                    player.sendMessage(PlayerMessage.direct("Pairing started with " + LOSBlocks.BLOCK_COMPLEX_SIGNAL.getName(tempComplexSignal.getId())));
            } else if (tempSignalBox != null) {
                blockEntityBox = tempSignalBox;
                if (blockEntitySignalPart == null && blockEntityComplexSignal == null)
                    player.sendMessage(PlayerMessage.direct("Pairing started with signal box."));
            }

            if (blockEntitySignalPart != null && blockEntityBox != null) {
                blockEntityBox.clearPreviousData();
                blockEntityBox.setTileSignalPartPos(blockEntitySignalPart.getPos(), (byte) 0);
                player.sendMessage(PlayerMessage.direct("Box paired with " + LOSBlocks.BLOCK_SIGNAL_PART.getName(blockEntitySignalPart.getId())));

                blockEntitySignalPart = null;
                blockEntityComplexSignal = null;
                blockEntityBox = null;
                return ClickResult.ACCEPTED;
            } else if (blockEntityComplexSignal != null && blockEntityBox != null) {
                blockEntityBox.clearPreviousData();
                blockEntityBox.setTileSignalPartPos(blockEntityComplexSignal.getPos(), (byte) 1);
                player.sendMessage(PlayerMessage.direct("Box paired with " + LOSBlocks.BLOCK_COMPLEX_SIGNAL.getName(blockEntityComplexSignal.getId())));

                blockEntitySignalPart = null;
                blockEntityComplexSignal = null;
                blockEntityBox = null;
                return ClickResult.ACCEPTED;
            }
        }
        return ClickResult.ACCEPTED;
    }

    @Override
    public void onClickAir(final Player player, final World world, final Player.Hand hand) {
        if (world.isServer && player.isCrouching()) {
            blockEntityBox = null;
            blockEntitySignalPart = null;
            blockEntityComplexSignal = null;
            player.sendMessage(PlayerMessage.direct("Pairing canceled."));
        }
    }
}
