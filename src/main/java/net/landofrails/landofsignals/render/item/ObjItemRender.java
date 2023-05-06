package net.landofrails.landofsignals.render.item;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ObjItemRender {

    public static final boolean IGNOREFNFEXCEPTION = true;

    private ObjItemRender() {
    }

    protected static final Map<Identifier, OBJModel> cache = new HashMap<>();

    public static ItemRender.IItemModel getModelFor(final Identifier id, final Vec3d translate, final float scale) {
        return getModelFor(id, translate, Vec3d.ZERO, null, scale);
    }

    @SuppressWarnings("java:S112")
    public static ItemRender.IItemModel getModelFor(final Identifier id, final Vec3d translate, final Vec3d rotation,
                                                    final Collection<String> collection, final float scale) {
        return (world, stack) -> new StandardModel().addCustom((state, partialTicks) -> {
            if (!cache.containsKey(id)) {
                try {
                    final OBJModel model;
                    if (collection != null) {
                        // TODO is null okay or should it be replaced with ""?
                        model = new OBJModel(id, 0, collection);
                    } else {
                        model = new OBJModel(id, 0);
                    }
                    cache.put(id, model);
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
            final OBJModel model = cache.get(id);
            String textureName = null;
            final TagCompound tag = stack.getTagCompound();
            if (collection != null && tag.hasKey("textureName")) {
                textureName = tag.getString("textureName");
            }

            state.translate(translate);
            state.rotate(rotation.x, 1, 0, 0);
            state.rotate(rotation.y, 0, 1, 0);
            state.rotate(rotation.z, 0, 0, 1);
            state.scale(scale, scale, scale);

            try (OBJRender.Binding vbo = model.binder().texture(textureName).bind(state)) {
                vbo.draw();
            }
        });
    }
}
