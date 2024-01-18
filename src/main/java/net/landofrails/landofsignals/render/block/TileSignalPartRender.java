package net.landofrails.landofsignals.render.block;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJGroup;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.BlendMode;
import cam72cam.mod.render.opengl.DirectDraw;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.render.opengl.Texture;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.flares.Flare;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.render.item.ItemRenderException;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.HighlightingUtil;
import net.landofrails.landofsignals.utils.Static;
import net.landofrails.landofsignals.utils.VecUtil;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TileSignalPartRender {

    private TileSignalPartRender() {

    }

    private static final Map<String, OBJModel> cache = new HashMap<>();
    private static final Map<String, Map<String, Flare[]>> flareCache = new HashMap<>();

    public static StandardModel render(final TileSignalPart tsp) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(tsp, state));
    }

    private static void renderStuff(final TileSignalPart tsp, RenderState state) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        ContentPackSignal signal = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(id);

        if(signal == null) {
            signal = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(Static.MISSING);
            tsp.setState("");
        }

        if (signal.getUseBase()) {
            renderBase(id, signal, tsp, state.clone());
        }
        renderSignals(id, signal, tsp, state.clone());

        // TODO remove/adjust when generalized
        if(id.contains("block_signal_part_light_flare"))
            renderFlares(id, signal, tsp, state.clone());
        //

        if(tsp.isHighlighting()){
            HighlightingUtil.renderHighlighting(state.clone());
        }

    }

    @SuppressWarnings("java:S1134")
    private static void renderBase(String blockId, ContentPackSignal signal, TileSignalPart tile, RenderState state) {

        final Vec3d offset = tile.getOffset();
        final Vec3d customScaling = tile.getScaling();
        final String base = signal.getBase();
        final String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(blockId);
                cache.put(objPath, new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states)));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJModel model = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling().clone();
        scale[0] *= customScaling.x;
        scale[1] *= customScaling.y;
        scale[2] *= customScaling.z;

        state.scale(scale[0], scale[1], scale[2]);
        state.translate(translate.x, translate.y, translate.z);
        state.rotate(tile.getBlockRotate(), 0, 1, 0);

        try (OBJRender.Binding vbo = model.binder().texture(base).bind(state)) {
            // Render
            vbo.draw();
        } catch (Exception e) {
            // Removes TileEntity on client-side, prevents crash
            ModCore.error("Removing local SignalPart (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
            tile.getWorld().breakBlock(tile.getPos());

        }

    }


    @SuppressWarnings("java:S1134")
    private static void renderSignals(String blockId, ContentPackSignal signal, TileSignalPart tile, RenderState state) {

        final Vec3d offset = tile.getOffset();
        final Vec3d customScaling = tile.getScaling();
        final String signalState = tile.getState();
        final String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(blockId);
                cache.put(objPath, new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states)));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJModel model = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling().clone();
        scale[0] *= customScaling.x;
        scale[1] *= customScaling.y;
        scale[2] *= customScaling.z;


        state.scale(scale[0], scale[1], scale[2]);
        state.translate(translate);
        state.rotate(tile.getBlockRotate(), 0, 1, 0);

        try (OBJRender.Binding vbo = model.binder().texture(signalState).bind(state)) {
            // Render
            vbo.draw();
        } catch (Exception e) {
            // Removes TileEntity on client-side, prevents crash
            ModCore.error("Removing local SignalPart (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
            tile.getWorld().breakBlock(tile.getPos());

        }
    }

    public static Map<String, OBJModel> cache(){
        return cache;
    }


    private static void renderFlares(String id, ContentPackSignal signal, TileSignalPart tile, RenderState renderState) {

        // TODO Do it sooner. And maybe there is a better implementation?
        if (!flareCache.containsKey(id)) {
            try {
                Map<String, List<Flare>> dynStateFlares = new HashMap<>();
                Flare[] flares = signal.getFlares();
                for(Flare flare : flares){
                    String[] flareStates = flare.getStates();
                    for(String state : flareStates){
                        dynStateFlares.putIfAbsent(state, new ArrayList<>());
                        dynStateFlares.get(state).add(flare);
                    }
                }
                Map<String, Flare[]> stateFlares = new HashMap<>();
                dynStateFlares.forEach((dynState, dynFlares) -> stateFlares.put(dynState, dynFlares.toArray(new Flare[0])));
                flareCache.put(id, stateFlares);
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }

        final String signalState = tile.getState();
        Flare[] flares = flareCache.get(id).get(signalState);

        if(flares == null) {
            // No flares - no rendering
            return;
        }

        for(Flare flare : flares){
            RenderState flareState = renderState.clone();
            String flareId = flare.getId();

            final String objPath = signal.getModel();
            final OBJModel model = cache.get(objPath);
            Predicate<Map.Entry<String, OBJGroup>> isLightFlare = group -> group.getKey().startsWith(flareId); // TODO Preload this as well?
            String errMsg = String.format("Uh oh. Did not find group(s) %s in model %s", flareId, objPath);
            Map<String, OBJGroup> flareGroups = model.groups.entrySet().stream().filter(isLightFlare).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if(flareGroups.isEmpty())
                throw new RuntimeException(errMsg);

            float red = flare.getRenderColor()[0];
            float green = flare.getRenderColor()[1];
            float blue = flare.getRenderColor()[2];

            int flareRotation = flare.getRotation();
            int flarePitch = flare.getPitch();
            float flareOffset = flare.getOffset();

            Identifier lightTex = new Identifier(LandOfSignals.MODID, "textures/light/antivignette.png");

            Collection<OBJGroup> flareGroupsOBJGroups = flareGroups.values();
            double maxZ = flareGroupsOBJGroups.stream().mapToDouble(g -> g.max.z).max().getAsDouble();
            double minZ = flareGroupsOBJGroups.stream().mapToDouble(g -> g.min.z).min().getAsDouble();
            double maxX = flareGroupsOBJGroups.stream().mapToDouble(g -> g.max.x).max().getAsDouble();
            double minX = flareGroupsOBJGroups.stream().mapToDouble(g -> g.min.x).min().getAsDouble();

            double scale = Math.max(maxZ - minZ, maxX - minX);


            // Translation and Intensity calculations

            Vec3d centerOfModel = model.centerOfGroups(model.groups());
            Vec3d centerOfLightFlare = model.centerOfGroups(flareGroups.keySet());
            Vec3d modelOffset = centerOfLightFlare.subtract(centerOfModel);
            modelOffset = new Vec3d(modelOffset.x, modelOffset.y, -modelOffset.z - flareOffset);
            Vec3d flareCenterOffset = new Vec3d(0.5f, 0.5f,0.5f); // Set position to center of block
            Vec3d combinedOffset = flareCenterOffset.add(modelOffset);

            Vec3d playerOffset = VecUtil.rotateWrongYaw(
                            new Vec3d(tile.getPos()).subtract(MinecraftClient.getPlayer().getPosition()),
                            tile.getBlockRotate() + 180).
                            subtract(combinedOffset);

            int viewAngle = 45;
            float intensity = 1 - Math.abs(Math.max(-viewAngle, Math.min(viewAngle, VecUtil.toWrongYaw(playerOffset) - 90))) / viewAngle;
            intensity *= Math.abs(playerOffset.x/50);
            intensity = Math.min(intensity, 1.5f);

            //

            flareState.texture(Texture.wrap(lightTex))
                    .lightmap(1, 1)
                    .depth_test(true)
                    .depth_mask(false)
                    .alpha_test(false).blend(new BlendMode(BlendMode.GL_SRC_ALPHA, BlendMode.GL_ONE_MINUS_SRC_ALPHA));

            flareState.translate(flareCenterOffset);

            flareState.rotate(flarePitch, 0, 0, 1);
            flareState.rotate((double) tile.getBlockRotate() + flareRotation,0,1,0);

            flareState.translate(modelOffset); // move it towards the position of the light flare

            // Moving flare

            if (intensity > 0.01) {
                RenderState matrix = flareState.clone();
                matrix.translate(0, 0, -((intensity / 2) * 3));
                double scale2 = Math.max(scale * 0.5, intensity * 2);
                matrix.scale(scale2, scale2, scale2);

                matrix.color(red, green, blue, 1 - (intensity/3f));

                DirectDraw buffer = new DirectDraw();
                buffer.vertex(-1, -1, 0).uv(0, 0);
                buffer.vertex(-1, 1, 0).uv(0, 1);
                buffer.vertex(1, 1, 0).uv(1, 1);
                buffer.vertex(1, -1, 0).uv(1, 0);
                buffer.draw(matrix);
            }

            //

            flareState.color((float)Math.sqrt(red), (float)Math.sqrt(green), (float)Math.sqrt(blue), 1 - (intensity/3f));

            flareState.scale(scale, scale, scale);

            DirectDraw buffer = new DirectDraw();
            buffer.vertex(-1, -1, 0).uv(0, 0);
            buffer.vertex(-1, 1, 0).uv(0, 1);
            buffer.vertex(1, 1, 0).uv(1, 1);
            buffer.vertex(1, -1, 0).uv(1, 0);
            buffer.draw(flareState);
        }

    }

}