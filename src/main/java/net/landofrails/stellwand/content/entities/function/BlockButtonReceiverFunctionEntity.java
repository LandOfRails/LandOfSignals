package net.landofrails.stellwand.content.entities.function;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import net.landofrails.stellwand.content.entities.storage.BlockButtonReceiverStorageEntity;

public class BlockButtonReceiverFunctionEntity extends BlockEntity {

    private BlockButtonReceiverStorageEntity entity;
    private boolean lastTryBreakByCreativePlayer = false;

    @SuppressWarnings("java:S112")
    protected BlockButtonReceiverFunctionEntity() {
        if (this instanceof BlockButtonReceiverStorageEntity)
            entity = (BlockButtonReceiverStorageEntity) this;
        else
            throw new RuntimeException("This should be a subclass of BlockButtonReceiverStorageEntity!");
    }

    @Override
    public ItemStack onPick() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean tryBreak(Player player) {
        lastTryBreakByCreativePlayer = player.isCreative();
        return true;
    }

    @Override
    public void onBreak() {
        if (!lastTryBreakByCreativePlayer) {
            getWorld().dropItem(onPick(), getPos());
        }
    }

}
