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

    @TagField("posSignalBox")
    Vec3i posSignalBox;
    @TagField("signalType")
    Byte signalType;
    @TagField("signalId")
    String signalId;

    public SignalBoxTileSignalPartPacket() {
    }

    public SignalBoxTileSignalPartPacket(final TileSignalPart tileSignalPart, final TileSignalBox tileSignalBox) {
        this.signalId = tileSignalPart.getId();
        this.posSignalBox = tileSignalBox.getPos();
        this.signalType = tileSignalBox.getSignalType();
    }

    public SignalBoxTileSignalPartPacket(final TileComplexSignal tileComplexSignal, final TileSignalBox tileSignalBox) {
        this.signalId = tileComplexSignal.getId();
        this.posSignalBox = tileSignalBox.getPos();
        this.signalType = tileSignalBox.getSignalType();
    }

    @Override
    protected void handle() {
        if (signalType == 0) {
            GuiSignalBoxSignalPart.open(getWorld().getBlockEntity(posSignalBox, TileSignalBox.class), signalId);
        } else if (signalType == 1) {
            GuiSignalBoxComplexSignal.open(getWorld().getBlockEntity(posSignalBox, TileSignalBox.class), signalId);
        } else {
            LandOfSignals.error("Can't open Signalbox, no tile entity given.");
        }
    }
}
