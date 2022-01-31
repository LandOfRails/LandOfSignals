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

public class ItemConnectorSignal extends AItemConnector {
    private static final long serialVersionUID = -7946475502287227609L;

    protected static void register() {
        AItemConnector.registerConnector(ItemConnectorSignal.class, ItemConnectorSignal::isConnectable);
    }

    public ItemConnectorSignal() {
        super();
    }

    public ItemConnectorSignal(TagCompound tag) {
        super(tag);
    }

    private static Boolean isConnectable(World world, Vec3i pos) {
        return SignalContainer.isSignal(world, pos);
    }

    @Override
    protected boolean canConnect(World world, Vec3i pos) {
        return getSender(world, pos) != null;
    }

    @Override
    protected Class<? extends AItemConnector> getImplementingClass() {
        return ItemConnectorSignal.class;
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
            ItemStack newItemStack = new ItemStack(CustomItems.ITEMCONNECTOR3, count);
            newItemStack.setTagCompound(this.toTag());
            player.setHeldItem(hand, newItemStack);
            ServerMessagePacket.send(player, EMessage.MESSAGE_NEW_SIGNAL_SELECTED, "" + pos.x, "" + pos.y, "" + pos.z);
            return true;

        } else if (world != null) {

            if (getStartWorld().getId() == world.getId()) {
                BlockSenderStorageEntity sender = getSender(world, pos);
                if (sender.isCompatible(getSignal(getStartWorld(), getStartPos()))) {
                    if (!player.isCrouching()) {
                        sender.signals.add(getStartPos());
                        ServerMessagePacket.send(player, EMessage.MESSAGE_SIGNAL_CONNECTED);
                    } else {
                        // Remove signalmode from signal
                        SignalContainer<?> signalContainer = SignalContainer.of(getStartWorld(), getStartPos());
                        signalContainer.removeSenderModesFrom(pos);
                        signalContainer.updateSignalModes();
                        // Remove signal from sender
                        sender.signals.remove(getStartPos());
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
