package net.landofrails.stellwand.content.network;

import java.util.Arrays;
import java.util.List;

import cam72cam.mod.ModCore;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.content.items.CustomItems;

public class ChangeHandHeldItem extends Packet {

	@TagField("player")
	private Player player;
	@TagField("itemStack")
	private ItemStack itemStack;
	@TagField("hand")
	private Hand hand;

	private List<CustomItem> validItems;

	public ChangeHandHeldItem() {
		validItems = Arrays.asList(CustomItems.ITEMBLOCKFILLER, CustomItems.ITEMBLOCKSENDER,
				CustomItems.ITEMBLOCKSIGNAL);
	}

	public ChangeHandHeldItem(Player player, ItemStack itemStack, Hand hand) {
		this.player = player;
		this.itemStack = itemStack;
		this.hand = hand;
	}

	@Override
	protected void handle() {
		if (isFromStellwand(itemStack))
			player.setHeldItem(hand, itemStack);
		else {
			ModCore.warn("Invalid Item in ChangeHandHeldItem Event:");
			ModCore.warn("ItemStack: " + itemStack.getDisplayName() + " NBT: " + itemStack.getTagCompound().toString());
			ModCore.warn("Player's UUID: " + player.getUUID());
		}

	}

	private boolean isFromStellwand(ItemStack itemStack) {

		for (CustomItem item : validItems) {
			if (itemStack.is(item))
				return true;
		}
		return false;
	}

}
