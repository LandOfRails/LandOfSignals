package net.landofrails.stellwand.content.items;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.ICustomTexturePath;

public class ItemMagnifyingGlass extends CustomItem implements ICustomTexturePath {

	private static final Random RANDOM = new Random();

	public ItemMagnifyingGlass() {
		super(LandOfSignals.MODID, "stellwand.magnifyingglass");
	}

	@Override
	public String getTexturePath() {
		return "items/mgnglass";
	}

	@Override
	public List<CreativeTab> getCreativeTabs() {
		return Arrays.asList(CustomTabs.STELLWAND_TAB);
	}

	@Override
	public ClickResult onClickBlock(Player player, World world, Vec3i pos, Hand hand, Facing facing, Vec3d inBlockPos) {
		BlockSenderStorageEntity sender = world.getBlockEntity(pos, BlockSenderStorageEntity.class);
		if (sender != null && world.isClient) {

			float r = (float) (RANDOM.nextFloat() / 2f + 0.5);
			float g = (float) (RANDOM.nextFloat() / 2f + 0.5);
			float b = (float) (RANDOM.nextFloat() / 2f + 0.5);

			sender.signals.forEach(vec -> {
				BlockSignalStorageEntity signal = world.getBlockEntity(vec, BlockSignalStorageEntity.class);
				if (signal != null)
					signal.setMarked(!signal.isMarked(), new float[]{r, g, b});
			});

			return ClickResult.ACCEPTED;
		} else if (world.isServer) {
			return ClickResult.ACCEPTED;
		}
		return super.onClickBlock(player, world, pos, hand, facing, inBlockPos);
	}
	
	@Override
	public void onClickAir(Player player, World world, Hand hand) {

		if (world.isClient) {
			world.getBlockEntities(BlockSignalStorageEntity.class).forEach(signal -> signal.setMarked(false, new float[]{0, 0, 0}));
		}
		super.onClickAir(player, world, hand);
	}

}
