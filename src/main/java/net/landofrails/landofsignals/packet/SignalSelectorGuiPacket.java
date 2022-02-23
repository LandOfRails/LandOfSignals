package net.landofrails.landofsignals.packet;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;

public class SignalSelectorGuiPacket extends Packet {

    @TagField("item")
    private ItemStack item;
    @TagField("slot")
    private int slot;
    @TagField("drop")
    private boolean drop;

    public SignalSelectorGuiPacket() {

    }

    public SignalSelectorGuiPacket(ItemStack item, int slot, boolean drop) {
        this.item = item;
        this.slot = slot;
        this.drop = drop;
    }

    @Override
    protected void handle() {
        if (!drop) getPlayer().getInventory().set(slot, item);
        else getPlayer().getWorld().dropItem(item, getPlayer().getPosition());
    }
}
