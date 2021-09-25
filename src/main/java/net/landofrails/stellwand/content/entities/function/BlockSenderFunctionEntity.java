package net.landofrails.stellwand.content.entities.function;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.entities.storage.BlockMultisignalStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.messages.EMessage;
import net.landofrails.stellwand.content.network.ChangeSignalModes;
import net.landofrails.stellwand.content.network.OpenSenderGui;
import net.landofrails.stellwand.content.network.ServerMessagePacket;
import net.landofrails.stellwand.utils.compact.SignalContainer;

public abstract class BlockSenderFunctionEntity extends BlockEntity {

    private BlockSenderStorageEntity entity;

    // Ein Kommentar damit der Client startet lol
    @SuppressWarnings("java:S112")
    protected BlockSenderFunctionEntity() {
        if (this instanceof BlockSenderStorageEntity)
            entity = (BlockSenderStorageEntity) this;
        else
            throw new RuntimeException(
                    "This should be a subclass of BlockSenderStorageEntity!");

    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.BLOCK;
    }

    @Override
    public ItemStack onPick() {
        ItemStack is = new ItemStack(CustomItems.ITEMBLOCKSENDER, 1);
        TagCompound tag = is.getTagCompound();
        tag.setString("itemId", entity.getContentPackBlockId());
        is.setTagCompound(tag);
        return is;
    }

    @Override
    public boolean onClick(Player player, Hand hand, Facing facing, Vec3d hit) {
        ItemStack item = player.getHeldItem(hand);

        ItemStack heldItem = player.getHeldItem(hand);
        if (heldItem != null && heldItem.is(CustomItems.ITEMMAGNIFYINGGLASS))
            return false;

        if (isAir(item) && getWorld().isServer) {
            if (!entity.signals.isEmpty()) {

                Vec3i signalPos = entity.signals.get(0);
                getWorld().keepLoaded(signalPos);
                if (getWorld().hasBlockEntity(signalPos, BlockSignalStorageEntity.class)) {
                    BlockSignalStorageEntity signalEntity = getWorld().getBlockEntity(signalPos, BlockSignalStorageEntity.class);
                    OpenSenderGui packet = new OpenSenderGui(getPos(), SignalContainer.of(signalEntity));
                    packet.sendToPlayer(player);
                } else if (getWorld().hasBlockEntity(signalPos, BlockMultisignalStorageEntity.class)) {
                    BlockMultisignalStorageEntity signalEntity = getWorld().getBlockEntity(signalPos, BlockMultisignalStorageEntity.class);
                    OpenSenderGui packet = new OpenSenderGui(getPos(), SignalContainer.of(signalEntity));
                    packet.sendToPlayer(player);
                } else {
                    entity.signals.remove(signalPos);
                    if (!getWorld().isClient)
                        ServerMessagePacket.send(player, EMessage.MESSAGE_NO_SIGNAL_FOUND, EMessage.MESSAGE_ERROR1.getRaw());
                }

            } else {
                if (!getWorld().isClient)
                    ServerMessagePacket.send(player, EMessage.MESSAGE_NO_SIGNALS_CONNECTED);
            }

            return true;
        }

        return false;
    }

    @Override
    public void onNeighborChange(Vec3i neighbor) {
        // Info: Only called on server-side

        boolean power = getWorld().getRedstone(getPos()) > 0;
        if (entity.hasPower != power) {
            entity.hasPower = power;

            updateSignals();

        }
    }

    public void updateSignals() {
        for (Vec3i signalPos : entity.signals) {
            getWorld().keepLoaded(signalPos);

            if (SignalContainer.isSignal(getWorld(), signalPos)) {
                SignalContainer<?> signalContainer = SignalContainer.of(getWorld(), signalPos);

                if ((entity.modePowerOn != null && !entity.modePowerOn.isEmpty()) && (entity.modePowerOff != null && !entity.modePowerOff.isEmpty())) {

                    signalContainer.addSenderModesFrom(getPos(), entity.signalGroup, entity.hasPower ? entity.modePowerOn : entity.modePowerOff);
                    signalContainer.updateSignalModes();
                    if (getWorld().isServer) {
                        ChangeSignalModes packet = new ChangeSignalModes(signalPos, signalContainer.getSenderModes());
                        packet.sendToAll();
                    }
                }

            } else if (getWorld().isBlockLoaded(signalPos)) {
                entity.signals.remove(signalPos);
            }
        }
    }

    @Override
    public void onBreak() {

        for (Vec3i signal : entity.signals) {
            getWorld().keepLoaded(signal);
            SignalContainer<?> signalContainer = null;
            if (getWorld().isBlock(signal, CustomBlocks.BLOCKSIGNAL)) {
                BlockSignalStorageEntity s = getWorld().getBlockEntity(signal, BlockSignalStorageEntity.class);
                signalContainer = SignalContainer.of(s);
            } else if (getWorld().isBlock(signal, CustomBlocks.BLOCKMULTISIGNAL)) {
                BlockMultisignalStorageEntity s = getWorld().getBlockEntity(signal, BlockMultisignalStorageEntity.class);
                signalContainer = SignalContainer.of(s);
            }

            if (signalContainer != null && signalContainer.hasSenderModesFrom(getPos())) {
                signalContainer.removeSenderModesFrom(getPos());

                signalContainer.updateSignalModes();
                if (getWorld().isServer) {
                    ChangeSignalModes packet = new ChangeSignalModes(signal, signalContainer.getSenderModes());
                    packet.sendToAll();
                }
            }
        }

        getWorld().dropItem(onPick(), getPos());
    }

    private boolean isAir(ItemStack item) {
        return item.is(ItemStack.EMPTY) || item.equals(ItemStack.EMPTY);
    }

}
