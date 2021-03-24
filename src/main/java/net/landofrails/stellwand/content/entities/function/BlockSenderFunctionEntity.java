package net.landofrails.stellwand.content.entities.function;

import java.text.MessageFormat;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.messages.Message;
import net.landofrails.stellwand.content.network.ChangeSignalModes;
import net.landofrails.stellwand.content.network.OpenSenderGui;
import net.landofrails.stellwand.utils.compact.LoSPlayer;

public abstract class BlockSenderFunctionEntity extends BlockEntity {

	private BlockSenderStorageEntity entity;

	// Ein Kommentar damit der Client startet lol
	@SuppressWarnings("java:S112")
	public BlockSenderFunctionEntity() {
		if (this instanceof BlockSenderStorageEntity)
			entity = (BlockSenderStorageEntity) this;
		else
			throw new RuntimeException(
					"This should be a subclass of BlockSenderStorageEntity!");
	}

	@Override
	public ItemStack onPick() {
		ItemStack is = new ItemStack(CustomItems.ITEMBLOCKSENDER, 1);
		TagCompound tag = is.getTagCompound();
		tag.setString("itemId", entity.getContentPackBlockId());
		is.setTagCompound(tag);
		return is;
	}

	@Override
	public boolean onClick(Player player, Hand hand, Facing facing, Vec3d hit) {
		ItemStack item = player.getHeldItem(hand);
		LoSPlayer p = new LoSPlayer(player);

		if (isAir(item) && p.getWorld().isServer) {
			if (!entity.signals.isEmpty()) {

				Vec3i signalid = entity.signals.get(0);
				getWorld().keepLoaded(signalid);
				if (getWorld().hasBlockEntity(signalid, BlockSignalStorageEntity.class)) {
					BlockSignalStorageEntity signalEntity = getWorld().getBlockEntity(signalid, BlockSignalStorageEntity.class);
					OpenSenderGui packet = new OpenSenderGui(getPos(), signalEntity);
					packet.sendToAllAround(player.getWorld(), player.getPosition(), 1);
				} else {
					entity.signals.remove(signalid);
					String msg = Message.MESSAGE_NO_SIGNAL_FOUND.toString();
					msg = MessageFormat.format(msg, "Doesnt exist anymore, removing it..");
					p.direct(msg);
				}

			} else {
				p.direct(Message.MESSAGE_NO_SIGNALS_CONNECTED.toString());
			}

			return true;
		}

		return false;
	}

	@Override
	public void onNeighborChange(Vec3i neighbor) {
		// Info: Only called on server-side

		boolean power = getWorld().getRedstone(getPos()) > 0;
		if (entity.hasPower != power) {
			entity.hasPower = power;

			updateSignals();

		}
	}

	@SuppressWarnings("java:S3776")
	public void updateSignals() {
		for (Vec3i signal : entity.signals) {
			getWorld().keepLoaded(signal);
			BlockSignalStorageEntity s = getWorld().getBlockEntity(signal, BlockSignalStorageEntity.class);

			if (s != null) {
				if (entity.modePowerOn != null && entity.modePowerOff != null) {
					s.senderModes.put(getPos(), entity.hasPower ? entity.modePowerOn : entity.modePowerOff);
					s.updateSignalMode();
					if (getWorld().isServer) {
						ChangeSignalModes packet = new ChangeSignalModes(signal, s.senderModes);
						packet.sendToAll();
					}
				}
			} else if (getWorld().isBlockLoaded(signal)) {
				entity.signals.remove(signal);
			}
		}
	}

	@Override
	public void onBreak() {

		for (Vec3i signal : entity.signals) {
			getWorld().keepLoaded(signal);
			BlockSignalStorageEntity s = getWorld().getBlockEntity(signal, BlockSignalStorageEntity.class);

			if (s != null && s.senderModes.containsKey(getPos())) {
				s.senderModes.remove(getPos());

				s.updateSignalMode();
				if (getWorld().isServer) {
					ChangeSignalModes packet = new ChangeSignalModes(signal, s.senderModes);
					packet.sendToAll();
				}
			}
		}

		super.onBreak();
	}

	private boolean isAir(ItemStack item) {
		return item.is(ItemStack.EMPTY) || item.equals(ItemStack.EMPTY);
	}

}
