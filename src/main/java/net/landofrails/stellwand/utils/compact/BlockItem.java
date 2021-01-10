package net.landofrails.stellwand.utils.compact;

import java.util.Arrays;
import java.util.List;

import cam72cam.mod.block.BlockEntity;
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
import net.landofrails.stellwand.content.tabs.CustomTabs;

public class BlockItem extends CustomItem {

	private AItemBlock<?, ?> block;
	private CreativeTab tab;

	public BlockItem(AItemBlock<?, ?> block, String name, CreativeTab tab) {
		super(LandOfSignals.MODID, name);
		this.block = block;
		this.tab = tab;
	}

	@Override
	public ClickResult onClickBlock(Player player, World world, Vec3i pos, Hand hand, Facing facing, Vec3d inBlockPos) {

		Vec3i target = world.isReplaceable(pos) ? pos : pos.offset(facing);

		if (isStandingInBlock(player.getBlockPosition().subtract(target)))
			return ClickResult.REJECTED;

		if (world.isAir(target) || world.isReplaceable(target)) {
			world.setBlock(target, block);
			BlockEntity blockEntity = world.getBlockEntity(target, block.getBlockEntityClass());
			if (blockEntity instanceof IRotatableBlockEntity) {
				IRotatableBlockEntity rot = (IRotatableBlockEntity) blockEntity;
				rot.setRotation(player.getRotationYawHead());
			}

			return ClickResult.ACCEPTED;
		}

		return ClickResult.REJECTED;
	}

	private boolean isStandingInBlock(Vec3i vec3i) {
		return vec3i.x == 0 && vec3i.z == 0 && (vec3i.y == 0 || vec3i.y == -1);
	}

	@Override
	public List<CreativeTab> getCreativeTabs() {
		return Arrays.asList(tab == null ? CustomTabs.STELLWAND_TAB : tab);
	}

}