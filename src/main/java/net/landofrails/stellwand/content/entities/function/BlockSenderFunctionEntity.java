package net.landofrails.stellwand.content.entities.function;

import java.util.Iterator;

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

		if (isAir(item)) {

			if (player.getWorld().isServer) {
				p.direct("Powered: " + entity.hasPower);
				p.direct("Signals ({0}):", entity.signals.size());
				for (Vec3i signal : entity.signals)
					p.direct("Signal: {0}, {1}, {2}", signal.x, signal.y, signal.z);
				return true;
			}

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
		boolean power = getWorld().getRedstone(getPos()) > 0;
		if (entity.hasPower != power) {
			entity.hasPower = power;
			update();
		}
	}

	@Override
	public void onBreak() {
		for (Vec3i signal : entity.signals) {
			BlockSignalStorageEntity signalEntity = RunTimeStorage
					.getSignal(signal);
			if (signalEntity != null) {
				signalEntity.modes.remove(entity.getPos());
				update();
			}
		}
		RunTimeStorage.removeSender(entity.getPos());
	}

	public void update() {

		Iterator<Vec3i> iterator = entity.signals.iterator();
		while (iterator.hasNext()) {
			Vec3i signal = iterator.next();
			BlockSignalStorageEntity signalEntity = RunTimeStorage
					.getSignal(signal);
			if (signalEntity == null) {
				iterator.remove();
			} else {
				if (entity.hasPower) {
					signalEntity.modes.put(entity.getPos(), entity.modePowerOn);
				} else {
					signalEntity.modes.put(entity.getPos(),
							entity.modePowerOff);
				}
				signalEntity.update();
			}
		}

	}

	private boolean isAir(ItemStack item) {
		return item.is(ItemStack.EMPTY) || item.equals(ItemStack.EMPTY);
	}

}
