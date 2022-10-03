package net.landofrails.stellwand.content.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.messages.EMessage;
import net.landofrails.stellwand.content.network.ServerMessagePacket;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.ICustomTexturePath;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ItemMagnifyingGlass extends CustomItem implements ICustomTexturePath {

    private static final Random RANDOM = new Random();

    public ItemMagnifyingGlass() {
        super(LandOfSignals.MODID, "stellwand.magnifyingglass");
    }

    @Override
    public String getTexturePath() {
        return "items/mgnglass";
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(CustomTabs.STELLWAND_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        if (world.isServer) {
            ServerMessagePacket.send(player, EMessage.MESSAGE_DEPRECATED);
        }
        return ClickResult.REJECTED;
    }

    @Override
    public void onClickAir(Player player, World world, Player.Hand hand) {
        if (world.isServer) {
            ServerMessagePacket.send(player, EMessage.MESSAGE_DEPRECATED);
        }
    }

}
