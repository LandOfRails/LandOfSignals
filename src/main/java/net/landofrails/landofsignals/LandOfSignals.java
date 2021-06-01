package net.landofrails.landofsignals;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import cam72cam.mod.ModCore;
import cam72cam.mod.ModEvent;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.net.Packet;
import cam72cam.mod.net.PacketDirection;
import cam72cam.mod.render.BlockRender;
import cam72cam.mod.render.GlobalRender;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.gui.overlay.ManipualtorOverlay;
import net.landofrails.landofsignals.packet.ManipulatorToClientPacket;
import net.landofrails.landofsignals.packet.ManipulatorToServerPacket;
import net.landofrails.landofsignals.packet.SignalBoxGuiToClientPacket;
import net.landofrails.landofsignals.packet.SignalBoxGuiToServerPacket;
import net.landofrails.landofsignals.packet.SignalBoxTileSignalPartPacket;
import net.landofrails.landofsignals.packet.SignalSelectorGuiPacket;
import net.landofrails.landofsignals.render.block.TileSignalBoxRender;
import net.landofrails.landofsignals.render.block.TileSignalLeverRender;
import net.landofrails.landofsignals.render.block.TileSignalPartAnimatedRender;
import net.landofrails.landofsignals.render.block.TileSignalPartRender;
import net.landofrails.landofsignals.render.block.TileSignalSO12Render;
import net.landofrails.landofsignals.render.block.TileTicketMachineDBRender;
import net.landofrails.landofsignals.render.block.TileTicketMachineSBBRender;
import net.landofrails.landofsignals.render.item.ItemSignalPartAnimatedRender;
import net.landofrails.landofsignals.render.item.ItemSignalPartRender;
import net.landofrails.landofsignals.render.item.ObjItemRender;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileSignalLever;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.tile.TileSignalPartAnimated;
import net.landofrails.landofsignals.tile.TileSignalSO12;
import net.landofrails.landofsignals.tile.TileTicketMachineDB;
import net.landofrails.landofsignals.tile.TileTicketMachineSBB;
import net.landofrails.landofsignals.utils.contentpacks.ContentPackHandler;
import net.landofrails.stellwand.Stellwand;

@SuppressWarnings("java:S112")
public class LandOfSignals extends ModCore.Mod {
    @SuppressWarnings("java:S1845")
    public static final String MODID = "landofsignals";
    public static final String VERSION = "0.0.3";

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
			ModCore.Mod.info("Detected MC Version: " + (mcVersion.isPresent() ? mcVersion.get() : "Failed"));

            ContentPackHandler.init();

            LOSBlocks.register();
            LOSItems.register();
            LOSGuis.register();
            Packet.register(SignalBoxGuiToServerPacket::new, PacketDirection.ClientToServer);
            Packet.register(SignalBoxGuiToClientPacket::new, PacketDirection.ServerToClient);
            Packet.register(SignalBoxTileSignalPartPacket::new, PacketDirection.ServerToClient);
            Packet.register(SignalSelectorGuiPacket::new, PacketDirection.ClientToServer);
            Packet.register(ManipulatorToClientPacket::new, PacketDirection.ServerToClient);
            Packet.register(ManipulatorToServerPacket::new, PacketDirection.ClientToServer);

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

                ItemRender.register(LOSItems.ITEM_SIGNAL_SELECTOR, new Identifier(MODID, "items/katanagear"));

                //SignalPart : Block
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_PART, TileSignalPartRender::render, TileSignalPart.class);
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED, TileSignalPartAnimatedRender::render, TileSignalPartAnimated.class);

                //SignalPart : Item
                ItemRender.register(LOSItems.ITEM_SIGNAL_PART, ItemSignalPartRender.getModelFor());
                ItemRender.register(LOSItems.ITEM_SIGNAL_PART_ANIMATED, ItemSignalPartAnimatedRender.getModelFor());
                break;
            case INITIALIZE:
                break;
            case SETUP:
                GlobalRender.registerOverlay(pt -> {
                    new ManipualtorOverlay().draw();
                });
            case RELOAD:
            case START:
            case FINALIZE:
                break;
        }
    }

	public Optional<String> getMCVersion() {

		for (Annotation annotation : ModCore.class.getAnnotations()) {
			if (annotation.annotationType().getName().endsWith("Mod")) {
				for (Method method : Mod.class.getDeclaredMethods()) {
					if (method.getName().contains("Minecraft") || method.getName().contains("minecraft")) {
						try {
							return Optional.of((String) method.invoke(annotation));
						} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace();
							return Optional.empty();
						}
					}
				}
			}
		}

		return Optional.empty();

	}

    @Override
    public void serverEvent(ModEvent event) {
        // Do nothing for now
        Stellwand.serverEvent(event);
    }
}
