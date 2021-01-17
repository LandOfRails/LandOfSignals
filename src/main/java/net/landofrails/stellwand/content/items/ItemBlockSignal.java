package net.landofrails.stellwand.content.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.guis.SelectItem;
import net.landofrails.stellwand.content.loader.Content;
import net.landofrails.stellwand.content.loader.ContentPackEntry;
import net.landofrails.stellwand.content.loader.ContentPackEntry.ContentPackEntryItem;
import net.landofrails.stellwand.content.network.ChangeHandHeldItem;
import net.landofrails.stellwand.content.tabs.CustomTabs;

public class ItemBlockSignal extends CustomItem {

	// VARIABLES
	public static final String MISSING = "missing";
	private static Map<String, OBJModel> models = new HashMap<>();
	private static Map<String, OBJRender> renderers = new HashMap<>();
	private static Map<String, float[]> rotations = new HashMap<>();
	private static Map<String, float[]> translations = new HashMap<>();
	private static Map<String, String> modes = new HashMap<>();

	//

	public ItemBlockSignal() {
		super(LandOfSignals.MODID, "stellwand.itemblocksignal");
	}

	// Only for Clientside
	public static void init() {
		if (renderers.isEmpty()) {
			try {
				// TODO CHANGE TEXTURE
				Identifier id = new Identifier(LandOfSignals.MODID, "models/block/landofsignals/so12/signalso12.obj");
				OBJModel model = new OBJModel(id, 0);
				models.put(MISSING, model);
				// Renderers in render function
				rotations.put(MISSING, new float[] { 0, 0, 0 });
				translations.put(MISSING, new float[] { 0, 0, 0 });
				modes.put(MISSING, null);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// ContentPack
			for (Entry<ContentPackEntry, String> entry : Content.getEntries().entrySet()) {
				try {
					ContentPackEntry cpe = entry.getKey();
					String packId = entry.getValue();
					String itemName = cpe.getBlockId(packId);
					ContentPackEntryItem item = cpe.getItem();
					Identifier id = new Identifier("stellwand", item.getModel());
					OBJModel model = new OBJModel(id, 0);

					models.put(itemName, model);
					// Renderers in render function
					rotations.put(itemName, item.getRotation());
					translations.put(itemName, item.getTranslation());
					modes.put(itemName, item.getMode());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	public List<CreativeTab> getCreativeTabs() {
		return Arrays.asList(CustomTabs.STELLWAND_TAB);
	}

	@Override
	public void onClickAir(Player player, World world, Hand hand) {

		if (world.isServer)
			return;

		List<ItemStack> itemStackList = new ArrayList<>();

		itemStackList.add(new ItemStack(CustomItems.ITEMBLOCKSIGNAL, 1));
		itemStackList.add(new ItemStack(CustomItems.ITEMCONNECTOR1, 1));

		for (Entry<ContentPackEntry, String> entry : Content.getEntries().entrySet()) {

			ContentPackEntry cpe = entry.getKey();
			ItemStack is = new ItemStack(CustomItems.ITEMBLOCKSIGNAL, 1);
			TagCompound tag = is.getTagCompound();
			tag.setString("itemId", cpe.getBlockId(entry.getValue()));
			is.setTagCompound(tag);
			itemStackList.add(is);

		}

		SelectItem si = new SelectItem();
		si.open(player, itemStackList, item -> {
			if (item != null) {
				player.setHeldItem(hand, item);
				ChangeHandHeldItem packet = new ChangeHandHeldItem(player, item, hand);
				packet.sendToServer();
			}
		});

	}

	@SuppressWarnings({ "java:S3776", "java:S112" })
	public static ItemRender.IItemModel getModelFor() {

		return (world, stack) -> new StandardModel().addCustom(() -> {

			TagCompound tag = stack.getTagCompound();
			String itemId = tag.getString("itemId");
			if (itemId == null || !models.containsKey(itemId)) {
				itemId = MISSING;
			}

			if (renderers.get(itemId) == null) {
				OBJModel model = models.get(itemId);
				renderers.put(itemId, new OBJRender(model));
			}

			OBJRender renderer = renderers.get(itemId);

			float[] translate = translations.get(itemId);
			float[] rotation = rotations.get(itemId);
			String mode = modes.get(itemId);
			int scale = 1;
			try (OpenGL.With ignored = OpenGL.matrix(); OpenGL.With ignored1 = renderer.bindTexture()) {
				GL11.glTranslated(translate[0], translate[1], translate[2]);
				GL11.glRotated(1, rotation[0], rotation[1], rotation[2]);
				GL11.glScaled(scale, scale, scale);
				if (mode == null) {
					renderer.draw();
				} else {
					renderer.drawGroups(Arrays.asList("general", mode));
				}
			}
		});
	}

	@Override
	public List<String> getTooltip(ItemStack itemStack) {
		String lore = "";
		TagCompound tag = itemStack.getTagCompound();
		if (tag.hasKey("itemId")) {
			String itemId = tag.getString("itemId");
			lore = Content.getNameForId(itemId);
		}
		return Arrays.asList(lore);
	}

}
