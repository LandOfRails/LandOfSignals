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

    public static void register() {
        // loads static classes and ctrs
    }
}
