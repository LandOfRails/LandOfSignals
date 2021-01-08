package net.landofrails.stellwand.content.blocks;

import java.util.ArrayList;
import java.util.List;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.BlockRender;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.render.item.ObjItemRender;
import net.landofrails.stellwand.content.blocks.others.BlockFiller;
import net.landofrails.stellwand.content.blocks.others.BlockFiller2;
import net.landofrails.stellwand.utils.AItemBlock;
import net.landofrails.stellwand.utils.BlockItemType;
import net.landofrails.stellwand.utils.ICustomRenderer;
import net.landofrails.stellwand.utils.UselessEntity;

public class CustomBlocks {

	private CustomBlocks() {

	}

	private static final List<AItemBlock<?, ?>> itemBlocks = new ArrayList<>();

	public static final BlockFiller BLOCKFILLER = new BlockFiller();
	public static final BlockFiller2 BLOCKFILLER2 = new BlockFiller2();

	public static void registerBlocks() {
		itemBlocks.add(BLOCKFILLER2);
	}

	public static void registerItemRenderers() {
		for (AItemBlock<?, ?> itemBlock : itemBlocks) {
			ICustomRenderer renderer = itemBlock;

			String path = renderer.getPath(BlockItemType.ITEM);
			Vec3d translate = renderer.getTranslate(BlockItemType.ITEM);
			Vec3d rotation = renderer.getRotation(BlockItemType.ITEM);
			float scale = renderer.getScale(BlockItemType.ITEM);

			ItemRender.register(itemBlock.getItem(), ObjItemRender
					.getModelFor(new Identifier(LandOfSignals.MODID, path), translate, rotation, null, scale));
		}
	}

	public static void registerBlockRenderers() {
		BlockRender.register(BLOCKFILLER, BLOCKFILLER::render, UselessEntity.class);

		// AItemBlocks
		for (AItemBlock<?, ?> itemBlock : itemBlocks) {
			BlockRender.register(itemBlock, itemBlock::render, itemBlock.getBlockEntityClass());
		}

	}

}
