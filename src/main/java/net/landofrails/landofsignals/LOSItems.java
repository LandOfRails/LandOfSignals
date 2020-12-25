package net.landofrails.landofsignals;

import net.landofrails.landofsignals.items.*;

public class LOSItems {
    public static final ItemSignalSO12 ITEM_SIGNALSO12 = new ItemSignalSO12(LandOfSignals.MODID, "ItemSignalSO12");
    public static final ItemSignalLever ITEM_SIGNAL_LEVER = new ItemSignalLever(LandOfSignals.MODID, "ItemSignalLever");
    public static final ItemTicketMachineDB ITEM_TICKET_MACHINE_DB = new ItemTicketMachineDB(LandOfSignals.MODID, "ItemTicketMachineDB");
    public static final ItemTicketMachineSBB ITEM_TICKET_MACHINE_SBB = new ItemTicketMachineSBB(LandOfSignals.MODID, "ItemTicketMachineSBB");

    //GroundItems
    public static final ItemGround ITEM_GROUND_VORSIGNAL = new ItemGround(LandOfSignals.MODID, "ItemGroundVorsignal", "BLOCK_GROUND_VORSIGNAL");

    //MidItems
    public static final ItemMid ITEM_MID_VORSIGNAL_MAST = new ItemMid(LandOfSignals.MODID, "ItemMidVorsignalMast", "BLOCK_MID_VORSIGNAL_MAST");

    //TopItems
    public static final ItemTop ITEM_TOP_VORSIGNAL_KOPF = new ItemTop(LandOfSignals.MODID, "ItemTopVorsignalKopf", "BLOCK_TOP_VORSIGNAL_KOPF");

    public static void register() {
        // loads static classes and ctrs
    }
}
