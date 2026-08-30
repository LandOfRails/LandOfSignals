package net.landofrails.landofsignals.render.item;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.common.ModelLoader;
import cam72cam.mod.model.common.mesh.Model;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.common.ModelConfig;
import cam72cam.mod.render.common.ModelRenderer;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.gui.GuiSignalPartAnimatedBox;
import net.landofrails.landofsignals.utils.Static;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemSignalPartAnimatedRender {

    private ItemSignalPartAnimatedRender() {

    }

    public static final boolean IGNOREFNFEXCEPTION = true;
    protected static final Map<String, Model> cache = new HashMap<>();

    @SuppressWarnings("java:S112")
    public static ItemRender.IItemModel getModelFor() {
        return (_, stack) -> new StandardModel().addCustom((state, _) -> {
            final TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getSignalParts().containsKey(itemId)) {
                itemId = Static.MISSING;
            }
            final Collection<String> collection = LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getStates(itemId);
            // TODO collection/states: is null okay or should it be replaced with ""?
            if (!cache.containsKey(itemId)) {
                try {
                    final Model model;
                    if (collection != null)
                        model = ModelLoader.load(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getPath(itemId)), collection);
                    else
                        model = ModelLoader.load(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getPath(itemId)));
                    cache.put(itemId, model);
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
            final Model model = cache.get(itemId);
            String textureName;
            if (collection != null) {
                textureName = GuiSignalPartAnimatedBox.getTexureName();
                if (!collection.contains(textureName)) textureName = null;
            } else
                textureName = null;
            final Vec3d translate = LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getItemTranslation(itemId);
            final float scale = (float) LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getScaling(itemId).x;

            state.translate(translate);
            state.scale(scale, scale, scale);

            ModelConfig cfg = new ModelConfig().variant(textureName);
            try (ModelRenderer.Binding bound = ModelRenderer.getRendererFor(model).bind(cfg, state)) {
                bound.enqueueOpaque();
            }
        });
    }
}
