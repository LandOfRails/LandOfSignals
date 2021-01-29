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
import net.landofrails.stellwand.content.blocks.others.BlockSender;
import net.landofrails.stellwand.content.blocks.others.BlockSignal;
import net.landofrails.stellwand.content.entities.BlockSignalEntity;
import net.landofrails.stellwand.utils.BlockItemType;
import net.landofrails.stellwand.utils.ICustomRenderer;
import net.landofrails.stellwand.utils.compact.AItemBlock;
import net.landofrails.stellwand.utils.exceptions.DuplicateBlockEntitiesException;

public class CustomBlocks {

	private CustomBlocks() {

	}

	private static final List<AItemBlock<?, ?>> itemBlocks = new ArrayList<>();

	public static final BlockFiller BLOCKFILLER = new BlockFiller();
	public static final BlockSender BLOCKSENDER = new BlockSender();
	public static final BlockSignal BLOCKSIGNAL = new BlockSignal();

	public static void registerBlocks() {
		itemBlocks.add(BLOCKSENDER);
		itemBlocks.add(BLOCKFILLER);
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

		List<Class<?>> classes = new ArrayList<>();
		for (AItemBlock<?, ?> itemBlock : itemBlocks) {
			Class<?> c = itemBlock.getBlockEntityClass();
			if (!classes.contains(c))
				classes.add(c);
			else {
				throw new DuplicateBlockEntitiesException("BlockEntity is used multiple times: " + c.getName());
			}
		}

		classes.clear();

		// AItemBlocks
		for (AItemBlock<?, ?> itemBlock : itemBlocks) {
			BlockRender.register(itemBlock, itemBlock::render, itemBlock.getBlockEntityClass());
		}

		// Others
		BlockRender.register(BLOCKSIGNAL, BlockSignalEntity::render, BlockSignalEntity.class);

	}

}
