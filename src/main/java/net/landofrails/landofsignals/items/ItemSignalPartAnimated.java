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
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.LandOfSignals;

import java.util.Collections;
import java.util.List;

public class ItemSignalPartAnimated extends CustomItem {

    public ItemSignalPartAnimated(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.HIDDEN_TAB);
    }

    @Override
    public List<ItemStack> getItemVariants(CreativeTab creativeTab) {
        return Collections.emptyList();
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        player.sendMessage(PlayerMessage.translate("message." + LandOfSignals.MODID + ":item.signalanimated"));
        return ClickResult.REJECTED;
    }

    @Override
    public String getCustomName(final ItemStack stack) {
        final TagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("itemId"))
            return LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getName(tag.getString("itemId"));
        else return "Error missing tag \"itemId\" for ItemSignalPartAnimated";
    }
}
