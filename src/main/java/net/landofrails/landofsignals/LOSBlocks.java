package net.landofrails.landofsignals;

import net.landofrails.api.contentpacks.v1.ContentPackSignalPart;
import net.landofrails.api.contentpacks.v2.ContentPack;
import net.landofrails.api.contentpacks.v2.ContentPackException;
import net.landofrails.api.contentpacks.v2.deco.ContentPackDeco;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.sign.ContentPackSign;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalState;
import net.landofrails.api.contentpacks.v2.signalbox.ContentPackSignalbox;
import net.landofrails.landofsignals.blocks.*;
import net.landofrails.landofsignals.contentpacks.ContentPackHandlerV1;
import net.landofrails.landofsignals.utils.Static;

import java.util.*;

public class LOSBlocks {

    private LOSBlocks() {

    }

    public static final BlockSignalSO12 BLOCK_SIGNAL_SO_12 = new BlockSignalSO12(LandOfSignals.MODID, "BlockSignalSO12");
    public static final BlockSignalLever BLOCK_SIGNAL_LEVER = new BlockSignalLever(LandOfSignals.MODID, "BlockSignalLever");
    public static final BlockTicketMachineDB BLOCK_TICKET_MACHINE_DB = new BlockTicketMachineDB(LandOfSignals.MODID, "BlockTicketMachineDB");
    public static final BlockTicketMachineSBB BLOCK_TICKET_MACHINE_SBB = new BlockTicketMachineSBB(LandOfSignals.MODID, "BlockTicketMachineSBB");
    public static final BlockSignalBox BLOCK_SIGNAL_BOX = new BlockSignalBox(LandOfSignals.MODID, "BlockSignalBox");
    public static final BlockDeco BLOCK_DECO = new BlockDeco(LandOfSignals.MODID, "BlockDeco");

    // Contentpack
    private static final ContentPack CONTENTPACK = new ContentPack("LandOfSignals", "LandOfSignals", "1.0", "2", null, null);
    private static final ContentPack CONTENTPACK_STELLWAND = new ContentPack("Stellwand", "Stellwand", "1.0", "2", null, null);

    //Signal
    public static final BlockSignalPart BLOCK_SIGNAL_PART = new BlockSignalPart(LandOfSignals.MODID, "blocksignalpart");
    public static final BlockSignalPartAnimated BLOCK_SIGNAL_PART_ANIMATED = null; // FIXME new BlockSignalPartAnimated(LandOfSignals.MODID, "blocksignalpartanimated");

    // Sign
    public static final BlockSignPart BLOCK_SIGN_PART = new BlockSignPart(LandOfSignals.MODID, "blocksign");

    @SuppressWarnings({"java:S117", "java:S1192", "java:S1171"})
    public static void register() {


        // loads static classes and ctrs
        ContentPackSignalPart MISSING_SIGNAL = new ContentPackSignalPart(Static.MISSING, Static.MISSING_NAME, Static.MISSING_OBJ, new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f}, new ArrayList<String>() {
            private static final long serialVersionUID = -1995088635629060337L;

            {
                add(null);
            }
        });

        registerSignalContentPack(MISSING_SIGNAL);

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

