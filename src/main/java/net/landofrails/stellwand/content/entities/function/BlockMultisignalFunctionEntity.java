package net.landofrails.stellwand.content.entities.function;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.stellwand.content.entities.storage.BlockMultisignalStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;

public abstract class BlockMultisignalFunctionEntity extends BlockEntity {

    private BlockMultisignalStorageEntity entity;
    private boolean lastTryBreakByCreativePlayer = false;

    @SuppressWarnings("java:S112")
    protected BlockMultisignalFunctionEntity() {
        if (this instanceof BlockMultisignalStorageEntity)
            entity = (BlockMultisignalStorageEntity) this;
        else
            throw new RuntimeException(
                    "This should be a subclass of BlockMultisignalStorageEntity!");
    }

    @Override
    public ItemStack onPick() {
        ItemStack itemStack = new ItemStack(CustomItems.ITEMBLOCKMULTISIGNAL, 1);
        TagCompound tag = itemStack.getTagCompound();
        tag.setString("itemId", entity.getContentPackBlockId());
        itemStack.setTagCompound(tag);
        return itemStack;
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
