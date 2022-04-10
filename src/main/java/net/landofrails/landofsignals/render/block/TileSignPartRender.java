package net.landofrails.landofsignals.render.block;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignPart;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TileSignPartRender {

    private TileSignPartRender() {

    }

    private static final Map<String, Map<Integer, OBJRender>> cache = new HashMap<>();

    public static StandardModel render(TileSignPart tsp) {
        return new StandardModel().addCustom(() -> renderStuff(tsp));
    }

    private static void renderStuff(TileSignPart tsp) {
        String itemId = tsp.getId();
        if (!cache.containsKey(itemId)) {
            Map<Integer, OBJRender> objRenderers = new HashMap<>();
            for (Map.Entry<Integer, String> path : LOSBlocks.BLOCK_SIGN_PART.getPath(itemId).entrySet()) {
                try {

                    OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, path.getValue()), 0);
                    objRenderers.put(path.getKey(), new OBJRender(model));
                } catch (Exception e) {
                    throw new RuntimeException("Error loading item model...", e);
                }
            }
            if (objRenderers.isEmpty()) {
                throw new RuntimeException("Couldnt find any ContentPackSignObjects!");
            } else {
                cache.put(itemId, objRenderers);
            }
        }
        for (Map.Entry<Integer, OBJRender> rendererEntry : cache.get(itemId).entrySet()) {
            Integer id = rendererEntry.getKey();
            OBJRender renderer = rendererEntry.getValue();
            try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With ignored1 = renderer.bindTexture(LOSBlocks.BLOCK_SIGN_PART.getTexture(itemId).get(id))) {
                Vec3d scale = LOSBlocks.BLOCK_SIGN_PART.getScaling(itemId).get(id);
                GL11.glScaled(scale.x, scale.y, scale.z);
                Vec3d trans = LOSBlocks.BLOCK_SIGN_PART.getTranslation(itemId).get(id).add(tsp.getOffset());
                String[] groups = LOSBlocks.BLOCK_SIGN_PART.getRenderGroups(itemId).get(id);
                GL11.glTranslated(trans.x, trans.y, trans.z);
                GL11.glRotated(tsp.getBlockRotate(), 0, 1, 0);
                if (groups.length > 0) {
                    renderer.drawGroups(Arrays.asList(groups));
                } else {
                    renderer.draw();
                }
            }
        }
    }

    /**
     * Releases the renderer in to the wild and frees the cache preventing a deadlock situation
     */
    public static void releaseRenderersIntoTheWild() {

        for (Map<Integer, OBJRender> entry : cache.values()) {
            entry.values().forEach(OBJRender::free);
        }
        cache.clear();
    }

}
