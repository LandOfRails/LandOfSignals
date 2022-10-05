package net.landofrails.landofsignals;

import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S1172", "java:S1068"})
public class LOSSignals {

    private LOSSignals() {

    }

    private static final List<BlockTypeEntity> blockSignalsList = new ArrayList<>();

    public static void registerSignal(final String author, final String name, final String path, final List<String> states, final Vec3d scaling, final Vec3d translation) {
        if (states == null) {
            //Adding stuff for common construct phase

        }
    }

    public static void register() {
        // It should be evaluated if this whole class is even needed.
    }

}
