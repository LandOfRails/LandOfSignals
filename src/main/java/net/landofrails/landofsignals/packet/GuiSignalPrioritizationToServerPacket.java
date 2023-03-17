package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.serialization.MapStringStringArrayMapper;
import net.landofrails.landofsignals.serialization.StringArrayMapper;
import net.landofrails.landofsignals.tile.TileComplexSignal;
import net.landofrails.landofsignals.tile.TileSignalPart;

import java.util.Map;

public class GuiSignalPrioritizationToServerPacket extends Packet {

    @TagField("signalPos")
    Vec3i signalPos;
    @TagField(value = "newStates", mapper = StringArrayMapper.class)
    String[] states;
    @TagField(value = "groupStates", mapper = MapStringStringArrayMapper.class)
    Map<String, String[]> groupStates;

    public GuiSignalPrioritizationToServerPacket() {

    }

    public GuiSignalPrioritizationToServerPacket(Vec3i signalPos, String[] states) {
        this.signalPos = signalPos;
        this.states = states;
    }

    public GuiSignalPrioritizationToServerPacket(Vec3i signalPos, Map<String, String[]> groupStates) {
        this.signalPos = signalPos;
        this.groupStates = groupStates;
    }

    @Override
    protected void handle() {
        if (!getWorld().isBlockLoaded(signalPos)) {
            getWorld().keepLoaded(signalPos);
        }

        if (states != null) {
            TileSignalPart tile = getWorld().getBlockEntity(signalPos, TileSignalPart.class);
            if (tile != null) {
                tile.setOrderedStates(states);
                tile.markDirty();
                tile.updateSignals();
            }
        } else if (groupStates != null) {
            TileComplexSignal tile = getWorld().getBlockEntity(signalPos, TileComplexSignal.class);
            if (tile != null) {
                tile.setOrderedGroupStates(groupStates);
                tile.markDirty();
                tile.updateSignals();
            }
        }

    }

}
