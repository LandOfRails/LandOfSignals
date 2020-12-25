package net.landofrails.landofsignals.utils;

import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.blocks.BlockGround;
import net.landofrails.landofsignals.blocks.BlockMid;
import net.landofrails.landofsignals.blocks.BlockTop;
import net.landofrails.landofsignals.items.ItemGround;
import net.landofrails.landofsignals.items.ItemMid;
import net.landofrails.landofsignals.items.ItemTop;
import scala.Tuple3;

import java.util.HashMap;
import java.util.Map;

public class Static {

    public static Map<String, Tuple3<Identifier, BlockGround, ItemGround>> listGroundModels = new HashMap<>();
    public static Map<String, Tuple3<Identifier, BlockMid, ItemMid>> listMidModels = new HashMap<>();
    public static Map<String, Tuple3<Identifier, BlockTop, ItemTop>> listTopModels = new HashMap<>();

}
