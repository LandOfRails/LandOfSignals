package net.landofrails.landofsignals;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.blocks.*;
import net.landofrails.landofsignals.utils.Static;
import scala.Tuple5;
import scala.Tuple6;

import java.util.ArrayList;

public class LOSBlocks {

    private LOSBlocks() {

    }

    public static final BlockSignalSO12 BLOCK_SIGNAL_SO_12 = new BlockSignalSO12(LandOfSignals.MODID, "BlockSignalSO12");
    public static final BlockSignalLever BLOCK_SIGNAL_LEVER = new BlockSignalLever(LandOfSignals.MODID, "BlockSignalLever");
    public static final BlockTicketMachineDB BLOCK_TICKET_MACHINE_DB = new BlockTicketMachineDB(LandOfSignals.MODID, "BlockTicketMachineDB");
    public static final BlockTicketMachineSBB BLOCK_TICKET_MACHINE_SBB = new BlockTicketMachineSBB(LandOfSignals.MODID, "BlockTicketMachineSBB");
    public static final BlockSignalBox BLOCK_SIGNAL_BOX = new BlockSignalBox(LandOfSignals.MODID, "BlockSignalBox");

    //GroundBlocks
    public static final BlockGround BLOCK_GROUND_VORSIGNAL = new BlockGround(LandOfSignals.MODID, "BlockGroundVorsignal");
    public static final BlockGround BLOCK_GROUND_HAUPTSIGNAL = new BlockGround(LandOfSignals.MODID, "BlockGroundHauptsignal");
    public static final BlockGround BLOCK_GROUND_GAMERTV = new BlockGround(LandOfSignals.MODID, "BlockGroundGamerTV");

    //MidBlocks
    public static final BlockMid BLOCK_MID_VORSIGNAL_MAST = new BlockMid(LandOfSignals.MODID, "BlockMidVorsignalMast");
    public static final BlockMid BLOCK_MID_HAUPTSIGNAL_SCHILD = new BlockMid(LandOfSignals.MODID, "BlockMidHauptsignalSchild");
    public static final BlockMid BLOCK_MID_GAMERTV = new BlockMid(LandOfSignals.MODID, "BlockMidGamerTV");

    //TopBlocks
    public static final BlockTop BLOCK_TOP_VORSIGNAL_KOPF = new BlockTop(LandOfSignals.MODID, "BlockTopVorsignalKopf");
    public static final BlockTop BLOCK_TOP_HAUPTSIGNAL_KOPF = new BlockTop(LandOfSignals.MODID, "BlockTopHauptsignalKopf");
    public static final BlockTop BLOCK_TOP_GAMERTV_VORSIGNAL = new BlockTop(LandOfSignals.MODID, "BlockTopGamertvVorsignal");
    public static final BlockTop BLOCK_TOP_GAMERTV_HVHP = new BlockTop(LandOfSignals.MODID, "BlockTopGamertvHvhp");
    public static final BlockTop BLOCK_TOP_GAMERTV_HVERSATZ = new BlockTop(LandOfSignals.MODID, "BlockTopGamertvHversatz");

