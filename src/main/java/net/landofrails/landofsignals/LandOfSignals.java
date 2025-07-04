package net.landofrails.landofsignals;

import cam72cam.mod.ModCore;
import cam72cam.mod.ModEvent;
import cam72cam.mod.config.ConfigFile;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.net.Packet;
import cam72cam.mod.net.PacketDirection;
import cam72cam.mod.render.BlockRender;
import cam72cam.mod.render.GlobalRender;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.text.Command;
import net.landofrails.landofsignals.commands.LandOfSignalsCommand;
import net.landofrails.landofsignals.configs.LandOfSignalsConfig;
import net.landofrails.landofsignals.contentpacks.ContentPackHandler;
import net.landofrails.landofsignals.gui.overlay.ManipualtorOverlay;
import net.landofrails.landofsignals.packet.*;
import net.landofrails.landofsignals.render.block.*;
import net.landofrails.landofsignals.render.item.*;
import net.landofrails.landofsignals.tile.*;
import net.landofrails.stellwand.Stellwand;

import java.lang.reflect.Method;
import java.util.Optional;

public class LandOfSignals extends ModCore.Mod {
    @SuppressWarnings({"java:S1845"})
    public static final String MODID = "landofsignals";
    // Current version
    @SuppressWarnings("unused")
    public static final String VERSION = "1.4.0";

    @Override
    public String modID() {
        return MODID;
    }

    @Override
    public void commonEvent(final ModEvent event) {

        // Stellwand commonEvent
        Stellwand.commonEvent(event);

        if (event == ModEvent.CONSTRUCT) {
            ModCore.Mod.info("Thanks for using LandOfSignals. Starting common construct now...");
            final Optional<String> mcVersion = getMCVersion();
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
            Packet.register(SignTextPacket::new, PacketDirection.ClientToServer);
            Packet.register(SignTextPacket::new, PacketDirection.ServerToClient);
            Packet.register(CommandClientPacket::new, PacketDirection.ServerToClient);
            Packet.register(GuiSignalPrioritizationToClientPacket::new, PacketDirection.ServerToClient);
            Packet.register(GuiSignalPrioritizationToServerPacket::new, PacketDirection.ClientToServer);
            Packet.register(GuiItemManipulatorToClient::new, PacketDirection.ServerToClient);

            Command.register(new LandOfSignalsCommand());
        } else if (event == ModEvent.INITIALIZE) {
            // LandOfSignals Config
            ConfigFile.sync(LandOfSignalsConfig.class);
        }

    }

    @Override
    public void clientEvent(final ModEvent event) {

        // Stellwand
        Stellwand.clientEvent(event);

        switch (event) {
            case CONSTRUCT:
                ModCore.Mod.info("Starting client construct...");

                // Block
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_LEVER, TileSignalLeverRender::render, TileSignalLever.class);
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_BOX, TileSignalBoxRender::render, TileSignalBox.class);
                BlockRender.register(LOSBlocks.BLOCK_DECO, TileDecoRender::render, TileDeco.class);
                BlockRender.register(LOSBlocks.BLOCK_CUSTOM_LEVER, TileCustomLeverRender::render, TileCustomLever.class);

                // Items
                ItemRender.register(LOSItems.ITEM_SIGNAL_LEVER, ObjItemRender.getModelFor(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/signalslever/signalslever.obj"), new Vec3d(0.5, 0.6, 0.5), 1));
                ItemRender.register(LOSItems.ITEM_CONNECTOR, new Identifier(LandOfSignals.MODID, "items/itemconnector1"));
                ItemRender.register(LOSItems.ITEM_MANIPULATOR, new Identifier(LandOfSignals.MODID, "items/manipulator"));
                ItemRender.register(LOSItems.ITEM_MAGNIFYING_GLASS, new Identifier(LandOfSignals.MODID, "items/magnifyingglass"));

                //SignalPart : Block
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_PART, TileSignalPartRender::render, TileSignalPart.class);
                BlockRender.register(LOSBlocks.BLOCK_COMPLEX_SIGNAL, TileComplexSignalRender::render, TileComplexSignal.class);
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED, TileSignalPartAnimatedRender::render, TileSignalPartAnimated.class);
                BlockRender.register(LOSBlocks.BLOCK_SIGN_PART, TileSignPartRender::render, TileSignPart.class);

                //SignalPart : Item
                ItemRender.register(LOSItems.ITEM_SIGNAL_PART, new ItemSignalPartRender());
                ItemRender.register(LOSItems.ITEM_COMPLEX_SIGNAL, new ItemComplexSignalRender());
                ItemRender.register(LOSItems.ITEM_SIGNAL_PART_ANIMATED, ItemSignalPartAnimatedRender.getModelFor());
                ItemRender.register(LOSItems.ITEM_SIGN_PART, new ItemSignPartRender());
                ItemRender.register(LOSItems.ITEM_SIGNAL_BOX, new ItemSignalBoxRender());
                ItemRender.register(LOSItems.ITEM_DECO, new ItemDecoRender());
                ItemRender.register(LOSItems.ITEM_CUSTOM_LEVER, new ItemCustomLeverRender());

                // Deprecated: Only for compatability - Removes warnings
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_SO_12, TileMissingRender::render, TileSignalSO12.class);
                BlockRender.register(LOSBlocks.BLOCK_TICKET_MACHINE_DB, TileMissingRender::render, TileTicketMachineDB.class);
                BlockRender.register(LOSBlocks.BLOCK_TICKET_MACHINE_SBB, TileMissingRender::render, TileTicketMachineSBB.class);

                break;
            case SETUP:
                GlobalRender.registerOverlay((state, partialticks) -> new ManipualtorOverlay().draw());

                if(LandOfSignalsConfig.preloadModels) {
                    ContentPackHandler.preloadModels();
                }

                break;
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
    public void serverEvent(final ModEvent event) {
        // Do nothing forever
        Stellwand.serverEvent(event);
    }
}
