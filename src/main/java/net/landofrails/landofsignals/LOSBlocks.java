package net.landofrails.landofsignals;

import net.landofrails.api.contentpacks.v1.ContentPackSignObject;
import net.landofrails.api.contentpacks.v1.ContentPackSignPart;
import net.landofrails.api.contentpacks.v1.ContentPackSignalPart;
import net.landofrails.landofsignals.blocks.*;
import net.landofrails.landofsignals.contentpacks.ContentPackHandlerV1;
import net.landofrails.landofsignals.utils.Static;

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
    public static final BlockSignalPart BLOCK_SIGNAL_PART = new BlockSignalPart(LandOfSignals.MODID, "blocksignalpart");
    public static final BlockSignalPartAnimated BLOCK_SIGNAL_PART_ANIMATED = null; // new BlockSignalPartAnimated(LandOfSignals.MODID, "blocksignalpartanimated");

    // Sign
    public static final BlockSignPart BLOCK_SIGN_PART = new BlockSignPart(LandOfSignals.MODID, "blocksign");

    public static void register() {
        // loads static classes and ctrs
        ContentPackSignalPart MISSING_SIGNAL = new ContentPackSignalPart(Static.MISSING, "Missing! Check your content packs", "models/block/others/blocknotfound/blocknotfound.obj", new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {
            private static final long serialVersionUID = -1995088635629060337L;

            {
                add(null);
            }
        });

        registerSignalContentPack(MISSING_SIGNAL);
        // FIXME Remove if possible
        LOSBlocks.BLOCK_SIGNAL_PART.add_depr(MISSING_SIGNAL);

        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_skymanluna_sperrsignal_sh0", "Sperrsignal Sh0", "models/block/landofsignals/skyman_luna/sperrsignal_sh0/sperrsignal_sh0.obj", new float[]{0.77f, -1.2f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {
            private static final long serialVersionUID = 4147652253740899871L;

            {
                add(null);
                add("white");
                add("off");
            }
        }));
        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_skymanluna_vorsignal_base", "Vorsignal Base", "models/block/landofsignals/skyman_luna/vr0_hv_vorsignal/boden/vorsignal_boden.obj", new float[]{0.77f, -1.2f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {
            private static final long serialVersionUID = -4735826034425691080L;

            {
                add(null);
            }
        }));
        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_skymanluna_mast", "Mast", "models/block/landofsignals/skyman_luna/mast/vorsignal_mast.obj", new float[]{0.77f, -1.2f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {
            private static final long serialVersionUID = -7736454714742385604L;

            {
                add(null);
            }
        }));
        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_skymanluna_vorsignal_kopf", "Vorsignal Kopf", "models/block/landofsignals/skyman_luna/vr0_hv_vorsignal/kopf/vorsignal_kopf.obj", new float[]{0.77f, -1.2f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {
            private static final long serialVersionUID = 7937173305097109394L;

            {
                add(null);
                add("green");
                add("greenyellow");
                add("off");
            }
        }));
        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_skymanluna_hauptsignal_base", "Hauptsignal Base", "models/block/landofsignals/skyman_luna/hv_hauptsignal/boden/hauptsignal_boden.obj", new float[]{0.77f, -0.4f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {
            private static final long serialVersionUID = 1986676804127454924L;

            {
                add(null);
            }
        }));
        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_skymanluna_hauptsignal_schild", "Hauptsignal Schild", "models/block/landofsignals/skyman_luna/hv_hauptsignal/schild/hauptsignal_schild.obj", new float[]{0.77f, -1.2f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {
            private static final long serialVersionUID = -1465489635273018445L;

            {
                add(null);
            }
        }));
        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_skymanluna_hauptsignal_kopf", "Hauptsignal Kopf", "models/block/landofsignals/skyman_luna/hv_hauptsignal/kopf/hauptsignal_kopf.obj", new float[]{0.77f, -0.4f, 0.77f}, new float[]{0.5f, -0.9f, 0.5f}, new float[]{0.63f, 0.63f, 0.63f}, new ArrayList<String>() {
            private static final long serialVersionUID = -176728690413574760L;

            {
                add(null);
                add("hp1");
                add("hp2");
                add("sh1");
                add("zs1");
                add("off");
            }
        }));

        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_gamertv_base", "Base", "models/block/landofsignals/gamertv/boden/hv_boden.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {
            private static final long serialVersionUID = 8010261550984398621L;

            {
                add(null);
            }
        }));
        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_gamertv_mast", "Mast", "models/block/landofsignals/gamertv/mast/hv_mast.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {
            private static final long serialVersionUID = 5869590144384251497L;

            {
                add(null);
            }
        }));
        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_gamertv_vorsignal", "Vorsignal", "models/block/landofsignals/gamertv/vorsignal/hv_vr.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {
            private static final long serialVersionUID = 1870792251691530651L;

            {
                add(null);
                add("gruen");
                add("gruenorange");
                add("off");
            }
        }));
        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_gamertv_hvhp", "HvHp", "models/block/landofsignals/gamertv/kopf/hv_hp.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {
            private static final long serialVersionUID = 882310492762338085L;

            {
                add(null);
                add("hp1");
                add("hp2");
                add("sh1");
                add("off");
            }
        }));
        registerSignalContentPack(new ContentPackSignalPart("block_signal_part_gamertv_hversatz", "Hv Ersatz", "models/block/landofsignals/gamertv/ersatzsignal/hv_ersatzsignal.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {
            private static final long serialVersionUID = -6520565720448485429L;

            {
                add(null);
                add("an");
            }
        }));

        // Signs

        final ContentPackSignPart MISSING_SIGN = new ContentPackSignPart(Static.MISSING, "Missing! Check your content packs", new ContentPackSignObject("models/block/others/blocknotfound/blocknotfound.obj", new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f}));
        BLOCK_SIGN_PART.add(MISSING_SIGN);
        final ContentPackSignObject blockSignPartMetalRod = new ContentPackSignObject("models/block/landofsignals/signs/gsar/metalrod/metalrod.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new String[]{"Metal_Rod01_MR01"});

        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_metal_rod", "GSAR Metal Rod", blockSignPartMetalRod));
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_ne1", "GSAR Trapeztafel NE1", new ContentPackSignObject("models/block/landofsignals/signs/gsar/ne1/signalne1.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})));

        // Signs: SH2
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_sh2_full_wo_light", "GSAR Schutzsignal SH2 (Full, w/o Light)", new ContentPackSignObject("models/block/landofsignals/signs/gsar/sh2/signalsh2.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f}, new float[]{0.5f, 0.5f, 0.5f}, new String[]{"Sign01_SI01", "MetalRodLong_MR02"})));
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_sh2_top_wo_light", "GSAR Schutzsignal SH2 (Top, w/o Light)", new ContentPackSignObject("models/block/landofsignals/signs/gsar/sh2/signalsh2.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new String[]{"Sign01_SI01", "MetalRod_MR01"})));

        // Signs: Hecto Signs
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_hecto_sign", "GSAR Hectosignal", new ContentPackSignObject("models/block/landofsignals/signs/gsar/hectosign/hectosign.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}), blockSignPartMetalRod));
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_bu5_sign", "GSAR Laeutetafel BU5", new ContentPackSignObject("models/block/landofsignals/signs/gsar/bu5/hectosign.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}), blockSignPartMetalRod));
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_lf2_sign", "GSAR Anfangsscheibe LF2", new ContentPackSignObject("models/block/landofsignals/signs/gsar/lf2/hectosign.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}), blockSignPartMetalRod));
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_lf3_sign", "GSAR Endscheibe LF3", new ContentPackSignObject("models/block/landofsignals/signs/gsar/lf3/hectosign.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}), blockSignPartMetalRod));
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_ne2_sign", "GSAR Vorsignaltafel NE2", new ContentPackSignObject("models/block/landofsignals/signs/gsar/ne2/hectosign.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}), blockSignPartMetalRod));
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_ne5_sign", "GSAR Haltetafel NE5", new ContentPackSignObject("models/block/landofsignals/signs/gsar/ne5/hectosign.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}), blockSignPartMetalRod));

        // Signs: BU
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_bu2_sign", "GSAR Rautentafel BU2", new ContentPackSignObject("models/block/landofsignals/signs/gsar/bu2/signalbu2.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}), blockSignPartMetalRod));
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_bu3_sign", "GSAR Warntafel BU3", new ContentPackSignObject("models/block/landofsignals/signs/gsar/bu3/signalbu3.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}), blockSignPartMetalRod));
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_bu4_sign", "GSAR Pfeiftafel BU4", new ContentPackSignObject("models/block/landofsignals/signs/gsar/bu4/signalbu4.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}), blockSignPartMetalRod));

        // Signs: LF
        BLOCK_SIGN_PART.add(new ContentPackSignPart("block_sign_part_gsar_lf1_sign", "GSAR Langsamfahrscheibe LF1", new ContentPackSignObject("models/block/landofsignals/signs/gsar/lf1/signallf1.obj", new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}), blockSignPartMetalRod));


    }

    private static void registerSignalContentPack(ContentPackSignalPart contentPackSignalPartV1) {
        ContentPackHandlerV1.convertToV2(contentPackSignalPartV1, false);

    }

}
