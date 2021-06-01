package net.landofrails.stellwand.content.items;

import java.util.Arrays;
import java.util.List;

import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.messages.EMessage;
import net.landofrails.stellwand.content.network.ServerMessagePacket;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.ICustomTexturePath;

public class ItemMagnifyingGlass extends CustomItem implements ICustomTexturePath {

	public ItemMagnifyingGlass() {
		super(LandOfSignals.MODID, "stellwand.magnifyingglass");
	}

	@Override
	public String getTexturePath() {
		return "items/mgnglass";
	}

	@Override
	public List<CreativeTab> getCreativeTabs() {
		return Arrays.asList(CustomTabs.STELLWAND_TAB);
	}

	@Override
	public ClickResult onClickBlock(Player player, World world, Vec3i pos, Hand hand, Facing facing, Vec3d inBlockPos) {
		BlockSignalStorageEntity signal = world.getBlockEntity(pos, BlockSignalStorageEntity.class);
		if (signal != null) {
			signal.setMarked(!signal.isMarked());

			if (world.isClient)
				player.sendMessage(PlayerMessage.direct(signal.isMarked() ? "Marked!" : "Mark removed!"));
		}
		return super.onClickBlock(player, world, pos, hand, facing, inBlockPos);
	}
	
	@Override
	public void onClickAir(Player player, World world, Hand hand) {

		if (world.isServer) {
			Packet packet = new ServerMessagePacket(player, EMessage.MESSAGE_ERROR1);
			// TODO: Not implemented yet
			// packet.sendToPlayer(player);
		}
		super.onClickAir(player, world, hand);
	}

}
