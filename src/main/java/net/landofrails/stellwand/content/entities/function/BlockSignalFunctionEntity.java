package net.landofrails.stellwand.content.entities.function;

import java.util.UUID;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.util.Facing;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;

public abstract class BlockSignalFunctionEntity extends BlockEntity {

	private BlockSignalStorageEntity entity;

	public BlockSignalFunctionEntity() {
		if (this instanceof BlockSignalStorageEntity)
			entity = (BlockSignalStorageEntity) this;
		else
			throw new RuntimeException(
					"This should be a subclass of BlockSignalStorageEntity!");
		// No code
	}

	@Override
	public ItemStack onPick() {
		ItemStack is = new ItemStack(CustomItems.ITEMBLOCKSIGNAL, 1);
		TagCompound tag = is.getTagCompound();
		tag.setString("itemId", entity.contentPackBlockId);
		is.setTagCompound(tag);
		return is;
	}

	@Override
	public boolean onClick(Player player, Hand hand, Facing facing, Vec3d hit) {
		if (entity.senders.size() < 3) {
			while (entity.senders.size() < 3)
				entity.senders.add(UUID.randomUUID());
			this.markDirty();
		}

		player.sendMessage(PlayerMessage.direct("Start of UUIDs"));
		for (UUID uuid : entity.senders)
			player.sendMessage(PlayerMessage.direct(uuid.toString()));
		player.sendMessage(PlayerMessage.direct("End of UUIDs"));

		return true;
	}

}
