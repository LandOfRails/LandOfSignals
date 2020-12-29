package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.IRedstoneProvider;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.utils.Static;

import java.util.UUID;

public class TileSignalBox extends BlockEntity implements IRedstoneProvider {

    @TagField("UuidTileTop")
    private UUID UUIDTileTop;

    @Override
    public ItemStack onPick() {
        return new ItemStack(LOSItems.ITEM_SIGNAL_BOX, 1);
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.BLOCK;
    }

    @Override
    public boolean onClick(Player player, Player.Hand hand, Facing facing, Vec3d hit) {
        LOSGuis.SIGNAL_BOX.open(player);
        return true;
    }

    @Override
    public int getStrongPower(Facing from) {
        return 0;
    }

    @Override
    public int getWeakPower(Facing from) {
        if (UUIDTileTop != null) {
            System.out.println(UUIDTileTop);
            TileTop entity = Static.listTopBlocks.get(UUIDTileTop);
            entity.setTexturePath(Static.listTopModels.get(entity.getBlock())._4().get("GREEN"));
        }
        return 0;
    }

    public void setUUID(UUID uuid) {
        this.UUIDTileTop = uuid;
    }
}
