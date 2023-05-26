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
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackSignalGroup;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackSignalState;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItem;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItemRenderType;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.serialization.EmptyStringMapper;
import net.landofrails.landofsignals.utils.Static;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("java:S3252")
public class ItemComplexSignalRender implements ItemRender.IItemModel {
    protected static final Map<String, OBJModel> cache = new HashMap<>();
    protected static final Map<String, List<String>> groupCache = new HashMap<>();

    private static final String SIGNAL_IDENTIFIER = "/signals/";

    public static void checkCache(String itemId, Collection<ContentPackSignalGroup> groups) {

        // Get first group, get first state, get first model
        Optional<String> firstPath = groups.iterator().next().getStates().values().iterator().next().getModels().keySet().stream().findFirst();

        if (!firstPath.isPresent())
            return;
        final String firstObjId = itemId + SIGNAL_IDENTIFIER + firstPath.get();
        if (cache.containsKey(firstObjId)) {
            return;
        }

        for (ContentPackSignalGroup group : groups) {
            for (ContentPackSignalState state : group.getStates().values()) {
                checkCache(itemId, state.getModels(), SIGNAL_IDENTIFIER, false);
            }
        }

    }

    public static void checkCache(String itemId, Map<String, ContentPackModel[]> models, String identifier, boolean checkIfAlreadyExisting) {
        if (checkIfAlreadyExisting) {
            Optional<String> firstPath = models.keySet().stream().findFirst();
            if (!firstPath.isPresent())
                return;
            final String firstObjId = itemId + identifier + firstPath.get();
            if (cache.containsKey(firstObjId)) {
                return;
            }
        }

        for (Map.Entry<String, ContentPackModel[]> modelEntry : models.entrySet()) {
            try {
                final String path = modelEntry.getKey();
                // identifiers: /signals / and /base/
                final String objId = itemId + identifier + path;

                Set<String> objTextures = LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().get(itemId).getObjTextures().get(path);
                if(objTextures.contains(null)){
                    objTextures.remove(null);
                    objTextures.add("");
                }
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, path), 0, objTextures);
                cache.putIfAbsent(objId, model);

                for (ContentPackModel signalModel : modelEntry.getValue()) {
                    String[] groups = signalModel.getObj_groups();
                    if (groups.length > 0) {
                        Predicate<String> targetGroup = renderOBJGroup -> Arrays.stream(groups).filter(Objects::nonNull).anyMatch(renderOBJGroup::startsWith);
                        List<String> modes = model.groups().stream().filter(targetGroup)
                                .collect(Collectors.toCollection(ArrayList::new));
                        String groupCacheId = objId + "@" + String.join("+", groups);
                        if(modes.contains(null)){
                            modes.remove(null);
                            modes.add("");
                        }
                        groupCache.put(groupCacheId, modes);
                    }
                }
            } catch (Exception e) {
                String message = String.format("Couldn't cache the following: itemId: %s; identifier: %s", itemId, identifier);
                throw new ItemRenderException(message, e);
            }
        }

    }

    @Override
    public StandardModel getModel(World world, ItemStack stack) {
        return new StandardModel().addCustom((state, partialTicks) -> {

            final TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().containsKey(itemId)) {
                itemId = Static.MISSING;
            }

            final Map<String, String> itemGroupStates = new HashMap<>(LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().get(itemId).getItemGroupStates());
            if (tag.hasKey("itemGroupState")) {
                itemGroupStates.putAll(tag.getMap("itemGroupState", EmptyStringMapper::fromNullString, value -> value.getString("string")));
            }
            itemGroupStates.replaceAll((key, value) -> value == null ? "" : value);

            renderBase(itemId, state);
            renderSignals(itemId, itemGroupStates, state);

        });
    }

    @Override
    public void applyTransform(ItemStack stack, ItemRender.ItemRenderType type, RenderState ctx) {

        // Implement ItemRenderType with new UMC rendering

        ItemRender.IItemModel.super.applyTransform(stack, type, ctx);
    }

    @SuppressWarnings("java:S1134")
    private static void renderBase(String itemId, RenderState state) {

        checkCache(itemId, LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().get(itemId).getBase(), "/base/", true);

        for (Map.Entry<String, ContentPackModel[]> baseModels : LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().get(itemId).getBase().entrySet()) {

            final String path = baseModels.getKey();

            final String objId = itemId + "/base/" + path;
            final OBJModel model = cache.get(objId);

            for (ContentPackModel baseModel : baseModels.getValue()) {

                RenderState iterationState = state.clone();

                final ContentPackItem item = baseModel.getItem().get(ContentPackItemRenderType.DEFAULT);
                final Vec3d translate = item.getAsVec3d(item::getTranslation);
                final Vec3d scale = item.getAsVec3d(item::getScaling);
                final Vec3d rotation = item.getAsVec3d(item::getRotation);

                iterationState.scale(scale);
                iterationState.translate(translate);
                iterationState.rotate(rotation.x, 1, 0, 0);
                iterationState.rotate(rotation.y, 0, 1, 0);
                iterationState.rotate(rotation.z, 0, 0, 1);

                try (OBJRender.Binding vbo = model.binder().texture(baseModel.getTextures()).bind(iterationState)) {

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

    @SuppressWarnings("java:S1134")
    private static void renderSignals(String itemId, Map<String, String> itemGroupStates, RenderState state) {
        final Map<String, ContentPackSignalGroup> signalGroups = LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().get(itemId).getSignals();

        checkCache(itemId, signalGroups.values());

        for (Map.Entry<String, ContentPackSignalGroup> signalGroup : signalGroups.entrySet()) {

            final ContentPackSignalState signalState = signalGroup.getValue().getStates().get(itemGroupStates.get(signalGroup.getKey()));

            for (Map.Entry<String, ContentPackModel[]> signalModels : signalState.getModels().entrySet()) {

                final String path = signalModels.getKey();

                final String objId = itemId + SIGNAL_IDENTIFIER + path;
                final OBJModel model = cache.get(objId);

                for (ContentPackModel signalModel : signalModels.getValue()) {

                    RenderState iterationState = state.clone();

                    final ContentPackItem item = signalModel.getItem().get(ContentPackItemRenderType.DEFAULT);
                    final Vec3d translate = item.getAsVec3d(item::getTranslation);
                    final Vec3d scale = item.getAsVec3d(item::getScaling);
                    final Vec3d rotation = item.getAsVec3d(item::getRotation);

                    iterationState.scale(scale);
                    iterationState.translate(translate);
                    iterationState.rotate(rotation.x, 1, 0, 0);
                    iterationState.rotate(rotation.y, 0, 1, 0);
                    iterationState.rotate(rotation.z, 0, 0, 1);

                    try (OBJRender.Binding vbo = model.binder().texture(signalModel.getTextures()).bind(iterationState)) {
                        // Render
                        String[] groups = signalModel.getObj_groups();
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

}
