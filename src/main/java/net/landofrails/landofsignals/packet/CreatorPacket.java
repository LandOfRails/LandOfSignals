package net.landofrails.landofsignals.packet;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.api.contentpacks.v2.EntryType;
import net.landofrails.landofsignals.creator.gui.GuiSelectContentpack;

public class CreatorPacket extends Packet {

    @TagField(value = "entryType", typeHint = EntryType.class)
    private EntryType entryType;

    public CreatorPacket() {

    }

    public CreatorPacket(EntryType entryType) {
        this.entryType = entryType;
    }

    public static void sendToPlayer(Player player, EntryType entryType) {
        CreatorPacket packet = new CreatorPacket(entryType);
        packet.sendToPlayer(player);
    }

    @Override
    protected void handle() {

        GuiSelectContentpack.open(MinecraftClient.getPlayer(), this.entryType);
    }
}
