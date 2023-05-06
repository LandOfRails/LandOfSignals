package net.landofrails.landofsignals.render.item;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
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
    protected static final Map<String, OBJModel> cache = new HashMap<>();

    @SuppressWarnings("java:S112")
    public static ItemRender.IItemModel getModelFor() {
        return (world, stack) -> new StandardModel().addCustom((state, partialTicks) -> {
            final TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getSignalParts().containsKey(itemId)) {
                itemId = Static.MISSING;
            }
            final Collection<String> collection = LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getStates(itemId);
            // TODO collection/states: is null okay or should it be replaced with ""?
            if (!cache.containsKey(itemId)) {
                try {
                    final OBJModel model;
                    if (collection != null)
                        model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getPath(itemId)), 0, collection);
                    else
                        model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getPath(itemId)), 0);
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
            final OBJModel model = cache.get(itemId);
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

            try (OBJRender.Binding vbo = model.binder().texture(textureName).bind(state)) {
                vbo.draw();
            }
        });
    }
}
