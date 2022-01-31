package net.landofrails.stellwand.content.items.connector;

import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.messages.EMessage;
import net.landofrails.stellwand.content.network.ServerMessagePacket;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.ICustomTexturePath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItemConnector extends CustomItem implements ICustomTexturePath {

    private final int variation;

    public ItemConnector() {
        this(1);
    }

    public ItemConnector(int variation) {
        super(LandOfSignals.MODID, "stellwand.itemconnector" + variation);
        this.variation = variation;
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(CustomTabs.STELLWAND_TAB);
    }

    @Override
    public List<ItemStack> getItemVariants(CreativeTab creativeTab) {
        List<ItemStack> items = new ArrayList<>();

        if (creativeTab != null && !creativeTab.equals(CustomTabs.STELLWAND_TAB))
            return items;

        if (variation == 1)
            items.add(new ItemStack(CustomItems.ITEMCONNECTOR1, 1));
        return items;
    }

    @Override
    public String getTexturePath() {
        return "items/itemconnector" + variation;
    }

    @Override
    public void onClickAir(Player player, World world, Hand hand) {
        if (variation != 1 && player.isCrouching()) {
            player.setHeldItem(hand, new ItemStack(CustomItems.ITEMCONNECTOR1, 1));
        }
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos,
                                    Hand hand, Facing facing, Vec3d inBlockPos) {

        if (world.isServer) {
            ItemStack itemStack = player.getHeldItem(hand);
            if (!AItemConnector.hasConnectorData(itemStack)) {
                if (AItemConnector.suitableConnectorExists(world, pos)) {
                    AItemConnector connector = Objects.requireNonNull(AItemConnector.getConnector(world, pos));
                    boolean result = connector.connect(world, pos, player, hand);
                    return result ? ClickResult.ACCEPTED : ClickResult.REJECTED;
                } else {
                    ServerMessagePacket.send(player, EMessage.MESSAGE_BLOCK_NOT_CONNECTABLE);
                    return ClickResult.REJECTED;
                }
            } else {
                AItemConnector connector = AItemConnector.getConnector(itemStack);
                if (connector != null && connector.canConnect(world, pos)) {
                    boolean result = connector.connect(world, pos, player, hand);
                    return result ? ClickResult.ACCEPTED : ClickResult.REJECTED;
                } else if (connector != null && connector.shouldOverrideConnector(world, pos)) {
                    // Override with new Connector
                    connector = Objects.requireNonNull(AItemConnector.getConnector(world, pos));
                    boolean result = connector.connect(world, pos, player, hand);
                    return result ? ClickResult.ACCEPTED : ClickResult.REJECTED;
                } else {
                    ServerMessagePacket.send(player, EMessage.MESSAGE_BLOCK_NOT_CONNECTABLE);
                    return ClickResult.REJECTED;
                }
            }


        } else {
            return ClickResult.REJECTED;
        }
    }

}
