package net.landofrails.landofsignals.utils;

import cam72cam.mod.math.Vec3i;
import net.landofrails.landofsignals.blocks.BlockSignalPart;
import net.landofrails.landofsignals.items.ItemSignalPart;

import java.util.*;

public class Static {

    private Static() {

    }

    public static Map<String, BlockSignalPart> blockSignalPartList = new HashMap<>();
    public static List<ItemSignalPart> itemSignalPartList = new ArrayList<>();
    public static Map<UUID, Vec3i> changingSignalPartList = new HashMap<>();

}
