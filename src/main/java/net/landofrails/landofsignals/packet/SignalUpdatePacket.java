package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.serialization.MapStringStringMapper;
import net.landofrails.landofsignals.tile.TileSignalPart;

import java.util.Map;

public class SignalUpdatePacket extends Packet {

    @TagField(value = "signalPos")
    private Vec3i signalPos;
    @TagField(value = "signalGroupStates", mapper = MapStringStringMapper.class)
    private Map<String, String> signalGroupStates;

    public SignalUpdatePacket() {

    }

    public SignalUpdatePacket(Vec3i signalPos, Map<String, String> signalGroupStates) {
        this.signalPos = signalPos;
        this.signalGroupStates = signalGroupStates;
    }

    @Override
    protected void handle() {
        if (!getWorld().isBlockLoaded(signalPos)) {
            getWorld().keepLoaded(signalPos);
        }

        TileSignalPart tile = getWorld().getBlockEntity(signalPos, TileSignalPart.class);
        tile.setSignalGroupStates(signalGroupStates);
    }
}
