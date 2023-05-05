package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.LandOfSignals;

public class TileTicketMachineSBB extends BlockEntity {

    private static final String REPLACEMENT = "LandOfSignals:deco_fahrkartenautomat_sbb";

    @TagField("Rotation")
    private float blockRotate;

    public TileTicketMachineSBB(final float rot) {
        blockRotate = rot;
    }

    @Override
    public ItemStack onPick() {
        ItemStack replacementItem = new ItemStack(LOSItems.ITEM_DECO, 1);
        TagCompound tag = replacementItem.getTagCompound();
        tag.setString("itemId", REPLACEMENT);
        replacementItem.setTagCompound(tag);

        return replacementItem;
    }

    @Override
    public boolean onClick(final Player player, final Player.Hand hand, final Facing facing, final Vec3d hit) {
        replaceMe();
        return true;
    }

    @Override
    public void onNeighborChange(Vec3i neighbor) {
        replaceMe();
    }

    private void replaceMe() {
        if (getWorld().isServer) {
            LandOfSignals.info("Replaced old Ticket Machine SBB with Deco Ticket Machine SBB at %s", getPos().toString());
            LOSBlocks.BLOCK_DECO.setRot((int) blockRotate);
            LOSBlocks.BLOCK_DECO.setId(REPLACEMENT);
            getWorld().setBlock(getPos(), LOSBlocks.BLOCK_DECO);
        }
    }
}
