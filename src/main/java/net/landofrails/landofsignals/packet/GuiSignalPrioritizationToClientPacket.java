package net.landofrails.landofsignals.packet;

import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.gui.GuiSignalPrioritization;
import net.landofrails.landofsignals.tile.TileComplexSignal;
import net.landofrails.landofsignals.tile.TileSignalPart;

public class GuiSignalPrioritizationToClientPacket extends Packet {

    @TagField("signalPos")
    Vec3i signalPos;
    @TagField("tileSignalPart")
    TileSignalPart tileSignalPart;
    @TagField("tileComplexSignal")
    TileComplexSignal tileComplexSignal;

    public GuiSignalPrioritizationToClientPacket() {

    }

    public GuiSignalPrioritizationToClientPacket(Vec3i signalPos, TileSignalPart tileSignalPart) {
        this.signalPos = signalPos;
        this.tileSignalPart = tileSignalPart;
    }

    public GuiSignalPrioritizationToClientPacket(Vec3i signalPos, TileComplexSignal tileComplexSignal) {
        this.signalPos = signalPos;
        this.tileComplexSignal = tileComplexSignal;
    }

    @Override
    protected void handle() {
        if (tileSignalPart != null)
            getWorld().setBlockEntity(signalPos, tileSignalPart);
        else if (tileComplexSignal != null)
            getWorld().setBlockEntity(signalPos, tileComplexSignal);
        GuiSignalPrioritization.open(signalPos, tileSignalPart);
    }

    public static void sendToPlayer(Player player, TileSignalPart tileSignalPart) {
        new GuiSignalPrioritizationToClientPacket(tileSignalPart.getPos(), tileSignalPart).sendToPlayer(player);
    }

    public static void sendToPlayer(Player player, TileComplexSignal tileComplexSignal) {
        new GuiSignalPrioritizationToClientPacket(tileComplexSignal.getPos(), tileComplexSignal).sendToPlayer(player);
    }

}
