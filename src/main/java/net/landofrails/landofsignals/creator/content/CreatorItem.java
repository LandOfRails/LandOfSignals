package net.landofrails.landofsignals.creator.content;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Optional;

public class CreatorItem extends CustomItem {
    public CreatorItem(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        final Optional<Vec3i> target = LandOfSignalsUtils.canPlaceBlock(world, pos, facing, player);
        if (!target.isPresent()) return ClickResult.REJECTED;

        final CreatorBlock block = LOSBlocks.BLOCK_CREATOR;
        final float rotationSteps = 90f;
        float rotation = (-(Math.round(player.getRotationYawHead() / rotationSteps) * rotationSteps) + 180);
        block.setRotation(rotation);
        world.setBlock(target.get(), block);
        return ClickResult.ACCEPTED;
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.ASSETS_TAB);
    }

    public static class CreatorRender implements ItemRender.IItemModel {

        private OBJRender renderer;

        @Override
        public StandardModel getModel(World world, ItemStack stack) {
            return new StandardModel().addCustom(() -> {

                try {
                    if (renderer == null) {
                        final OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/creatorblock/creator_workbench.obj"), 0);
                        renderer = new OBJRender(model);
                    }
                    try (final OpenGL.With ignored = OpenGL.matrix(); final OpenGL.With ignored1 = renderer.bindTexture()) {
                        GL11.glTranslatef(0.5f, 0f, 0.5f);
                        renderer.draw();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void applyTransform(ItemRender.ItemRenderType type) {
            ItemRender.IItemModel.super.applyTransform(type);
        }
    }

}
