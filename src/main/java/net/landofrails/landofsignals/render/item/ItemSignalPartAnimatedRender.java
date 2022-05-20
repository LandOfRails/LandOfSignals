package net.landofrails.landofsignals.render.item;

import cam72cam.mod.ModCore;
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
import java.util.*;

public class ItemSignalPartAnimatedRender {
    public static final boolean IGNOREFNFEXCEPTION = true;
    protected static final Map<String, OBJModel> cache = new HashMap<>();

    public static ItemRender.IItemModel getModelFor() {
        return (world, stack) -> new StandardModel().addCustom((state, partialTicks) -> {
            TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getSignalParts().containsKey(itemId)) {
                itemId = Static.MISSING;
            }
            Collection<String> collection = LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getStates(itemId);
            List<String> states = null;
            if (collection != null) {
                states = new ArrayList<>(collection);
            }
            if (!cache.containsKey(itemId)) {
                try {
                    OBJModel model;
                    if (states != null) {
                        // TODO Remove if UMC fixes this issue
                        if (!states.contains(""))
                            states.add("");
                        states.remove(null);
                        model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getPath(itemId)), 0, states);
                    } else {
                        model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getPath(itemId)), 0);
                    }
                    cache.put(itemId, model);
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
            OBJModel model = cache.get(itemId);
            String textureName;
            if (collection != null) {
                textureName = GuiSignalPartAnimatedBox.getTexureName();
                if (!collection.contains(textureName)) textureName = null;
            } else
                textureName = null;
            float scale = (float) LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getScaling(itemId).x;

            state.translate(LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getItemTranslation(itemId));
            state.scale(scale, scale, scale);
            try (OBJRender.Binding vbo = model.binder().texture(textureName).bind(state)) {
                vbo.draw();
            }
        });
    }
}
