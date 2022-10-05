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

    public static ItemRender.IItemModel getModelFor(final Identifier id, final Vec3d translate, final float scale) {
        return getModelFor(id, translate, Vec3d.ZERO, null, scale);
    }

    @SuppressWarnings("java:S112")
    public static ItemRender.IItemModel getModelFor(final Identifier id, final Vec3d translate, final Vec3d rotation,
                                                    final Collection<String> collection, final float scale) {
        return (world, stack) -> new StandardModel().addCustom(() -> {
            if (!cache.containsKey(id)) {
                try {
                    final OBJModel model;
                    if (collection != null) {
                        model = new OBJModel(id, 0, collection);
                    } else {
                        model = new OBJModel(id, 0);
                    }
                    final OBJRender renderer = new OBJRender(model);
                    cache.put(id, renderer);
                } catch (final FileNotFoundException e) {
                    if (IGNOREFNFEXCEPTION) {
                        ModCore.Mod.error("Model not found: " + e.getMessage(), e.getMessage());
                        return;
                    } else {
                        throw new RuntimeException("Error loading item model...", e);
                    }
                } catch (final Exception e) {
                    throw new RuntimeException("Error loading item model...", e);
                }
            }
            final OBJRender renderer = cache.get(id);
            String textureName = null;
            final TagCompound tag = stack.getTagCompound();
            if (collection != null && tag.hasKey("textureName")) {
                textureName = tag.getString("textureName");
            }
            try (final OpenGL.With ignored = OpenGL.matrix(); final OpenGL.With ignored1 = renderer.bindTexture(textureName)) {
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
