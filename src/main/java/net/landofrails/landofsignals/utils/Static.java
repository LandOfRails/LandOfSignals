package net.landofrails.landofsignals.utils;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.blocks.BlockGround;
import net.landofrails.landofsignals.blocks.BlockMid;
import net.landofrails.landofsignals.blocks.BlockTop;
import net.landofrails.landofsignals.items.ItemGround;
import net.landofrails.landofsignals.items.ItemMid;
import net.landofrails.landofsignals.items.ItemTop;
import scala.Tuple5;
import scala.Tuple6;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Static {

    //Vec3d = scale, Vec3d = translation
    public static Map<String, Tuple5<Identifier, BlockGround, ItemGround, Vec3d, Vec3d>> listGroundModels = new HashMap<>();
    public static Map<String, Tuple5<Identifier, BlockMid, ItemMid, Vec3d, Vec3d>> listMidModels = new HashMap<>();
    public static Map<String, Tuple6<Identifier, BlockTop, ItemTop, List<String>, Vec3d, Vec3d>> listTopModels = new HashMap<>();
    public static Map<UUID, Vec3i> listTopBlocks = new HashMap<>();

}