        registerSignContentPack(Static.MISSING, Static.MISSING_NAME, false, models(Static.MISSING_OBJ, new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f})}));
        final Map.Entry<String, ContentPackModel[]> blockSignPartMetalRod = new AbstractMap.SimpleEntry<>("models/block/landofsignals/signs/gsar/metalrod/metalrod.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new String[]{"Metal_Rod01_MR01"})});

        registerSignContentPack("block_sign_part_gsar_metal_rod", "GSAR Metal Rod", false, Collections.singletonMap(blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));
        registerSignContentPack("block_sign_part_gsar_ne1", "GSAR Trapeztafel NE1", true, models("models/block/landofsignals/signs/gsar/ne1/signalne1.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));

        // Signs: SH2
        registerSignContentPack("block_sign_part_gsar_sh2_full_wo_light", "GSAR Schutzsignal SH2 (Full, w/o Light)", false, Collections.singletonMap("models/block/landofsignals/signs/gsar/sh2/signalsh2.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new float[]{1f, 1f, 1f}, new float[]{0.5f, 0.5f, 0.5f}, new String[]{"Sign01_SI01", "MetalRodLong_MR02"})}));
        registerSignContentPack("block_sign_part_gsar_sh2_top_wo_light", "GSAR Schutzsignal SH2 (Top, w/o Light)", false, Collections.singletonMap("models/block/landofsignals/signs/gsar/sh2/signalsh2.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new String[]{"Sign01_SI01", "MetalRod_MR01"})}));

        // Signs: Hecto Signs
        registerSignContentPack("block_sign_part_gsar_hecto_sign", "GSAR Hectosignal", true, models("models/block/landofsignals/signs/gsar/hectosign/hectosign.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));
        registerSignContentPack("block_sign_part_gsar_bu5_sign", "GSAR Laeutetafel BU5", false, models("models/block/landofsignals/signs/gsar/bu5/hectosign.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));
        registerSignContentPack("block_sign_part_gsar_lf2_sign", "GSAR Anfangsscheibe LF2", false, models("models/block/landofsignals/signs/gsar/lf2/hectosign.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));
        registerSignContentPack("block_sign_part_gsar_lf3_sign", "GSAR Endscheibe LF3", false, models("models/block/landofsignals/signs/gsar/lf3/hectosign.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));
        registerSignContentPack("block_sign_part_gsar_ne2_sign", "GSAR Vorsignaltafel NE2", false, models("models/block/landofsignals/signs/gsar/ne2/hectosign.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));
        registerSignContentPack("block_sign_part_gsar_ne5_sign", "GSAR Haltetafel NE5", false, models("models/block/landofsignals/signs/gsar/ne5/hectosign.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));

        // Signs: BU
        registerSignContentPack("block_sign_part_gsar_bu2_sign", "GSAR Rautentafel BU2", false, models("models/block/landofsignals/signs/gsar/bu2/signalbu2.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));
        registerSignContentPack("block_sign_part_gsar_bu3_sign", "GSAR Warntafel BU3", false, models("models/block/landofsignals/signs/gsar/bu3/signalbu3.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));
        registerSignContentPack("block_sign_part_gsar_bu4_sign", "GSAR Pfeiftafel BU4", false, models("models/block/landofsignals/signs/gsar/bu4/signalbu4.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));

        // Signs: LF
        registerSignContentPack("block_sign_part_gsar_lf1_sign", "GSAR Langsamfahrscheibe LF1", false, models("models/block/landofsignals/signs/gsar/lf1/signallf1.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));
        registerSignContentPack("block_sign_part_gsar_lf6_sign", "GSAR Geschwindigkeits-Ankuendesignal LF6", false, models("models/block/landofsignals/signs/gsar/lf6/signallf6.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0.2f, 0.5f}, new float[]{0.5f, 0.2f, 0.5f}, new float[]{1f, 1f, 1f}, new String[]{"Cube.001_Cube.002"})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));
        registerSignContentPack("block_sign_part_gsar_lf7_sign", "GSAR Geschwindigkeitssignal LF7", false, models("models/block/landofsignals/signs/gsar/lf7/signallf7.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));

        // Signs: RA
        registerSignContentPack("block_sign_part_gsar_ra10a_sign", "GSAR Rangierhalttafel RA10 (a)", false, models("models/block/landofsignals/signs/gsar/ra10a/signalra10a.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}));
        registerSignContentPack("block_sign_part_gsar_ra10b_sign", "GSAR Rangierhalttafel RA10 (b)", false, models("models/block/landofsignals/signs/gsar/ra10b/signalra10b.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}));
        registerSignContentPack("block_sign_part_gsar_ra11a_sign", "GSAR Wartezeichen RA11 (a)", false, models("models/block/landofsignals/signs/gsar/ra11a/signalra11a.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));
        registerSignContentPack("block_sign_part_gsar_ra11b_sign", "GSAR Wartezeichen RA11 (b)", false, models("models/block/landofsignals/signs/gsar/ra11b/signalra11b.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 1f, 1f})}, blockSignPartMetalRod.getKey(), blockSignPartMetalRod.getValue()));

        // Stellwand

        registerSingleGroupStellwandContent(
                "block_signal_straight_track",
                "Signal Straight Track",
                "models/block/stellwand/blocksignal/blocktrackstraight/blocksignal.obj",
                keyValueLinkedMap("Off", "off", "White", "white", "Red", "red"),
                "white"
        );

        registerSingleGroupStellwandContent(
                "block_signal_straight_unisolated",
                "Signal Straight Unisolated",
                "models/block/stellwand/blocksignal/blocktrackunisolated/unisolated.obj",
                keyValueLinkedMap("Off", "off", "White", "white"),
                "white"
        );

        registerSingleGroupStellwandContent(
                "block_signal_diagonal_track_dl",
                "Signal Diagonal Track DL",
                "models/block/stellwand/blocksignal/trackdiag/downleft/trackdiagdownleft.obj",
                keyValueLinkedMap("Off", "off", "White", "white", "Red", "red"),
                "white"
        );

        registerSingleGroupStellwandContent(
                "block_signal_diagonal_track_dr",
                "Signal Diagonal Track DR",
                "models/block/stellwand/blocksignal/trackdiag/downright/trackdiagdownright.obj",
                keyValueLinkedMap("Off", "off", "White", "white", "Red", "red"),
                "white"
        );

        registerSingleGroupStellwandContent(
                "block_signal_diagonal_track_ul",
                "Signal Diagonal Track UL",
                "models/block/stellwand/blocksignal/trackdiag/upleft/trackdiagupleft.obj",
                keyValueLinkedMap("Off", "off", "White", "white", "Red", "red"),
                "white"
        );

        registerSingleGroupStellwandContent(
                "block_signal_diagonal_track_ur",
                "Signal Diagonal Track UR",
                "models/block/stellwand/blocksignal/trackdiag/upright/trackdiagupright.obj",
                keyValueLinkedMap("Off", "off", "White", "white", "Red", "red"),
                "white"
        );

        registerSingleGroupStellwandContent(
                "block_signal_hsig_left",
                "Signal HSig Left",
                "models/block/stellwand/blocksignal/trackmainsignal/left/hsigleft.obj",
                keyValueLinkedMap("Off", "off", "Green", "green", "Orange", "orange", "Red", "red"),
                "green"
        );

        registerSingleGroupStellwandContent(
                "block_signal_hsig_right",
                "Signal HSig Right",
                "models/block/stellwand/blocksignal/trackmainsignal/right/hsigright.obj",
                keyValueLinkedMap("Off", "off", "Green", "green", "Orange", "orange", "Red", "red"),
                "green"
        );

        registerSingleGroupStellwandContent(
                "block_signal_dwarf_bottom",
                "Dwarfsignal bottom",
                "models/block/stellwand/blocksignal/blockdwarfsignal/blockdwarfsignalbottom.obj",
                keyValueLinkedMap("Off", "off", "Green", "green"),
                "green"
        );

        registerSingleGroupStellwandContent(
                "block_signal_dwarf_top",
                "Dwarfsignal top",
                "models/block/stellwand/blocksignal/blockdwarfsignal/blockdwarfsignaltop.obj",
                keyValueLinkedMap("Off", "off", "Green", "green"),
                "green"
        );

        registerSingleGroupStellwandContent(
                "block_signal_track_switch_lrd",
                "Track switch (Left -> Right, Down)",
                "models/block/stellwand/blocksignal/trackswitch/lrd/trackswitch.obj",
                keyValueLinkedMap("Off", "off", "Main single white", "main_signalwhite", "Main double white", "main_doublewhite",
                        "Branch white", "branch_white", "Main double red", "main_doublered", "Branch red", "branch_red"),
                "branch_white"
        );

        registerSingleGroupStellwandContent(
                "block_signal_track_switch_lru",
                "Track switch (Left -> Right, Up)",
                "models/block/stellwand/blocksignal/trackswitch/lru/trackswitch.obj",
                keyValueLinkedMap("Off", "off", "Main single white", "main_signalwhite", "Main double white", "main_doublewhite", "Branch white",
                        "branch_white", "Main double red", "main_doublered", "Branch red", "branch_red"),
                "branch_white"
        );

        registerSingleGroupStellwandContent(
                "block_signal_track_switch_rld",
                "Track switch (Right -> Left, Down)",
                "models/block/stellwand/blocksignal/trackswitch/rld/trackswitch.obj",
                keyValueLinkedMap("Off", "off", "Main single white", "main_signalwhite", "Main double white", "main_doublewhite", "Branch white",
                        "branch_white", "Main double red", "main_doublered", "Branch red", "branch_red"),
                "branch_white"
        );

        registerSingleGroupStellwandContent(
                "block_signal_track_switch_rlu",
                "Track switch (Right -> Left, Up)",
                "models/block/stellwand/blocksignal/trackswitch/rlu/trackswitch.obj",
                keyValueLinkedMap("Off", "off", "Main single white", "main_signalwhite", "Main double white", "main_doublewhite", "Branch white",
                        "branch_white", "Main double red", "main_doublered", "Branch red", "branch_red"),
                "branch_white"
        );

        registerStreckenblock();

        // Signalboxes

        registerSignalboxContentPack(Static.MISSING, Static.MISSING_NAME, models(Static.MISSING_OBJ, new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f})}));
        registerSignalboxContentPack(Static.SIGNALBOX_DEFAULT, "Signalbox", models("models/block/landofsignals/signalbox/untitled.obj", new ContentPackModel[]{new ContentPackModel(new float[]{1.6f, 0f, 1.6f}, new float[]{1.6f, 0f, 1.6f}, new float[]{.3f, .3f, .3f})}));
        registerSignalboxContentPack("signalbox_stellwand", "Signalbox (Stellwand)", models("models/block/stellwand/blocksender/blocksender/blocksender.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f})}));
        registerSignalboxContentPack("signalbox_microchip", "Signalbox Microchip (Stellwand)", models("models/block/stellwand/blocksender/microchip/microchip.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f})}));

        // Deco

        registerDecoContentPack(Static.MISSING, Static.MISSING_NAME, models(Static.MISSING_OBJ, new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f})}));
        registerDecoContentPack("deco_fahrkartenautomat_db", "Ticket Machine (Deutsche Bahn)", models("models/block/landofsignals/fahrkartenautomat_db/fahrkartenautomat_db.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{1f, 0f, 0.5f}, new float[]{1f, 1f, 1f}, new float[]{0.5f, 0.5f, 0.5f})}));
        registerDecoContentPack("deco_fahrkartenautomat_sbb", "Ticket Machine (SBB)", models("models/block/landofsignals/fahrkartenautomat_sbb/ticketautomat.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.75f, 0f, 0.75f}, new float[]{1.6f, 0f, 0.5f}, new float[]{0.65f, 0.65f, 0.65f}, new float[]{0.3f, 0.3f, 0.3f})}));
        registerDecoContentPack("deco_signal_so12", "Signal SO12", models("models/block/landofsignals/so12/signalso12.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.25f, 0f, 0.25f}, new float[]{1f, 1f, 1f}, new float[]{2f, 2f, 2f})}));

        registerDecoContentPackStellwand("deco_filler", "Block Filler", models("models/block/stellwand/blockfiller/blockfiller/blockfiller.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0f, 0.5f}, new float[]{0.5f, 0, 0.5f}, new float[]{1f, 1f, 1f}, new float[]{1f, 1f, 1f})}));
        registerDecoContentPackStellwand("deco_straight_track", "Straight track", models("models/block/stellwand/blockfiller/trackstraight/trackstraight.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f}, new float[]{1f, 1f, 1f}, new float[]{0f, 180f, 0f})}));
        registerDecoContentPackStellwand("deco_trackdiag_down_left", "Diagonal track (down - left)", models("models/block/stellwand/blockfiller/trackdiag/dl/trackdiag.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f}, new float[]{1f, 1f, 1f}, new float[]{0f, 180f, 0f})}));
        registerDecoContentPackStellwand("deco_trackdiag_down_right", "Diagonal track (down - right)", models("models/block/stellwand/blockfiller/trackdiag/dr/trackdiag.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f}, new float[]{1f, 1f, 1f}, new float[]{0f, 180f, 0f})}));
        registerDecoContentPackStellwand("deco_trackdiag_up_left", "Diagonal track (up - left)", models("models/block/stellwand/blockfiller/trackdiag/ul/trackdiag.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f}, new float[]{1f, 1f, 1f}, new float[]{0f, 180f, 0f})}));
        registerDecoContentPackStellwand("deco_trackdiag_up_right", "Diagonal track (up - right)", models("models/block/stellwand/blockfiller/trackdiag/ur/trackdiag.obj", new ContentPackModel[]{new ContentPackModel(new float[]{0.5f, 0.5f, 0.5f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1f, 1f, 1f}, new float[]{1f, 1f, 1f}, new float[]{0f, 180f, 0f})}));
        
    }

    private static void registerStreckenblock() {
        String objPath = "models/block/stellwand/blockstreckenblock/streckenblock.obj";
        Map<String, ContentPackSignalGroup> groups = new HashMap<>();

        String[][] preparedGroups = new String[][]{
                {"topLeft", "Top left"},
                {"topRight", "Top right"},
                {"bottomLeft", "Bottom left"},
                {"bottomRight", "Bottom right"}
        };
        String[][] preparedStates = new String[][]{
                {"Black", "black"},
                {"White", "white"},
                {"Red", "red"}
        };

        for (String[] group : preparedGroups) {
            String groupId = group[0];
            String groupName = group[1];
            LinkedHashMap<String, ContentPackSignalState> states = new LinkedHashMap<>();
            for (String[] state : preparedStates) {
                String stateId = groupId + state[0];
                String stateName = groupName + " " + state[1];
                states.put(stateId, new ContentPackSignalState(stateName, signalModels(objPath, stateId, new float[]{.5f, 0f, .5f}, new float[]{.5f, 0f, .5f})));
            }
            groups.put(groupName, new ContentPackSignalGroup(groupName, states));
        }

        ContentPackSignal stellwandMultisignal = new ContentPackSignal(
                "Streckenblock",
                "block_signal_streckenblock",
                90f,
                LOSTabs.SIGNALS_TAB,
                signalModels(objPath, "general", new float[]{.5f, 0f, .5f}, new float[]{.5f, 0f, .5f}),
                groups,
                keyValueLinkedMap("Top left", "topLeftWhite", "Bottom right", "bottomRightRed"),
                null,
                null
        );
        registerStellwandContent(stellwandMultisignal);
    }

    private static void registerSignalContentPack(ContentPackSignalPart contentPackSignalPartV1) {
        ContentPackHandlerV1.convertToV2(contentPackSignalPartV1, false, CONTENTPACK);
    }

    private static void registerSignContentPack(String id, String name, boolean writable, Map<String, ContentPackModel[]> models) {
        ContentPackSign contentPackSign = new ContentPackSign();
        contentPackSign.setId(id);
        contentPackSign.setName(name);
        contentPackSign.setWriteable(writable);
        contentPackSign.setBase(models);

        contentPackSign.validate(missing -> {
            throw new ContentPackException(String.format(Static.MISSING_ATTRIBUTES, missing));
        }, CONTENTPACK);

        BLOCK_SIGN_PART.add(contentPackSign);
    }

    private static void registerDecoContentPack(String id, String name, Map<String, ContentPackModel[]> models) {
        ContentPackDeco contentPackDeco = new ContentPackDeco();
        contentPackDeco.setId(id);
        contentPackDeco.setName(name);
        contentPackDeco.setBase(models);

        contentPackDeco.validate(missing -> {
            throw new ContentPackException(String.format(Static.MISSING_ATTRIBUTES, missing));
        }, CONTENTPACK);

        BLOCK_DECO.add(contentPackDeco);
    }

    private static void registerDecoContentPackStellwand(String id, String name, Map<String, ContentPackModel[]> models) {
        ContentPackDeco contentPackDeco = new ContentPackDeco();
        contentPackDeco.setId(id);
        contentPackDeco.setName(name);
        contentPackDeco.setBase(models);
        contentPackDeco.setRotationSteps(90f);

        contentPackDeco.validate(missing -> {
            throw new ContentPackException(String.format(Static.MISSING_ATTRIBUTES, missing));
        }, CONTENTPACK_STELLWAND);

        BLOCK_DECO.add(contentPackDeco);
    }

    private static void registerSingleGroupStellwandContent(String id, String name, String objPath, Map<String, String> signalNameAndId, String itemGroup) {
        String groupIdName = "default";

        LinkedHashMap<String, ContentPackSignalState> states = new LinkedHashMap<>();
        signalNameAndId.forEach((signalName, signalId) ->
                states.put(signalId, new ContentPackSignalState(signalName, signalModels(objPath, signalId)))
        );
        Map<String, ContentPackSignalGroup> group = Collections.singletonMap(groupIdName, new ContentPackSignalGroup("default", states));

        registerStellwandContent(
                new ContentPackSignal(
                        name,
                        id,
                        90f,
                        LOSTabs.SIGNALS_TAB,
                        signalModels(objPath, "general"),
                        group,
                        Collections.singletonMap(groupIdName, itemGroup),
                        null,
                        null
                )
        );
    }

    private static void registerStellwandContent(ContentPackSignal contentPackSignal) {
        contentPackSignal.validate(missing -> {
            throw new ContentPackException(String.format(Static.MISSING_ATTRIBUTES, missing));
        }, CONTENTPACK_STELLWAND);
        BLOCK_SIGNAL_PART.add(contentPackSignal);
    }

    private static void registerSignalboxContentPack(String id, String name, Map<String, ContentPackModel[]> models) {

        ContentPackSignalbox contentPackSignalbox = new ContentPackSignalbox();
        contentPackSignalbox.setId(id);
        contentPackSignalbox.setName(name);
        contentPackSignalbox.setBase(models);
        contentPackSignalbox.setRotationSteps(45f);

        contentPackSignalbox.validate(missing -> {
            throw new ContentPackException(String.format(Static.MISSING_ATTRIBUTES, missing));
        }, CONTENTPACK);

        BLOCK_SIGNAL_BOX.add(contentPackSignalbox);

    }

    private static Map<String, ContentPackModel[]> models(Object... data) {
        Map<String, ContentPackModel[]> map = new HashMap<>();
        for (int index = 0; index < data.length; index += 2) {
            map.put((String) data[index], (ContentPackModel[]) data[index + 1]);
        }
        return map;
    }

    private static Map<String, String> keyValueLinkedMap(String... data) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int index = 0; index < data.length; index += 2) {
            map.put(data[index], data[index + 1]);
        }
        return map;
    }

    private static Map<String, ContentPackModel[]> signalModels(String objPath, String objGroup) {
        return models(
                objPath,
                new ContentPackModel[]{
                        new ContentPackModel(
                                new float[]{0.5f, 0.5f, 0.5f},
                                new float[]{0.5f, 0.5f, 0.5f},
                                new float[]{1f, 1f, 1f},
                                new float[]{1f, 1f, 1f},
                                new float[]{0f, 180f, 0f},
                                new String[]{objGroup}
                        )
                }
        );
    }

    private static Map<String, ContentPackModel[]> signalModels(String objPath, String objGroup, float[] blockTranslation, float[] itemTranslation) {
        return models(
                objPath,
                new ContentPackModel[]{
                        new ContentPackModel(
                                blockTranslation,
                                itemTranslation,
                                new float[]{1f, 1f, 1f},
                                new float[]{1f, 1f, 1f},
                                new float[]{0f, 180f, 0f},
                                new String[]{objGroup}
                        )
                }
        );
    }

}
