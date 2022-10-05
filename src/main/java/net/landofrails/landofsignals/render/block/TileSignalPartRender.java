package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.parent.ContentPackBlock;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalState;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TileSignalPartRender {

    private TileSignalPartRender() {

    }

    private static final Map<String, OBJRender> cache = new HashMap<>();

    public static StandardModel render(final TileSignalPart tsp) {
        return new StandardModel().addCustom(() -> renderStuff(tsp));
    }

    private static void renderStuff(final TileSignalPart tsp) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        renderBase(id, tsp);
        renderSignals(id, tsp);

    }

    @SuppressWarnings("java:S1134")
    private static void renderBase(String blockId, TileSignalPart tile) {

        for (Map.Entry<String, ContentPackModel[]> baseModels : LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(blockId).getBase().entrySet()) {

            final String path = baseModels.getKey();

            final String objId = blockId + "/" + path;
            if (!cache.containsKey(objId)) {
                try {
                    Set<String> objTextures = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(blockId).getObjTextures().get(path);
                    cache.put(objId, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, path), 0, objTextures)));
                } catch (Exception e) {
                    throw new BlockRenderException("Error loading block model/renderer...", e);
                }
            }
            final OBJRender renderer = cache.get(objId);

            for (ContentPackModel baseModel : baseModels.getValue()) {
                final ContentPackBlock block = baseModel.getBlock();
                final Vec3d translate = block.getAsVec3d(block::getTranslation);
                final Vec3d scale = block.getAsVec3d(block::getScaling);
                final Vec3d rotation = block.getAsVec3d(block::getRotation);
                final List<OpenGL.With> closables = new ArrayList<>();
                try {
                    // Load
                    closables.add(OpenGL.matrix());
                    for (String texture : baseModel.getTextures()) {
                        closables.add(renderer.bindTexture(texture));
                    }
                    if (closables.size() == 1) {
                        closables.add(renderer.bindTexture());
                    }

                    // Render
                    GL11.glScaled(scale.x, scale.y, scale.z);
                    GL11.glTranslated(translate.x, translate.y, translate.z);
                    GL11.glRotated(rotation.x, 1, 0, 0);
                    GL11.glRotated(tile.getBlockRotate() + rotation.y, 0, 1, 0);
                    GL11.glRotated(rotation.z, 0, 0, 1);

                    String[] groups = baseModel.getObj_groups();
                    if (groups.length == 0) {
                        renderer.draw();
                    } else {
                        // FIXME This is nasty - Maybe cache this?
                        Predicate<String> targetGroup = renderOBJGroup -> Arrays.stream(groups).anyMatch(renderOBJGroup::startsWith);
                        ArrayList<String> modes = renderer.model.groups().stream().filter(targetGroup)
                                .collect(Collectors.toCollection(ArrayList::new));
                        renderer.drawGroups(modes);
                    }

                } catch (Exception e) {
                    // Removes TileEntity on client-side, prevents crash
                    ModCore.error("Removing local SignalPart (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
                    tile.getWorld().breakBlock(tile.getPos());

                } finally {
                    closables.forEach(OpenGL.With::close);
                }


            }
        }
    }

    @SuppressWarnings("java:S1134")
    private static void renderSignals(final String blockId, final TileSignalPart tile) {
        final Map<String, ContentPackSignalGroup> signalGroups = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(blockId).getSignals();

        final Map<String, String> tileSignalGroups = tile.getSignalGroupStates();

        for (Map.Entry<String, ContentPackSignalGroup> signalGroupEntry : signalGroups.entrySet()) {

            final String groupId = signalGroupEntry.getKey();
            final ContentPackSignalGroup signalGroup = signalGroupEntry.getValue();

            final String tileSignalState = tileSignalGroups.get(groupId);
            final ContentPackSignalState signalState = signalGroup.getStates().get(tileSignalState);

            for (Map.Entry<String, ContentPackModel[]> signalModels : signalState.getModels().entrySet()) {

                final String path = signalModels.getKey();

                final String objId = blockId + "/" + path;
                if (!cache.containsKey(objId)) {
                    try {
                        Set<String> objTextures = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(blockId).getObjTextures().get(path);
                        cache.put(objId, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, path), 0, objTextures)));
                    } catch (Exception e) {
                        throw new BlockRenderException("Error loading block model/renderer...", e);
                    }
                }
                final OBJRender renderer = cache.get(objId);

                for (ContentPackModel signalModel : signalModels.getValue()) {
                    ContentPackBlock block = signalModel.getBlock();
                    final Vec3d translate = block.getAsVec3d(block::getTranslation);
                    final Vec3d scale = block.getAsVec3d(block::getScaling);
                    final Vec3d rotation = block.getAsVec3d(block::getRotation);
                    final List<OpenGL.With> closables = new ArrayList<>();
                    try {
                        // Load
                        closables.add(OpenGL.matrix());
                        for (String texture : signalModel.getTextures()) {
                            closables.add(renderer.bindTexture(texture));
                        }
                        if (closables.size() == 1) {
                            closables.add(renderer.bindTexture());
                        }

                        // Render
                        GL11.glScaled(scale.x, scale.y, scale.z);
                        GL11.glTranslated(translate.x, translate.y, translate.z);
                        GL11.glRotated(rotation.x, 1, 0, 0);
                        GL11.glRotated(tile.getBlockRotate() + rotation.y, 0, 1, 0);
                        GL11.glRotated(rotation.z, 0, 0, 1);

                        String[] groups = signalModel.getObj_groups();
                        if (groups.length == 0) {
                            renderer.draw();
                        } else {
                            // FIXME This is nasty - Maybe cache this?
                            Predicate<String> targetGroup = renderOBJGroup -> Arrays.stream(groups).anyMatch(renderOBJGroup::startsWith);
                            ArrayList<String> modes = renderer.model.groups().stream().filter(targetGroup)
                                    .collect(Collectors.toCollection(ArrayList::new));
                            renderer.drawGroups(modes);
                        }

                    } catch (Exception e) {
                        // Removes TileEntity on client-side, prevents crash
                        ModCore.error("Removing local SignalPart (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
                        tile.getWorld().breakBlock(tile.getPos());

                    } finally {
                        closables.forEach(OpenGL.With::close);
                    }
                }

            }


        }
    }

}