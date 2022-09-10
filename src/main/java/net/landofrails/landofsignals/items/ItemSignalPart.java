package net.landofrails.landofsignals.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.configs.LandOfSignalsConfig;
import net.landofrails.landofsignals.configs.LegacyMode;
import net.landofrails.landofsignals.packet.legacymode.LegacyModePromptToClientPacket;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;
import net.landofrails.landofsignals.utils.Static;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("java:S1192")
public class ItemSignalPart extends CustomItem {

    public ItemSignalPart(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.HIDDEN_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        Optional<Vec3i> target = LandOfSignalsUtils.canPlaceBlock(world, pos, facing, player);
        if (!target.isPresent()) return ClickResult.REJECTED;

        String itemId = player.getHeldItem(hand).getTagCompound().getString("itemId");

        float rotationSteps = LOSBlocks.BLOCK_SIGNAL_PART.getRotationSteps(itemId);

        int rotation = (int) (-(Math.round(player.getRotationYawHead() / rotationSteps) * rotationSteps) + 180);
        TileSignalPart tileSignalPart = world.getBlockEntity(pos, TileSignalPart.class);
        if (tileSignalPart != null && !player.isCrouching()) rotation = tileSignalPart.getBlockRotate();

        if (LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(itemId)) {
            if (world.isServer && LandOfSignalsConfig.legacyMode == LegacyMode.PROMPT) {
                new LegacyModePromptToClientPacket(target.get(), itemId, rotation).sendToPlayer(player);
                return ClickResult.ACCEPTED;
            } else if (!world.isServer && world.isClient) {
                return ClickResult.ACCEPTED;
            }
        }
        LOSBlocks.BLOCK_SIGNAL_PART.setRot(rotation);
        LOSBlocks.BLOCK_SIGNAL_PART.setId(itemId);
        LegacyMode legacyMode = LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(itemId) ? LandOfSignalsConfig.legacyMode : LegacyMode.OFF;
        LOSBlocks.BLOCK_SIGNAL_PART.setLegacyMode(legacyMode);
        world.setBlock(target.get(), LOSBlocks.BLOCK_SIGNAL_PART);
        return ClickResult.ACCEPTED;

    }

    @Override
    public void onClickAir(Player player, World world, Player.Hand hand) {
        if (world.isServer) {
            String itemId = player.getHeldItem(hand).getTagCompound().getString("itemId");
            if (!itemId.contains(":")) {
                List<String> foundIds = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals()
                        .keySet().stream().filter(itemIdIterator -> itemIdIterator.contains(":") && !itemIdIterator.equals("MISSING") && itemIdIterator
                                .split(":")[1].equalsIgnoreCase(itemId)).collect(Collectors.toList());
                if (foundIds.isEmpty()) {
                    player.sendMessage(PlayerMessage.direct("§cThe id \"" + itemId + "\" couldn't be resolved to an existing contentpack!"));
                    player.setHeldItem(hand, ItemStack.EMPTY);
                } else if (foundIds.size() == 1) {
                    ItemStack newSignalPart = new ItemStack(LOSItems.ITEM_SIGNAL_PART, player.getHeldItem(hand).getCount());
                    String foundId = foundIds.get(0);

                    TagCompound tag = newSignalPart.getTagCompound();
                    tag.setString("itemId", foundId);
                    newSignalPart.setTagCompound(tag);

                    player.setHeldItem(hand, newSignalPart);
                    player.sendMessage(PlayerMessage.direct("§cThe id \"" + itemId + "\" was replaced with \"" + foundId + "\"!"));
                } else {
                    player.sendMessage(PlayerMessage.direct("§cThe id \"" + itemId + "\" couldn't be resolved to a single contentpack! (multiple Ids found)"));
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public List<ItemStack> getItemVariants(CreativeTab creativeTab) {
        List<ItemStack> itemStackList = new ArrayList<>();

        if (creativeTab != null && creativeTab.equals(LOSTabs.get(LOSTabs.SIGNALS_TAB))) {
            for (String id : LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().keySet()) {
                if (!id.equals(Static.MISSING)) {
                    ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
                    TagCompound tag = is.getTagCompound();
                    tag.setString("itemId", id);
                    is.setTagCompound(tag);
                    itemStackList.add(is);
                }
            }
        }

        itemStackList.sort(Comparator.comparing(LandOfSignalsUtils::getUniqueIdOfItemStack));

        return itemStackList;
    }

    @Override
    public String getCustomName(ItemStack stack) {
        TagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("itemId")) {
            String itemId = tag.getString("itemId");
            return LOSBlocks.BLOCK_SIGNAL_PART.getName(itemId);
        } else {
            return "Error missing tag \"itemId\" for ItemSignalPart";
        }
    }

    @Override
    public List<String> getTooltip(ItemStack itemStack) {
        String itemId = itemStack.getTagCompound().getString("itemId");
        List<String> tooltips = new ArrayList<>();
        if (itemId != null) {
            tooltips.add("ID: " + itemId);
        }
        return tooltips;
    }

}
