package net.landofrails.stellwand.content.entities.rendering;

import static net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity.MISSING;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.ModCore;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.loader.Content;
import net.landofrails.stellwand.content.loader.ContentPackEntry;
import net.landofrails.stellwand.content.loader.ContentPackEntry.ContentPackEntryBlock;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;

public class BlockSenderRenderEntity implements IRotatableBlockEntity {

	// Entity
	private BlockSenderStorageEntity entity;

	// Static values
	private static Map<String, OBJModel> models = new HashMap<>();
	private static Map<String, OBJRender> renderers = new HashMap<>();
	private static Map<String, float[]> rotations = new HashMap<>();
	private static Map<String, float[]> translations = new HashMap<>();

	// Rendering
	private OBJModel model;
	private OBJRender renderer;
	private float[] rotation;
	private float[] translation;
	//

	public BlockSenderRenderEntity(BlockSenderStorageEntity entity) {
		this.entity = entity;
	}

	// Standard Methods
	@Override
	public void setRotation(float rotationYawHead) {
		entity.rot = -Math.round(rotationYawHead / 90) * 90f;
	}

	//

	// Getter
	public OBJModel getModel() {

		if (model == null) {
			if (entity.contentPackBlockId != null
					&& models.containsKey(entity.contentPackBlockId))
				model = models.get(entity.contentPackBlockId);
			else
				model = models.get(MISSING);
		}

		return model;
	}

	public OBJRender getRenderer() {
		if (renderer == null) {
			if (entity.contentPackBlockId != null
					&& renderers.containsKey(entity.contentPackBlockId))
				renderer = renderers.get(entity.contentPackBlockId);
			else
				renderer = renderers.get(MISSING);
		}
		return renderer;
	}

	public float[] getTranslation() {
		if (translation == null) {
			if (entity.contentPackBlockId != null
					&& translations.containsKey(entity.contentPackBlockId))
				translation = translations.get(entity.contentPackBlockId);
			else
				translation = new float[] { 0.5f, 0, 0.5f };
		}
		return translation;
	}

	public float[] getRotation() {
		if (rotation == null) {
			if (entity.contentPackBlockId != null
					&& rotations.containsKey(entity.contentPackBlockId))
				rotation = rotations.get(entity.contentPackBlockId);
			else
				rotation = new float[] { 0, 0, 0 };
		}
		return rotation;
	}

	public float getRot() {
		return entity.rot;
	}
	//

	// Rendering
	public static StandardModel render(BlockSenderStorageEntity entity) {
		return new StandardModel().addCustom(partialTicks -> renderStuff(entity, partialTicks));
	}

	public static void check(boolean isClient) {
		if (models.isEmpty() && renderers.isEmpty()) {
			try {
				Identifier id = new Identifier(Stellwand.DOMAIN, "models/block/others/blocknotfound/blocknotfound.obj");
				OBJModel m = new OBJModel(id, 0);
				models.put(MISSING, m);
				rotations.put(MISSING, new float[] { 0, 0, 0 });
				translations.put(MISSING, new float[] { 0.5f, 0.5f, 0.5f });
				if (isClient)
					renderers.put(MISSING, new OBJRender(m));
			} catch (Exception e) {
				ModCore.Mod.error(e.getMessage());
			}
			// Add contentpack stuff
			for (Entry<ContentPackEntry, String> entry : Content.getBlockSenders().entrySet()) {
				try {
					ContentPackEntry cpe = entry.getKey();
					String packId = entry.getValue();
					String blockId = cpe.getBlockId(packId);
					ContentPackEntryBlock block = cpe.getBlock();
					String objPath = cpe.getModel();
					Identifier id = new Identifier("stellwand", objPath);
					OBJModel m = new OBJModel(id, 0);
					models.put(blockId, m);
					rotations.put(blockId, block.getRotation());
					translations.put(blockId, block.getTranslation());
					if (isClient)
						renderers.put(blockId, new OBJRender(m));
				} catch (Exception e) {
					ModCore.Mod.error(e.getMessage());
				}
			}
		}
	}

	@SuppressWarnings("java:S1172")
	private static void renderStuff(BlockSenderStorageEntity entity,
			float partialTicks) {

		check(entity.getWorld().isClient);

		OBJModel model = entity.renderEntity.getModel();
		OBJRender renderer = entity.renderEntity.getRenderer();
		float[] translation = entity.renderEntity.getTranslation();
		float[] rotation = entity.renderEntity.getRotation();

		try {
			if (renderer == null || model == null) {
				ModCore.warn("Block has no renderer!!");
				return;
			}
			try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
				GL11.glTranslated(translation[0], translation[1], translation[2]);

				GL11.glRotated(rotation[0], 1, 0, 0);
				GL11.glRotated(entity.renderEntity.getRot() + rotation[1], 0, 1,
						0);
				GL11.glRotated(rotation[2], 0, 0, 1);

				renderer.draw();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
