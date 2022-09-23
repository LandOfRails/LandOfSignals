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
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class ItemDecoRender implements ItemRender.IItemModel {

    protected static final Map<String, OBJRender> cache = new HashMap<>();

    @Override
    public StandardModel getModel(World world, ItemStack stack) {
        return new StandardModel().addCustom(() -> {

            TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString("itemId");
            if (itemId == null || !LOSBlocks.BLOCK_DECO.getContentpackDeco().containsKey(itemId)) {
                itemId = Static.MISSING;
            }

            renderBase(itemId);

        });
    }

    @Override
    public void applyTransform(ItemRender.ItemRenderType type) {

        // TODO Implement ItemRenderType with new UMC rendering
        ItemRender.IItemModel.super.applyTransform(type);
    }

    private static void renderBase(String itemId) {

        for (Map.Entry<String, ContentPackModel[]> baseModels : LOSBlocks.BLOCK_DECO.getContentpackDeco().get(itemId).getBase().entrySet()) {

            String path = baseModels.getKey();

            String objId = itemId + "/" + path;
            if (!cache.containsKey(objId)) {
                try {
                    cache.put(objId, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, path), 0)));
                } catch (Exception e) {
                    throw new ItemRenderException("Error loading item model/renderer...", e);
                }
            }
            OBJRender renderer = cache.get(objId);

            for (ContentPackModel baseModel : baseModels.getValue()) {
                ContentPackItem item = baseModel.getItem().get(ContentPackItemRenderType.DEFAULT);
                Vec3d translate = item.getAsVec3d(item::getTranslation);
                Vec3d scale = item.getAsVec3d(item::getScaling);
                Vec3d rotation = item.getAsVec3d(item::getRotation);
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
                    GL11.glRotated(rotation.y, 0, 1, 0);
                    GL11.glRotated(rotation.z, 0, 0, 1);

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
}
