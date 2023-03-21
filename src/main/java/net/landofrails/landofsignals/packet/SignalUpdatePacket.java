package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.serialization.MapStringStringMapper;
import net.landofrails.landofsignals.tile.TileComplexSignal;
import net.landofrails.landofsignals.tile.TileSignalPart;

import java.util.Map;

public class SignalUpdatePacket extends Packet {

    @TagField("signalPos")
    private Vec3i signalPos;
    @TagField("signalType")
    private Byte signalType;

    @TagField("state")
    private String state;

    @TagField(value = "signalGroupStates", mapper = MapStringStringMapper.class)
    private Map<String, String> signalGroupStates;

    public SignalUpdatePacket() {

    }

    public SignalUpdatePacket(Vec3i signalPos, String state) {
        this.signalPos = signalPos;
        this.state = state;
        this.signalType = (byte) 0;
    }

    public SignalUpdatePacket(Vec3i signalPos, Map<String, String> signalGroupStates) {
        this.signalPos = signalPos;
        this.signalGroupStates = signalGroupStates;
        this.signalType = (byte) 1;
    }

    @Override
    protected void handle() {
        if (!getWorld().isBlockLoaded(signalPos)) {
            getWorld().keepLoaded(signalPos);
        }

        if (0 == signalType) {
            // Simple signal
            TileSignalPart tile = getWorld().getBlockEntity(signalPos, TileSignalPart.class);
            tile.setState(state);
        } else if (1 == signalType) {
            // Complex signal
            TileComplexSignal tile = getWorld().getBlockEntity(signalPos, TileComplexSignal.class);
            tile.setSignalGroupStates(signalGroupStates);
        }
    }
}
