package net.landofrails.landofsignals.render.item;

import cam72cam.mod.item.ItemStack;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.world.World;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("java:S3252")
public class ItemSignalPartRender implements ItemRender.IItemModel {
    protected static final Map<String, OBJRender> cache = new HashMap<>();
    protected static final Map<String, Boolean> cacheInfoOldContentPack = new HashMap<>();

    @Override
    public StandardModel getModel(World world, ItemStack stack) {
        return new StandardModel().addCustom(() -> {

            final TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().containsKey(itemId)) {
                itemId = Static.MISSING;
            }

            String itemState = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(itemId).getItemState();
            if (tag.hasKey("itemState")) {
                itemState = tag.getString("itemState");
            }

            ContentPackSignal signal = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(itemId);
            renderBase(signal, itemId);
            renderSignals(signal, itemId, itemState);

        });
    }

    @SuppressWarnings("java:S1134")
    private static void renderBase(ContentPackSignal signal, String itemId) {

        String baseState = signal.getBase();

        if (baseState == null) {
            return;
        }

        String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                cache.put(objPath, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0)));
                cacheInfoOldContentPack.putIfAbsent(itemId, LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(itemId));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJRender renderer = cache.get(objPath);

        final float[] translate = signal.getItemTranslation();
        final float[] scale = signal.getItemScaling();

        try (OpenGL.With ignored1 = OpenGL.matrix(); OpenGL.With ignored2 = renderer.bindTexture(baseState)) {

            // Render
            if (Boolean.FALSE.equals(cacheInfoOldContentPack.get(itemId))) {
                GL11.glScaled(scale[0], scale[1], scale[2]);
                GL11.glTranslated(translate[0], translate[1], translate[2]);
            } else {
                GL11.glTranslated(translate[0], translate[1], translate[2]);
                GL11.glScaled(scale[0], scale[1], scale[2]);
            }

            renderer.draw();
        }
    }

    @SuppressWarnings("java:S1134")
    private static void renderSignals(ContentPackSignal signal, String itemId, String itemState) {
        String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                cache.put(objPath, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0)));
                cacheInfoOldContentPack.putIfAbsent(itemId, LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(itemId));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJRender renderer = cache.get(objPath);

        final float[] translate = signal.getItemTranslation();
        final float[] scale = signal.getItemScaling();

        try (OpenGL.With ignored1 = OpenGL.matrix(); OpenGL.With ignored2 = renderer.bindTexture(itemState)) {

            // Render
            if (Boolean.FALSE.equals(cacheInfoOldContentPack.get(itemId))) {
                GL11.glScaled(scale[0], scale[1], scale[2]);
                GL11.glTranslated(translate[0], translate[1], translate[2]);
            } else {
                GL11.glTranslated(translate[0], translate[1], translate[2]);
                GL11.glScaled(scale[0], scale[1], scale[2]);
            }

            renderer.draw();
        }
    }

}
