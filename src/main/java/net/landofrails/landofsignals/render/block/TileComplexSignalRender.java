package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackComplexSignal;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackSignalGroup;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackSignalState;
import net.landofrails.api.contentpacks.v2.parent.ContentPackBlock;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileComplexSignal;
import net.landofrails.landofsignals.utils.FlareUtils;
import net.landofrails.landofsignals.utils.HighlightingUtil;
import net.landofrails.landofsignals.utils.Static;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static net.landofrails.landofsignals.utils.LandOfSignalsUtils.objIdWithGroup;
import static net.landofrails.landofsignals.utils.LandOfSignalsUtils.objIdWithoutGroup;

public class TileComplexSignalRender {

    private TileComplexSignalRender() {

    }

    private static final Map<String, OBJModel> cache = new HashMap<>();
    private static final Map<String, List<String>> groupCache = new HashMap<>();

    public static void checkCache(String blockId, Map<String, ContentPackSignalGroup> groups, String identifier) {

        // Get first group, get first state, get first
        Map.Entry<String, ContentPackSignalGroup> firstGroup = groups.entrySet().iterator().next();
        String firstGroupId = firstGroup.getKey();
        Optional<String> firstPath = firstGroup.getValue().getStates().values().iterator().next().getModels().keySet().stream().findFirst();

        if (!firstPath.isPresent())
            return;
        final String firstObjId = objIdWithGroup(blockId, identifier, firstGroupId, firstPath.get());
        if (cache.containsKey(firstObjId)) {
            return;
        }

        for (Map.Entry<String, ContentPackSignalGroup> group : groups.entrySet()) {
            for (ContentPackSignalState state : group.getValue().getStates().values()) {
                checkCache(blockId, group.getKey(), state.getModels(), identifier, false);
            }
        }

    }

