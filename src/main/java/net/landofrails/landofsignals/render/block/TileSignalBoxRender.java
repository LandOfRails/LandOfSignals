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
import net.landofrails.api.contentpacks.v2.signalbox.ContentPackSignalbox;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class TileSignalBoxRender {

    private TileSignalBoxRender() {

    }

    private static OBJRender renderer;
    private static OBJModel model;

    private static final Map<String, OBJRender> cache = new HashMap<>();

    public static StandardModel render(TileSignalBox tsp) {
        return new StandardModel().addCustom(() -> renderStuff(tsp));
    }

    private static void renderStuff(TileSignalBox tsp) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        renderBase(id, tsp);

    }

    private static void renderBase(String blockId, TileSignalBox tile) {

        ContentPackSignalbox contentPackSignalboxes = LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().get(blockId);
        for (Map.Entry<String, ContentPackModel[]> baseModels : contentPackSignalboxes.getBase().entrySet()) {

            String path = baseModels.getKey();

            String objId = blockId + "/" + path;
            if (!cache.containsKey(objId)) {
                try {
                    Set<String> objTextures = LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().get(blockId).getObjTextures().get(path);
                    cache.put(objId, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, path), 0, objTextures)));

                } catch (Exception e) {
                    throw new BlockRenderException("Error loading block model/renderer...", e);
                }
            }
            OBJRender renderer = cache.get(objId);

            for (ContentPackModel baseModel : baseModels.getValue()) {
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
                    GL11.glScaled(scale.x, scale.y, scale.z);
                    GL11.glTranslated(translate.x, translate.y, translate.z);
                    GL11.glRotated(rotation.x, 1, 0, 0);
                    GL11.glRotated(tile.getBlockRotate() + rotation.y, 0, 1, 0);
                    GL11.glRotated(rotation.z, 0, 0, 1);

                    String[] groups = baseModel.getObj_groups();
                    if (groups.length == 0) {
                        renderer.draw();
                    } else {
                        renderer.drawGroups(Arrays.asList(groups));
                    }

                } catch (Exception e) {
                    // Removes TileEntity on client-side, prevents crash
                    ModCore.error("Removing local Signalbox- (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
                    tile.getWorld().breakBlock(tile.getPos());

                } finally {
                    closables.forEach(OpenGL.With::close);
                }


            }
        }
    }

}
