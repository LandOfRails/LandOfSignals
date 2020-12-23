package net.landofrails.landofsignals;

import net.landofrails.landofsignals.blocks.BlockSignalLever;
import net.landofrails.landofsignals.blocks.BlockSignalSO12;
import net.landofrails.landofsignals.blocks.BlockVr0_Hv_Vorsignal;

public class LOSBlocks {
    public static final BlockSignalSO12 BLOCK_SIGNAL_SO_12 = new BlockSignalSO12(LandOfSignals.MODID, "BlockSignalSO12");
    public static final BlockSignalLever BLOCK_SIGNAL_LEVER = new BlockSignalLever(LandOfSignals.MODID, "BlockSignalLever");
    public static final BlockVr0_Hv_Vorsignal BLOCK_VR_0_HV_VORSIGNAL = new BlockVr0_Hv_Vorsignal(LandOfSignals.MODID, "BlockVr0_Hv_Vorsignal");

    public static void register() {
        // loads static classes and ctrs
    }
}
