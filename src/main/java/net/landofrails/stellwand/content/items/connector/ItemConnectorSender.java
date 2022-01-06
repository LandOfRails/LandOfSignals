package net.landofrails.stellwand.content.items.connector;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.world.World;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.messages.EMessage;
import net.landofrails.stellwand.content.network.ServerMessagePacket;
import net.landofrails.stellwand.utils.compact.SignalContainer;

public class ItemConnectorSender extends AItemConnector {
    private static final long serialVersionUID = -7946475502287227609L;

    protected static void register() {
        AItemConnector.registerConnector(ItemConnectorSender.class, ItemConnectorSender::isConnectable);
    }

    public ItemConnectorSender() {
        super();
    }

    public ItemConnectorSender(TagCompound tag) {
        super(tag);
    }

    private static Boolean isConnectable(World world, Vec3i pos) {
        return getSender(world, pos) != null;
    }

    @Override
    protected boolean canConnect(World world, Vec3i pos) {
        return SignalContainer.isSignal(world, pos);
    }

    @Override
    protected Class<? extends AItemConnector> getImplementingClass() {
        return ItemConnectorSender.class;
    }

    @Override
    public boolean shouldOverrideConnector(World world, Vec3i pos) {
        return isConnectable(world, pos);
    }

    @Override
    protected boolean connect(World world, Vec3i pos, Player player, Player.Hand hand) {

        if (getStartWorld() == null && getStartPos() == null) {

            int count = player.getHeldItem(hand).getCount();
            setStartWorld(world);
            setStartPos(pos);
            ItemStack newItemStack = new ItemStack(CustomItems.ITEMCONNECTOR2, count);
            newItemStack.setTagCompound(this.toTag());
            player.setHeldItem(hand, newItemStack);
            ServerMessagePacket.send(player, EMessage.MESSAGE_NEW_SENDER_SELECTED, "" + pos.x, "" + pos.y, "" + pos.z);
            return true;

        } else if (world != null) {

            if (getStartWorld().getId() == world.getId()) {
                BlockSenderStorageEntity sender = getSender(getStartWorld(), getStartPos());
                if (sender.isCompatible(getSignal(world, pos))) {
                    if (!player.isCrouching()) {
                        sender.signals.add(pos);
                        ServerMessagePacket.send(player, EMessage.MESSAGE_SIGNAL_CONNECTED);
                    } else {
                        sender.signals.remove(pos);
                        ServerMessagePacket.send(player, EMessage.MESSAGE_SIGNAL_DISCONNECTED);
                    }
                    sender.updateSignals();
                    sender.markDirty();
                    return true;


                } else {
                    ServerMessagePacket.send(player, EMessage.MESSAGE_SIGNALS_MUST_BE_EQUAL);
                }
            } else {
                ServerMessagePacket.send(player, EMessage.MESSAGE_NOT_THE_SAME_WORLD);
            }

        }

        return false;
    }
}
