package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.serialization.StringArrayMapper;
import net.landofrails.landofsignals.tile.TileSignalPart;

public class GuiSignalPrioritizationToServerPacket extends Packet {

    @TagField("signalPos")
    Vec3i signalPos;
    @TagField(value = "newStates", mapper = StringArrayMapper.class)
    String[] states;

    public GuiSignalPrioritizationToServerPacket() {

    }

    public GuiSignalPrioritizationToServerPacket(Vec3i signalPos, String[] states) {
        this.signalPos = signalPos;
        this.states = states;
    }

    @Override
    protected void handle() {
        if (!getWorld().isBlockLoaded(signalPos)) {
            getWorld().keepLoaded(signalPos);
        }

        TileSignalPart tile = getWorld().getBlockEntity(signalPos, TileSignalPart.class);
        if (tile != null) {
            tile.setOrderedStates(states);
            tile.markDirty();
            tile.updateSignals();
        }

    }

}
