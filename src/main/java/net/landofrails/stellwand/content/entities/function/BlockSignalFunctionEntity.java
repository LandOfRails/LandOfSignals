package net.landofrails.stellwand.content.entities.function;

import java.util.Map.Entry;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
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
	public void load(TagCompound nbt) throws SerializationException {
		// Setzen der Variablen und TagFields:
		super.load(nbt);
		// Registrieren des Signals:
		RunTimeStorage.register(getPos(), entity);
	}

	@Override
	public ItemStack onPick() {
		ItemStack is = new ItemStack(CustomItems.ITEMBLOCKSIGNAL, 1);
		TagCompound tag = is.getTagCompound();
		tag.setString("itemId", entity.getContentPackBlockId());
		is.setTagCompound(tag);
		return is;
	}

	@Override
	public boolean onClick(Player player, Hand hand, Facing facing, Vec3d hit) {
		ItemStack item = player.getHeldItem(hand);
		LoSPlayer p = new LoSPlayer(player);
		if (isAir(item)) {
			String side = player.getWorld().isServer ? "Server" : "Client";
			p.direct("Side: {0}", side);
			p.direct("Pos: {0}", getPos().toString());
			p.direct("Displaymode: {0}", entity.displayMode);
			for (Entry<Vec3i, String> entry : entity.senderModes.entrySet()) {
				p.direct("Sender {0} Mode {1}", entry.getKey(), entry.getValue());
			}
		}
		return false;
	}

	@Override
	public void onBreak() {
		RunTimeStorage.removeSignal(entity.getPos());
	}

	private boolean isAir(ItemStack item) {
		return item.is(ItemStack.EMPTY) || item.equals(ItemStack.EMPTY);
	}

}
