package net.landofrails.landofsignals;

import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.blocks.*;
import net.landofrails.landofsignals.utils.Static;
import scala.Tuple3;

public class LOSBlocks {
    public static final BlockSignalSO12 BLOCK_SIGNAL_SO_12 = new BlockSignalSO12(LandOfSignals.MODID, "BlockSignalSO12");
    public static final BlockSignalLever BLOCK_SIGNAL_LEVER = new BlockSignalLever(LandOfSignals.MODID, "BlockSignalLever");
    public static final BlockTicketMachineDB BLOCK_TICKET_MACHINE_DB = new BlockTicketMachineDB(LandOfSignals.MODID, "BlockTicketMachineDB");

    //GroundBlocks
    public static final BlockGround BLOCK_GROUND_VORSIGNAL = new BlockGround(LandOfSignals.MODID, "BlockGroundVorsignal");

    //MidBlocks
    public static final BlockMid BLOCK_MID_VORSIGNAL_MAST = new BlockMid(LandOfSignals.MODID, "BlockMidVorsignalMast");

    //TopBlocks
    public static final BlockTop BLOCK_TOP_VORSIGNAL_KOPF = new BlockTop(LandOfSignals.MODID, "BlockTopVorsignalKopf");

    public static void register() {
        // loads static classes and ctrs

        //Register in list GROUND
        Static.listGroundModels.put("BLOCK_GROUND_VORSIGNAL", new Tuple3(new Identifier(LandOfSignals.MODID, "models/block/vr0_hv_vorsignal/boden/vorsignal_boden.obj"), BLOCK_GROUND_VORSIGNAL, LOSItems.ITEM_GROUND_VORSIGNAL));

        //Register in list MID
        Static.listMidModels.put("BLOCK_MID_VORSIGNAL_MAST", new Tuple3(new Identifier(LandOfSignals.MODID, "models/block/vr0_hv_vorsignal/mast/vorsignal_mast.obj"), BLOCK_MID_VORSIGNAL_MAST, LOSItems.ITEM_MID_VORSIGNAL_MAST));

        //Register in list TOP
        Static.listTopModels.put("BLOCK_TOP_VORSIGNAL_KOPF", new Tuple3(new Identifier(LandOfSignals.MODID, "models/block/vr0_hv_vorsignal/kopf/vorsignal_kopf.obj"), BLOCK_TOP_VORSIGNAL_KOPF, LOSItems.ITEM_TOP_VORSIGNAL_KOPF));
    }
}
