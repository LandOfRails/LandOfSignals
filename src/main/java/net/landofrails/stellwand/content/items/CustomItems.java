package net.landofrails.stellwand.content.items;

import java.util.ArrayList;
import java.util.List;

import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.render.item.ObjItemRender;
import net.landofrails.stellwand.content.items.blocks.ItemBlockFiller;
import net.landofrails.stellwand.utils.ICustomRenderer;
import net.landofrails.stellwand.utils.ICustomTexturePath;

public class CustomItems {

	protected static List<CustomItem> customItems = new ArrayList<>();
	public static final ItemConnector ITEMCONNECTOR1 = new ItemConnector(1);
	public static final ItemConnector ITEMCONNECTOR2 = new ItemConnector(2);
	public static final ItemConnector ITEMCONNECTOR3 = new ItemConnector(3);
	public static final ItemBlockFiller ITEMBLOCKFILLER = new ItemBlockFiller();

	public static void register() {
		customItems.add(ITEMCONNECTOR1);
		customItems.add(ITEMCONNECTOR2);
		customItems.add(ITEMCONNECTOR3);
		customItems.add(ITEMBLOCKFILLER);
	}

	// Clientsided
	public static void registerRenderers() {
		for (CustomItem item : customItems) {
			if (item instanceof ICustomTexturePath) {
				ICustomTexturePath path = (ICustomTexturePath) item;
				ItemRender.register(item, new Identifier(LandOfSignals.MODID, path.getTexturePath()));
			} else if (item instanceof ICustomRenderer) {
				ICustomRenderer renderer = (ICustomRenderer) item;

				String path = renderer.getPath();
				Vec3d translate = renderer.getTranslate();
				Vec3d rotation = renderer.getRotation();
				float scale = renderer.getScale();

				ItemRender.register(item, ObjItemRender.getModelFor(new Identifier(LandOfSignals.MODID, path),
						translate, rotation, null, scale));
			} else {
				ItemRender.register(item, new Identifier(LandOfSignals.MODID, "items/" + item.getRegistryName()));
			}
		}
	}

}
