package net.landofrails.landofsignals;

import net.landofrails.landofsignals.items.*;

public class LOSItems {

    private LOSItems() {

    }

    public static final ItemSignalSO12 ITEM_SIGNALSO12 = new ItemSignalSO12(LandOfSignals.MODID, "ItemSignalSO12");
    public static final ItemSignalLever ITEM_SIGNAL_LEVER = new ItemSignalLever(LandOfSignals.MODID, "ItemSignalLever");
    public static final ItemTicketMachineDB ITEM_TICKET_MACHINE_DB = new ItemTicketMachineDB(LandOfSignals.MODID, "ItemTicketMachineDB");
    public static final ItemTicketMachineSBB ITEM_TICKET_MACHINE_SBB = new ItemTicketMachineSBB(LandOfSignals.MODID, "ItemTicketMachineSBB");
    public static final ItemSignalBox ITEM_SIGNAL_BOX = new ItemSignalBox(LandOfSignals.MODID, "ItemSignalBox");
    public static final ItemConnector ITEM_CONNECTOR = new ItemConnector(LandOfSignals.MODID, "ItemConnector");

    //GroundItems
    public static final ItemGround ITEM_GROUND_VORSIGNAL = new ItemGround(LandOfSignals.MODID, "ItemGroundVorsignal", "BLOCK_GROUND_VORSIGNAL");
    public static final ItemGround ITEM_GROUND_GAMERTV = new ItemGround(LandOfSignals.MODID, "ItemGroundGamerTV", "BLOCK_GROUND_GAMERTV");

    //MidItems
    public static final ItemMid ITEM_MID_VORSIGNAL_MAST = new ItemMid(LandOfSignals.MODID, "ItemMidVorsignalMast", "BLOCK_MID_VORSIGNAL_MAST");
    public static final ItemMid ITEM_MID_GAMERTV = new ItemMid(LandOfSignals.MODID, "ItemMidGamerTV", "BLOCK_MID_GAMERTV");

    //TopItems
    public static final ItemTop ITEM_TOP_GAMERTV_HVHP = new ItemTop(LandOfSignals.MODID, "ItemTopGamertvHvhp", "BLOCK_TOP_GAMERTV_HVHP");
    public static final ItemTop ITEM_TOP_VORSIGNAL_KOPF = new ItemTop(LandOfSignals.MODID, "ItemTopVorsignalKopf", "BLOCK_TOP_VORSIGNAL_KOPF");
    public static final ItemTop ITEM_TOP_GAMERTV_VORSIGNAL = new ItemTop(LandOfSignals.MODID, "ItemTopGamertvVorsignal", "BLOCK_TOP_GAMERTV_VORSIGNAL");
    public static final ItemTop ITEM_TOP_GAMERTV_HVERSATZ = new ItemTop(LandOfSignals.MODID, "ItemTopGamertvHversatz", "BLOCK_TOP_GAMERTV_HVERSATZ");

    public static void register() {
        // loads static classes and ctrs
    }
}
