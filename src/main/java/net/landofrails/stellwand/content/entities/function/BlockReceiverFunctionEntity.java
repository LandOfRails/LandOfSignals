package net.landofrails.stellwand.content.entities.function;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.stellwand.content.entities.storage.BlockReceiverStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;

public class BlockReceiverFunctionEntity extends BlockEntity {

    private BlockReceiverStorageEntity entity;

    @SuppressWarnings("java:S112")
    protected BlockReceiverFunctionEntity() {
        if (this instanceof BlockReceiverStorageEntity)
            entity = (BlockReceiverStorageEntity) this;
        else
            throw new RuntimeException("This should be a subclass of BlockReceiverStorageEntity!");
    }

    @Override
    public ItemStack onPick() {
        ItemStack is = new ItemStack(CustomItems.ITEMBLOCKRECEIVER, 1);
        TagCompound tag = is.getTagCompound();
        tag.setString("itemId", entity.getContentPackBlockId());
        is.setTagCompound(tag);
        return is;
    }

    @Override
    public void onBreak() {
        getWorld().dropItem(onPick(), getPos());
    }
}
