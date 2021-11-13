package net.landofrails.stellwand.content.items.connector;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.messages.EMessage;
import net.landofrails.stellwand.content.network.ServerMessagePacket;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.utils.ICustomTexturePath;
import net.landofrails.stellwand.utils.compact.LoSPlayer;
import net.landofrails.stellwand.utils.compact.SignalContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItemConnector extends CustomItem implements ICustomTexturePath {

    // Constants
    private static final String SENDERKEY = "senderPos";
    private static final String SIGNALKEY = "signalPos";

    //

    private int variation;

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
            LoSPlayer p = new LoSPlayer(player);
            if (ConnectionHandler.Checker.isConnectable(world, pos)) {
                ItemStack itemStack = player.getHeldItem(hand);

                return Objects.requireNonNull(ConnectionHandler.Connector.connect(p, world, pos, itemStack), "ConnectionHandler should not return null!");
            } else {
                ServerMessagePacket.send(player, EMessage.MESSAGE_BLOCK_NOT_CONNECTABLE);
                return ClickResult.REJECTED;
            }
        } else {
            return ClickResult.PASS;
        }


        if (itemStack.is(CustomItems.ITEMCONNECTOR1)) {
            return onClickBlockWithItemConnector1(player, world, hand, pos);
        } else if (itemStack.is(CustomItems.ITEMCONNECTOR2)) {
            return onClickBlockWithItemConnector2(player, world, hand, pos);
        } else if (itemStack.is(CustomItems.ITEMCONNECTOR3)) {
            return onClickBlockWithItemConnector3(player, world, hand, pos);
        }

        return ClickResult.PASS;
    }

    private ClickResult onClickBlockWithItemConnector1(Player player, World world, Hand hand, Vec3i pos) {
        LoSPlayer p = new LoSPlayer(player);

        SignalContainer<?> signalEntity = getSignal(world, pos);
        BlockSenderStorageEntity senderEntity = getSender(world, pos);

        if (signalEntity != null) {
            selectSignal(p, hand, pos);
            return ClickResult.ACCEPTED;
        } else if (senderEntity != null) {
            selectSender(p, hand, pos);
            return ClickResult.ACCEPTED;
        }

        return ClickResult.PASS;
    }

    private ClickResult onClickBlockWithItemConnector3(Player player, World world, Hand hand, Vec3i pos) {

        ItemStack itemStack = player.getHeldItem(hand);
        TagCompound nbt = itemStack.getTagCompound();
        LoSPlayer p = new LoSPlayer(player);

        SignalContainer<?> signalEntity = getSignal(world, pos);
        BlockSenderStorageEntity senderEntity = getSender(world, pos);

        if (signalEntity != null) {
            selectSignal(p, hand, pos);
            return ClickResult.ACCEPTED;
        } else if (senderEntity != null && nbt.hasKey(SIGNALKEY)) {
            Vec3i signalPos = nbt.getVec3i(SIGNALKEY);
            boolean d = player.isCrouching();
            if (senderEntity.isCompatible(getSignal(world, signalPos))) {
                connect(world, senderEntity.getPos(), signalPos, d);
                if (p.getWorld().isServer)
                    ServerMessagePacket.send(player, d ? EMessage.MESSAGE_SIGNAL_DISCONNECTED : EMessage.MESSAGE_SIGNAL_CONNECTED);
            } else {
                if (p.getWorld().isServer)
                    ServerMessagePacket.send(player, EMessage.MESSAGE_SIGNALS_MUST_BE_EQUAL);
            }

            return ClickResult.ACCEPTED;
        }

        return ClickResult.PASS;
    }

    private ClickResult onClickBlockWithItemConnector2(Player player, World world, Hand hand, Vec3i pos) {

        ItemStack itemStack = player.getHeldItem(hand);
        TagCompound nbt = itemStack.getTagCompound();
        LoSPlayer p = new LoSPlayer(player);

        SignalContainer<?> signalEntity = getSignal(world, pos);
        BlockSenderStorageEntity senderEntity = getSender(world, pos);

        for (Class<? extends BlockEntity> clazz : connectableClasses) {
            if (IConnectable.class.isAssignableFrom(clazz)) {
                if (world.hasBlockEntity(pos, clazz)) {
                    connect(clazz, world, pos, itemStack, p, hand);
                }
            }
        }

        if (signalEntity != null && nbt.hasKey(SENDERKEY)) {
            Vec3i senderPos = nbt.getVec3i(SENDERKEY);
            boolean d = player.isCrouching();
            BlockSenderStorageEntity sender = getSender(world, senderPos);
            if (sender.isCompatible(signalEntity)) {
                connect(world, senderPos, signalEntity.getPos(), d);
                if (p.getWorld().isServer)
                    ServerMessagePacket.send(player, d ? EMessage.MESSAGE_SIGNAL_DISCONNECTED : EMessage.MESSAGE_SIGNAL_CONNECTED);
            } else {
                if (p.getWorld().isServer)
                    ServerMessagePacket.send(player, EMessage.MESSAGE_SIGNALS_MUST_BE_EQUAL);
            }
            return ClickResult.ACCEPTED;
        } else if (senderEntity != null) {
            selectSender(p, hand, pos);
            return ClickResult.ACCEPTED;
        }
        return ClickResult.PASS;
    }

    private static <T extends BlockEntity> void connect(Class<T> clazz, World world, Vec3i pos, ItemStack connector, LoSPlayer p, Player.Hand hand) {

        if (connector.is(CustomItems.ITEMCONNECTOR1)) {
            return onClickBlockWithItemConnector1(p, world, hand, pos);
        } else if (connector.is(CustomItems.ITEMCONNECTOR2)) {
            return onClickBlockWithItemConnector2(p, world, hand, pos);
        } else if (connector.is(CustomItems.ITEMCONNECTOR3)) {
            return onClickBlockWithItemConnector3(p, world, hand, pos);
        }

        T blockEntity = world.getBlockEntity(pos, clazz);
        if (blockEntity instanceof IConnectable) {
            ((IConnectable) blockEntity).tryConnect(connector, 0, p, hand);
        }
    }

    // Helper

    public SignalContainer<?> getSignal(World world, Vec3i pos) {
        world.keepLoaded(pos);
        boolean isSignal = SignalContainer.isSignal(world, pos);
        if (!isSignal)
            return null;
        return SignalContainer.of(world, pos);
    }

    public BlockSenderStorageEntity getSender(World world, Vec3i pos) {
        world.keepLoaded(pos);
        return world.getBlockEntity(pos, BlockSenderStorageEntity.class);
    }

    public void selectSignal(LoSPlayer player, Hand hand, Vec3i pos) {

        if (player.getWorld().isServer) {
            ItemStack stack = new ItemStack(CustomItems.ITEMCONNECTOR3, 1);
            stack.getTagCompound().setVec3i(SIGNALKEY, pos);
            player.setHeldItem(hand, stack);
            ServerMessagePacket.send(player, EMessage.MESSAGE_NEW_SIGNAL_SELECTED, "" + pos.x, "" + pos.y, "" + pos.z);

        }
    }

    public void selectSender(LoSPlayer player, Hand hand, Vec3i pos) {
        if (player.getWorld().isServer) {
            ItemStack stack = new ItemStack(CustomItems.ITEMCONNECTOR2, 1);
            stack.getTagCompound().setVec3i(SENDERKEY, pos);
            player.setHeldItem(hand, stack);
            ServerMessagePacket.send(player, EMessage.MESSAGE_NEW_SENDER_SELECTED, "" + pos.x, "" + pos.y, "" + pos.z);
        }
    }

    public void connect(World world, Vec3i senderPos, Vec3i signalPos, boolean disconnect) {
        if (world.isServer) {
            BlockSenderStorageEntity sender = getSender(world, senderPos);
            if (!disconnect) {
                sender.signals.add(signalPos);
            } else {
                sender.signals.remove(signalPos);
            }
            sender.updateSignals();
            sender.markDirty();
        }
    }

}
