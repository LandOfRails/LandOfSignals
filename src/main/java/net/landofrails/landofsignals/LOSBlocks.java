package net.landofrails.landofsignals;

import net.landofrails.landofsignals.blocks.*;
import net.landofrails.landofsignals.utils.Static;
import net.landofrails.landofsignals.utils.contentpacks.ContentPackAnimation;
import net.landofrails.landofsignals.utils.contentpacks.ContentPackSignalPart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LOSBlocks {

    private LOSBlocks() {

    }

    public static final BlockSignalSO12 BLOCK_SIGNAL_SO_12 = new BlockSignalSO12(LandOfSignals.MODID, "BlockSignalSO12");
    public static final BlockSignalLever BLOCK_SIGNAL_LEVER = new BlockSignalLever(LandOfSignals.MODID, "BlockSignalLever");
    public static final BlockTicketMachineDB BLOCK_TICKET_MACHINE_DB = new BlockTicketMachineDB(LandOfSignals.MODID, "BlockTicketMachineDB");
    public static final BlockTicketMachineSBB BLOCK_TICKET_MACHINE_SBB = new BlockTicketMachineSBB(LandOfSignals.MODID, "BlockTicketMachineSBB");
    public static final BlockSignalBox BLOCK_SIGNAL_BOX = new BlockSignalBox(LandOfSignals.MODID, "BlockSignalBox");

    //Signal
    public static final BlockSignalPart BLOCK_SIGNAL_PART = new BlockSignalPart(LandOfSignals.MODID, "blocksignalpart");
    public static final BlockSignalPartAnimated BLOCK_SIGNAL_PART_ANIMATED = new BlockSignalPartAnimated(LandOfSignals.MODID, "blocksignalpartanimated");

    public static void register() {
        // loads static classes and ctrs
        ContentPackSignalPart MISSING = new ContentPackSignalPart(Static.MISSING, "Missing! Check your content packs", "models/block/others/blocknotfound/blocknotfound.obj", new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {{
            add(null);
        }}, new HashMap<String, List<ContentPackAnimation>>() {{
            put(Static.MISSING, new ArrayList<ContentPackAnimation>() {{
                add(new ContentPackAnimation(new ArrayList<String>() {{
                    add(Static.MISSING);
                }}, new float[]{0f, 0f, 0f}, new float[]{0f, 0f, 0f}, Static.MISSING, 0f, 0f));
            }});
        }});
        BLOCK_SIGNAL_PART.add(MISSING);
        BLOCK_SIGNAL_PART_ANIMATED.add(MISSING);

        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_skymanluna_sperrsignal_sh0", "Sperrsignal Sh0", "models/block/landofsignals/skyman_luna/sperrsignal_sh0/sperrsignal_sh0.obj", new float[]{0.77f, -1.2f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {{
            add(null);
            add("white");
            add("off");
        }}));
        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_skymanluna_vorsignal_base", "Vorsignal Base", "models/block/landofsignals/skyman_luna/vr0_hv_vorsignal/boden/vorsignal_boden.obj", new float[]{0.77f, -1.2f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {{
            add(null);
        }}));
        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_skymanluna_mast", "Mast", "models/block/landofsignals/skyman_luna/mast/vorsignal_mast.obj", new float[]{0.77f, -1.2f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {{
            add(null);
        }}));
        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_skymanluna_vorsignal_kopf", "Vorsignal Kopf", "models/block/landofsignals/skyman_luna/vr0_hv_vorsignal/kopf/vorsignal_kopf.obj", new float[]{0.77f, -1.2f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {{
            add(null);
            add("green");
            add("greenyellow");
            add("off");
        }}));
        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_skymanluna_hauptsignal_base", "Hauptsignal Base", "models/block/landofsignals/skyman_luna/hv_hauptsignal/boden/hauptsignal_boden.obj", new float[]{0.77f, -0.4f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {{
            add(null);
        }}));
        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_skymanluna_hauptsignal_schild", "Hauptsignal Schild", "models/block/landofsignals/skyman_luna/hv_hauptsignal/schild/hauptsignal_schild.obj", new float[]{0.77f, -1.2f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {{
            add(null);
        }}));
        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_skymanluna_hauptsignal_kopf", "Hauptsignal Kopf", "models/block/landofsignals/skyman_luna/hv_hauptsignal/kopf/hauptsignal_kopf.obj", new float[]{0.77f, -0.4f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {{
            add(null);
            add("hp1");
            add("hp2");
            add("sh1");
            add("zs1");
            add("off");
        }}));

        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_gamertv_base", "Base", "models/block/landofsignals/gamertv/boden/hv_boden.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {{
            add(null);
        }}));
        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_gamertv_mast", "Mast", "models/block/landofsignals/gamertv/mast/hv_mast.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {{
            add(null);
        }}));
        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_gamertv_vorsignal", "Vorsignal", "models/block/landofsignals/gamertv/vorsignal/hv_vr.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {{
            add(null);
            add("gruen");
            add("gruenorange");
            add("off");
        }}));
        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_gamertv_hvhp", "HvHp", "models/block/landofsignals/gamertv/kopf/hv_hp.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {{
            add(null);
            add("hp1");
            add("hp2");
            add("sh1");
            add("off");
        }}));
        BLOCK_SIGNAL_PART.add(new ContentPackSignalPart("block_signal_part_gamertv_hversatz", "Hv Ersatz", "models/block/landofsignals/gamertv/ersatzsignal/hv_ersatzsignal.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {{
            add(null);
            add("an");
        }}));
    }
}
