package net.landofrails.landofsignals.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.configs.LandOfSignalsConfig;
import net.landofrails.landofsignals.configs.LegacyMode;
import net.landofrails.landofsignals.packet.legacymode.LegacyModePromptToClientPacket;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ItemSignalPart extends CustomItem {

    public ItemSignalPart(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(LOSTabs.HIDDEN_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        Optional<Vec3i> target = LandOfSignalsUtils.canPlaceBlock(world, pos, facing, player);
        if (!target.isPresent()) return ClickResult.REJECTED;

        String itemId = player.getHeldItem(hand).getTagCompound().getString("itemId");
        int rot = -(Math.round(player.getRotationYawHead() / 10) * 10) + 180;
        TileSignalPart tileSignalPart = world.getBlockEntity(pos, TileSignalPart.class);
        if (tileSignalPart != null && !player.isCrouching()) rot = tileSignalPart.getBlockRotate();

        if (LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(itemId)) {
            if (world.isServer && LandOfSignalsConfig.legacyMode == LegacyMode.PROMPT) {
                new LegacyModePromptToClientPacket(target.get(), itemId, rot).sendToPlayer(player);
                return ClickResult.ACCEPTED;
            } else if (!world.isServer && world.isClient) {
                return ClickResult.ACCEPTED;
            }
        }
        LOSBlocks.BLOCK_SIGNAL_PART.setRot(rot);
        LOSBlocks.BLOCK_SIGNAL_PART.setId(itemId);
        LegacyMode legacyMode = LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(itemId) ? LandOfSignalsConfig.legacyMode : LegacyMode.OFF;
        LOSBlocks.BLOCK_SIGNAL_PART.setLegacyMode(legacyMode);
        world.setBlock(target.get(), LOSBlocks.BLOCK_SIGNAL_PART);
        return ClickResult.ACCEPTED;

    }

    @Override
    public String getCustomName(ItemStack stack) {
        TagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("itemId")) {
            String itemId = tag.getString("itemId");
            if (LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().containsKey(itemId)) {
                return LOSBlocks.BLOCK_SIGNAL_PART.getName(itemId);
            }
            // FIXME Remove this implementation
            return LOSBlocks.BLOCK_SIGNAL_PART.getName_depr(itemId);
        } else return "Error missing tag \"itemId\" for ItemSignalPart";
    }
}
