package net.landofrails.stellwand.content.entities.rendering;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.util.Facing;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.loader.Content;
import net.landofrails.stellwand.content.loader.ContentPackEntry;
import net.landofrails.stellwand.content.loader.ContentPackEntry.ContentPackEntryBlock;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;

public class BlockFillerRenderEntity extends BlockEntity implements IRotatableBlockEntity {

	// Global static variables
	public static final String MISSING = "missing";

	// Global TagFields
	@TagField
	private String contentPackBlockId = MISSING;

	@TagField
	private float rot = 0;

	private static Map<String, OBJModel> models = new HashMap<>();
	private static Map<String, OBJRender> renderers = new HashMap<>();
	private static Map<String, float[]> rotations = new HashMap<>();
	private static Map<String, float[]> translations = new HashMap<>();

	// Storing data
	// Rendering
	private OBJModel model;
	private OBJRender renderer;
	private float[] rotation;
	private float[] translation;
	//

	// Standard Methods
	@Override
	public void setRotation(float rotationYawHead) {
		rot = -Math.round(rotationYawHead / 90) * 90f;
	}

	@Override
	public ItemStack onPick() {
		ItemStack is = new ItemStack(CustomItems.ITEMBLOCKFILLER, 1);
		TagCompound tag = is.getTagCompound();
		tag.setString("itemId", contentPackBlockId);
		is.setTagCompound(tag);
		return is;
	}
	//

	// Getter
	public OBJModel getModel() {

		if (model == null) {
			if (contentPackBlockId != null && models.containsKey(contentPackBlockId))
				model = models.get(contentPackBlockId);
			else
				model = models.get(MISSING);
		}

		return model;
	}

	public OBJRender getRenderer() {
		if (renderer == null) {
			if (contentPackBlockId != null && renderers.containsKey(contentPackBlockId))
				renderer = renderers.get(contentPackBlockId);
			else
				renderer = renderers.get(MISSING);
		}
		return renderer;
	}

	public float[] getTranslation() {
		if (translation == null) {
			if (contentPackBlockId != null && translations.containsKey(contentPackBlockId))
				translation = translations.get(contentPackBlockId);
			else
				translation = new float[] { 0.5f, 0, 0.5f };
		}
		return translation;
	}

	public float[] getRotation() {
		if (rotation == null) {
			if (contentPackBlockId != null && rotations.containsKey(contentPackBlockId))
				rotation = rotations.get(contentPackBlockId);
			else
				rotation = new float[] { 0, 0, 0 };
		}
		return rotation;
	}

	public float getRot() {
		return rot;
	}
	//

	// ID
	public void setContentBlockId(String id) {
		this.contentPackBlockId = id;
	}
	//

	// Rendering
	public static StandardModel render(BlockFillerRenderEntity entity) {
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
			for (Entry<ContentPackEntry, String> entry : Content.getBlockFillers().entrySet()) {
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
	private static void renderStuff(BlockFillerRenderEntity entity, float partialTicks) {

		check(entity.getWorld().isClient);

		OBJModel model = entity.getModel();
		OBJRender renderer = entity.getRenderer();
		float[] translation = entity.getTranslation();
		float[] rotation = entity.getRotation();

		try {
			if (renderer == null || model == null) {
				ModCore.warn("Block has no renderer!!");
				return;
			}
			try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
				GL11.glTranslated(translation[0], translation[1], translation[2]);

				GL11.glRotated(rotation[0], 1, 0, 0);
				GL11.glRotated(entity.getRot() + rotation[1], 0, 1, 0);
				GL11.glRotated(rotation[2], 0, 0, 1);

				renderer.draw();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Events
	@Override
	public boolean onClick(Player player, Hand hand, Facing facing, Vec3d hit) {

		player.sendMessage(PlayerMessage.direct("Rotation: " + rot));
		float[] r = getRotation();
		player.sendMessage(PlayerMessage.direct("Rotations: " + r[0] + ", " + r[1] + ", " + r[2]));
		r = rotations.get(contentPackBlockId);
		if (r != null)
			player.sendMessage(PlayerMessage.direct("Rotations2: " + r[0] + ", " + r[1] + ", " + r[2]));
		player.sendMessage(PlayerMessage.direct("Id: " + contentPackBlockId));

		return true;
	}

}
