package net.landofrails.landofsignals.utils;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.model.obj.OBJGroup;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.opengl.BlendMode;
import cam72cam.mod.render.opengl.DirectDraw;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.render.opengl.Texture;
import cam72cam.mod.resource.Identifier;
import com.google.common.collect.Sets;
import net.landofrails.api.contentpacks.v2.flares.Flare;
import net.landofrails.api.contentpacks.v2.sign.ContentPackSign;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.render.block.TileSignPartRender;
import net.landofrails.landofsignals.render.block.TileSignalPartRender;
import net.landofrails.landofsignals.render.item.ItemRenderException;
import net.landofrails.landofsignals.tile.TileSignPart;
import net.landofrails.landofsignals.tile.TileSignalPart;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlareUtils {

    private static final Identifier LIGHT_TEX = new Identifier(LandOfSignals.MODID, "textures/light/antivignette.png");

    private static final Map<String, Map<String, List<Flare>>> signalpartFlareCache = new HashMap<>();
    private static final Map<String, List<Flare>> signFlareCache = new HashMap<>();


    private FlareUtils(){

    }

    // Render flares

    public static void renderFlares(String id, ContentPackSignal signal, TileSignalPart tile, RenderState renderState) {

        if (!signalpartFlareCache.containsKey(id)) {
            cacheFlares(id, signal);
        }

        final String signalState = tile.getState();
        List<Flare> flares = signalpartFlareCache.get(id).get(signalState);

        if(flares == null) {
            // No flares - no rendering
            return;
        }

        renderFlares(flares.toArray(new Flare[0]), tile.getPos(), tile.getBlockRotate(), tile.getScaling(), tile.getOffset(), renderState);

    }

    public static void renderFlares(String id, ContentPackSign sign, TileSignPart tile, RenderState renderState) {

        if(!signFlareCache.containsKey(id)){
            cacheFlares(id, sign);
        }

        List<Flare> flares = signFlareCache.get(id);
        renderFlares(flares.toArray(new Flare[0]), tile.getPos(), tile.getBlockRotate(), tile.getScaling(), tile.getOffset(), renderState);

    }

    // Cache multiple flares

    public static void cacheFlares(String signalId, ContentPackSignal signal){
        if(signal.getFlares().length == 0) return;
        try {
            Map<String, List<Flare>> stateFlares = new HashMap<>();
            Flare[] flares = signal.getFlares();
            for(Flare flare : flares){
                cacheFlare(flare, stateFlares, signal);
            }
            signalpartFlareCache.put(signalId, stateFlares);
        } catch (Exception e) {
            throw new ItemRenderException("Error loading item model/renderer...", e);
        }
    }

    public static void cacheFlares(String signId, ContentPackSign sign){
        if(sign.getFlares().length == 0) return;
        try {
            List<Flare> cachedFlares = new ArrayList<>();
            for(Flare flare : sign.getFlares()){
                cacheFlare(flare, cachedFlares, sign);
            }
            signFlareCache.put(signId, cachedFlares);
        } catch (Exception e) {
            throw new ItemRenderException("Error loading item model/renderer...", e);
        }
    }

    // Cache one flare

    private static void cacheFlare(Flare flare, List<Flare> cachedFlares, ContentPackSign sign) {

        final String objPath = sign.getUniqueId() + "/" + flare.getObjPath();
        final String flareId = flare.getId();
        final OBJModel model = TileSignPartRender.cache().get(objPath);

        float[] modelTranslation = sign.getBase().get(flare.getObjPath())[0].getBlock().getTranslation();
        float[] modelScaling = sign.getBase().get(flare.getObjPath())[0].getBlock().getScaling();

        String errMsg = String.format("Uh oh. Did not find obj_path %s in model %s for flare %s", flare.getObjPath(), sign.getUniqueId(), flareId);
        if(model == null)
            throw new RuntimeException(errMsg);

        //

        cacheFlare(flare, flareId, model, objPath, modelTranslation, modelScaling);
        cachedFlares.add(flare);
    }

    private static void cacheFlare(Flare flare, Map<String, List<Flare>> stateFlares, ContentPackSignal signal){

        final String flareId = flare.getId();
        final String objPath = signal.getModel();
        final OBJModel model = TileSignalPartRender.cache().get(objPath);

        float[] modelTranslation = signal.getTranslation();
        float[] modelScaling = signal.getScaling();

        //

        cacheFlare(flare, flareId, model, objPath, modelTranslation, modelScaling);

        String[] flareStates = flare.getStates();
        if(flare.isAlwaysOn())
            flareStates = signal.getStates(); // All states
        for(String state : flareStates){
            stateFlares.putIfAbsent(state, new ArrayList<>());
            stateFlares.get(state).add(flare);
        }

    }

    //// COMMON / SHARED LOGIC

    public static void renderFlares(Flare[] flares, Vec3i pos, int blockRotate, Vec3d scaling, Vec3d offset, RenderState renderState) {
        for(Flare flare : flares){
            RenderState flareState = renderState.clone();

            float red = flare.getRenderColor()[0];
            float green = flare.getRenderColor()[1];
            float blue = flare.getRenderColor()[2];

            Vec3d flareRotation = flare.getPrecalculatedData().rotation;

            double scale = flare.getPrecalculatedData().lampScale;

            // Translation and Intensity calculations

            Vec3d playerOffset = VecUtil.rotateWrongYaw(
                            new Vec3d(pos).subtract(MinecraftClient.getPlayer().getPosition()),
                            blockRotate + 180).
                    subtract(flare.getPrecalculatedData().offset);

            int viewAngle = 45;
            float intensity = 1 - Math.abs(Math.max(-viewAngle, Math.min(viewAngle, VecUtil.toWrongYaw(playerOffset) - 90))) / viewAngle;
            intensity *= (float) Math.abs(playerOffset.x/50);
            intensity = Math.min(intensity, 1.5f);

            //

            flareState.texture(Texture.wrap(LIGHT_TEX))
                    .lightmap(1, 1)
                    .depth_test(true)
                    .depth_mask(false)
                    .alpha_test(false).blend(new BlendMode(BlendMode.GL_SRC_ALPHA, BlendMode.GL_ONE_MINUS_SRC_ALPHA));

            flareState.scale(scaling);

            flareState.translate(offset);
            flareState.translate(flare.getPrecalculatedData().offset);

            flareState.rotate(flareRotation.x, 1,0,0);
            flareState.rotate(flareRotation.y + blockRotate,0,1,0);
            flareState.rotate(flareRotation.z, 0,0, 1);

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

    private static void cacheFlare(Flare flare, String flareId, OBJModel model, String objPath, float[] modelTranslation, float[] modelScaling){
        Predicate<Map.Entry<String, OBJGroup>> isLightFlare = group -> group.getKey().startsWith(flareId);
        String errMsg = String.format("Uh oh. Did not find group(s) %s in model %s", flareId, objPath);
        Map<String, OBJGroup> flareGroups = model.groups.entrySet().stream().filter(isLightFlare).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if(flareGroups.isEmpty())
            throw new RuntimeException(errMsg);

        //

        Collection<OBJGroup> flareGroupsOBJGroups = flareGroups.values();
        double maxZ = flareGroupsOBJGroups.stream().mapToDouble(g -> g.max.z).max().getAsDouble();
        double minZ = flareGroupsOBJGroups.stream().mapToDouble(g -> g.min.z).min().getAsDouble();
        double maxX = flareGroupsOBJGroups.stream().mapToDouble(g -> g.max.x).max().getAsDouble();
        double minX = flareGroupsOBJGroups.stream().mapToDouble(g -> g.min.x).min().getAsDouble();

        double lampScale = Math.max((maxZ - minZ) * modelScaling[2], (maxX - minX) * modelScaling[0]);

        /*

        float flareOffset = flare.getOffset();
        Set<String> groups = flare.getObjGroups() != null && flare.getObjGroups().length > 0
                ? Arrays.stream(flare.getObjGroups()).collect(Collectors.toSet())
                : model.groups();
        Vec3d centerOfModel = model.centerOfGroups(groups);
        Vec3d centerOfLightFlare = model.centerOfGroups(flareGroups.keySet());
        Vec3d modelOffset = centerOfLightFlare.subtract(centerOfModel);
        modelOffset = new Vec3d(modelOffset.x, modelOffset.y, -modelOffset.z - flareOffset);
        float yCorrection = 0.5f;
        Vec3d flareCenterOffset = new Vec3d(modelTranslation[0], modelTranslation[1] + yCorrection, modelTranslation[2]);
        Vec3d combinedOffset = flareCenterOffset.add(modelOffset);

        */

        // Scaling for the flare from the block in the contentpack
        Vec3d scaling = new Vec3d(modelScaling[0], modelScaling[1], modelScaling[2]);

        Set<String> groups = flare.getObjGroups().length > 0
                ? Arrays.stream(flare.getObjGroups()).collect(Collectors.toSet())
                : model.groups();
        Vec3d centerOfModel = model.centerOfGroups(groups);

        double xCorrection = centerOfModel.x;
        double yCorrection = centerOfModel.y;
        double zCorrection= -(centerOfModel.z + flare.getOffset());
        Vec3d offset = new Vec3d(modelTranslation[0] + xCorrection, modelTranslation[1] + yCorrection, modelTranslation[2] + zCorrection);

        Vec3d centerOfLightFlare = model.centerOfGroups(flareGroups.keySet());
        Vec3d modelOffset = centerOfLightFlare.subtract(centerOfModel);
        modelOffset = new Vec3d(modelOffset.x, modelOffset.y, -modelOffset.z);
        offset = offset.add(modelOffset);

        // Rotation for the flare from the contentpack
        Vec3d rotation = new Vec3d(0, flare.getRotation(), flare.getPitch());

        flare.savePrecalculatedData(flareGroups, scaling, lampScale, offset, rotation);
    }

}
