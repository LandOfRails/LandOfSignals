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
import net.landofrails.landofsignals.gui.GuiSignalBox;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemSignalPartRender {
    public static final boolean IGNOREFNFEXCEPTION = true;
    protected static final Map<String, OBJRender> cache = new HashMap<>();

    public static ItemRender.IItemModel getModelFor() {
        return (world, stack) -> new StandardModel().addCustom(() -> {
            TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_SIGNAL_PART.getSignalParts().containsKey(itemId)) {
                itemId = Static.MISSING;
            }
            Collection<String> collection = LOSBlocks.BLOCK_SIGNAL_PART.getStates(itemId);
            if (!cache.containsKey(itemId)) {
                try {
                    OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART.getPath(itemId)), 0);
                    OBJRender renderer = new OBJRender(model);
                    cache.put(itemId, renderer);
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
            OBJRender renderer = cache.get(itemId);
            String textureName;
            if (collection != null)
                textureName = GuiSignalBox.getTexureName();
            else
                textureName = null;
            Vec3d translate = LOSBlocks.BLOCK_SIGNAL_PART.getItemTranslation(itemId);
            float scale = (float) LOSBlocks.BLOCK_SIGNAL_PART.getScaling(itemId).x;
            try (OpenGL.With ignored = OpenGL.matrix(); OpenGL.With ignored1 = renderer.bindTexture(textureName)) {
                GL11.glTranslated(translate.x, translate.y, translate.z);
                GL11.glScaled(scale, scale, scale);
                renderer.draw();
            }
        });
    }
}
