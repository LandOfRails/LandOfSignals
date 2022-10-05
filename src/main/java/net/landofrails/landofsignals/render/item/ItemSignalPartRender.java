package net.landofrails.landofsignals.render.item;

import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.world.World;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItem;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItemRenderType;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalState;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.serialization.EmptyStringMapper;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("java:S3252")
public class ItemSignalPartRender implements ItemRender.IItemModel {
    protected static final Map<String, OBJRender> cache = new HashMap<>();
    protected static final Map<String, Boolean> cacheInfoOldContentPack = new HashMap<>();

    @Override
    public StandardModel getModel(World world, ItemStack stack) {
        return new StandardModel().addCustom(() -> {

            final TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().containsKey(itemId)) {
                itemId = Static.MISSING;
            }

            final Map<String, String> itemGroupStates = new HashMap<>(LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(itemId).getItemGroupStates());
            if (tag.hasKey("itemGroupState")) {
                itemGroupStates.putAll(tag.getMap("itemGroupState", EmptyStringMapper::fromNullString, value -> value.getString("string")));
            }

            renderBase(itemId);
            renderSignals(itemId, itemGroupStates);

        });
    }


    @SuppressWarnings("java:S1135")
    @Override
    public void applyTransform(ItemRender.ItemRenderType type) {

        // TODO Implement ItemRenderType with new UMC rendering

        ItemRender.IItemModel.super.applyTransform(type);
    }

    @SuppressWarnings("java:S1134")
    private static void renderBase(String itemId) {

        for (Map.Entry<String, ContentPackModel[]> baseModels : LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(itemId).getBase().entrySet()) {

            final String path = baseModels.getKey();

            final String objId = itemId + "/" + path;
            if (!cache.containsKey(objId)) {
                try {
                    cache.put(objId, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, path), 0)));
                    cacheInfoOldContentPack.putIfAbsent(itemId, LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(itemId));
                } catch (Exception e) {
                    throw new ItemRenderException("Error loading item model/renderer...", e);
                }
            }
            final OBJRender renderer = cache.get(objId);

            for (ContentPackModel baseModel : baseModels.getValue()) {
                final ContentPackItem item = baseModel.getItem().get(ContentPackItemRenderType.DEFAULT);
                final Vec3d translate = item.getAsVec3d(item::getTranslation);
                final Vec3d scale = item.getAsVec3d(item::getScaling);
                final Vec3d rotation = item.getAsVec3d(item::getRotation);
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
                    if (Boolean.FALSE.equals(cacheInfoOldContentPack.get(itemId))) {
                        GL11.glScaled(scale.x, scale.y, scale.z);
                        GL11.glTranslated(translate.x, translate.y, translate.z);
                        GL11.glRotated(rotation.x, 1, 0, 0);
                        GL11.glRotated(rotation.y, 0, 1, 0);
                        GL11.glRotated(rotation.z, 0, 0, 1);
                    } else {
                        GL11.glTranslated(translate.x, translate.y, translate.z);
                        GL11.glRotated(rotation.x, 1, 0, 0);
                        GL11.glRotated(rotation.y, 0, 1, 0);
                        GL11.glRotated(rotation.z, 0, 0, 1);
                        GL11.glScaled(scale.x, scale.y, scale.z);
                    }

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

                } finally {
                    closables.forEach(OpenGL.With::close);
                }


            }
        }
    }

    @SuppressWarnings("java:S1134")
    private static void renderSignals(String itemId, Map<String, String> itemGroupStates) {
        final Map<String, ContentPackSignalGroup> signalGroups = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(itemId).getSignals();
        for (Map.Entry<String, ContentPackSignalGroup> signalGroup : signalGroups.entrySet()) {

            final ContentPackSignalState signalState = signalGroup.getValue().getStates().get(itemGroupStates.get(signalGroup.getKey()));

            for (Map.Entry<String, ContentPackModel[]> signalModels : signalState.getModels().entrySet()) {

                final String path = signalModels.getKey();

                final String objId = itemId + "/" + path;
                if (!cache.containsKey(objId)) {
                    try {
                        final Set<String> objTextures = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(itemId).getObjTextures().get(path);
                        cache.put(objId, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, path), 0, objTextures)));
                        cacheInfoOldContentPack.putIfAbsent(itemId, LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(itemId));
                    } catch (final Exception e) {
                        throw new ItemRenderException("Error loading item model/renderer...", e);
                    }
                }
                final OBJRender renderer = cache.get(objId);

                for (ContentPackModel signalModel : signalModels.getValue()) {
                    final ContentPackItem item = signalModel.getItem().get(ContentPackItemRenderType.DEFAULT);
                    final Vec3d translate = item.getAsVec3d(item::getTranslation);
                    final Vec3d scale = item.getAsVec3d(item::getScaling);
                    final Vec3d rotation = item.getAsVec3d(item::getRotation);
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
                        if (Boolean.FALSE.equals(cacheInfoOldContentPack.get(itemId))) {
                            GL11.glScaled(scale.x, scale.y, scale.z);
                            GL11.glTranslated(translate.x, translate.y, translate.z);
                            GL11.glRotated(rotation.x, 1, 0, 0);
                            GL11.glRotated(rotation.y, 0, 1, 0);
                            GL11.glRotated(rotation.z, 0, 0, 1);
                        } else {
                            GL11.glTranslated(translate.x, translate.y, translate.z);
                            GL11.glRotated(rotation.x, 1, 0, 0);
                            GL11.glRotated(rotation.y, 0, 1, 0);
                            GL11.glRotated(rotation.z, 0, 0, 1);
                            GL11.glScaled(scale.x, scale.y, scale.z);
                        }

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

                    } finally {
                        closables.forEach(OpenGL.With::close);
                    }
                }

            }


        }
    }

}
