package net.landofrails.stellwand.content.entities.function;

import java.util.Map;
import java.util.UUID;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.storage.RunTimeStorage;
import net.landofrails.stellwand.utils.compact.LoSPlayer;

public abstract class BlockSignalFunctionEntity extends BlockEntity {

	private BlockSignalStorageEntity entity;

	@SuppressWarnings("java:S112")
	public BlockSignalFunctionEntity() {
		if (this instanceof BlockSignalStorageEntity)
			entity = (BlockSignalStorageEntity) this;
		else
			throw new RuntimeException(
					"This should be a subclass of BlockSignalStorageEntity!");
	}

	@Override
	public ItemStack onPick() {
		ItemStack is = new ItemStack(CustomItems.ITEMBLOCKSIGNAL, 1);
		TagCompound tag = is.getTagCompound();
		tag.setString("itemId", entity.getContentBlockId());
		is.setTagCompound(tag);
		return is;
	}

	@Override
	public boolean onClick(Player player, Hand hand, Facing facing, Vec3d hit) {
		ItemStack item = player.getHeldItem(hand);
		if (getWorld().isServer && item.is(ItemStack.EMPTY)) {
			LoSPlayer p = new LoSPlayer(player);
			p.direct("UUID of Signal: " + entity.signalId.toString());
			return true;
		}
		return false;
	}

	@Override
	public void onBreak() {
		RunTimeStorage.removeSignal(entity.signalId);
	}

	public void update() {
		Map<String, String> blockModes = entity.renderEntity.getModes();
		Map<UUID, String> senderModes = entity.modes;

		String actualMode = entity.renderEntity.getMode();
		for (String mode : blockModes.values()) {
			if (senderModes.containsValue(mode))
				actualMode = mode;
		}

		entity.renderEntity.setMode(actualMode);
	}

}
