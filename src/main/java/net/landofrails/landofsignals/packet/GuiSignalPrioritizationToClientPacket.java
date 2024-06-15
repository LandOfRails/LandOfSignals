package net.landofrails.landofsignals.packet;

import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.gui.GuiSignalPrioritization;
import net.landofrails.landofsignals.serialization.MapStringStringArrayMapper;
import net.landofrails.landofsignals.serialization.StringArrayMapper;
import net.landofrails.landofsignals.tile.TileComplexSignal;
import net.landofrails.landofsignals.tile.TileSignalPart;

import java.util.Map;

public class GuiSignalPrioritizationToClientPacket extends Packet {

    public static final int SIGNAL_PART_ID = 1;
    public static final int COMPLEX_SIGNAL_ID = 2;

    @TagField("signalPos")
    Vec3i signalPos;
    @TagField("signalType")
    int signalType;
    @TagField("signalId")
    String signalId;
    @TagField(value = "orderedStates", mapper = StringArrayMapper.class)
    String[] orderedStates = null;
    @TagField(value = "orderedGroupStates", mapper = MapStringStringArrayMapper.class)
    Map<String, String[]> orderedGroupStates = null;

    public GuiSignalPrioritizationToClientPacket() {

    }

    public GuiSignalPrioritizationToClientPacket(TileSignalPart tileSignalPart) {
        this.signalPos = tileSignalPart.getPos();
        this.signalType = SIGNAL_PART_ID;
        this.signalId = tileSignalPart.getId();
        this.orderedStates = tileSignalPart.getOrderedStates();
    }

    public GuiSignalPrioritizationToClientPacket(TileComplexSignal tileComplexSignal) {
        this.signalPos = tileComplexSignal.getPos();
        this.signalType = COMPLEX_SIGNAL_ID;
        this.signalId = tileComplexSignal.getId();
        this.orderedGroupStates = tileComplexSignal.getOrderedGroupStates();
    }

    @Override
    protected void handle() {
        GuiSignalPrioritization.open(signalPos, signalType, signalId, orderedStates, orderedGroupStates);
    }

    public static void sendToPlayer(Player player, TileSignalPart tileSignalPart) {
        tileSignalPart.markDirty();
        new GuiSignalPrioritizationToClientPacket(tileSignalPart).sendToPlayer(player);
    }

    public static void sendToPlayer(Player player, TileComplexSignal tileComplexSignal) {
        tileComplexSignal.markDirty();
        new GuiSignalPrioritizationToClientPacket(tileComplexSignal).sendToPlayer(player);
    }

}
