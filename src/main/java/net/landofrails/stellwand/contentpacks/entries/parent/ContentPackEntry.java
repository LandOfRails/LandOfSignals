package net.landofrails.stellwand.contentpacks.entries.parent;

import cam72cam.mod.ModCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.stellwand.contentpacks.types.EntryType;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

import java.io.IOException;
import java.io.InputStream;

public abstract class ContentPackEntry {

    protected String name;
    protected String model;


    protected ContentPackEntry() {

    }

    protected ContentPackEntry(String name, String model) {
        this.name = name;
        this.model = model;
    }

    public String getBlockId(String packId) {
        return packId + ":" + name;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public abstract ContentPackEntryBlock getBlock();


    public <T extends ContentPackEntryBlock> T getBlock(Class<T> cls) {

        try {
            return cls.cast(getBlock());
        } catch (ClassCastException e2) {
            ModCore.error("Tried to cast blockclass: %s",
                    getBlock() != null && getBlock().getClass() != null ? getBlock().getClass().getName() : "null");
            ModCore.error("Tried to cast to: %s", cls.getName());

            throw new ContentPackException("Weird cast exception above.");
        }

    }

    public abstract ContentPackEntryItem getItem();

    public <T extends ContentPackEntryItem> T getItem(Class<T> cls) {
        if (cls.isInstance(getItem()))
            return cls.cast(getItem());
        return null;
    }

    public abstract EntryType getType();

    public boolean isType(EntryType type) {
        if (type == null)
            return false;
        return type.equals(getType());
    }

    // Statics
    public static ContentPackEntry fromJson(InputStream inputStream, EntryType type) {
        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read;

        try {
            while ((read = inputStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            throw new ContentPackException("Cant read ContentPackEntry of type " + type.toString() + ": " + e.getMessage());
        }

        String json = s.toString();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, type.getTypeClass());

    }

}
