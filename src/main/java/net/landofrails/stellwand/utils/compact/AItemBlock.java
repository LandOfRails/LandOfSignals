package net.landofrails.stellwand.utils.compact;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.utils.BlockItemType;
import net.landofrails.stellwand.utils.ICustomRenderer;

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

}
