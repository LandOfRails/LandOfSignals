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
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.Static;
import net.landofrails.landofsignals.utils.contentpacks.ContentPackSignObject;
import org.lwjgl.opengl.GL11;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ItemSignPartRender {

    private ItemSignPartRender() {

    }

    public static final boolean IGNOREFNFEXCEPTION = true;
    protected static final Map<String, Map<Integer, OBJRender>> renderCache = new HashMap<>();
    protected static final Map<String, Map<Integer, ContentPackSignObject>> signObjectCache = new HashMap<>();

    public static ItemRender.IItemModel getModelFor() {
        return (world, stack) -> new StandardModel().addCustom(() -> {
            TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_SIGN_PART.getSignParts().containsKey(itemId)) {
                itemId = Static.MISSING;
            }
            if (!renderCache.containsKey(itemId)) {
                Map<Integer, OBJRender> objRenderers = new HashMap<>();
                signObjectCache.put(itemId, LOSBlocks.BLOCK_SIGN_PART.getSignObjects(itemId));
                for (Map.Entry<Integer, String> path : LOSBlocks.BLOCK_SIGN_PART.getPath(itemId).entrySet()) {
                    try {
                        OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, path.getValue()), 0);
                        objRenderers.put(path.getKey(), new OBJRender(model));
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
                if (objRenderers.isEmpty()) {
                    throw new RuntimeException("Couldnt find any ContentPackSignObjects!");
                } else {
                    renderCache.put(itemId, objRenderers);
                }
            }
            for (Map.Entry<Integer, OBJRender> rendererEntry : renderCache.get(itemId).entrySet()) {
                Integer id = rendererEntry.getKey();
                OBJRender renderer = rendererEntry.getValue();
                Vec3d translate = LOSBlocks.BLOCK_SIGN_PART.getItemTranslation(itemId).get(id);
                Vec3d scale = LOSBlocks.BLOCK_SIGN_PART.getItemScaling(itemId).get(id);
                try (OpenGL.With ignored = OpenGL.matrix(); OpenGL.With ignored1 = renderer.bindTexture(LOSBlocks.BLOCK_SIGN_PART.getTexture(itemId).get(id))) {
                    GL11.glTranslated(translate.x, translate.y, translate.z);
                    GL11.glScaled(scale.x, scale.y, scale.z);
                    String[] groups = LOSBlocks.BLOCK_SIGN_PART.getRenderGroups(itemId).get(id);
                    if (groups.length > 0) {
                        renderer.drawGroups(Arrays.asList(groups));
                    } else {
                        renderer.draw();
                    }
                }
            }
        });
    }
}
