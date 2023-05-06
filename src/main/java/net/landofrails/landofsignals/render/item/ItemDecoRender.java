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

public class ItemDecoRender implements ItemRender.IItemModel {

    protected static final Map<String, OBJModel> cache = new HashMap<>();
    private static final Map<String, List<String>> groupCache = new HashMap<>();

    private static void checkCache(String itemId, Map<String, ContentPackModel[]> models) {
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

                Set<String> objTextures = LOSBlocks.BLOCK_DECO.getContentpackDeco().get(itemId).getObjTextures().get(path);
                // TODO is null okay or should it be replaced with ""?
                objTextures.remove(null);
                objTextures.add("");
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
        return new StandardModel().addCustom((state, partialTicks) -> {

            TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_DECO.getContentpackDeco().containsKey(itemId)) {
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

        checkCache(itemId, LOSBlocks.BLOCK_DECO.getContentpackDeco().get(itemId).getBase());

        for (Map.Entry<String, ContentPackModel[]> baseModels : LOSBlocks.BLOCK_DECO.getContentpackDeco().get(itemId).getBase().entrySet()) {

            String path = baseModels.getKey();

            String objId = itemId + "/" + path;
            OBJModel model = cache.get(objId);

            for (ContentPackModel baseModel : baseModels.getValue()) {
                ContentPackItem item = baseModel.getItem().get(ContentPackItemRenderType.DEFAULT);
                Vec3d translate = item.getAsVec3d(item::getTranslation);
                Vec3d scale = item.getAsVec3d(item::getScaling);
                Vec3d rotation = item.getAsVec3d(item::getRotation);

                state.scale(scale);
                state.translate(translate);
                state.rotate(rotation.x, 1, 0, 0);
                state.rotate(rotation.y, 0, 1, 0);
                state.rotate(rotation.z, 0, 0, 1);

                String texture = baseModel.getTextures();
                if(texture == null)
                    texture = "";
                try (OBJRender.Binding vbo = model.binder().texture(texture).bind(state)) {

                    // Render
                    String[] groups = baseModel.getObj_groups();
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
