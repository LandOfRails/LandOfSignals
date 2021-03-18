package net.landofrails.stellwand.content.entities.function;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.guis.CustomGuis;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.messages.Message;
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
	public void load(TagCompound nbt) throws SerializationException {
		super.load(nbt);
		RunTimeStorage.register(getPos(), entity);
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

		if (isAir(item) && p.getWorld().isClient) {
			if (!entity.signals.isEmpty()) {
				CustomGuis.selectSenderModes.open(player, entity.getPos());
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

			for (Vec3i signal : entity.signals) {
				BlockSignalStorageEntity s = getWorld().getBlockEntity(signal, BlockSignalStorageEntity.class);

				if (s != null) {
					s.senderModes.put(getPos(), power ? entity.modePowerOn : entity.modePowerOff);
					s.updateSignalMode();
					if (getWorld().isServer) {
						ChangeSignalModes packet = new ChangeSignalModes(signal, s.senderModes);
						packet.sendToAll();
					}
				}
			}

		}

	}

	@Override
	public void onBreak() {
		RunTimeStorage.removeSender(entity.getPos());
	}

	private boolean isAir(ItemStack item) {
		return item.is(ItemStack.EMPTY) || item.equals(ItemStack.EMPTY);
	}

}
