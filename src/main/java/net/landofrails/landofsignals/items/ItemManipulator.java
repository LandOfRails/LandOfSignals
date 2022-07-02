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

public class ItemManipulator extends CustomItem {

    private static final boolean DISABLED = true;

    private BlockEntity block;
    private Vec3d playerMainPos;
    public static boolean editIngame = false;
    public static boolean editHeight = false;
    public static boolean sneak = false;

    public ItemManipulator(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.SIGNALS_TAB);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        sneak = player.isCrouching();
        BlockEntity block = world.getBlockEntity(pos, BlockEntity.class);

        // FIXME Check implementation for signals and signs
        // FIXME Enable tool after check
        if (DISABLED) {
            if (world.isClient)
                player.sendMessage(PlayerMessage.translate("message." + LandOfSignals.MODID + ":item.manipulator.disabled"));
            block = null;
        }

        if (block instanceof IManipulate) {
            this.block = block;
            this.playerMainPos = MinecraftClient.getPlayer().getPosition();
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

    public void setPlayerMainPos(Vec3d playerMainPos) {
        this.playerMainPos = playerMainPos;
    }

    public void clearBlock() {
        block = null;
        playerMainPos = null;
    }
}
