package net.landofrails.stellwand.content.entities.function;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.stellwand.content.entities.storage.BlockFillerStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;

public abstract class BlockFillerFunctionEntity extends BlockEntity {

    private BlockFillerStorageEntity entity;
    private boolean lastTryBreakByCreativePlayer = false;

    @SuppressWarnings("java:S112")
    protected BlockFillerFunctionEntity() {
        if (this instanceof BlockFillerStorageEntity)
            entity = (BlockFillerStorageEntity) this;
        else
            throw new RuntimeException("This should be a subclass of BlockFillerStorageEntity!");
    }

    @Override
    public ItemStack onPick() {
        ItemStack is = new ItemStack(CustomItems.ITEMBLOCKFILLER, 1);
        TagCompound tag = is.getTagCompound();
        tag.setString("itemId", entity.getContentPackBlockId());
        is.setTagCompound(tag);
        return is;
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
