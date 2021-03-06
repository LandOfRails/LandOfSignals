package net.landofrails.stellwand.content.items;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.ICustomTexturePath;
import net.landofrails.stellwand.utils.compact.LoSPlayer;

public class ItemConnector extends CustomItem implements ICustomTexturePath {

	private int variation = -1;

	public ItemConnector() {
		this(1);
	}

	public ItemConnector(int variation) {
		super(LandOfSignals.MODID, "stellwand.itemconnector" + variation);
		this.variation = variation;
	}

	@Override
	public List<CreativeTab> getCreativeTabs() {

		CreativeTab tab = variation == 1 ? CustomTabs.STELLWAND_TAB : CustomTabs.HIDDEN_TAB;
		return Arrays.asList(tab);
	}

	@Override
	public String getTexturePath() {
		return "items/stellwand/connector/itemconnector" + variation;
	}

	@Override
	public void onClickAir(Player player, World world, Hand hand) {
		if (variation != 1 && player.isCrouching()) {
			player.setHeldItem(hand, new ItemStack(CustomItems.ITEMCONNECTOR1, 1));
		}
	}

	@Override
	public ClickResult onClickBlock(Player player, World world, Vec3i pos,
			Hand hand, Facing facing, Vec3d inBlockPos) {

		ItemStack itemStack = player.getHeldItem(hand);
		TagCompound nbt = itemStack.getTagCompound();
		LoSPlayer p = new LoSPlayer(player);

		if (itemStack.is(CustomItems.ITEMCONNECTOR1)) {
			// Item type: 1 - Blank

			if (world.hasBlockEntity(pos, BlockSenderStorageEntity.class)) {
				BlockSenderStorageEntity entity = world.getBlockEntity(pos,
						BlockSenderStorageEntity.class);

				if (player.isCrouching()) {
					// Removing all signals from sender
					entity.signals.clear();
					p.direct("All connections removed. ({0}, {1}, {2})", pos.x,
							pos.y, pos.z);
				} else {
					// Change to Item type 2, set senderId
					ItemStack newItem = new ItemStack(
							CustomItems.ITEMCONNECTOR2, 1);
					newItem.getTagCompound().setUUID("senderId",
							entity.senderId);
					player.setHeldItem(hand, newItem);

					p.direct("Sender selected ({0}, {1}, {2})", pos.x, pos.y,
							pos.z);
					p.direct("Next: Click the signals you want to connect.");
				}

				return ClickResult.ACCEPTED;
			} else if (world.hasBlockEntity(pos,
					BlockSignalStorageEntity.class)) {
				BlockSignalStorageEntity entity = world.getBlockEntity(pos,
						BlockSignalStorageEntity.class);

				if (player.isCrouching()) {
					// Remove all senders from signal
					List<BlockSenderStorageEntity> list = world
							.getBlockEntities(BlockSenderStorageEntity.class);

					list.stream()
							.filter(s -> s.signals.contains(entity.signalId))
							.forEach(s -> s.signals.remove(entity.signalId));

					p.direct("All connections removed. ({0}, {1}, {2})", pos.x,
							pos.y, pos.z);

				} else {
					// Change to Item type 3, set signalId
					ItemStack newItem = new ItemStack(
							CustomItems.ITEMCONNECTOR3, 1);
					newItem.getTagCompound().setUUID("signalId",
							entity.signalId);
					player.setHeldItem(hand, newItem);

					p.direct("Sender selected ({0}, {1}, {2})", pos.x, pos.y,
							pos.z);
					p.direct("Next: Click the senders you want to connect.");
				}

				return ClickResult.ACCEPTED;
			}

		} else if (itemStack.is(CustomItems.ITEMCONNECTOR2)) {
			// Sender

			if (world.hasBlockEntity(pos, BlockSenderStorageEntity.class)) {
				// set senderId
				BlockSenderStorageEntity entity = world.getBlockEntity(pos,
						BlockSenderStorageEntity.class);

				nbt.setUUID("senderId", entity.senderId);

				p.direct("Sender selected ({0}, {1}, {2})", pos.x, pos.y,
						pos.z);
				p.direct("Next: Click the senders you want to connect.");

				return ClickResult.ACCEPTED;
			} else if (world.hasBlockEntity(pos,
					BlockSignalStorageEntity.class)) {
				// connect signal to sender
				BlockSignalStorageEntity entity = world.getBlockEntity(pos,
						BlockSignalStorageEntity.class);

				List<BlockSenderStorageEntity> list = world
						.getBlockEntities(BlockSenderStorageEntity.class);
				UUID senderId = nbt.getUUID("senderId");
				// @formatter:off
				list.stream()
						.filter(sender -> sender.senderId.equals(senderId))
						.forEach(s -> s.signals.add(entity.signalId));
				// @formatter:on

				p.direct("Signal connected to sender.");
				return ClickResult.ACCEPTED;
			}

		} else if (itemStack.is(CustomItems.ITEMCONNECTOR3)) {
			// Signal

			if (world.hasBlockEntity(pos, BlockSenderStorageEntity.class)) {
				// connect sender to signal
				BlockSenderStorageEntity entity = world.getBlockEntity(pos,
						BlockSenderStorageEntity.class);

				UUID signalId = nbt.getUUID("signalId");
				entity.signals.add(signalId);

				p.direct("Sender connected to signal.");

				return ClickResult.ACCEPTED;
			} else if (world.hasBlockEntity(pos,
					BlockSignalStorageEntity.class)) {
				// connect signal to sender
				BlockSignalStorageEntity entity = world.getBlockEntity(pos,
						BlockSignalStorageEntity.class);

				nbt.setUUID("signalId", entity.signalId);

				p.direct("Signal selected ({0}, {1}, {2})", pos.x, pos.y,
						pos.z);
				p.direct("Next: Click the signals you want to connect.");

				return ClickResult.ACCEPTED;
			}

		}

		

		return ClickResult.PASS;
	}

}
