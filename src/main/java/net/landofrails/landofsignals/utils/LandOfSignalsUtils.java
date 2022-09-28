package net.landofrails.landofsignals.utils;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;

import java.util.Optional;

public class LandOfSignalsUtils {
    private LandOfSignalsUtils() {
    }

    public static Optional<Vec3i> canPlaceBlock(World world, Vec3i pos, Facing facing, Player player) {
        Vec3i target = world.isReplaceable(pos) ? pos : pos.offset(facing);

        if (LandOfSignalsUtils.isStandingInBlock(player.getBlockPosition().subtract(target)))
            return Optional.empty();

        if (world.isAir(target) || world.isReplaceable(target)) {
            return Optional.of(target);
        }
        return Optional.empty();
    }

    private static boolean isStandingInBlock(Vec3i vec3i) {
        return vec3i.x == 0 && vec3i.z == 0 && (vec3i.y == 0 || vec3i.y == -1);
    }

    public static String getUniqueIdOfItemStack(ItemStack itemStack) {
        return itemStack.getTagCompound().getString("itemId");
    }

    public static boolean isVersionSupported(String lastVersion, String lastSupportedVersion) {

        String lastVersionClean = lastVersion.split("-")[0];
        String[] lastVersionMaMiPa = lastVersionClean.split("\\.");
        String lastVersionSuffix = lastVersion.split("-").length > 1 ? lastVersion.split("-", 2)[1] : null;

        String lastSupportedVersionClean = lastSupportedVersion.split("-")[0];
        String[] lastSupportedVersionMaMiPa = lastSupportedVersion.split("\\.");
        String lastSupportedVersionSuffix = lastSupportedVersion.split("-").length > 1 ? lastSupportedVersion.split("-", 2)[1] : null;

        if (lastSupportedVersionMaMiPa.length != 3 || lastVersionMaMiPa.length != 3) {
            throw new LandOfSignalsUtilsException(String.format("LastVersion (%s) or LastSupportedVersion (%s) should contain Major.Minor.Patch!", lastVersionClean, lastSupportedVersionClean));
        }

        // Check Major.Minor.Patch
        for (int index = 0; index < 3; index++) {
            if (!lastSupportedVersionMaMiPa[index].equalsIgnoreCase("*")) {
                int lastMaMiPa = Integer.parseInt(lastVersionMaMiPa[index]);
                int lastSupportedMaMiPa = Integer.parseInt(lastSupportedVersionMaMiPa[index]);

                if (lastMaMiPa < lastSupportedMaMiPa) {
                    return false;
                }
            }
        }

        // Check Suffix
        if (lastSupportedVersionSuffix != null) {
            return lastSupportedVersionSuffix.equalsIgnoreCase(lastVersionSuffix);
        }

        return true;
    }

    private static final class LandOfSignalsUtilsException extends RuntimeException {

        private static final long serialVersionUID = 3833210873552862773L;

        public LandOfSignalsUtilsException(String msg) {
            super(msg);
        }

    }

}
