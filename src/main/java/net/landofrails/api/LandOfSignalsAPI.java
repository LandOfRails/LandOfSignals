package net.landofrails.api;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.tile.TileSignalPart;

import javax.annotation.Nullable;
import javax.vecmath.Vector3d;
import java.util.List;

public class LandOfSignalsAPI {

    private LandOfSignalsAPI() {

    }

    static class LandOfSignals {

        /**
         * Returns true if block at given position is of type
         *
         * @param mcWorld
         * @param pos
         * @return
         */
        public boolean isSignalblock(net.minecraft.world.World mcWorld,
                                     Vector3d pos) {

            World world = World.get(mcWorld);
            Vec3i position = new Vec3i(pos.x, pos.y, pos.z);

            TileSignalPart tileSignal = world.getBlockEntity(position,
                    TileSignalPart.class);

            return tileSignal != null;
        }

        @Nullable
        public List<String> getStates(net.minecraft.world.World mcWorld,
                                      Vector3d pos) {
            World world = World.get(mcWorld);
            Vec3i position = new Vec3i(pos.x, pos.y, pos.z);

            TileSignalPart tileSignal = world.getBlockEntity(position,
                    TileSignalPart.class);

            if (tileSignal != null) {
                return LOSBlocks.BLOCK_SIGNAL_PART.getStates(tileSignal.getId());
            }

            return null;
        }

        @Nullable
        public String getState(net.minecraft.world.World mcWorld,
                               Vector3d pos) {
            World world = World.get(mcWorld);
            Vec3i position = new Vec3i(pos.x, pos.y, pos.z);

            TileSignalPart tileSignal = world.getBlockEntity(position,
                    TileSignalPart.class);

            if (tileSignal != null) {
                return tileSignal.getTexturePath();
            }

            return null;
        }

        public void setState(net.minecraft.world.World mcWorld, Vector3d pos,
                             @Nullable String state) {
            World world = World.get(mcWorld);
            Vec3i position = new Vec3i(pos.x, pos.y, pos.z);

            TileSignalPart tileSignal = world.getBlockEntity(position,
                    TileSignalPart.class);

            if (tileSignal != null) {
                tileSignal.setTexturePath(state);
            }
        }

    }

    static class Stellwand {

        public void notImplementedYet() {
            // FIXME Implement before next release.
        }

    }

}
