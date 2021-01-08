package net.landofrails.stellwand.content.blocks.others;

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
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.blocks.others.BlockFiller2.ItemBlockFiller2;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.AItemBlock;
import net.landofrails.stellwand.utils.BlockItemType;
import net.landofrails.stellwand.utils.UselessEntity;

public class BlockFiller2 extends AItemBlock<ItemBlockFiller2, UselessEntity> {

	// Instance of the ITEM
	private static final ItemBlockFiller2 ITEM = new ItemBlockFiller2();

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
		return new UselessEntity(new ItemStack(ITEM, 1));
	}

	// Return of ITEM Instance
	@Override
	public ItemBlockFiller2 getItem() {
		return ITEM;
	}

	// BlockEntity Class
	@Override
	public Class<UselessEntity> getBlockEntityClass() {
		return UselessEntity.class;
	}

	// The ITEM class
	public static class ItemBlockFiller2 extends CustomItem {

		@Override
		public ClickResult onClickBlock(Player player, World world, Vec3i pos, Hand hand, Facing facing,
				Vec3d inBlockPos) {
			world.setBlock(pos.offset(facing), CustomBlocks.BLOCKFILLER);
			return ClickResult.ACCEPTED;
		}

		public ItemBlockFiller2() {
			super(LandOfSignals.MODID, "stellwand.itemBlockFiller2");
		}

		@Override
		public List<CreativeTab> getCreativeTabs() {
			return Arrays.asList(CustomTabs.STELLWAND_TAB);
		}

	}

}
