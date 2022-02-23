package net.landofrails.landofsignals;

import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.math.Vec3d;

import java.util.ArrayList;

public class LOSSignals {

    private LOSSignals() {

    }

    public static ArrayList<BlockTypeEntity> blockSignalsList = new ArrayList<>();

    public static void registerSignal(final String author, final String name, final String path, final ArrayList<String> states, final Vec3d scaling, final Vec3d translation) {
        if (states == null) {
            //Adding stuff for common construct phase

        }
    }

    public static void register() {

    }

}
