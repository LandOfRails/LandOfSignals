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
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackComplexSignal;
import net.landofrails.api.contentpacks.v2.deco.ContentPackDeco;
import net.landofrails.api.contentpacks.v2.flares.Flare;
import net.landofrails.api.contentpacks.v2.lever.ContentPackLever;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.sign.ContentPackSign;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.render.block.*;
import net.landofrails.landofsignals.tile.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static net.landofrails.landofsignals.utils.LandOfSignalsUtils.objIdWithGroup;
import static net.landofrails.landofsignals.utils.LandOfSignalsUtils.objIdWithoutGroup;
import static net.landofrails.landofsignals.utils.Static.ACTIVE;
import static net.landofrails.landofsignals.utils.Static.INACTIVE;

@SuppressWarnings("java:S1075")
public class FlareUtils {

    public static final String ERR_LOAD_BLOCK_MODEL_RENDER = "Error loading block model/renderer...";

    private static final Identifier LIGHT_TEX = new Identifier(LandOfSignals.MODID, "textures/light/light.png");

    private static final Map<String, Map<String, List<Flare>>> signalpartFlareCache = new HashMap<>();
    private static final Map<String, List<Flare>> signFlareCache = new HashMap<>();
    private static final Map<String, List<Flare>> decoFlareCache = new HashMap<>();
    private static final Map<String, Map<String, List<Flare>>> leverFlareCache = new HashMap<>();
    private static final Map<String, Map<String, Map<String, List<Flare>>>> complexSignalFlareCache = new HashMap<>();

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

    public static void renderFlares(String id, ContentPackDeco deco, TileDeco tile, RenderState renderState) {

        if(!decoFlareCache.containsKey(id)){
            cacheFlares(id, deco);
        }

        List<Flare> flares = decoFlareCache.get(id);
        renderFlares(flares.toArray(new Flare[0]), tile.getPos(), tile.getBlockRotate(), tile.getScaling(), tile.getOffset(), renderState);

    }

    public static void renderFlares(String id, ContentPackLever lever, TileCustomLever tile, RenderState renderState) {

        if (!leverFlareCache.containsKey(id)) {
            cacheFlares(id, lever);
        }

        final String signalState = tile.isActive() ? ACTIVE : INACTIVE;
        List<Flare> flares = leverFlareCache.get(id).get(signalState);

        if(flares == null) {
            // No flares - no rendering
            return;
        }

        renderFlares(flares.toArray(new Flare[0]), tile.getPos(), tile.getBlockRotate(), tile.getScaling(), tile.getOffset(), renderState);

    }

