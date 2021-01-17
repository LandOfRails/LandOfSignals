package net.landofrails.stellwand.content.network;

import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;

public class ChangeHandHeldItem extends Packet {

	@TagField("player")
	private Player player;
	@TagField("itemStack")
	private ItemStack itemStack;
	@TagField("hand")
	private Hand hand;

	public ChangeHandHeldItem() {

	}

	public ChangeHandHeldItem(Player player, ItemStack itemStack, Hand hand) {
		this.player = player;
		this.itemStack = itemStack;
		this.hand = hand;
	}

	@Override
	protected void handle() {
		player.setHeldItem(hand, itemStack);
	}

}
