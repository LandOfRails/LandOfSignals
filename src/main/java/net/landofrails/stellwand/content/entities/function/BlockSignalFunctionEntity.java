package net.landofrails.stellwand.content.entities.function;

import java.util.Map;
import java.util.Map.Entry;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import net.landofrails.stellwand.Stellwand;
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
		if (isAir(item)) {
			LoSPlayer p = new LoSPlayer(player);
			String side = Stellwand.isServer ? "Server" : "Client";
			p.direct("Side: " + side);
			p.direct("Signal: " + entity.getPos().toString());
			p.direct("Mode: " + entity.getMode());
			for (Entry<Vec3i, String> entry : entity.modes.entrySet())
				p.direct("Modes: {0}, {1}", entry.getKey().toString(), entry.getValue());
			return true;
		}
		return false;
	}

	@Override
	public void onBreak() {
		RunTimeStorage.removeSignal(entity.getPos());
	}

	public void updateSignalMode() {
		Map<String, String> blockModes = entity.renderEntity.getModes();
		Map<Vec3i, String> senderModes = entity.modes;

		String actualMode = entity.getMode();
		if (blockModes != null) {
			for (String mode : blockModes.values()) {
				if (senderModes.containsValue(mode))
					actualMode = mode;
			}
		}

		entity.setMode(actualMode);
	}

	private boolean isAir(ItemStack item) {
		return item.is(ItemStack.EMPTY) || item.equals(ItemStack.EMPTY);
	}

}
