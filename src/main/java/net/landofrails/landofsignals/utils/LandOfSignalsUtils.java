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

    public static boolean isVersionSupported(final String lastVersion, final String lastSupportedVersion) {

        final String lastVersionClean = lastVersion.split("-")[0];
        final String[] lastVersionMaMiPa = lastVersionClean.split("\\.");
        final String lastVersionSuffix = lastVersion.split("-").length > 1 ? lastVersion.split("-", 2)[1] : null;

        final String lastSupportedVersionClean = lastSupportedVersion.split("-")[0];
        final String[] lastSupportedVersionMaMiPa = lastSupportedVersion.split("\\.");
        final String lastSupportedVersionSuffix = lastSupportedVersion.split("-").length > 1 ? lastSupportedVersion.split("-", 2)[1] : null;

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
