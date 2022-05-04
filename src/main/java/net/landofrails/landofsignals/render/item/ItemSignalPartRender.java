package net.landofrails.landofsignals.render.item;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItem;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalModel;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalState;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class ItemSignalPartRender {
    public static final boolean IGNOREFNFEXCEPTION = true;
    protected static final Map<String, OBJRender> cache = new HashMap<>();
    protected static final Map<String, List<String>> cacheDefaultGroups = new HashMap<>();

    public static ItemRender.IItemModel getModelFor() {
        return (world, stack) -> new StandardModel().addCustom(() -> {

            TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            boolean isOld = LOSBlocks.BLOCK_SIGNAL_PART.getSignalParts_depr().containsKey(itemId);
            boolean isNew = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().containsKey(itemId);
            if (itemId == null || (!isOld && !isNew)) {
                itemId = Static.MISSING;
            }

            if (isNew) {

                Collection<ContentPackSignalGroup> signalGroups = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(itemId).getSignals().values();

                for (ContentPackSignalGroup signalGroup : signalGroups) {

                    ContentPackSignalState signalState = signalGroup.getStates().values().iterator().next();

                    for (Map.Entry<String, ContentPackSignalModel[]> signalModels : signalState.getModels().entrySet()) {

                        String path = signalModels.getKey();

                        String objId = itemId + "/" + path;
                        if (!cache.containsKey(objId)) {
                            try {
                                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, path), 0);
                                cache.put(objId, new OBJRender(model));
                                cacheDefaultGroups.put(objId, model.groups().stream().filter(g -> g.startsWith("general")).collect(Collectors.toList()));

                            } catch (Exception e) {
                                throw new RuntimeException("Error loading item model/renderer...", e);
                            }
                        }
                        OBJRender renderer = cache.get(objId);

                        for (ContentPackSignalModel signalModel : signalModels.getValue()) {
                            ContentPackItem item = signalModel.getItem();
                            Vec3d translate = item.getAsVec3d(item::getTranslation);
                            Vec3d scale = item.getAsVec3d(item::getScaling);
                            Vec3d rotation = item.getAsVec3d(item::getRotation);
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

                                // FIXME Dont render base every single time
                                List<String> groups = new ArrayList<>(Arrays.asList(signalModel.getObj_groups()));
                                groups.addAll(cacheDefaultGroups.get(objId));
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

                return;
            }

            // Old rendering
            // FIXME Remove after converter for ContentPack v1 to v2 is implemented

            Collection<String> collection = LOSBlocks.BLOCK_SIGNAL_PART.getStates_depr(itemId);
            if (!cache.containsKey(itemId)) {
                try {
                    OBJModel model;
                    if (collection != null) {
                        model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART.getPath_depr(itemId)), 0, collection);
                    } else {
                        model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART.getPath_depr(itemId)), 0);
                    }
                    OBJRender renderer = new OBJRender(model);
                    cache.put(itemId, renderer);
                } catch (FileNotFoundException e) {
                    if (IGNOREFNFEXCEPTION) {
                        ModCore.Mod.error("Model not found: " + e.getMessage(), e.getMessage());
                        return;
                    } else {
                        throw new RuntimeException("Error loading item model...", e);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error loading item model...", e);
                }
            }
            OBJRender renderer = cache.get(itemId);
            String textureName = null;
            if (collection != null && tag.hasKey("textureName")) {
                textureName = tag.getString("textureName");
            }
            Vec3d translate = LOSBlocks.BLOCK_SIGNAL_PART.getItemTranslation_depr(itemId);
            Vec3d scale = LOSBlocks.BLOCK_SIGNAL_PART.getItemScaling_depr(itemId);
            try (OpenGL.With ignored = OpenGL.matrix(); OpenGL.With ignored1 = renderer.bindTexture(textureName)) {
                GL11.glTranslated(translate.x, translate.y, translate.z);
                GL11.glScaled(scale.x, scale.y, scale.z);
                renderer.draw();
            }
        });
    }
}
