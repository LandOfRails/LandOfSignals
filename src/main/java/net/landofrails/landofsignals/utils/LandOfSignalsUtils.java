package net.landofrails.landofsignals.utils;

import cam72cam.mod.entity.Player;
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

}
