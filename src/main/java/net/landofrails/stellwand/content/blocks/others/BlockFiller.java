package net.landofrails.stellwand.content.blocks.others;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.block.Material;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.utils.UselessEntity;

public class BlockFiller extends BlockTypeEntity {

	private static OBJRender renderer;
	private static OBJModel model;

	public BlockFiller() {
		super(LandOfSignals.MODID, "stellwand.blockFiller");
	}

	public StandardModel render(UselessEntity uselessEntity) {
		return new StandardModel().addCustom(() -> renderStuff(uselessEntity));
	}

	private static void renderStuff(UselessEntity uselessEntity) {
		if (renderer == null || model == null) {
			try {
				model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/stellwand/blockfiller.obj"), 0);
				renderer = new OBJRender(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
			GL11.glTranslated(0.5, 0, 0.5);
			renderer.draw();
		}
	}

	@Override
	public Material getMaterial() {
		return Material.METAL;
	}

	@Override
	protected BlockEntity constructBlockEntity() {
		return new UselessEntity();
	}

}
