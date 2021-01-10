package net.landofrails.landofsignals;

import net.landofrails.landofsignals.items.*;

public class LOSItems {

    private LOSItems() {

    }

    public static final ItemSignalSO12 ITEM_SIGNALSO12 = new ItemSignalSO12(LandOfSignals.MODID, "item_signal_so12");
    public static final ItemSignalLever ITEM_SIGNAL_LEVER = new ItemSignalLever(LandOfSignals.MODID, "item_signal_lever");
    public static final ItemTicketMachineDB ITEM_TICKET_MACHINE_DB = new ItemTicketMachineDB(LandOfSignals.MODID, "item_ticket_machine_db");
    public static final ItemTicketMachineSBB ITEM_TICKET_MACHINE_SBB = new ItemTicketMachineSBB(LandOfSignals.MODID, "item_ticket_machine_sbb");
    public static final ItemSignalBox ITEM_SIGNAL_BOX = new ItemSignalBox(LandOfSignals.MODID, "item_signal_box");
    public static final ItemConnector ITEM_CONNECTOR = new ItemConnector(LandOfSignals.MODID, "item_connector");

    //GroundItems
    public static final ItemGround ITEM_GROUND_VORSIGNAL = new ItemGround(LandOfSignals.MODID, "item_ground_vorsignal", "BLOCK_GROUND_VORSIGNAL");
    public static final ItemGround ITEM_GROUND_HAUPTSIGNAL = new ItemGround(LandOfSignals.MODID, "item_ground_hauptsignal", "BLOCK_GROUND_HAUPTSIGNAL");
    public static final ItemGround ITEM_GROUND_GAMERTV = new ItemGround(LandOfSignals.MODID, "item_ground_gamertv", "BLOCK_GROUND_GAMERTV");

    //MidItems
    public static final ItemMid ITEM_MID_VORSIGNAL_MAST = new ItemMid(LandOfSignals.MODID, "item_mid_vorsignal_mast", "BLOCK_MID_VORSIGNAL_MAST");
    public static final ItemMid ITEM_MID_HAUPTSIGNAL_SCHILD = new ItemMid(LandOfSignals.MODID, "item_mid_hauptsignal_schild", "BLOCK_MID_HAUPTSIGNAL_SCHILD");
    public static final ItemMid ITEM_MID_GAMERTV = new ItemMid(LandOfSignals.MODID, "item_mid_gamertv", "BLOCK_MID_GAMERTV");

    //TopItems
    public static final ItemTop ITEM_TOP_VORSIGNAL_KOPF = new ItemTop(LandOfSignals.MODID, "item_top_vorsignal_kopf", "BLOCK_TOP_VORSIGNAL_KOPF");
    public static final ItemTop ITEM_TOP_HAUPTSIGNAL_KOPF = new ItemTop(LandOfSignals.MODID, "item_top_hauptsignal_kopf", "BLOCK_TOP_HAUPTSIGNAL_KOPF");
    public static final ItemTop ITEM_TOP_GAMERTV_HVHP = new ItemTop(LandOfSignals.MODID, "item_top_gamertv_hvhp", "BLOCK_TOP_GAMERTV_HVHP");
    public static final ItemTop ITEM_TOP_GAMERTV_VORSIGNAL = new ItemTop(LandOfSignals.MODID, "item_top_gamertv_vorsignal", "BLOCK_TOP_GAMERTV_VORSIGNAL");
    public static final ItemTop ITEM_TOP_GAMERTV_HVERSATZ = new ItemTop(LandOfSignals.MODID, "item_top_gamertv_hversatz", "BLOCK_TOP_GAMERTV_HVERSATZ");

    public static void register() {
        // loads static classes and ctrs
    }
}
