package net.landofrails.stellwand.content.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import net.landofrails.stellwand.content.messages.Message;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.ICustomTexturePath;
import net.landofrails.stellwand.utils.compact.LoSPlayer;

public class ItemConnector extends CustomItem implements ICustomTexturePath {

	// Constants
	private static final String SENDERKEY = "senderPos";
	private static final String SIGNALKEY = "signalPos";
	//

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
		return Arrays.asList(CustomTabs.STELLWAND_TAB);
	}

	@Override
	public List<ItemStack> getItemVariants(CreativeTab creativeTab) {
		List<ItemStack> items = new ArrayList<>();

		if (creativeTab == null || !creativeTab.equals(CustomTabs.STELLWAND_TAB))
			return items;

		if (variation == 1)
			items.add(new ItemStack(CustomItems.ITEMCONNECTOR1, 1));
		return items;
	}

	@Override
	public String getTexturePath() {
		return "items/itemconnector" + variation;
	}

	@Override
	public void onClickAir(Player player, World world, Hand hand) {
		if (variation != 1 && player.isCrouching()) {
			player.setHeldItem(hand, new ItemStack(CustomItems.ITEMCONNECTOR1, 1));
		}
	}

	@SuppressWarnings("java:S3776")
	@Override
	public ClickResult onClickBlock(Player player, World world, Vec3i pos,
			Hand hand, Facing facing, Vec3d inBlockPos) {

		ItemStack itemStack = player.getHeldItem(hand);
		TagCompound nbt = itemStack.getTagCompound();
		LoSPlayer p = new LoSPlayer(player);

		BlockSignalStorageEntity signalEntity = getSignal(world, pos);
		BlockSenderStorageEntity senderEntity = getSender(world, pos);

		if (itemStack.is(CustomItems.ITEMCONNECTOR1)) {
			if (signalEntity != null) {
				selectSignal(p, hand, pos);
				return ClickResult.ACCEPTED;
			} else if (senderEntity != null) {
				selectSender(p, hand, pos);
				return ClickResult.ACCEPTED;
			}
		} else if (itemStack.is(CustomItems.ITEMCONNECTOR2)) {
			if (signalEntity != null && nbt.hasKey(SENDERKEY)) {
				Vec3i senderPos = nbt.getVec3i(SENDERKEY);
				boolean d = player.isCrouching();
				BlockSenderStorageEntity sender = getSender(world, senderPos);
				if (sender.isCompatible(signalEntity)) {
					connect(world, senderPos, signalEntity.getPos(), d);
					if (p.getWorld().isServer)
						p.direct(d ? Message.MESSAGE_SIGNAL_DISCONNECTED.toString() : Message.MESSAGE_SIGNAL_CONNECTED.toString());
				} else {
					if (p.getWorld().isServer)
						p.direct(Message.MESSAGE_SIGNALS_MUST_BE_EQUAL.toString());
				}
				return ClickResult.ACCEPTED;
			} else if (senderEntity != null) {
				selectSender(p, hand, pos);
				return ClickResult.ACCEPTED;
			}
		} else if (itemStack.is(CustomItems.ITEMCONNECTOR3)) {
			if(signalEntity != null) {
				selectSignal(p, hand, pos);
				return ClickResult.ACCEPTED;
			} else if (senderEntity != null && nbt.hasKey(SIGNALKEY)) {
				Vec3i signalPos = nbt.getVec3i(SIGNALKEY);
				boolean d = player.isCrouching();
				if (senderEntity.isCompatible(getSignal(world, signalPos))) {
					connect(world, senderEntity.getPos(), signalPos, d);
					if (p.getWorld().isServer)
						p.direct(d ? Message.MESSAGE_SIGNAL_DISCONNECTED.toString() : Message.MESSAGE_SIGNAL_CONNECTED.toString());
				} else {
					if (p.getWorld().isServer)
						p.direct(Message.MESSAGE_SIGNALS_MUST_BE_EQUAL.toString());
				}

				return ClickResult.ACCEPTED;
			}
		}

		return ClickResult.PASS;
	}

	// Helper

	public BlockSignalStorageEntity getSignal(World world, Vec3i pos) {
		world.keepLoaded(pos);
		return world.getBlockEntity(pos, BlockSignalStorageEntity.class);
	}

	public BlockSenderStorageEntity getSender(World world, Vec3i pos) {
		world.keepLoaded(pos);
		return world.getBlockEntity(pos, BlockSenderStorageEntity.class);
	}

	public void selectSignal(LoSPlayer player, Hand hand, Vec3i pos) {

		if (player.getWorld().isServer) {
			ItemStack stack = new ItemStack(CustomItems.ITEMCONNECTOR3, 1);
			stack.getTagCompound().setVec3i(SIGNALKEY, pos);
			player.setHeldItem(hand, stack);
			player.direct(Message.MESSAGE_NEW_SIGNAL_SELECTED.toString(), pos.x, pos.y, pos.z);
		}
	}

	public void selectSender(LoSPlayer player, Hand hand, Vec3i pos) {
		if (player.getWorld().isServer) {
			ItemStack stack = new ItemStack(CustomItems.ITEMCONNECTOR2, 1);
			stack.getTagCompound().setVec3i(SENDERKEY, pos);
			player.setHeldItem(hand, stack);
			player.direct(Message.MESSAGE_NEW_SENDER_SELECTED.toString(), pos.x, pos.y, pos.z);
		}
	}

	public void connect(World world, Vec3i senderPos, Vec3i signalPos, boolean disconnect) {
		if (world.isServer) {
			BlockSenderStorageEntity sender = getSender(world, senderPos);
			if (!disconnect) {
				sender.signals.add(signalPos);
			} else {
				sender.signals.remove(signalPos);
			}
			sender.updateSignals();
			sender.markDirty();
		}
	}

}
