package net.landofrails.landofsignals;

import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.blocks.BlockGround;
import net.landofrails.landofsignals.blocks.BlockSignalLever;
import net.landofrails.landofsignals.blocks.BlockSignalSO12;
import net.landofrails.landofsignals.blocks.BlockVr0_Hv_Vorsignal;
import net.landofrails.landofsignals.utils.Static;

public class LOSBlocks {
    public static final BlockSignalSO12 BLOCK_SIGNAL_SO_12 = new BlockSignalSO12(LandOfSignals.MODID, "BlockSignalSO12");
    public static final BlockSignalLever BLOCK_SIGNAL_LEVER = new BlockSignalLever(LandOfSignals.MODID, "BlockSignalLever");
    public static final BlockVr0_Hv_Vorsignal BLOCK_VR_0_HV_VORSIGNAL = new BlockVr0_Hv_Vorsignal(LandOfSignals.MODID, "BlockVr0_Hv_Vorsignal");

    //GroundBlocks
    public static final BlockGround BLOCK_GROUND_VORSIGNAL = new BlockGround(LandOfSignals.MODID, "BlockGroundVorsignal");
    public static final BlockGround BLOCK_GROUND_VORSIGNAL_MAST = new BlockGround(LandOfSignals.MODID, "BlockGroundVorsignalMast");
    public static final BlockGround BLOCK_GROUND_VORSIGNAL_KOPF = new BlockGround(LandOfSignals.MODID, "BlockGroundVorsignalKopf");

    //MidBlocks

    //TopBlocks

    public static void register() {
        // loads static classes and ctrs

        //Register in list
        Static.listGroundModels.put(BLOCK_GROUND_VORSIGNAL, new Identifier(LandOfSignals.MODID, "models/block/vr0_hv_vorsignal/boden/vorsignal_boden.obj"));
        Static.listGroundModels.put(BLOCK_GROUND_VORSIGNAL_MAST, new Identifier(LandOfSignals.MODID, "models/block/vr0_hv_vorsignal/mast/vorsignal_mast.obj"));
        Static.listGroundModels.put(BLOCK_GROUND_VORSIGNAL_KOPF, new Identifier(LandOfSignals.MODID, "models/block/vr0_hv_vorsignal/kopf/vorsignal_kopf.obj"));
    }
}
