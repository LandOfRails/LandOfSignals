package net.landofrails.landofsignals.packet;

import cam72cam.mod.net.Packet;
import net.landofrails.landofsignals.LOSGuis;

public class ConfigGuiPacket extends Packet {

    public ConfigGuiPacket() {
    }

    @Override
    protected void handle() {
        LOSGuis.CONFIG.open(getPlayer());
    }
}
