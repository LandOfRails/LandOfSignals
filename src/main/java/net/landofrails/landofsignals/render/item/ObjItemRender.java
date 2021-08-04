package net.landofrails.landofsignals.render.item;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import org.lwjgl.opengl.GL11;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ObjItemRender {

    public static final boolean IGNOREFNFEXCEPTION = true;

    private ObjItemRender() {
    }

    protected static final Map<Identifier, OBJRender> cache = new HashMap<>();

    public static ItemRender.IItemModel getModelFor(Identifier id, Vec3d translate, float scale) {
        return getModelFor(id, translate, Vec3d.ZERO, null, scale);
    }

    public static ItemRender.IItemModel getModelFor(Identifier id, Vec3d translate, Vec3d rotation,
                                                    Collection<String> collection, float scale) {
        return (world, stack) -> new StandardModel().addCustom(() -> {
            if (!cache.containsKey(id)) {
                try {
                    OBJModel model;
                    if (collection != null) {
                        model = new OBJModel(id, 0, collection);
                    } else {
                        model = new OBJModel(id, 0);
                    }
                    OBJRender renderer = new OBJRender(model);
                    cache.put(id, renderer);
                } catch (FileNotFoundException e) {
                    if (IGNOREFNFEXCEPTION) {
                        ModCore.Mod.error("Model not found: " + e.getMessage(), e.getMessage());
                        return;
                    } else {
                        throw new RuntimeException("Error loading item model...", e);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error loading item model...", e);
                }
            }
            OBJRender renderer = cache.get(id);
            String textureName = null;
            TagCompound tag = stack.getTagCompound();
            if (collection != null && tag.hasKey("textureName")) {
                textureName = tag.getString("textureName");
            }
            try (OpenGL.With ignored = OpenGL.matrix(); OpenGL.With ignored1 = renderer.bindTexture(textureName)) {
                GL11.glTranslated(translate.x, translate.y, translate.z);
                GL11.glRotated(rotation.x, 1, 0, 0);
                GL11.glRotated(rotation.y, 0, 1, 0);
                GL11.glRotated(rotation.z, 0, 0, 1);
                GL11.glScaled(scale, scale, scale);
                renderer.draw();
            }
        });
    }
}
