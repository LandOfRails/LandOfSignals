package net.landofrails.landofsignals.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.utils.HighlightingUtil;

import java.util.List;

public class ItemMagnifyingGlass extends CustomItem {

    public ItemMagnifyingGlass(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.ASSETS_TAB);
    }

    @Override
    @SuppressWarnings({"java:S2696", "java:S1134"})
    public ClickResult onClickBlock(final Player player, final World world, final Vec3i pos, final Player.Hand hand, final Facing facing, final Vec3d inBlockPos) {
        HighlightingUtil.renderHighlighting();
        if (!world.isClient) {
            return ClickResult.REJECTED;
        }
        if (hand.equals(Player.Hand.SECONDARY)) {
            return ClickResult.PASS;
        }
        if (!world.isBlock(pos, LOSBlocks.BLOCK_SIGNAL_BOX)) {
            return ClickResult.PASS;
        }

        toggleHighlighting(world);

        TileSignalBox signalBox = world.getBlockEntity(pos, TileSignalBox.class);
        signalBox.toggleHighlighting();

        return ClickResult.ACCEPTED;
    }

    @Override
    public void onClickAir(final Player player, final World world, final Player.Hand hand) {
        if (!world.isClient || hand.equals(Player.Hand.SECONDARY)) return;
        toggleHighlighting(world);
    }

    private void toggleHighlighting(final World world) {
        final List<TileSignalBox> signalBoxes = world.getBlockEntities(TileSignalBox.class);
        signalBoxes.stream().filter(TileSignalBox::isHighlighting).forEach(TileSignalBox::toggleHighlighting);
    }
}
