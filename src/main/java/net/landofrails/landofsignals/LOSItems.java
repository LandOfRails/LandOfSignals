package net.landofrails.landofsignals;

import net.landofrails.landofsignals.items.ItemSignalLever;
import net.landofrails.landofsignals.items.ItemSignalSO12;
import net.landofrails.landofsignals.items.ItemVr0_Hv_Vorsignal;

public class LOSItems {
    public static final ItemSignalSO12 ITEM_SIGNALSO12 = new ItemSignalSO12(LandOfSignals.MODID, "ItemSignalSO12");
    public static final ItemSignalLever ITEM_SIGNAL_LEVER = new ItemSignalLever(LandOfSignals.MODID, "ItemSignalLever");
    public static final ItemVr0_Hv_Vorsignal ITEM_VR_0_HV_VORSIGNAL = new ItemVr0_Hv_Vorsignal(LandOfSignals.MODID, "ItemVr0_Hv_Vorsignal");

    public static void register() {
        // loads static classes and ctrs
    }
}