    public static void checkCache(String blockId, String groupId, Map<String, ContentPackModel[]> models, String identifier, boolean checkIfAlreadyExisting) {
        if (checkIfAlreadyExisting) {
            Optional<String> firstPath = models.keySet().stream().findFirst();
            if (!firstPath.isPresent())
                return;
            final String firstObjId = objIdWithGroup(blockId, identifier, groupId, firstPath.get());
            if (cache.containsKey(firstObjId)) {
                return;
            }
        }

        for (Map.Entry<String, ContentPackModel[]> modelEntry : models.entrySet()) {
            try {
                final String path = modelEntry.getKey();
                // identifiers: /signals / and /base/
                final String objId = objIdWithGroup(blockId, identifier, groupId, path);

                Set<String> objTextures = LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().get(blockId).getObjTextures().get(path);
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, path), 0, objTextures);
                cache.putIfAbsent(objId, model);

                for (ContentPackModel signalModel : modelEntry.getValue()) {
                    String[] groups = signalModel.getObj_groups();
                    if (groups.length > 0) {
                        Predicate<String> targetGroup = renderOBJGroup -> Arrays.stream(groups).filter(Objects::nonNull).anyMatch(renderOBJGroup::startsWith);
                        List<String> modes = model.groups().stream().filter(targetGroup)
                                .collect(Collectors.toCollection(ArrayList::new));
                        String groupCacheId = objId + "@" + String.join("+", groups);
                        groupCache.put(groupCacheId, modes);
                    }
                }
            } catch (Exception e) {
                String message = String.format("Couldn't cache the following: blockId: %s; identifier: %s", blockId, identifier);
                throw new BlockRenderException(message, e);
            }
        }

    }

    public static StandardModel render(final TileComplexSignal tsp) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(tsp, state));
    }

    private static void renderStuff(final TileComplexSignal tsp, RenderState state) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        ContentPackComplexSignal signal = LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().get(id);

        renderBase(signal, tsp, state);
        renderSignals(signal, tsp, state);

        if(signal.getFlares().length > 0)
            FlareUtils.renderFlares(id, signal, tsp, state.clone());

        if(tsp.isHighlighting()){
            HighlightingUtil.renderHighlighting(state.clone());
        }

    }

    @SuppressWarnings("java:S1134")
    private static void renderBase(ContentPackComplexSignal signal, TileComplexSignal tile, RenderState state) {

        final Vec3d offset = tile.getOffset();
        final Vec3d customScaling = tile.getScaling();

        checkCache(signal.getUniqueId(), "", signal.getBase(), "base", true);

        for (Map.Entry<String, ContentPackModel[]> baseModels : signal.getBase().entrySet()) {

            final String path = baseModels.getKey();

            // Needs to be split from signal
            final String objId = objIdWithoutGroup(signal.getUniqueId(), "base", path);
            final OBJModel model = cache.get(objId);

            for (ContentPackModel baseModel : baseModels.getValue()) {

                RenderState iterationState = state.clone();

                final ContentPackBlock block = baseModel.getBlock();
                final Vec3d translate = block.getAsVec3d(block::getTranslation).add(offset);
                final Vec3d scale = Static.multiply(block.getAsVec3d(block::getScaling), customScaling);
                final Vec3d rotation = block.getAsVec3d(block::getRotation);

                iterationState.scale(scale);
                iterationState.translate(translate);
                iterationState.rotate(rotation.x,1, 0, 0);
                iterationState.rotate(tile.getBlockRotate() + rotation.y, 0, 1, 0);
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

                } catch (Exception e) {
                    // Removes TileEntity on client-side, prevents crash
                    ModCore.error("Removing local ComplexSignal (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
                    tile.getWorld().breakBlock(tile.getPos());

                }
            }
        }
    }

    @SuppressWarnings("java:S1134")
    private static void renderSignals(final ContentPackComplexSignal signal, final TileComplexSignal tile, RenderState state) {

        final Vec3d offset = tile.getOffset();
        final Vec3d customScaling = tile.getScaling();

        final Map<String, ContentPackSignalGroup> signalGroups = signal.getSignals();

        final Map<String, String> tileSignalGroups = tile.getSignalGroupStates();

        checkCache(signal.getUniqueId(), signalGroups, "signals");

        for (Map.Entry<String, ContentPackSignalGroup> signalGroupEntry : signalGroups.entrySet()) {

            final String groupId = signalGroupEntry.getKey();
            final ContentPackSignalGroup signalGroup = signalGroupEntry.getValue();

            final String tileSignalState = tileSignalGroups.get(groupId);
            final ContentPackSignalState signalState = signalGroup.getStates().get(tileSignalState);

            for (Map.Entry<String, ContentPackModel[]> signalModels : signalState.getModels().entrySet()) {

                final String path = signalModels.getKey();

                // Needs to be split from base
                final String objId = objIdWithGroup(signal.getUniqueId(), "signals", groupId, path);
                final OBJModel model = cache.get(objId);

                for (ContentPackModel signalModel : signalModels.getValue()) {

                    RenderState iterationState = state.clone();

                    ContentPackBlock block = signalModel.getBlock();
                    final Vec3d translate = block.getAsVec3d(block::getTranslation).add(offset);
                    final Vec3d scale = Static.multiply(block.getAsVec3d(block::getScaling), customScaling);
                    final Vec3d rotation = block.getAsVec3d(block::getRotation);

                    iterationState.scale(scale);
                    iterationState.translate(translate);
                    iterationState.rotate(rotation.x,1, 0, 0);
                    iterationState.rotate(tile.getBlockRotate() + rotation.y, 0, 1, 0);
                    iterationState.rotate(rotation.z, 0, 0, 1);
                    try (OBJRender.Binding vbo = model.binder().texture(signalModel.getTextures()).bind(iterationState)) {

                        String[] groups = signalModel.getObj_groups();

                        if (groups.length == 0) {
                            vbo.draw();
                        } else {

                            String groupCacheId = objId + "@" + String.join("+", groups);
                            vbo.draw(groupCache.get(groupCacheId));
                        }

                    } catch (Exception e) {
                        // Removes TileEntity on client-side, prevents crash
                        ModCore.error("Removing local ComplexSignal (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
                        tile.getWorld().breakBlock(tile.getPos());

                    }
                }

            }

        }
    }

    public static Map<String, OBJModel> cache() {
        return cache;
    }
}