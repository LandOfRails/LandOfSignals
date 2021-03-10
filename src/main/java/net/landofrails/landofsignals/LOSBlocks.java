package net.landofrails.landofsignals;

import cam72cam.mod.math.Vec3d;
import net.landofrails.landofsignals.blocks.*;

import java.util.ArrayList;

public class LOSBlocks {

    private LOSBlocks() {

    }

    public static final BlockSignalSO12 BLOCK_SIGNAL_SO_12 = new BlockSignalSO12(LandOfSignals.MODID, "BlockSignalSO12");
    public static final BlockSignalLever BLOCK_SIGNAL_LEVER = new BlockSignalLever(LandOfSignals.MODID, "BlockSignalLever");
    public static final BlockTicketMachineDB BLOCK_TICKET_MACHINE_DB = new BlockTicketMachineDB(LandOfSignals.MODID, "BlockTicketMachineDB");
    public static final BlockTicketMachineSBB BLOCK_TICKET_MACHINE_SBB = new BlockTicketMachineSBB(LandOfSignals.MODID, "BlockTicketMachineSBB");
    public static final BlockSignalBox BLOCK_SIGNAL_BOX = new BlockSignalBox(LandOfSignals.MODID, "BlockSignalBox");

    //Signal
    public static final BlockSignalPart BLOCK_SIGNAL_PART_SKYMANLUNA_SPERRSIGNAL_SH0 = new BlockSignalPart("block_signal_part_skymanluna_sperrsignal_sh0", null, "models/block/landofsignals/skyman_luna/sperrsignal_sh0/sperrsignal_sh0.obj", new Vec3d(0.77, -1.2, 0.77), new Vec3d(0.5, -0.9, 0.5), new Vec3d(0.63, 0.63, 0.63), new ArrayList<String>() {{
        add(null);
        add("white");
        add("off");
    }});
    public static final BlockSignalPart BLOCK_SIGNAL_PART_SKYMANLUNA_VORSIGNAL_BASE = new BlockSignalPart("block_signal_part_skymanluna_vorsignal_base", null, "models/block/landofsignals/skyman_luna/vr0_hv_vorsignal/boden/vorsignal_boden.obj", new Vec3d(0.77, -1.2, 0.77), new Vec3d(0.5, -0.9, 0.5), new Vec3d(0.63, 0.63, 0.63));
    public static final BlockSignalPart BLOCK_SIGNAL_PART_SKYMANLUNA_MAST = new BlockSignalPart("block_signal_part_skymanluna_mast", null, "models/block/landofsignals/skyman_luna/mast/vorsignal_mast.obj", new Vec3d(0.77, -1.2, 0.77), new Vec3d(0.5, -0.9, 0.5), new Vec3d(0.63, 0.63, 0.63));
    public static final BlockSignalPart BLOCK_SIGNAL_PART_SKYMANLUNA_VORSIGNAL_KOPF = new BlockSignalPart("block_signal_part_skymanluna_vorsignal_kopf", null, "models/block/landofsignals/skyman_luna/vr0_hv_vorsignal/kopf/vorsignal_kopf.obj", new Vec3d(0.77, -1.2, 0.77), new Vec3d(0.5, -0.9, 0.5), new Vec3d(0.63, 0.63, 0.63), new ArrayList<String>() {{
        add(null);
        add("green");
        add("greenyellow");
        add("off");
    }});
    public static final BlockSignalPart BLOCK_SIGNAL_PART_SKYMANLUNA_HAUPTSIGNAL_BASE = new BlockSignalPart("block_signal_part_skymanluna_hauptsignal_base", null, "models/block/landofsignals/skyman_luna/hv_hauptsignal/boden/hauptsignal_boden.obj", new Vec3d(0.77, -0.4, 0.77), new Vec3d(0.5, -0.9, 0.5), new Vec3d(0.63, 0.63, 0.63));
    public static final BlockSignalPart BLOCK_SIGNAL_PART_SKYMANLUNA_HAUPTSIGNAL_SCHILD = new BlockSignalPart("block_signal_part_skymanluna_hauptsignal_schild", null, "models/block/landofsignals/skyman_luna/hv_hauptsignal/schild/hauptsignal_schild.obj", new Vec3d(0.77, -1.2, 0.77), new Vec3d(0.5, -0.9, 0.5), new Vec3d(0.63, 0.63, 0.63));
    public static final BlockSignalPart BLOCK_SIGNAL_PART_SKYMANLUNA_HAUPTSIGNAL_KOPF = new BlockSignalPart("block_signal_part_skymanluna_hauptsignal_kopf", null, "models/block/landofsignals/skyman_luna/hv_hauptsignal/kopf/hauptsignal_kopf.obj", new Vec3d(0.77, -0.4, 0.77), new Vec3d(0.5, -0.9, 0.5), new Vec3d(0.63, 0.63, 0.63), new ArrayList<String>() {{
        add(null);
        add("hp1");
        add("hp2");
        add("sh1");
        add("zs1");
        add("off");
    }});

    public static final BlockSignalPart BLOCK_SIGNAL_PART_GAMERTV_BASE = new BlockSignalPart("block_signal_part_gamertv_base", null, "models/block/landofsignals/gamertv/boden/hv_boden.obj", new Vec3d(0.5, 0, 0.5), new Vec3d(1, 1, 1));
    public static final BlockSignalPart BLOCK_SIGNAL_PART_GAMERTV_MAST = new BlockSignalPart("block_signal_part_gamertv_mast", null, "models/block/landofsignals/gamertv/mast/hv_mast.obj", new Vec3d(0.5, 0, 0.5), new Vec3d(1, 1, 1));
    public static final BlockSignalPart BLOCK_SIGNAL_PART_GAMERTV_VORSIGNAL = new BlockSignalPart("block_signal_part_gamertv_vorsignal", null, "models/block/landofsignals/gamertv/vorsignal/hv_vr.obj", new Vec3d(0.5, 0, 0.5), new Vec3d(1, 1, 1), new ArrayList<String>() {{
        add(null);
        add("gruen");
        add("gruenorange");
        add("off");
    }});
    public static final BlockSignalPart BLOCK_SIGNAL_PART_GAMERTV_HVHP = new BlockSignalPart("block_signal_part_gamertv_hvhp", null, "models/block/landofsignals/gamertv/kopf/hv_hp.obj", new Vec3d(0.5, 0, 0.5), new Vec3d(1, 1, 1), new ArrayList<String>() {{
        add(null);
        add("hp1");
        add("hp2");
        add("sh1");
        add("off");
    }});
    public static final BlockSignalPart BLOCK_SIGNAL_PART_GAMERTV_HVERSATZ = new BlockSignalPart("block_signal_part_gamertv_hversatz", null, "models/block/landofsignals/gamertv/ersatzsignal/hv_ersatzsignal.obj", new Vec3d(0.5, 0, 0.5), new Vec3d(1, 1, 1), new ArrayList<String>() {{
        add(null);
        add("an");
    }});

    public static void register() {
        // loads static classes and ctrs
    }
}
