package net.landofrails.landofsignals.render.item;

import cam72cam.mod.item.ItemStack;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.Static;

@SuppressWarnings("java:S3252")
public class ItemCreatorRender implements ItemRender.IItemModel {

    @Override
    public StandardModel getModel(World world, ItemStack stack) {
        return new StandardModel().addCustom(() -> {
            String path = stack.getTagCompound().getString("path");
            if (path == null) {
                path = Static.MISSING_OBJ;
            }
            final OBJModel model;
            try {
                model = new OBJModel(new Identifier(LandOfSignals.MODID, path), 0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            final OBJRender renderer = new OBJRender(model);
            try (final OpenGL.With ignored = OpenGL.matrix(); final OpenGL.With ignored1 = renderer.bindTexture()) {
                renderer.draw();
            }
        });
    }

    @Override
    public void applyTransform(ItemRender.ItemRenderType type) {
        ItemRender.IItemModel.super.applyTransform(type);
    }
}
