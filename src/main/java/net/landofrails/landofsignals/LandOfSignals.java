package net.landofrails.landofsignals;

import java.util.Map;

import cam72cam.mod.ModCore;
import cam72cam.mod.ModEvent;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.net.Packet;
import cam72cam.mod.net.PacketDirection;
import cam72cam.mod.render.BlockRender;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.blocks.BlockSignalPart;
import net.landofrails.landofsignals.items.ItemSignalPart;
import net.landofrails.landofsignals.packet.SignalBoxGuiPacket;
import net.landofrails.landofsignals.packet.SignalSelectorGuiPacket;
import net.landofrails.landofsignals.render.block.TileSignalBoxRender;
import net.landofrails.landofsignals.render.block.TileSignalLeverRender;
import net.landofrails.landofsignals.render.block.TileSignalPartRender;
import net.landofrails.landofsignals.render.block.TileSignalSO12Render;
import net.landofrails.landofsignals.render.block.TileTicketMachineDBRender;
import net.landofrails.landofsignals.render.block.TileTicketMachineSBBRender;
import net.landofrails.landofsignals.render.item.ObjItemRender;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileSignalLever;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.tile.TileSignalSO12;
import net.landofrails.landofsignals.tile.TileTicketMachineDB;
import net.landofrails.landofsignals.tile.TileTicketMachineSBB;
import net.landofrails.landofsignals.utils.ContentPackHandler;
import net.landofrails.landofsignals.utils.Static;
import net.landofrails.stellwand.Stellwand;

@SuppressWarnings("java:S112")
public class LandOfSignals extends ModCore.Mod {
	@SuppressWarnings("java:S1845")
	public static final String MODID = "landofsignals";
	public static final String VERSION = "0.0.2";

	@Override
	public String modID() {
		return MODID;
	}

	@Override
	public void commonEvent(ModEvent event) {

		if (event == ModEvent.CONSTRUCT) {
			ModCore.Mod.info("Thanks for using LandOfSignals. Starting common construct now...");

			// Stellwand
			Stellwand.commonEvent();

			ContentPackHandler.init();

			LOSBlocks.register();
			LOSItems.register();
			Packet.register(SignalBoxGuiPacket::new, PacketDirection.ClientToServer);
			Packet.register(SignalSelectorGuiPacket::new, PacketDirection.ClientToServer);
		}

	}

	@Override
	public void clientEvent(ModEvent event) {
		switch (event) {
		case CONSTRUCT:
			ModCore.Mod.info("Starting client construct...");

			// Stellwand
			Stellwand.clientEvent();

			// Block
			BlockRender.register(LOSBlocks.BLOCK_SIGNAL_SO_12, TileSignalSO12Render::render, TileSignalSO12.class);
			BlockRender.register(LOSBlocks.BLOCK_SIGNAL_LEVER, TileSignalLeverRender::render, TileSignalLever.class);
			BlockRender.register(LOSBlocks.BLOCK_TICKET_MACHINE_DB, TileTicketMachineDBRender::render,
					TileTicketMachineDB.class);
			BlockRender.register(LOSBlocks.BLOCK_TICKET_MACHINE_SBB, TileTicketMachineSBBRender::render,
					TileTicketMachineSBB.class);
			BlockRender.register(LOSBlocks.BLOCK_SIGNAL_BOX, TileSignalBoxRender::render, TileSignalBox.class);

			// Items
			ItemRender.register(LOSItems.ITEM_SIGNALSO12,
					ObjItemRender.getModelFor(
							new Identifier(LandOfSignals.MODID, "models/block/landofsignals/so12/signalso12.obj"),
							new Vec3d(0.5, 0, 0.5), 2));
			ItemRender.register(LOSItems.ITEM_SIGNAL_LEVER,
					ObjItemRender.getModelFor(
							new Identifier(LandOfSignals.MODID,
									"models/block/landofsignals/signalslever/signalslever.obj"),
							new Vec3d(0.5, 0.6, 0.5), 1));
			ItemRender.register(LOSItems.ITEM_TICKET_MACHINE_DB,
					ObjItemRender.getModelFor(
							new Identifier(LandOfSignals.MODID,
									"models/block/landofsignals/fahrkartenautomat_db/fahrkartenautomat_db.obj"),
							new Vec3d(0.5, 0, 0.5), 0.5f));
			ItemRender.register(LOSItems.ITEM_TICKET_MACHINE_SBB,
					ObjItemRender.getModelFor(
							new Identifier(LandOfSignals.MODID,
									"models/block/landofsignals/fahrkartenautomat_sbb/ticketautomat.obj"),
							new Vec3d(0.5, 0, 0.5), 0.3f));
			ItemRender.register(LOSItems.ITEM_SIGNAL_BOX,
					ObjItemRender.getModelFor(
							new Identifier(LandOfSignals.MODID, "models/block/landofsignals/signalbox/untitled.obj"),
							new Vec3d(0.5, 0, 0.5), 0.25f));
			ItemRender.register(LOSItems.ITEM_CONNECTOR, new Identifier(LandOfSignals.MODID, "items/itemconnector1"));

			ItemRender.register(LOSItems.ITEM_SIGNAL_SELECTOR, new Identifier(MODID, "items/katanagear"));

			// SignalPart : Block
			for (Map.Entry<String, BlockSignalPart> entry : Static.blockSignalPartList.entrySet()) {
				BlockRender.register(entry.getValue(), TileSignalPartRender::render, TileSignalPart.class);
			}

			// SignalPart : Item
			for (ItemSignalPart item : Static.itemSignalPartList) {
				BlockSignalPart block = item.getBlock();
				ItemRender.register(item, ObjItemRender.getModelFor(new Identifier(MODID, block.getPath()),
						block.getItemTranslation(), Vec3d.ZERO, block.getStates(), (float) block.getScaling().x));
			}
			break;
		case INITIALIZE:
		case SETUP:
		case RELOAD:
		case START:
		case FINALIZE:
			break;
		}
	}

	@Override
	public void serverEvent(ModEvent event) {
		// Do nothing for now
	}
}
