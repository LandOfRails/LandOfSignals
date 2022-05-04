package net.landofrails.api.contentpacks.v2.signal;

import net.landofrails.api.contentpacks.v2.parent.ContentPackBlock;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItem;

import java.util.function.Consumer;

public class ContentPackSignalModel {

    private String[] textures;
    private String[] obj_groups;
    private ContentPackItem item;
    private ContentPackBlock block;

    public ContentPackSignalModel(String[] textures, String[] obj_groups, ContentPackItem item, ContentPackBlock block) {
        this.textures = textures;
        this.obj_groups = obj_groups;
        this.item = item;
        this.block = block;
    }

    public void validate(Consumer<String> modelConsumer) {

        if (textures == null) {
            textures = new String[0];
        }
        if (obj_groups == null) {
            obj_groups = new String[0];
        }
        if (item == null) {
            item = new ContentPackItem(null, null, null);
        }
        if (block == null) {
            block = new ContentPackBlock(null, null, null);
        }
        item.validate(modelConsumer);
        block.validate(modelConsumer);

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

    public ContentPackItem getItem() {
        return item;
    }

    public void setItem(ContentPackItem item) {
        this.item = item;
    }

    public ContentPackBlock getBlock() {
        return block;
    }

    public void setBlock(ContentPackBlock block) {
        this.block = block;
    }
}