    public static void renderFlares(String id, ContentPackComplexSignal signal, TileComplexSignal tile, RenderState renderState) {

        if (!complexSignalFlareCache.containsKey(id)) {
            cacheFlares(id, signal);
        }

        final Map<String, String> signalStates = tile.getSignalGroupStates();
        // Signal-Flares
        for(Map.Entry<String, String> groupState : signalStates.entrySet()){
            List<Flare> flares = complexSignalFlareCache.get(id).get(groupState.getKey()).get(groupState.getValue());

            if(flares == null) {
                // No flares - no rendering for this group
                continue;
            }

            renderFlares(flares.toArray(new Flare[0]), tile.getPos(), tile.getBlockRotate(), tile.getScaling(), tile.getOffset(), renderState);
        }
        // Base-Flares
        Map<String, List<Flare>> baseGroup = complexSignalFlareCache.get(id).get(null);
        if(baseGroup != null){
            List<Flare> flares = baseGroup.get(null);
            renderFlares(flares.toArray(new Flare[0]), tile.getPos(), tile.getBlockRotate(), tile.getScaling(), tile.getOffset(), renderState);
        }

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
            throw new BlockRenderException(ERR_LOAD_BLOCK_MODEL_RENDER, e);
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
            throw new BlockRenderException(ERR_LOAD_BLOCK_MODEL_RENDER, e);
        }
    }

    public static void cacheFlares(String signId, ContentPackDeco deco){
        if(deco.getFlares().length == 0) return;
        try {
            List<Flare> cachedFlares = new ArrayList<>();
            for(Flare flare : deco.getFlares()){
                cacheFlare(flare, cachedFlares, deco);
            }
            decoFlareCache.put(signId, cachedFlares);
        } catch (Exception e) {
            throw new BlockRenderException(ERR_LOAD_BLOCK_MODEL_RENDER, e);
        }
    }

    public static void cacheFlares(String signalId, ContentPackLever lever){
        if(lever.getFlares().length == 0) return;
        try {
            Map<String, List<Flare>> stateFlares = new HashMap<>();
            Flare[] flares = lever.getFlares();
            for(Flare flare : flares){
                cacheFlare(flare, stateFlares, lever);
            }
            leverFlareCache.put(signalId, stateFlares);
        } catch (Exception e) {
            throw new BlockRenderException(ERR_LOAD_BLOCK_MODEL_RENDER, e);
        }
    }

    public static void cacheFlares(String signalId, ContentPackComplexSignal signal){
        if(signal.getFlares().length == 0) return;
        try {
            Map<String, Map<String, List<Flare>>> groupStateFlares = new HashMap<>();
            Flare[] flares = signal.getFlares();
            for(String groupId : signal.getSignals().keySet()){
                Map<String, List<Flare>> stateFlares = new HashMap<>();
                for(Flare flare : flares){
                    if(groupId.equals(flare.getGroupId())){
                        cacheFlare(flare, stateFlares, signal);
                    }
                }

                groupStateFlares.put(groupId, stateFlares);
            }

            // base = (groupId == null) && isAlwaysOn
            Predicate<Flare> isBaseFlare = flare -> flare.isAlwaysOn() && flare.getGroupId() == null;
            if(Arrays.stream(flares).anyMatch(isBaseFlare)){
                Map<String, List<Flare>> stateFlares = new HashMap<>();
                Arrays.stream(flares)
                        .filter(isBaseFlare)
                        .forEach(flare -> cacheFlare(flare, stateFlares, signal));
                groupStateFlares.put(null, stateFlares);
            }

            complexSignalFlareCache.put(signalId, groupStateFlares);
        } catch (Exception e) {
            throw new BlockRenderException(ERR_LOAD_BLOCK_MODEL_RENDER, e);
        }
    }

    // Cache one flare

    private static void cacheFlare(Flare flare, List<Flare> cachedFlares, ContentPackSign sign) {

        final String objPath = sign.getUniqueId() + "/" + flare.getObjPath();
        final String flareId = flare.getId();
        final OBJModel model = TileSignPartRender.cache().get(objPath);

        float[] modelTranslation = sign.getBase().get(flare.getObjPath())[flare.getObjPathIndex()].getBlock().getTranslation();
        float[] modelScaling = sign.getBase().get(flare.getObjPath())[flare.getObjPathIndex()].getBlock().getScaling();

        String errMsg = String.format("Uh oh. Did not find obj_path %s in model %s for flare %s", flare.getObjPath(), sign.getUniqueId(), flareId);
        if(model == null)
            throw new BlockRenderException(errMsg);

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

    private static void cacheFlare(Flare flare, List<Flare> cachedFlares, ContentPackDeco deco) {

        final String objPath = deco.getUniqueId() + "/" + flare.getObjPath();
        final String flareId = flare.getId();
        final OBJModel model = TileDecoRender.cache().get(objPath);

        float[] modelTranslation = deco.getBase().get(flare.getObjPath())[flare.getObjPathIndex()].getBlock().getTranslation();
        float[] modelScaling = deco.getBase().get(flare.getObjPath())[flare.getObjPathIndex()].getBlock().getScaling();

        String errMsg = String.format("Uh oh. Did not find obj_path %s in model %s for flare %s", flare.getObjPath(), deco.getUniqueId(), flareId);
        if(model == null)
            throw new BlockRenderException(errMsg);

        //

        cacheFlare(flare, flareId, model, objPath, modelTranslation, modelScaling);
        cachedFlares.add(flare);
    }

    private static void cacheFlare(Flare flare, Map<String, List<Flare>> stateFlares, ContentPackLever lever){

        String[] flareStates = flare.getStates();
        if(flare.isAlwaysOn())
            flareStates = new String[]{ACTIVE, INACTIVE}; // All states
        for(String state : flareStates){
            final String flareId = flare.getId();
            final String objPath = lever.getUniqueId() + "/" + flare.getObjPath() + ":" + state;
            final OBJModel model = TileCustomLeverRender.cache().get(objPath);

            Map<String, ContentPackModel[]> models =
                    state.equalsIgnoreCase(ACTIVE) ? lever.getActive() : lever.getInactive();
            float[] modelTranslation = models.get(flare.getObjPath())[flare.getObjPathIndex()].getBlock().getTranslation();
            float[] modelScaling = models.get(flare.getObjPath())[flare.getObjPathIndex()].getBlock().getScaling();

            //

            cacheFlare(flare, flareId, model, objPath, modelTranslation, modelScaling);

            stateFlares.putIfAbsent(state, new ArrayList<>());
            stateFlares.get(state).add(flare);
        }

    }

    private static void cacheFlare(Flare flare, Map<String, List<Flare>> stateFlares, ContentPackComplexSignal signal){

        if(flare.isAlwaysOn() && flare.getGroupId() == null){
            // Base flare

            final String flareId = flare.getId();
            final String objPath = objIdWithoutGroup(signal.getUniqueId(), "base", flare.getObjPath());
            final OBJModel model = TileComplexSignalRender.cache().get(objPath);

            ContentPackModel[] models = signal.getBase().get(flare.getObjPath());

            float[] modelTranslation = models[flare.getObjPathIndex()].getBlock().getTranslation();
            float[] modelScaling = models[flare.getObjPathIndex()].getBlock().getScaling();

            //

            cacheFlare(flare, flareId, model, objPath, modelTranslation, modelScaling);

            stateFlares.putIfAbsent(null, new ArrayList<>());
            stateFlares.get(null).add(flare);

            return;
        }

        // Group+State flare
        String[] flareStates = flare.getStates();
        for(String state : flareStates){
            final String flareId = flare.getId();
            final String objPath = flare.getGroupId() != null ?
                    objIdWithGroup(signal.getUniqueId(), "signals", flare.getGroupId(), flare.getObjPath()) :
                    objIdWithoutGroup(signal.getUniqueId(), "signals", flare.getObjPath());
            final OBJModel model = TileComplexSignalRender.cache().get(objPath);

            ContentPackModel[] models = signal.getSignals().get(flare.getGroupId()).getStates().get(state).getModels().get(flare.getObjPath());

            float[] modelTranslation = models[flare.getObjPathIndex()].getBlock().getTranslation();
            float[] modelScaling = models[flare.getObjPathIndex()].getBlock().getScaling();

            //

            cacheFlare(flare, flareId, model, objPath, modelTranslation, modelScaling);

            stateFlares.putIfAbsent(state, new ArrayList<>());
            stateFlares.get(state).add(flare);
        }

    }

    //// COMMON / SHARED LOGIC

    public static void renderFlares(Flare[] flares, Vec3i pos, int blockRotate, Vec3d scaling, Vec3d offset, RenderState renderState) {
        for(Flare flare : flares){
            RenderState flareState = renderState.clone();
            Flare.PrecalculatedData precalculatedData = flare.getPrecalculatedData();

            float red = flare.getRenderColor()[0];
            float green = flare.getRenderColor()[1];
            float blue = flare.getRenderColor()[2];

            Vec3d flareRotation = precalculatedData.rotation;

            double scale = precalculatedData.lampScale;

            // Translation and Intensity calculations

            Vec3d playerOffset = VecUtil.rotateWrongYaw(
                            new Vec3d(pos).subtract(MinecraftClient.getPlayer().getPosition()),
                            blockRotate + 180).
                    subtract(precalculatedData.preOffset);

            int viewAngle = 45;
            float intensity = 1 - Math.abs(Math.max(-viewAngle, Math.min(viewAngle, VecUtil.toWrongYaw(playerOffset) - 90))) / viewAngle;
            intensity *= (float) Math.abs(playerOffset.x/50);
            intensity *= 0.5f;
            intensity = Math.min(intensity, 0.25f);

            //

            flareState.texture(Texture.wrap(precalculatedData.flareTextureIdentifier))
                    .lightmap(1, 1)
                    .depth_test(true)
                    .depth_mask(false)
                    .alpha_test(false).blend(new BlendMode(BlendMode.GL_SRC_ALPHA, BlendMode.GL_ONE_MINUS_SRC_ALPHA));

            flareState.scale(scaling);

            flareState.translate(offset);

            flareState.translate(precalculatedData.preOffset);

            flareState.rotate(flareRotation.x, 1,0,0);
            flareState.rotate(flareRotation.y + blockRotate,0,1,0);
            flareState.rotate(flareRotation.z, 0,0, 1);

            flareState.translate(precalculatedData.postOffset);

            flareState.rotate(flare.getPostRotation(), 0,1, 0);

            flareState.translate(0,0, -flare.getOffset());

            // Moving flare

            if (flare.getScaleWithDistance() && intensity > 0.01) {
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
            throw new BlockRenderException(errMsg);

        //

        Collection<OBJGroup> flareGroupsOBJGroups = flareGroups.values();
        double maxZ = flareGroupsOBJGroups.stream().mapToDouble(g -> g.max.z).max().getAsDouble();
        double minZ = flareGroupsOBJGroups.stream().mapToDouble(g -> g.min.z).min().getAsDouble();
        double maxX = flareGroupsOBJGroups.stream().mapToDouble(g -> g.max.x).max().getAsDouble();
        double minX = flareGroupsOBJGroups.stream().mapToDouble(g -> g.min.x).min().getAsDouble();


        double lampScale = Math.max((maxZ - minZ) * modelScaling[2], (maxX - minX) * modelScaling[0]) * 0.65d;
        lampScale *= flare.getScalingMultiplier();

        // Scaling for the flare from the block in the contentpack
        Vec3d scaling = new Vec3d(modelScaling[0], modelScaling[1], modelScaling[2]);

        Set<String> groups = model.groups();
        if(flare.getObjGroups().length > 0){
            groups.removeIf(group -> Arrays.stream(flare.getObjGroups()).noneMatch(group::startsWith));
        }
        Vec3d centerOfModel = model.centerOfGroups(groups);

        double xCorrection = -centerOfModel.x;
        double yCorrection = centerOfModel.y;
        double zCorrection = -centerOfModel.z;
        Vec3d preOffset = new Vec3d(modelTranslation[0], modelTranslation[1], modelTranslation[2]);
        Vec3d postOffset = new Vec3d(xCorrection, yCorrection, zCorrection);

        Vec3d centerOfLightFlare = model.centerOfGroups(flareGroups.keySet());
        Vec3d modelOffset = centerOfLightFlare.subtract(centerOfModel);
        modelOffset = new Vec3d(-modelOffset.x, modelOffset.y, -(modelOffset.z));
        postOffset = postOffset.add(modelOffset);

        // Rotation for the flare from the contentpack
        Vec3d rotation = new Vec3d(0, flare.getRotation(), flare.getPitch());

        // Identifier for the flare texture
        Identifier flareTextureIdentifier = flare.getTexture() != null ? new Identifier(LandOfSignals.MODID, flare.getTexture()) : LIGHT_TEX;

        flare.savePrecalculatedData(flareGroups, scaling, lampScale, preOffset, postOffset, rotation, flareTextureIdentifier);
    }

}
