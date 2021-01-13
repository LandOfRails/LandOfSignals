package net.landofrails.stellwand.content.items;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.loader.Content;
import net.landofrails.stellwand.content.loader.ContentPackEntry;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.BlockItemType;
import net.landofrails.stellwand.utils.ICustomRenderer;

public class ItemBlockSignal extends CustomItem implements ICustomRenderer {

	public ItemBlockSignal() {
		super(LandOfSignals.MODID, "itemblocksignal");
	}

	@Override
	public List<CreativeTab> getCreativeTabs() {
		return Arrays.asList(CustomTabs.STELLWAND_TAB);
	}

	@Override
	public void onClickAir(Player player, World world, Hand hand) {

		for (Entry<ContentPackEntry, String> entry : Content.getEntries().entrySet()) {

			ContentPackEntry cpe = entry.getKey();

			player.sendMessage(PlayerMessage.direct(cpe.getItem().getModel()));

		}

	}

	@Override
	public Vec3d getTranslate(BlockItemType type) {
		return type == BlockItemType.BLOCK ? new Vec3d(0.5, 0, 0.5) : new Vec3d(0.5, 0.15, 0.5);
	}

	// Block/Item Scale
	@Override
	public float getScale(BlockItemType type) {
		return type == BlockItemType.BLOCK ? 0 : 0.7f;
	}

	// Block/Item Rotation
	@Override
	public Vec3d getRotation(BlockItemType type) {
		return type == BlockItemType.BLOCK ? new Vec3d(0, 0, 0) : new Vec3d(30, 30, 0);
	}

	// Block/Item (OBJ-)Path
	@Override
	public String getPath(BlockItemType type) {
		return "models/block/stellwand/blocksignal/blocksignal.obj";
	}

}
