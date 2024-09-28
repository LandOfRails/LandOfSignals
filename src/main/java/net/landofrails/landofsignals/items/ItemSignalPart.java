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
import cam72cam.mod.text.TextUtil;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;
import net.landofrails.landofsignals.utils.Static;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("java:S1192")
public class ItemSignalPart extends CustomItem {

    private static final Map<String, List<String>> cacheTooltips = new HashMap<>();

    private static final String ITEMIDKEY = "itemId";
    private static final String MSG_NOT_UTF8 = "message.landofsignals:non.utf.eight.items";
    private static final String MSG_HAS_FLARES = "message.landofsignals:signalpart.hasflares";
    private static final String MSG_LOS_TRUE = "message.landofsignals:true";
    private static final String MSG_LOS_FALSE = "message.landofsignals:false";

    public ItemSignalPart(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.SIGNALS_TAB);
    }

    @Override
    public ClickResult onClickBlock(final Player player, final World world, final Vec3i pos, final Player.Hand hand, final Facing facing, final Vec3d inBlockPos) {
        final Optional<Vec3i> target = LandOfSignalsUtils.canPlaceBlock(world, pos, facing, player);
        if (!target.isPresent()) return ClickResult.REJECTED;

        String itemId = player.getHeldItem(hand).getTagCompound().getString(ITEMIDKEY);

        if(!LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().containsKey(itemId)) return ClickResult.REJECTED;

        float rotationSteps = LOSBlocks.BLOCK_SIGNAL_PART.getRotationSteps(itemId);

        int rotation = (int) (-(Math.round(player.getRotationYawHead() / rotationSteps) * rotationSteps) + 180);
        final TileSignalPart tileSignalPart = world.getBlockEntity(pos, TileSignalPart.class);
        if (tileSignalPart != null && !player.isCrouching()) rotation = tileSignalPart.getBlockRotate();

        LOSBlocks.BLOCK_SIGNAL_PART.setRot(rotation);
        LOSBlocks.BLOCK_SIGNAL_PART.setId(itemId);
        world.setBlock(target.get(), LOSBlocks.BLOCK_SIGNAL_PART);
        return ClickResult.ACCEPTED;

    }

    @Override
    public void onClickAir(Player player, World world, Player.Hand hand) {
        if (world.isServer) {
            String itemId = player.getHeldItem(hand).getTagCompound().getString(ITEMIDKEY);
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
                    tag.setString(ITEMIDKEY, foundId);
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

        if (creativeTab == null || creativeTab.equals(LOSTabs.get(LOSTabs.SIGNALS_TAB))) {
            for (String id : LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().keySet()) {
                if (!id.equals(Static.MISSING)) {
                    ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
                    TagCompound tag = is.getTagCompound();
                    tag.setString(ITEMIDKEY, id);
                    is.setTagCompound(tag);
                    itemStackList.add(is);
                }
            }
        }

        itemStackList.sort(Comparator.comparing(LandOfSignalsUtils::getUniqueIdOfItemStack));

        return itemStackList;
    }

    @Override
    public String getCustomName(final ItemStack stack) {
        final TagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey(ITEMIDKEY)) {
            String itemId = tag.getString(ITEMIDKEY);
            return LOSBlocks.BLOCK_SIGNAL_PART.getName(itemId);
        } else {
            return "Error missing tag \"itemId\" for ItemSignalPart";
        }
    }

    @Override
    public List<String> getTooltip(ItemStack itemStack) {
        String itemId = itemStack.getTagCompound().getString(ITEMIDKEY);
        if(itemId == null){
            return new ArrayList<>();
        }
        if(!cacheTooltips.containsKey(itemId)){
            cacheTooltip(itemId);
        }
        return cacheTooltips.get(itemId);
    }

    private void cacheTooltip(String itemId) {
        List<String> tooltips = new ArrayList<>();
        String delimiter = ":";
        if (itemId.split(delimiter).length == 2) {
            tooltips.add("Pack: " + itemId.split(delimiter)[0]);
            tooltips.add("ID: " + itemId.split(delimiter)[1]);
        } else {
            tooltips.add("ID: " + itemId);
        }

        boolean isUTF8 = LOSBlocks.BLOCK_SIGNAL_PART.isUTF8(itemId);
        if (!isUTF8) {
            tooltips.add("");
            tooltips.add(TextUtil.translate(MSG_NOT_UTF8));
        }

        boolean hasFlares = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(itemId).getFlares().length > 0;
        Object[] hasFlaresRawText = new Object[]{TextUtil.translate(hasFlares ? MSG_LOS_TRUE : MSG_LOS_FALSE)};
        // String has to be converted to array by us, build pipeline is not able to do it itself.
        tooltips.add(TextUtil.translate(MSG_HAS_FLARES, hasFlaresRawText));
        cacheTooltips.put(itemId, tooltips);
    }
}
