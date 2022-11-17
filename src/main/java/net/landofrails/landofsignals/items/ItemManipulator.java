package net.landofrails.landofsignals.items;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.IManipulate;

import java.util.List;

@SuppressWarnings("java:S1117")
public class ItemManipulator extends CustomItem {

    private static final boolean DISABLED = true;

    private BlockEntity block;
    private Vec3d playerMainPos;
    @SuppressWarnings({"java:S1444", "java:S1104"})
    public static boolean editIngame;
    @SuppressWarnings({"java:S1444", "java:S1104"})
    public static boolean editHeight;
    @SuppressWarnings({"java:S1444", "java:S1104"})
    public static boolean sneak;

    public ItemManipulator(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.SIGNALS_TAB);
    }

    @Override
    @SuppressWarnings({"java:S2696", "java:S1134"})
    public ClickResult onClickBlock(final Player player, final World world, final Vec3i pos, final Player.Hand hand, final Facing facing, final Vec3d inBlockPos) {
        sneak = player.isCrouching();
        BlockEntity block = world.getBlockEntity(pos, BlockEntity.class);

        // CHECK: Implementation for signals and signs
        // AFTERWARDS: Enable tool after check
        if (DISABLED) {
            if (world.isClient)
                player.sendMessage(PlayerMessage.translate("message." + LandOfSignals.MODID + ":item.manipulator.disabled"));
            block = null;
        }
        if (block instanceof IManipulate) {
            this.block = block;
            playerMainPos = MinecraftClient.getPlayer().getPosition();
            if (world.isClient) {
                LOSGuis.MANIPULATOR.open(player, block.getPos());
                return ClickResult.ACCEPTED;
            }
        }
        return ClickResult.REJECTED;
    }

    public BlockEntity getBlock() {
        return block;
    }

    public Vec3d getPlayerMainPos() {
        return playerMainPos;
    }

    public void setPlayerMainPos(final Vec3d playerMainPos) {
        this.playerMainPos = playerMainPos;
    }

    public void clearBlock() {
        block = null;
        playerMainPos = null;
    }
}
