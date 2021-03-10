package net.landofrails.stellwand.content.entities.function;

import java.util.Iterator;
import java.util.UUID;

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
import net.landofrails.stellwand.content.guis.CustomGuis;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.network.ChangeSignalModes;
import net.landofrails.stellwand.storage.RunTimeStorage;
import net.landofrails.stellwand.utils.compact.LoSPlayer;

public abstract class BlockSenderFunctionEntity extends BlockEntity {

	private BlockSenderStorageEntity entity;

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
		tag.setString("itemId", entity.getContentBlockId());
		is.setTagCompound(tag);
		return is;
	}

	@Override
	public boolean onClick(Player player, Hand hand, Facing facing, Vec3d hit) {
		ItemStack item = player.getHeldItem(hand);
		LoSPlayer p = new LoSPlayer(player);

		if (getWorld().isServer)
			return true;

		if (item.isEmpty()) {

			if (!entity.signals.isEmpty()) {
				p.direct("Select modes");
				CustomGuis.selectSenderModes.open(player, entity.getPos());
			} else {
				p.direct("Sender must have atleast 1 signal connected");
			}

			return true;
		}
		return false;
	}

	@Override
	public void onNeighborChange(Vec3i neighbor) {
		if (!getWorld().isServer)
			return;

		boolean power = getWorld().getRedstone(getPos()) > 0;
		if (entity.hasPower != power) {
			entity.hasPower = power;
			update();
		}

	}

	@Override
	public void onBreak() {
		if (!getWorld().isServer)
			return;

		for (UUID signal : entity.signals) {
			BlockSignalStorageEntity signalEntity = RunTimeStorage
					.getSignal(signal);
			if (signalEntity != null) {
				signalEntity.modes.remove(entity.senderId);
				update();
			}
		}
		RunTimeStorage.removeSender(entity.senderId);
	}

	public void update() {
		if (!getWorld().isServer)
			return;

		Iterator<UUID> iterator = entity.signals.iterator();
		while (iterator.hasNext()) {
			UUID signal = iterator.next();
			BlockSignalStorageEntity signalEntity = RunTimeStorage
					.getSignal(signal);
			if (signalEntity == null) {
				iterator.remove();
			} else {
				if (entity.hasPower) {
					signalEntity.modes.put(entity.senderId, entity.modePowerOn);
				} else {
					signalEntity.modes.put(entity.senderId,
							entity.modePowerOff);
				}
				new ChangeSignalModes(signalEntity.getPos(), signalEntity.modes)
						.sendToAll();
			}
		}

	}

}
