package net.landofrails.stellwand.content.blocks.others;

import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import net.landofrails.stellwand.content.blocks.others.BlockSender.BlockEntitySender;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.BlockItemType;
import net.landofrails.stellwand.utils.compact.AItemBlock;
import net.landofrails.stellwand.utils.compact.AItemBlockEntity;
import net.landofrails.stellwand.utils.compact.BlockItem;

public class BlockSender extends AItemBlock<BlockItem, BlockEntitySender> {

	// Instance of the ITEM
	private BlockItem item;

	// Block name
	public BlockSender() {
		super("stellwand.blocksender");
	}

	// Block/Item Translation
	@Override
	public Vec3d getTranslate(BlockItemType type) {
		return type == BlockItemType.BLOCK ? new Vec3d(0.5, 0.5, 0.5) : new Vec3d(0.5, 0.5, 0.5);
	}

	// Block/Item Scale
	@Override
	public float getScale(BlockItemType type) {
		return type == BlockItemType.BLOCK ? 0 : 0.7f;
	}

	// Block/Item Rotation
	@Override
	public Vec3d getRotation(BlockItemType type) {
		return type == BlockItemType.BLOCK ? new Vec3d(0, 0, 0) : new Vec3d(30, 30, 0);
	}

	// Block/Item (OBJ-)Path
	@Override
	public String getPath(BlockItemType type) {
		return "models/block/stellwand/blocksender.obj";
	}

	// The BlockEntity
	@Override
	protected BlockEntitySender constructBlockEntity() {
		return new BlockEntitySender();
	}

	// Return of ITEM Instance
	@Override
	public BlockItem getItem() {
		if (item == null)
			item = new BlockItem(this, "stellwand.itemblocksender", CustomTabs.STELLWAND_TAB);
		return item;
	}

	public class BlockEntitySender extends AItemBlockEntity {
		@Override
		public ItemStack onPick() {
			return new ItemStack(getItem(), 1);
		}
	}

}