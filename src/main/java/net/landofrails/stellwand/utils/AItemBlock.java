package net.landofrails.stellwand.utils;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;

public abstract class AItemBlock<I extends CustomItem, T extends BlockEntity> extends BlockTypeEntity
		implements ICustomRenderer {

	private OBJRender renderer;
	private OBJModel model;

	public AItemBlock(String name) {
		super(LandOfSignals.MODID, name);
	}

	@Override
	protected abstract T constructBlockEntity();

	public abstract Class<T> getBlockEntityClass();

	public abstract I getItem();

	public StandardModel render(BlockEntity blockEntity) {
		return new StandardModel().addCustom(() -> renderStuff(this, blockEntity));
	}

	public void renderStuff(AItemBlock<?, ?> aItemBlock, BlockEntity blockEntity) {

		if (renderer == null || model == null) {
			try {
				model = new OBJModel(new Identifier(LandOfSignals.MODID, aItemBlock.getPath(BlockItemType.BLOCK)), 0);
				renderer = new OBJRender(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (renderer == null)
			return;
		try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
			GL11.glTranslated(0.5, 0, 0.5);
			renderer.draw();
		}
	}

	// Just an example
	public abstract static class ABlockItem extends CustomItem {

		private AItemBlock<CustomItem, BlockEntity> block;

		public ABlockItem(AItemBlock<CustomItem, BlockEntity> block, CreativeTab tab) {
			super(LandOfSignals.MODID, "item" + block.id.getPath());
			this.block = block;
		}

		@Override
		public ClickResult onClickBlock(Player player, World world, Vec3i pos, Hand hand, Facing facing,
				Vec3d inBlockPos) {

			if (world.isAir(pos) || world.isReplaceable(pos)) {
				world.setBlock(pos.offset(facing), block);
				BlockEntity blockEntity = world.getBlockEntity(pos, block.getBlockEntityClass());
				if (blockEntity instanceof ARotatableBlockEntity) {
					ARotatableBlockEntity rot = (ARotatableBlockEntity) blockEntity;
					rot.setRotation(player.getRotationYawHead());
				}
				return ClickResult.ACCEPTED;
			}

			return ClickResult.REJECTED;
		}

	}

	//
	public abstract static class ARotatableBlockEntity extends BlockEntity {

		protected abstract void setRotation(float rotationYawHead);

	}

}
