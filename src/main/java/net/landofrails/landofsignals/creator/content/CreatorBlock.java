package net.landofrails.landofsignals.creator.content;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.creator.gui.GuiSelectContentpack;
import org.lwjgl.opengl.GL11;

public class CreatorBlock extends BlockTypeEntity {

    private float workbench_rotation;

    public CreatorBlock(String modID, String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new CreatorTile(workbench_rotation);
    }

    public void setRotation(float rotation) {
        workbench_rotation = rotation;
    }

    public static class CreatorTile extends BlockEntity {

        @TagField("workbenchRotation")
        private float workbenchRotation;

        // Settings
        @TagField("hiddenWhileSneaking")
        private boolean hiddenWhileSneaking = true;

        public CreatorTile(float workbenchRotation) {
            this.workbenchRotation = workbenchRotation;
        }

        public double getBlockRotate() {
            return workbenchRotation;
        }

        public boolean isHiddenWhileSneaking() {
            return hiddenWhileSneaking;
        }

        // Functions

        @Override
        public ItemStack onPick() {
            return new ItemStack(LOSItems.ITEM_CREATOR, 1);
        }

        @Override
        public boolean onClick(Player player, Player.Hand hand, Facing facing, Vec3d hit) {
            if (!player.getHeldItem(hand).isEmpty())
                return false;

            if (!getWorld().isClient)
                return true;

            GuiSelectContentpack.open();

            return true;
        }
    }

    public static class CreatorRender {

        private CreatorRender() {

        }

        private static OBJRender renderer;
        private static OBJModel model;

        public static StandardModel render(CreatorTile ts) {
            return new StandardModel().addCustom(() -> renderWorkbench(ts));
        }

        @SuppressWarnings("java:S1172")
        private static void renderWorkbench(CreatorTile ts) {

            try {
                if (ts.isHiddenWhileSneaking() && MinecraftClient.getPlayer().isCrouching()) {
                    return;
                }

                if (renderer == null || model == null) {
                    model = new OBJModel(
                            new Identifier(LandOfSignals.MODID, "models/block/landofsignals/creatorblock/creator_workbench.obj"),
                            0);
                    renderer = new OBJRender(model);
                }
                try (final OpenGL.With ignored = OpenGL.matrix(); final OpenGL.With ignored1 = renderer.bindTexture()) {
                    GL11.glTranslatef(0.5f, 0f, 0.5f);
                    GL11.glRotated(ts.getBlockRotate(), 0, 1, 0);

                    renderer.draw();
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

    }

}
