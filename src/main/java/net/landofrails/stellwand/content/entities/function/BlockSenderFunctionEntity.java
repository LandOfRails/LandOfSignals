package net.landofrails.stellwand.content.entities.function;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.messages.EMessage;
import net.landofrails.stellwand.content.network.ChangeSignalModes;
import net.landofrails.stellwand.content.network.OpenSenderGui;
import net.landofrails.stellwand.content.network.ServerMessagePacket;
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
	public IBoundingBox getBoundingBox() {
		return IBoundingBox.BLOCK;
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

		ItemStack heldItem = player.getHeldItem(hand);
		if (heldItem != null && heldItem.is(CustomItems.ITEMMAGNIFYINGGLASS))
			return false;

		if (isAir(item) && p.getWorld().isServer) {
			if (!entity.signals.isEmpty()) {

				Vec3i signalid = entity.signals.get(0);
				getWorld().keepLoaded(signalid);
				if (getWorld().hasBlockEntity(signalid, BlockSignalStorageEntity.class)) {
					BlockSignalStorageEntity signalEntity = getWorld().getBlockEntity(signalid, BlockSignalStorageEntity.class);
					OpenSenderGui packet = new OpenSenderGui(getPos(), signalEntity);
					packet.sendToAllAround(player.getWorld(), player.getPosition(), 1.0f);
				} else {
					entity.signals.remove(signalid);

					ServerMessagePacket.send(player, EMessage.MESSAGE_NO_SIGNAL_FOUND, EMessage.MESSAGE_ERROR1.getRaw());
				}

			} else {
				ServerMessagePacket.send(player, EMessage.MESSAGE_NO_SIGNALS_CONNECTED);
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

		getWorld().dropItem(onPick(), getPos());
	}

	private boolean isAir(ItemStack item) {
		return item.is(ItemStack.EMPTY) || item.equals(ItemStack.EMPTY);
	}

}
