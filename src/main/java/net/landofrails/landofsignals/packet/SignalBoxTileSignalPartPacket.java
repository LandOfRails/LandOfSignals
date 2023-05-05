package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.gui.GuiSignalBoxComplexSignal;
import net.landofrails.landofsignals.gui.GuiSignalBoxSignalPart;
import net.landofrails.landofsignals.tile.TileComplexSignal;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileSignalPart;

public class SignalBoxTileSignalPartPacket extends Packet {

    @TagField("tileSignalPart")
    TileSignalPart tileSignalPart;
    @TagField("tileComplexSignal")
    TileComplexSignal tileComplexSignal;

    @TagField("tileSignalBox")
    TileSignalBox tileSignalBox;
    @TagField("posSignalBox")
    Vec3i posSignalBox;
    @TagField("signalType")
    Byte signalType;

    public SignalBoxTileSignalPartPacket() {
    }

    public SignalBoxTileSignalPartPacket(final TileSignalPart tileSignalPart, final TileSignalBox tileSignalBox) {
        this.tileSignalPart = tileSignalPart;
        this.tileSignalBox = tileSignalBox;
        this.posSignalBox = tileSignalBox.getPos();
        this.signalType = tileSignalBox.getSignalType();
    }

    public SignalBoxTileSignalPartPacket(final TileComplexSignal tileComplexSignal, final TileSignalBox tileSignalBox) {
        this.tileComplexSignal = tileComplexSignal;
        this.tileSignalBox = tileSignalBox;
        this.posSignalBox = tileSignalBox.getPos();
        this.signalType = tileSignalBox.getSignalType();
    }

    @Override
    protected void handle() {
        if (tileSignalPart != null) {
            getWorld().setBlockEntity(posSignalBox, tileSignalBox);
            getWorld().getBlockEntity(posSignalBox, TileSignalBox.class).setTileSignalPart(tileSignalPart);
            GuiSignalBoxSignalPart.open(tileSignalBox);
        } else if (tileComplexSignal != null) {
            getWorld().setBlockEntity(posSignalBox, tileSignalBox);
            getWorld().getBlockEntity(posSignalBox, TileSignalBox.class).setTileComplexSignal(tileComplexSignal);
            GuiSignalBoxComplexSignal.open(tileSignalBox);
        } else {
            LandOfSignals.error("Can't open Signalbox, no tile entity given.");
        }
    }
}
