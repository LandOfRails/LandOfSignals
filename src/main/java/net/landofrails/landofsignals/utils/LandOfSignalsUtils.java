package net.landofrails.landofsignals.utils;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSItems;

import java.util.Optional;

public class LandOfSignalsUtils {

    private LandOfSignalsUtils() {
    }

    public static Optional<Vec3i> canPlaceBlock(final World world, final Vec3i pos, final Facing facing, final Player player) {
        final Vec3i target = world.isReplaceable(pos) ? pos : pos.offset(facing);

        if (isStandingInBlock(player.getBlockPosition().subtract(target)))
            return Optional.empty();

        if (world.isAir(target) || world.isReplaceable(target)) {
            return Optional.of(target);
        }
        return Optional.empty();
    }

    private static boolean isStandingInBlock(final Vec3i vec3i) {
        return vec3i.x == 0 && vec3i.z == 0 && (vec3i.y == 0 || vec3i.y == -1);
    }

    public static String getUniqueIdOfItemStack(final ItemStack itemStack) {
        return itemStack.getTagCompound().getString("itemId");
    }

    public static boolean isLandOfSignalsItem(final ItemStack itemStack){
        return itemStack.is(LOSItems.ITEM_CONNECTOR)
                || itemStack.is(LOSItems.ITEM_MANIPULATOR)
                || itemStack.is(LOSItems.ITEM_MAGNIFYING_GLASS)
                || itemStack.is(LOSItems.ITEM_COMPLEX_SIGNAL)
                || itemStack.is(LOSItems.ITEM_DECO)
                || itemStack.is(LOSItems.ITEM_SIGN_PART)
                || itemStack.is(LOSItems.ITEM_SIGNAL_BOX)
                || itemStack.is(LOSItems.ITEM_SIGNAL_LEVER)
                || itemStack.is(LOSItems.ITEM_SIGNAL_PART)
                || itemStack.is(LOSItems.ITEM_SIGNAL_PART_ANIMATED);
    }

    public static String objIdWithGroup(String blockId, String identifier, String groupId, String objPath){
        return String.format("%s/%s/%s/%s", blockId, identifier, groupId, objPath);
    }

    public static String objIdWithoutGroup(String blockId, String identifier, String objPath){
        return objIdWithGroup(blockId, identifier, "", objPath);
    }

}
