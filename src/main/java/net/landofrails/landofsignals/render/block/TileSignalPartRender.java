package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.parent.ContentPackBlock;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalModel;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalState;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class TileSignalPartRender {

    private TileSignalPartRender() {

    }

    private static final Map<String, OBJRender> cache = new HashMap<>();

    public static StandardModel render(TileSignalPart tsp) {
        return new StandardModel().addCustom(() -> renderStuff(tsp));
    }

    private static void renderStuff(TileSignalPart tsp) {

        String id = tsp.getId();

        // TODO Implement new system.

        boolean isOld = LOSBlocks.BLOCK_SIGNAL_PART.getSignalParts_depr().containsKey(id);
        boolean isNew = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().containsKey(id);
        if (id == null || (!isOld && !isNew)) {
            id = Static.MISSING;
        }

        if (isNew) {

            renderBase(id);
            renderSignals(id);

            return;
        }

        // FIXME remove after implementation of contentpackconverter
        // Old dumb stuff, yk

        if (!cache.containsKey(id)) {
            try {
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART.getPath_depr(id)), 0, LOSBlocks.BLOCK_SIGNAL_PART.getStates_depr(id));
                OBJRender renderer = new OBJRender(model);
                cache.put(id, renderer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OBJRender renderer = cache.get(id);
        try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture(tsp.getTexturePath())) {
            Vec3d scale = LOSBlocks.BLOCK_SIGNAL_PART.getScaling_depr(id);
            GL11.glScaled(scale.x, scale.y, scale.z);
            Vec3d trans = LOSBlocks.BLOCK_SIGNAL_PART.getTranslation_depr(id).add(tsp.getOffset());
            GL11.glTranslated(trans.x, trans.y, trans.z);
            GL11.glRotated(tsp.getBlockRotate(), 0, 1, 0);
            renderer.draw();
        } catch (Exception e) {
            // Removes TileEntity on client-side, prevents crash
            ModCore.error("Removing local SignalPart (x%d, y%d, z%d) due to exceptions: %s", tsp.getPos().x, tsp.getPos().y, tsp.getPos().z, e.getMessage());
            tsp.getWorld().breakBlock(tsp.getPos());
        }
    }

    private static void renderBase(String blockId) {

        for (Map.Entry<String, ContentPackSignalModel[]> baseModels : LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(blockId).getBase().entrySet()) {

            String path = baseModels.getKey();

            String objId = blockId + "/" + path;
            if (!cache.containsKey(objId)) {
                try {
                    cache.put(objId, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, path), 0)));

                } catch (Exception e) {
                    throw new BlockRenderException("Error loading block model/renderer...", e);
                }
            }
            OBJRender renderer = cache.get(objId);

            for (ContentPackSignalModel baseModel : baseModels.getValue()) {
                ContentPackBlock block = baseModel.getBlock();
                Vec3d translate = block.getAsVec3d(block::getTranslation);
                Vec3d scale = block.getAsVec3d(block::getScaling);
                Vec3d rotation = block.getAsVec3d(block::getRotation);
                List<OpenGL.With> closables = new ArrayList<>();
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
                    GL11.glRotated(rotation.x, 1, 0, 0);
                    GL11.glRotated(rotation.y, 0, 1, 0);
                    GL11.glRotated(rotation.z, 0, 0, 1);
                    GL11.glTranslated(translate.x, translate.y, translate.z);
                    GL11.glScaled(scale.x, scale.y, scale.z);

                    String[] groups = baseModel.getObj_groups();
                    if (groups.length == 0) {
                        renderer.draw();
                    } else {
                        renderer.drawGroups(Arrays.asList(groups));
                    }

                } finally {
                    closables.forEach(OpenGL.With::close);
                }


            }
        }
    }

    private static void renderSignals(String blockId) {
        Collection<ContentPackSignalGroup> signalGroups = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(blockId).getSignals().values();

        for (ContentPackSignalGroup signalGroup : signalGroups) {

            ContentPackSignalState signalState = signalGroup.getStates().values().iterator().next();

            for (Map.Entry<String, ContentPackSignalModel[]> signalModels : signalState.getModels().entrySet()) {

                String path = signalModels.getKey();

                String objId = blockId + "/" + path;
                if (!cache.containsKey(objId)) {
                    try {
                        cache.put(objId, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, path), 0)));
                    } catch (Exception e) {
                        throw new BlockRenderException("Error loading block model/renderer...", e);
                    }
                }
                OBJRender renderer = cache.get(objId);

                for (ContentPackSignalModel signalModel : signalModels.getValue()) {
                    ContentPackBlock block = signalModel.getBlock();
                    Vec3d translate = block.getAsVec3d(block::getTranslation);
                    Vec3d scale = block.getAsVec3d(block::getScaling);
                    Vec3d rotation = block.getAsVec3d(block::getRotation);
                    List<OpenGL.With> closables = new ArrayList<>();
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
                        GL11.glRotated(rotation.x, 1, 0, 0);
                        GL11.glRotated(rotation.y, 0, 1, 0);
                        GL11.glRotated(rotation.z, 0, 0, 1);
                        GL11.glTranslated(translate.x, translate.y, translate.z);
                        GL11.glScaled(scale.x, scale.y, scale.z);

                        List<String> groups = Arrays.asList(signalModel.getObj_groups());
                        if (groups.isEmpty()) {
                            renderer.draw();
                        } else {
                            renderer.drawGroups(groups);
                        }

                    } finally {
                        closables.forEach(OpenGL.With::close);
                    }
                }

            }


        }
    }

}