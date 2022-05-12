package net.landofrails.landofsignals;

import cam72cam.mod.ModCore;
import cam72cam.mod.ModEvent;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.net.Packet;
import cam72cam.mod.net.PacketDirection;
import cam72cam.mod.render.BlockRender;
import cam72cam.mod.render.GlobalRender;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.contentpacks.ContentPackHandler;
import net.landofrails.landofsignals.gui.overlay.ManipualtorOverlay;
import net.landofrails.landofsignals.packet.*;
import net.landofrails.landofsignals.render.block.*;
import net.landofrails.landofsignals.render.item.ItemSignPartRender;
import net.landofrails.landofsignals.render.item.ItemSignalPartAnimatedRender;
import net.landofrails.landofsignals.render.item.ItemSignalPartRender;
import net.landofrails.landofsignals.render.item.ObjItemRender;
import net.landofrails.landofsignals.tile.*;
import net.landofrails.stellwand.Stellwand;

import java.lang.reflect.Method;
import java.util.Optional;

public class LandOfSignals extends ModCore.Mod {
    @SuppressWarnings({"java:S1845"})
    public static final String MODID = "landofsignals";
    public static final String VERSION = "0.0.4";

    @Override
    public String modID() {
        return MODID;
    }

    @Override
    public void commonEvent(ModEvent event) {

        // Stellwand commonEvent
        Stellwand.commonEvent(event);

        if (event == ModEvent.CONSTRUCT) {
            ModCore.Mod.info("Thanks for using LandOfSignals. Starting common construct now...");
            Optional<String> mcVersion = getMCVersion();
            ModCore.Mod.info("Detected MC Version: " + mcVersion.orElse("Failed to receive"));

            ContentPackHandler.init();

            LOSTabs.register();

            LOSBlocks.register();
            LOSItems.register();
            LOSGuis.register();
            Packet.register(SignalBoxGuiToServerPacket::new, PacketDirection.ClientToServer);
            Packet.register(SignalBoxGuiToClientPacket::new, PacketDirection.ServerToClient);
            Packet.register(SignalBoxTileSignalPartPacket::new, PacketDirection.ServerToClient);
            Packet.register(SignalSelectorGuiPacket::new, PacketDirection.ClientToServer);
            Packet.register(SignSelectorGuiPacket::new, PacketDirection.ClientToServer);
            Packet.register(ManipulatorToClientPacket::new, PacketDirection.ServerToClient);
            Packet.register(ManipulatorToServerPacket::new, PacketDirection.ClientToServer);
            Packet.register(SignalUpdatePacket::new, PacketDirection.ServerToClient);

        }

    }

    @Override
    public void clientEvent(ModEvent event) {

        // Stellwand
        Stellwand.clientEvent(event);

        switch (event) {
            case CONSTRUCT:
                ModCore.Mod.info("Starting client construct...");

                // Block
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_SO_12, TileSignalSO12Render::render, TileSignalSO12.class);
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_LEVER, TileSignalLeverRender::render, TileSignalLever.class);
                BlockRender.register(LOSBlocks.BLOCK_TICKET_MACHINE_DB, TileTicketMachineDBRender::render, TileTicketMachineDB.class);
                BlockRender.register(LOSBlocks.BLOCK_TICKET_MACHINE_SBB, TileTicketMachineSBBRender::render, TileTicketMachineSBB.class);
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_BOX, TileSignalBoxRender::render, TileSignalBox.class);

                // Items
                ItemRender.register(LOSItems.ITEM_SIGNALSO12, ObjItemRender.getModelFor(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/so12/signalso12.obj"), new Vec3d(0.5, 0, 0.5), 2));
                ItemRender.register(LOSItems.ITEM_SIGNAL_LEVER, ObjItemRender.getModelFor(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/signalslever/signalslever.obj"), new Vec3d(0.5, 0.6, 0.5), 1));
                ItemRender.register(LOSItems.ITEM_TICKET_MACHINE_DB, ObjItemRender.getModelFor(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/fahrkartenautomat_db/fahrkartenautomat_db.obj"), new Vec3d(0.5, 0, 0.5), 0.5f));
                ItemRender.register(LOSItems.ITEM_TICKET_MACHINE_SBB, ObjItemRender.getModelFor(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/fahrkartenautomat_sbb/ticketautomat.obj"), new Vec3d(0.5, 0, 0.5), 0.3f));
                ItemRender.register(LOSItems.ITEM_SIGNAL_BOX, ObjItemRender.getModelFor(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/signalbox/untitled.obj"), new Vec3d(0.5, 0, 0.5), 0.25f));
                ItemRender.register(LOSItems.ITEM_CONNECTOR, new Identifier(LandOfSignals.MODID, "items/itemconnector1"));
                ItemRender.register(LOSItems.ITEM_MANIPULATOR, new Identifier(LandOfSignals.MODID, "items/manipulator"));

                ItemRender.register(LOSItems.ITEM_SIGNAL_SELECTOR, new Identifier(MODID, "items/signalchest"));
                ItemRender.register(LOSItems.ITEM_SIGN_SELECTOR, new Identifier(MODID, "items/signchest"));

                //SignalPart : Block
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_PART, TileSignalPartRender::render, TileSignalPart.class);
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED, TileSignalPartAnimatedRender::render, TileSignalPartAnimated.class);
                BlockRender.register(LOSBlocks.BLOCK_SIGN_PART, TileSignPartRender::render, TileSignPart.class);

                //SignalPart : Item
                ItemRender.register(LOSItems.ITEM_SIGNAL_PART, new ItemSignalPartRender());
                ItemRender.register(LOSItems.ITEM_SIGNAL_PART_ANIMATED, ItemSignalPartAnimatedRender.getModelFor());
                ItemRender.register(LOSItems.ITEM_SIGN_PART, ItemSignPartRender.getModelFor());
                break;
            case SETUP:
                GlobalRender.registerOverlay(pt -> new ManipualtorOverlay().draw());
                break;
            case RELOAD:

                // Release renderers after reload (rejoining a world for example)

                TileSignalBoxRender.releaseRenderersIntoTheWild();
                TileSignalLeverRender.releaseRenderersIntoTheWild();
                TileSignalPartAnimatedRender.releaseRenderersIntoTheWild();
                TileSignalSO12Render.releaseRenderersIntoTheWild();
                TileTicketMachineDBRender.releaseRenderersIntoTheWild();
                TileTicketMachineSBBRender.releaseRenderersIntoTheWild();
                TileSignPartRender.releaseRenderersIntoTheWild();

                break;
            case FINALIZE:
            default:
                break;
        }
    }

    public Optional<String> getMCVersion() {
        try {
            Class<?> loader = Class.forName("net.minecraftforge.fml.common.Loader");
            Method instanceMethod = loader.getMethod("instance");
            Object loaderInstance = instanceMethod.invoke(null);
            Method getMCVersionString = loader.getMethod("getMCVersionString");
            String version = (String) getMCVersionString.invoke(loaderInstance);

            return Optional.ofNullable(version);

        } catch (Exception e) {

            return Optional.empty();
        }

    }

    @Override
    public void serverEvent(ModEvent event) {
        // Do nothing for now
        Stellwand.serverEvent(event);
    }
}
