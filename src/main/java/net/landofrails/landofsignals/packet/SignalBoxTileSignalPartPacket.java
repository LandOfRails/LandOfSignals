package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.tile.TileSignalPartAnimated;

public class SignalBoxTileSignalPartPacket extends Packet {

    @TagField("tileSignalPart")
    TileSignalPart tileSignalPart;
    @TagField("tileSignalPartAnimated")
    TileSignalPartAnimated tileSignalPartAnimated;
    @TagField("posSignalBox")
    Vec3i posSignalBox;

    public SignalBoxTileSignalPartPacket() {
    }

    public SignalBoxTileSignalPartPacket(TileSignalPart tileSignalPart, Vec3i posSignalBox) {
        this.tileSignalPart = tileSignalPart;
        this.posSignalBox = posSignalBox;
    }

    public SignalBoxTileSignalPartPacket(TileSignalPartAnimated tileSignalPartAnimated, Vec3i posSignalBox) {
        this.tileSignalPartAnimated = tileSignalPartAnimated;
        this.posSignalBox = posSignalBox;
    }

    @Override
    protected void handle() {
        if (tileSignalPart != null)
            getWorld().getBlockEntity(posSignalBox, TileSignalBox.class).setTileSignalPart(tileSignalPart);
        else
            getWorld().getBlockEntity(posSignalBox, TileSignalBox.class).setTileSignalPartAnimated(tileSignalPartAnimated);
        LOSGuis.SIGNAL_ANIMATED_BOX.open(getPlayer(), posSignalBox);
    }
}
