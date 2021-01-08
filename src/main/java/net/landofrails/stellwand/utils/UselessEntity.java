package net.landofrails.stellwand.utils;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.item.ItemStack;

public class UselessEntity extends BlockEntity {

	private ItemStack itemStack;

	public UselessEntity() {

	}

	public UselessEntity(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	@Override
	public ItemStack onPick() {
		return itemStack;
	}

}
