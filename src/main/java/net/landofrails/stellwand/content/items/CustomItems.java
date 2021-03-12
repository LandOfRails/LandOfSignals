package net.landofrails.stellwand.content.items;

import java.util.ArrayList;
import java.util.List;

import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.render.item.ObjItemRender;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.utils.BlockItemType;
import net.landofrails.stellwand.utils.ICustomRenderer;
import net.landofrails.stellwand.utils.ICustomTexturePath;

public class CustomItems {

	private CustomItems() {

	}

	protected static List<CustomItem> itemList = new ArrayList<>();
	public static final ItemConnector ITEMCONNECTOR1 = new ItemConnector(1);
	public static final ItemConnector ITEMCONNECTOR2 = new ItemConnector(2);
	public static final ItemConnector ITEMCONNECTOR3 = new ItemConnector(3);

	// Blocks
	public static final ItemBlockFiller ITEMBLOCKFILLER = new ItemBlockFiller();
	public static final ItemBlockSignal ITEMBLOCKSIGNAL = new ItemBlockSignal();
	public static final ItemBlockSender ITEMBLOCKSENDER = new ItemBlockSender();

	public static void register() {
		itemList.add(ITEMCONNECTOR1);
		itemList.add(ITEMCONNECTOR2);
		itemList.add(ITEMCONNECTOR3);

		ItemRender.register(ITEMBLOCKFILLER, ItemBlockFiller.getModelFor());
		ItemRender.register(ITEMBLOCKSIGNAL, ItemBlockSignal.getModelFor());
		ItemRender.register(ITEMBLOCKSENDER, ItemBlockSender.getModelFor());

	}

	// Clientsided
	public static void registerRenderers() {
		for (CustomItem item : itemList) {
			if (item instanceof ICustomTexturePath) {
				ICustomTexturePath path = (ICustomTexturePath) item;
				ItemRender.register(item, new Identifier(Stellwand.DOMAIN, path.getTexturePath()));
			} else if (item instanceof ICustomRenderer) {
				ICustomRenderer renderer = (ICustomRenderer) item;

				String path = renderer.getPath(BlockItemType.ITEM);
				Vec3d translate = renderer.getTranslate(BlockItemType.ITEM);
				Vec3d rotation = renderer.getRotation(BlockItemType.ITEM);
				float scale = renderer.getScale(BlockItemType.ITEM);

				ItemRender.register(item, ObjItemRender.getModelFor(new Identifier(Stellwand.DOMAIN, path),
						translate, rotation, null, scale));
			} else {
				ItemRender.register(item, new Identifier(Stellwand.DOMAIN, "items/" + item.getRegistryName()));
			}
		}
	}

}
