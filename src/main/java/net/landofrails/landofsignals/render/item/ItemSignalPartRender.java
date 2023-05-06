package net.landofrails.landofsignals.render.item;

import cam72cam.mod.item.ItemStack;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.world.World;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.Static;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("java:S3252")
public class ItemSignalPartRender implements ItemRender.IItemModel {
    protected static final Map<String, OBJModel> cache = new HashMap<>();
    protected static final Map<String, Boolean> cacheInfoOldContentPack = new HashMap<>();

    @Override
    public StandardModel getModel(World world, ItemStack stack) {
        return new StandardModel().addCustom((state, partialTicks) -> {

            final TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().containsKey(itemId)) {
                itemId = Static.MISSING;
            }

            String itemState = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(itemId).getItemState();
            if (tag.hasKey("itemState") && !itemId.equals(Static.MISSING)) {
                itemState = tag.getString("itemState");
            }

            ContentPackSignal signal = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(itemId);
            renderBase(signal, itemId, state);
            renderSignals(signal, itemId, itemState, state);

        });
    }

    @SuppressWarnings("java:S1134")
    private static void renderBase(ContentPackSignal signal, String itemId, RenderState state) {

        String baseState = signal.getBase();

        if (baseState == null) {
            return;
        }

        String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(itemId);
                // TODO is null okay or should it be replaced with ""?
                cache.put(objPath, new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states)));
                cacheInfoOldContentPack.putIfAbsent(itemId, LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(itemId));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJModel model = cache.get(objPath);

        final float[] translate = signal.getItemTranslation();
        final float[] scale = signal.getItemScaling();

        state.translate(translate[0], translate[1], translate[2]);
        state.scale(scale[0], scale[1], scale[2]);

        try (OBJRender.Binding vbo = model.binder().texture(baseState).bind(state)) {

            // Render
            vbo.draw();
        }
    }

    @SuppressWarnings("java:S1134")
    private static void renderSignals(ContentPackSignal signal, String itemId, String itemState, RenderState state) {
        String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(itemId);
                cache.put(objPath, new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states)));
                cacheInfoOldContentPack.putIfAbsent(itemId, LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(itemId));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJModel model = cache.get(objPath);

        final float[] translate = signal.getItemTranslation();
        final float[] scale = signal.getItemScaling();

        state.translate(translate[0], translate[1], translate[2]);
        state.scale(scale[0], scale[1], scale[2]);

        try (OBJRender.Binding vbo = model.binder().texture(itemState).bind(state)) {

            // Render
            vbo.draw();
        }
    }

}
