package net.landofrails.stellwand.content.items.blocks;

import java.util.Arrays;
import java.util.List;

import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.ICustomRenderer;

public class ItemBlockFiller extends CustomItem implements ICustomRenderer {

	public ItemBlockFiller() {
		super(LandOfSignals.MODID, "stellwand.itemBlockFiller");
	}

	@Override
	public List<CreativeTab> getCreativeTabs() {
		return Arrays.asList(CustomTabs.STELLWAND_TAB);
	}

	@Override
	public Vec3d getTranslate() {
		return new Vec3d(0.5, 0, 0.5);
	}

	@Override
	public float getScale() {
		return 0.5f;
	}

	@Override
	public String getPath() {
		return "models/block/stellwand/blockfiller.obj";
	}

	@Override
	public ClickResult onClickBlock(Player player, World world, Vec3i pos, Hand hand, Facing facing, Vec3d inBlockPos) {
		world.setBlock(pos.offset(facing), CustomBlocks.BLOCKFILLER);
		return ClickResult.ACCEPTED;
	}

	@Override
	public Vec3d getRotation() {
		return new Vec3d(45, 45, 0);
	}

}
