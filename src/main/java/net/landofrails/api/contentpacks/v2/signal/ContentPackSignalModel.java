package net.landofrails.api.contentpacks.v2.signal;

import net.landofrails.api.contentpacks.v2.parent.ContentPackBlock;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItem;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItemRenderType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ContentPackSignalModel {

    private String[] textures;
    @SuppressWarnings("java:S116")
    private String[] obj_groups;
    private Map<ContentPackItemRenderType, ContentPackItem> item;
    private ContentPackBlock block;

    private String texturesRef;
    private String obj_groupsRef;
    private Map<ContentPackItemRenderType, String> itemRefs;
    private String blockRef;

    public ContentPackSignalModel() {

    }

    @SuppressWarnings("java:S117")
    public ContentPackSignalModel(String[] textures, String[] obj_groups, Map<ContentPackItemRenderType, ContentPackItem> item, ContentPackBlock block) {
        this.textures = textures;
        this.obj_groups = obj_groups;
        this.item = item;
        this.block = block;
    }

    public void validate(Consumer<String> modelConsumer, ContentPackSignalReferences references) {

        if (textures == null) {
            textures = references.getTexturesOrElse(texturesRef, new String[0]);
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
            if (!itemRefs.isEmpty())
                System.out.println("ItemRefs > 0");
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

    public String[] getTextures() {
        return textures;
    }

    public void setTextures(String[] textures) {
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