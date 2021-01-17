package net.landofrails.landofsignals;

import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.math.Vec3d;

import java.util.ArrayList;

public class LOSSignals {

    private LOSSignals() {

    }

    public static ArrayList<BlockTypeEntity> blockSignalsList = new ArrayList<>();

    public static void registerSignal(String author, String name, String path, ArrayList<String> states, Vec3d scaling, Vec3d translation) {
        if (states == null) {
            //Adding stuff for common construct phase

        }
    }

    public static void register() {

    }

}
