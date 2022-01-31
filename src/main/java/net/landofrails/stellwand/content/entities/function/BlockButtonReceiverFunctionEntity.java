package net.landofrails.stellwand.content.entities.function;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.stellwand.content.entities.storage.BlockButtonReceiverStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;

public class BlockButtonReceiverFunctionEntity extends BlockEntity {

    private BlockButtonReceiverStorageEntity entity;

    @SuppressWarnings("java:S112")
    protected BlockButtonReceiverFunctionEntity() {
        if (this instanceof BlockButtonReceiverStorageEntity)
            entity = (BlockButtonReceiverStorageEntity) this;
        else
            throw new RuntimeException("This should be a subclass of BlockButtonReceiverStorageEntity!");
    }

    @Override
    public ItemStack onPick() {
        ItemStack is = new ItemStack(CustomItems.ITEMBLOCKBUTTONRECEIVER, 1);
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
