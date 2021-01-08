package net.landofrails.stellwand.content.blocks.others;

import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.BlockItemType;
import net.landofrails.stellwand.utils.UselessEntity;
import net.landofrails.stellwand.utils.compact.AItemBlock;
import net.landofrails.stellwand.utils.compact.BlockItem;

public class BlockFiller2 extends AItemBlock<BlockItem, UselessEntity> {

	// Instance of the ITEM
	private BlockItem item;

	// Block name
	public BlockFiller2() {
		super("stellwand.blockFiller2");
	}

	// Block/Item Translation
	@Override
	public Vec3d getTranslate(BlockItemType type) {
		return type == BlockItemType.BLOCK ? new Vec3d(0, 0, 0) : new Vec3d(0.5, 0.25, 0.5);
	}

	// Block/Item Scale
	@Override
	public float getScale(BlockItemType type) {
		return type == BlockItemType.BLOCK ? 0 : 0.5f;
	}

	// Block/Item Rotation
	@Override
	public Vec3d getRotation(BlockItemType type) {
		return type == BlockItemType.BLOCK ? new Vec3d(0, 0, 0) : new Vec3d(30, 30, 0);
	}

	// Block/Item (OBJ-)Path
	@Override
	public String getPath(BlockItemType type) {
		return "models/block/stellwand/blockfiller.obj";
	}

	// The BlockEntity
	@Override
	protected UselessEntity constructBlockEntity() {
		return new UselessEntity(new ItemStack(getItem(), 1));
	}

	// Return of ITEM Instance
	@Override
	public BlockItem getItem() {
		if (item == null)
			item = new BlockItem(this, "stellwand.itemBlockFiller2", CustomTabs.STELLWAND_TAB);
		return item;
	}

	// BlockEntity Class
	@Override
	public Class<UselessEntity> getBlockEntityClass() {
		return UselessEntity.class;
	}

}
