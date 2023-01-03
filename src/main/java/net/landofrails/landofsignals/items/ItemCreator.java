package net.landofrails.landofsignals.items;

import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.Static;

import java.util.List;

public class ItemCreator extends CustomItem {

    public ItemCreator(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.HIDDEN_TAB);
    }

    @SuppressWarnings("java:S112")
    public static ItemRender.IItemModel getModelFor() {
        return (world, stack) -> new StandardModel().addCustom(() -> {
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

}
