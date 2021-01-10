package net.landofrails.stellwand.utils.compact;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
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

	protected OBJRender renderer;
	protected OBJModel model;

	public AItemBlock(String name) {
		super(LandOfSignals.MODID, name);
	}

	@Override
	protected abstract T constructBlockEntity();

	@SuppressWarnings("unchecked")
	public Class<T> getBlockEntityClass() {
		return (Class<T>) this.constructBlockEntity().getClass();
	}

	public abstract I getItem();

	public StandardModel render(BlockEntity blockEntity) {
		return new StandardModel().addCustom(() -> renderStuff(this, blockEntity));
	}

	public void renderStuff(AItemBlock<?, ?> aItemBlock, BlockEntity blockEntity) {

		if (renderer == null || model == null) {
			try {
				ModCore.Mod.info("Registering OBJ: " + this.getPath(BlockItemType.BLOCK),
						this.getPath(BlockItemType.BLOCK));
				model = new OBJModel(new Identifier(LandOfSignals.MODID, this.getPath(BlockItemType.BLOCK)), 0);
				renderer = new OBJRender(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (renderer == null)
			return;
		try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
			Vec3d translate = this.getTranslate(BlockItemType.BLOCK);
			GL11.glTranslated(translate.x, translate.y, translate.z);
			Vec3d rotation = this.getRotation(BlockItemType.BLOCK);
			GL11.glRotated(1, rotation.x, rotation.y, rotation.z);
			renderer.draw();
		}

	}

}
