package net.landofrails.landofsignals.render.item;

import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
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

public class ItemCustomLeverRender implements ItemRender.IItemModel {

    protected static final Map<String, OBJModel> cache = new HashMap<>();
    private static final Map<String, List<String>> groupCache = new HashMap<>();

    public static void checkCache(String itemId, Map<String, ContentPackModel[]> models) {
        Optional<String> firstPath = models.keySet().stream().findFirst();
        if (!firstPath.isPresent())
            return;
        final String firstObjId = itemId + "/" + firstPath.get();
        if (cache.containsKey(firstObjId)) {
            return;
        }

        for (Map.Entry<String, ContentPackModel[]> modelEntry : models.entrySet()) {
            try {
                final String path = modelEntry.getKey();
                final String objId = itemId + "/" + path;

                Set<String> objTextures = LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(itemId).getObjTextures().get(path);
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, path), 0, objTextures);
                cache.putIfAbsent(objId, model);

                for (ContentPackModel decoModel : modelEntry.getValue()) {
                    String[] groups = decoModel.getObj_groups();
                    if (groups.length > 0) {
                        Predicate<String> targetGroup = renderOBJGroup -> Arrays.stream(groups).anyMatch(renderOBJGroup::startsWith);
                        List<String> modes = model.groups().stream().filter(targetGroup)
                                .collect(Collectors.toCollection(ArrayList::new));
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
        return new StandardModel().addCustom((renderState, partialTicks) -> {

            TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().containsKey(itemId)) {
                itemId = Static.MISSING;
            }

            renderInactive(renderState.clone(), itemId);

        });
    }

    @Override
    public void applyTransform(ItemStack stack, ItemRender.ItemRenderType type, RenderState ctx) {

        // Implement ItemRenderType
        ItemRender.IItemModel.super.applyTransform(stack, type, ctx);
    }

    private static void renderInactive(RenderState renderState, String itemId) {

        checkCache(itemId, LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(itemId).getInactive());

        for (Map.Entry<String, ContentPackModel[]> inactiveModels : LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(itemId).getInactive().entrySet()) {

            String path = inactiveModels.getKey();

            String objId = itemId + "/" + path;
            OBJModel model = cache.get(objId);

            for (ContentPackModel inactiveModel : inactiveModels.getValue()) {

                RenderState iterationState = renderState.clone();

                ContentPackItem item = inactiveModel.getItem().get(ContentPackItemRenderType.DEFAULT);
                Vec3d translate = item.getAsVec3d(item::getTranslation);
                Vec3d scale = item.getAsVec3d(item::getScaling);
                Vec3d rotation = item.getAsVec3d(item::getRotation);

                try (OBJRender.Binding vbo = model.binder().texture(inactiveModel.getTextures()).bind(iterationState)) {

                    // Render
                    iterationState.scale(scale.x, scale.y, scale.z);
                    iterationState.translate(translate.x, translate.y, translate.z);
                    iterationState.rotate(rotation.x, 1, 0, 0);
                    iterationState.rotate(rotation.y, 0, 1, 0);
                    iterationState.rotate(rotation.z, 0, 0, 1);

                    String[] groups = inactiveModel.getObj_groups();
                    if (groups.length == 0) {
                        vbo.draw();
                    } else {
                        String groupCacheId = objId + "@" + String.join("+", groups);
                        vbo.draw(groupCache.get(groupCacheId));
                    }

                }
            }
        }
    }
}
