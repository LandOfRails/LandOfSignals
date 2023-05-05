package net.landofrails.api.contentpacks.v2.parent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings({"java:S100", "java:S116", "java:S117"})
public class ContentPackModel {

    private String textures;
    private String[] obj_groups;
    private Map<ContentPackItemRenderType, ContentPackItem> item;
    private ContentPackBlock block;

    private String texturesRef;
    private String obj_groupsRef;
    private Map<ContentPackItemRenderType, String> itemRefs;
    private String blockRef;

    public ContentPackModel() {

    }

    @SuppressWarnings("java:S117")
    public ContentPackModel(String textures, String[] obj_groups, Map<ContentPackItemRenderType, ContentPackItem> item, ContentPackBlock block) {
        this.textures = textures;
        this.obj_groups = obj_groups;
        this.item = item;
        this.block = block;
    }

    public ContentPackModel(float[] blockTranslation, float[] itemTranslation, float[] scaling) {
        this.block = new ContentPackBlock(blockTranslation, null, scaling);
        this.item = new HashMap<>();
        item.put(ContentPackItemRenderType.DEFAULT, new ContentPackItem(itemTranslation, null, scaling));
    }

    public ContentPackModel(float[] blockTranslation, float[] itemTranslation, float[] scaling, String[] obj_groups) {
        this.block = new ContentPackBlock(blockTranslation, null, scaling);
        this.item = new HashMap<>();
        item.put(ContentPackItemRenderType.DEFAULT, new ContentPackItem(itemTranslation, null, scaling));
        this.obj_groups = obj_groups;
    }

    public ContentPackModel(float[] blockTranslation, float[] itemTranslation, float[] blockScaling, float[] itemScaling) {
        this.block = new ContentPackBlock(blockTranslation, null, blockScaling);
        this.item = new HashMap<>();
        item.put(ContentPackItemRenderType.DEFAULT, new ContentPackItem(itemTranslation, null, itemScaling));
    }

    public ContentPackModel(float[] blockTranslation, float[] itemTranslation, float[] blockScaling, float[] itemScaling, String[] obj_groups) {
        this.block = new ContentPackBlock(blockTranslation, null, blockScaling);
        this.item = new HashMap<>();
        item.put(ContentPackItemRenderType.DEFAULT, new ContentPackItem(itemTranslation, null, itemScaling));
        this.obj_groups = obj_groups;
    }

    public ContentPackModel(float[] blockTranslation, float[] itemTranslation, float[] blockScaling, float[] itemScaling, float[] rotation) {
        this.block = new ContentPackBlock(blockTranslation, rotation, blockScaling);
        this.item = new HashMap<>();
        item.put(ContentPackItemRenderType.DEFAULT, new ContentPackItem(itemTranslation, rotation, itemScaling));
    }

    public ContentPackModel(float[] blockTranslation, float[] itemTranslation, float[] blockScaling, float[] itemScaling, float[] rotation, String[] obj_groups) {
        this.block = new ContentPackBlock(blockTranslation, rotation, blockScaling);
        this.item = new HashMap<>();
        item.put(ContentPackItemRenderType.DEFAULT, new ContentPackItem(itemTranslation, rotation, itemScaling));
        this.obj_groups = obj_groups;
    }

    public void validate(Consumer<String> modelConsumer, ContentPackReferences references) {

        if (textures == null) {
            textures = references.getTexturesOrElse(texturesRef, null);
        }
        if (obj_groups == null) {
            obj_groups = references.getObj_groupsOrElse(obj_groupsRef, new String[0]);
        }
        if (item == null) {
            item = new HashMap<>();
        }
        if (block == null) {
            block = references.getContentPackBlockOrElse(blockRef, new ContentPackBlock(null, null, null, null, null, null));
        }

        if (itemRefs == null) {
            itemRefs = Collections.emptyMap();
        }

        if (item.size() != ContentPackItemRenderType.values().length) {
            for (ContentPackItemRenderType cpirt : ContentPackItemRenderType.values()) {
                item.putIfAbsent(cpirt, references.getContentPackItemOrElse(itemRefs.get(cpirt), new ContentPackItem(null, null, null, null, null, null)));
            }
        }
        item.forEach((key, value) -> {
            Consumer<String> itemConsumer = text -> modelConsumer.accept("[" + key.name() + ": " + text + "]");
            value.validate(itemConsumer, references);
        });
        block.validate(modelConsumer, references);

    }

    public String getTextures() {
        return textures;
    }

    public void setTextures(String textures) {
        this.textures = textures;
    }

    public String[] getObj_groups() {
        return obj_groups;
    }

    public void setObj_groups(String[] obj_groups) {
        this.obj_groups = obj_groups;
    }

    public Map<ContentPackItemRenderType, ContentPackItem> getItem() {
        return item;
    }

    public void setItem(Map<ContentPackItemRenderType, ContentPackItem> item) {
        this.item = item;
    }

    public ContentPackBlock getBlock() {
        return block;
    }

    public void setBlock(ContentPackBlock block) {
        this.block = block;
    }
}