    public static void register() {
        // loads static classes and ctrs

        //Vec3d = scale, Vec3d = translation

        //Register in list GROUND
        Static.listGroundModels.put("BLOCK_GROUND_VORSIGNAL", new Tuple5<>(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/skyman_luna/vr0_hv_vorsignal/boden/vorsignal_boden.obj"), BLOCK_GROUND_VORSIGNAL, LOSItems.ITEM_GROUND_VORSIGNAL, new Vec3d(0.63, 0.63, 0.63), new Vec3d(0.77, -1.2, 0.77)));
        Static.listGroundModels.put("BLOCK_GROUND_HAUPTSIGNAL", new Tuple5<>(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/skyman_luna/hv_hauptsignal/boden/hauptsignal_boden.obj"), BLOCK_GROUND_HAUPTSIGNAL, LOSItems.ITEM_GROUND_HAUPTSIGNAL, new Vec3d(0.63, 0.63, 0.63), new Vec3d(0.77, -0.4, 0.77)));
        Static.listGroundModels.put("BLOCK_GROUND_GAMERTV", new Tuple5<>(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/gamertv/boden/hv_boden.obj"), BLOCK_GROUND_GAMERTV, LOSItems.ITEM_GROUND_GAMERTV, new Vec3d(1, 1, 1), new Vec3d(0.5, 0, 0.5)));

        //Register in list MID
        Static.listMidModels.put("BLOCK_MID_VORSIGNAL_MAST", new Tuple5<>(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/skyman_luna/mast/vorsignal_mast.obj"), BLOCK_MID_VORSIGNAL_MAST, LOSItems.ITEM_MID_VORSIGNAL_MAST, new Vec3d(0.63, 0.63, 0.63), new Vec3d(0.77, -1.2, 0.77)));
        Static.listMidModels.put("BLOCK_MID_HAUPTSIGNAL_SCHILD", new Tuple5<>(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/skyman_luna/hv_hauptsignal/schild/hauptsignal_schild.obj"), BLOCK_MID_HAUPTSIGNAL_SCHILD, LOSItems.ITEM_MID_HAUPTSIGNAL_SCHILD, new Vec3d(0.63, 0.63, 0.63), new Vec3d(0.77, -1.2, 0.77)));
        Static.listMidModels.put("BLOCK_MID_GAMERTV", new Tuple5<>(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/gamertv/mast/hv_mast.obj"), BLOCK_MID_GAMERTV, LOSItems.ITEM_MID_GAMERTV, new Vec3d(1, 1, 1), new Vec3d(0.5, 0, 0.5)));

        //Register in list TOP
        Static.listTopModels.put("BLOCK_TOP_VORSIGNAL_KOPF", new Tuple6<>(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/skyman_luna/vr0_hv_vorsignal/kopf/vorsignal_kopf.obj"), BLOCK_TOP_VORSIGNAL_KOPF, LOSItems.ITEM_TOP_VORSIGNAL_KOPF, new ArrayList<String>() {{
            add(null);
            add("green");
            add("greenyellow");
            add("off");
        }}, new Vec3d(0.63, 0.63, 0.63), new Vec3d(0.77, -1.2, 0.77)));
        Static.listTopModels.put("BLOCK_TOP_HAUPTSIGNAL_KOPF", new Tuple6<>(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/skyman_luna/hv_hauptsignal/kopf/hauptsignal_kopf.obj"), BLOCK_TOP_HAUPTSIGNAL_KOPF, LOSItems.ITEM_TOP_HAUPTSIGNAL_KOPF, new ArrayList<String>() {{
            add(null);
            add("hp1");
            add("hp2");
            add("sh1");
            add("zs1");
            add("off");
        }}, new Vec3d(0.63, 0.63, 0.63), new Vec3d(0.77, -0.4, 0.77)));
        Static.listTopModels.put("BLOCK_TOP_GAMERTV_VORSIGNAL", new Tuple6<>(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/gamertv/vorsignal/hv_vr.obj"), BLOCK_TOP_GAMERTV_VORSIGNAL, LOSItems.ITEM_TOP_GAMERTV_VORSIGNAL, new ArrayList<String>() {{
            add(null);
            add("gruen");
            add("gruenorange");
            add("off");
        }}, new Vec3d(1, 1, 1), new Vec3d(0.5, 0, 0.5)));
        Static.listTopModels.put("BLOCK_TOP_GAMERTV_HVHP", new Tuple6<>(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/gamertv/kopf/hv_hp.obj"), BLOCK_TOP_GAMERTV_HVHP, LOSItems.ITEM_TOP_GAMERTV_HVHP, new ArrayList<String>() {{
            add(null);
            add("hp1");
            add("hp2");
            add("sh1");
            add("off");
        }}, new Vec3d(1, 1, 1), new Vec3d(0.5, 0, 0.5)));
        Static.listTopModels.put("BLOCK_TOP_GAMERTV_HVERSATZ", new Tuple6<>(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/gamertv/ersatzsignal/hv_ersatzsignal.obj"), BLOCK_TOP_GAMERTV_HVERSATZ, LOSItems.ITEM_TOP_GAMERTV_HVERSATZ, new ArrayList<String>() {{
            add(null);
            add("an");
        }}, new Vec3d(1, 1, 1), new Vec3d(0.5, 0, 0.5)));
    }
}
