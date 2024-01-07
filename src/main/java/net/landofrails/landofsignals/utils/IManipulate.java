package net.landofrails.landofsignals.utils;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;

import java.util.HashSet;
import java.util.Set;

public interface IManipulate {

    void setOffset(Vec3d vec);

    Vec3d getOffset();

    void setRotation(int rotation);

    int getRotation();

    void setScaling(Vec3d scaling);

    Vec3d getScaling();

    // Util

    /**
     * Applies changes to the listed blocks.
     * Can be called from either server or client.
     * Won't sync.
     *
     * @param world Client or server world
     * @param blockPos Position of the core block
     * @param cascade Should the changes be cascaded down and upwards?
     * @param offset Amount of offset the block(s) should be set to
     * @param rotation Amount of rotation the block(s) should be set to
     * @param scaling Scaling the block(s) should be set to
     */
    static void applyChanges(World world, Vec3i blockPos, Boolean cascade, Vec3d offset, int rotation, Vec3d scaling){
        final Set<Vec3i> blockPositions = new HashSet<>();
        if (Boolean.TRUE.equals(cascade)) {
            //UP
            int i = 0;
            while (world.getBlockEntity(blockPos.up(i), BlockEntity.class) instanceof IManipulate) {
                blockPositions.add(blockPos.up(i));
                i++;
            }
            blockPositions.add(blockPos);
            //DOWN
            int j = 0;
            while (world.getBlockEntity(blockPos.down(j), BlockEntity.class) instanceof IManipulate) {
                blockPositions.add(blockPos.down(j));
                j++;
            }
        } else {
            blockPositions.add(blockPos);
        }

        for (final Vec3i bp : blockPositions) {
            final BlockEntity block = world.getBlockEntity(bp, BlockEntity.class);
            if (block instanceof IManipulate) {
                final IManipulate manipulate = (IManipulate) block;
                manipulate.setOffset(offset);
                manipulate.setRotation(rotation);
                manipulate.setScaling(scaling);
            }
        }
    }

}
