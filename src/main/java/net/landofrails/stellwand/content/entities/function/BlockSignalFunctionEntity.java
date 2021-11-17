package net.landofrails.stellwand.content.entities.function;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.util.Facing;
import net.landofrails.stellwand.config.StellwandConfig;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;

public abstract class BlockSignalFunctionEntity extends BlockEntity {

    private static final String SIGNALKEY = "signalPos";
    private BlockSignalStorageEntity entity;

    @SuppressWarnings("java:S112")
    protected BlockSignalFunctionEntity() {
        if (this instanceof BlockSignalStorageEntity)
            entity = (BlockSignalStorageEntity) this;
        else
            throw new RuntimeException(
                    "This should be a subclass of BlockSignalStorageEntity!");
    }

    @Override
    public ItemStack onPick() {
        ItemStack is = new ItemStack(CustomItems.ITEMBLOCKSIGNAL, 1);
        TagCompound tag = is.getTagCompound();
        tag.setString("itemId", entity.getContentPackBlockId());
        is.setTagCompound(tag);
        return is;
    }

    @Override
    public boolean onClick(Player player, Hand hand, Facing facing, Vec3d hit) {
        if (StellwandConfig.Debugging.debugOutput) {
            player.sendMessage(PlayerMessage.direct("ContentBlockId: " + entity.contentPackBlockId));
            player.sendMessage(PlayerMessage.direct("DisplayMode: " + entity.displayMode));
        }
        return super.onClick(player, hand, facing, hit);
    }

    @Override
    public void onBreak() {
        getWorld().dropItem(onPick(), getPos());
    }


}
