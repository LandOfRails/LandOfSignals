package net.landofrails.landofsignals.render.item;

import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.common.ModelLoader;
import cam72cam.mod.model.common.mesh.Model;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.common.ModelConfig;
import cam72cam.mod.render.common.ModelRenderer;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.world.World;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItem;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItemRenderType;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.Static;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemSignalBoxRender implements ItemRender.IItemModel {

    protected static final Map<String, Model> cache = new HashMap<>();
    private static final Map<String, List<String>> groupCache = new HashMap<>();

    public static void checkCache(String itemId, Map<String, ContentPackModel[]> models) {
        Optional<String> firstPath = models.keySet().stream().findFirst();
        if (firstPath.isEmpty())
            return;
        final String firstObjId = itemId + "/" + firstPath.get();
        if (cache.containsKey(firstObjId)) {
            return;
        }

        for (Map.Entry<String, ContentPackModel[]> modelEntry : models.entrySet()) {
            try {
                final String path = modelEntry.getKey();
                final String objId = itemId + "/" + path;

                Set<String> objTextures = LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().get(itemId).getObjTextures().get(path);
                if(objTextures.contains(null)){
                    objTextures.remove(null);
                    objTextures.add("");
                }
                Model model = ModelLoader.load(new Identifier(LandOfSignals.MODID, path), objTextures);
                cache.putIfAbsent(objId, model);

                for (ContentPackModel signalBoxModel : modelEntry.getValue()) {
                    String[] groups = signalBoxModel.getObj_groups();
                    if (groups.length > 0) {
                        Predicate<String> targetGroup = renderOBJGroup -> Arrays.stream(groups).anyMatch(renderOBJGroup::startsWith);
                        List<String> modes = model.groups().stream().filter(targetGroup)
                                .collect(Collectors.toCollection(ArrayList::new));
                        if(modes.contains(null)){
                            modes.remove(null);
                            modes.add("");
                        }
                        String groupCacheId = objId + "@" + String.join("+", groups);
                        groupCache.put(groupCacheId, modes);
                    }
                }
            } catch (Exception e) {
                String message = String.format("Couldn't cache the following: itemId: %s", itemId);
                throw new ItemRenderException(message, e);
            }
        }

    }

    @Override
    public StandardModel getModel(World world, ItemStack stack) {
        return new StandardModel().addCustom((state, _) -> {

            TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().containsKey(itemId)) {
                itemId = Static.MISSING;
            }

            renderBase(itemId, state);

        });
    }

    @Override
    public void applyTransform(ItemStack stack, ItemRender.ItemRenderType type, RenderState ctx) {
        // Implement ItemRenderType with new UMC rendering
        ItemRender.IItemModel.super.applyTransform(stack, type, ctx);
    }

    private static void renderBase(String itemId, RenderState state) {

        checkCache(itemId, LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().get(itemId).getBase());

        for (Map.Entry<String, ContentPackModel[]> baseModels : LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().get(itemId).getBase().entrySet()) {

            String path = baseModels.getKey();

            String objId = itemId + "/" + path;
            Model model = cache.get(objId);

            for (ContentPackModel baseModel : baseModels.getValue()) {

                RenderState iterationState = state.clone();

                ContentPackItem item = baseModel.getItem().get(ContentPackItemRenderType.DEFAULT);
                Vec3d translate = item.getAsVec3d(item::getTranslation);
                Vec3d scale = item.getAsVec3d(item::getScaling);
                Vec3d rotation = item.getAsVec3d(item::getRotation);

                iterationState.scale(scale);
                iterationState.translate(translate);
                iterationState.rotate(rotation.x, 1, 0, 0);
                iterationState.rotate(rotation.y, 0, 1, 0);
                iterationState.rotate(rotation.z, 0, 0, 1);

                ModelConfig cfg = new ModelConfig().variant(baseModel.getTextures());
                try (ModelRenderer.Binding bound = ModelRenderer.getRendererFor(model).bind(cfg, iterationState)) {

                    // Render
                    String[] groups = baseModel.getObj_groups();
                    if (groups.length == 0) {
                        bound.enqueueOpaque();
                    } else {
                        String groupCacheId = objId + "@" + String.join("+", groups);
                        bound.enqueueOpaque(groupCache.get(groupCacheId));
                    }

                }

            }
        }
    }
}
