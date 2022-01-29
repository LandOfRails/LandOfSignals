package net.landofrails.api;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.utils.compact.SignalContainer;

import javax.annotation.Nullable;
import javax.vecmath.Vector3d;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("unused")
/*
 * @Author Danielxs01
 * @since 0.0.4
 *
 * Official LandOfSignalsAPI
 */
public class LandOfSignalsAPI {

    private static final Random RANDOM = new Random();

    private LandOfSignalsAPI() {

    }

    static class LandOfSignals {

        /**
         * Returns true if block at given position is of type
         *
         * @param mcWorld The Minecraft world
         * @param pos     The Minecraft position
         * @return true if landofsignal-block exists
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

        /**
         * Returns true if world and position point to a (multi-)signal block.
         *
         * @param mcWorld The Minecraft world
         * @param pos     The Minecraft position
         * @return true if stellwand-signal exist
         */
        public static boolean isSignal(net.minecraft.world.World mcWorld, Vector3d pos) {
            World world = World.get(mcWorld);
            Vec3i position = new Vec3i(pos.x, pos.y, pos.z);
            return SignalContainer.isSignal(world, position);
        }

        /**
         * Returns true if world and position point to a sender block.
         *
         * @param mcWorld The Minecraft world
         * @param pos     The Minecraft position
         * @return true if the sender exists
         */
        public static boolean isSender(net.minecraft.world.World mcWorld, Vector3d pos) {
            World world = World.get(mcWorld);
            Vec3i position = new Vec3i(pos.x, pos.y, pos.z);
            return world.isBlock(position, CustomBlocks.BLOCKSENDER);
        }

        /**
         * <pre>
         * If the world and position points to a signal, one of the two values will be returned:
         * 1) Signal
         * Map<[null], Map<[signalmodename], [signalmodeid]>>
         * null = Default group for simple signal blocks
         * signalmodename = Name of the signalmode thats being displayed in the GUI
         * signalmodeid = Id of the signalmode which is unique for the whole modes contained in the map.
         *
         * 2) Multisignal
         * Map<[groupname], Map<[signalmodename], [signalmodeid]>>
         * groupname = Groupname that normally corresponds to a single signal on the multiblock
         * signalmodename = Name of the signalmode thats being displayed in the GUI
         * signalmodeid = Id of the signalmode which is unique for the whole modes contained in the map.
         * </pre>
         *
         * @param mcWorld The Minecraft World
         * @param pos     The Minecraft Position
         * @return Map with all possible modes
         */
        public static Map<String, Map<String, String>> getPossibleStates(net.minecraft.world.World mcWorld, Vector3d pos) {
            World world = World.get(mcWorld);
            Vec3i position = new Vec3i(pos.x, pos.y, pos.z);
            if (SignalContainer.isSignal(world, position)) {
                SignalContainer<?> signalContainer = SignalContainer.of(world, position);
                return Collections.unmodifiableMap(signalContainer.getPossibleModes());
            } else {
                return Collections.emptyMap();
            }
        }

        public void setMarked(net.minecraft.world.World mcWorld, Vector3d pos, boolean marking) {

            float r = (float) (RANDOM.nextFloat() / 2f + 0.5);
            float g = (float) (RANDOM.nextFloat() / 2f + 0.5);
            float b = (float) (RANDOM.nextFloat() / 2f + 0.5);

            setMarked(mcWorld, pos, marking, new float[]{r, g, b});
        }

        public void setMarked(net.minecraft.world.World mcWorld, Vector3d pos, boolean marking, float[] color) {
            World world = World.get(mcWorld);
            Vec3i position = new Vec3i(pos.x, pos.y, pos.z);
            if (SignalContainer.isSignal(world, position)) {
                SignalContainer.of(world, position).setMarked(marking, color);
            }
        }

        public boolean hasState(net.minecraft.world.World mcWorld, Vector3d signalPos, Vector3d virtualSenderPos) {
            World world = World.get(mcWorld);
            Vec3i position = new Vec3i(signalPos.x, signalPos.y, signalPos.z);
            Vec3i senderPosition = new Vec3i(virtualSenderPos.x, virtualSenderPos.y, virtualSenderPos.z);
            if (SignalContainer.isSignal(world, position)) {
                return SignalContainer.of(world, position).hasSenderModesFrom(senderPosition);
            }
            return false;
        }

        public boolean addState(net.minecraft.world.World mcWorld, Vector3d signalPos, Vector3d virtualSenderPos, String signalmodename, String signalmodeid) {
            World world = World.get(mcWorld);
            Vec3i position = new Vec3i(signalPos.x, signalPos.y, signalPos.z);
            Vec3i senderPosition = new Vec3i(virtualSenderPos.x, virtualSenderPos.y, virtualSenderPos.z);
            if (SignalContainer.isSignal(world, position)) {
                SignalContainer<?> signalContainer = SignalContainer.of(world, position);
                signalContainer.addSenderModesFrom(senderPosition, signalmodename, signalmodeid);
                signalContainer.updateSignalModes();
                return true;
            }

            return false;
        }

        public boolean removeStates(net.minecraft.world.World mcWorld, Vector3d signalPos, Vector3d virtualSenderPos) {
            World world = World.get(mcWorld);
            Vec3i position = new Vec3i(signalPos.x, signalPos.y, signalPos.z);
            Vec3i senderPosition = new Vec3i(virtualSenderPos.x, virtualSenderPos.y, virtualSenderPos.z);
            if (SignalContainer.isSignal(world, position)) {
                SignalContainer<?> signalContainer = SignalContainer.of(world, position);
                signalContainer.removeSenderModesFrom(senderPosition);
                signalContainer.updateSignalModes();
                return true;
            }

            return false;
        }

    }

}
