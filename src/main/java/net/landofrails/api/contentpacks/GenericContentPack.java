package net.landofrails.api.contentpacks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

import java.io.IOException;
import java.io.InputStream;

public final class GenericContentPack {

    private String addonversion;
    private String name;
    private String packversion;
    private String author;

    public GenericContentPack() {

    }

    public GenericContentPack(String addonversion, String name, String packversion, String author) {
        this.addonversion = addonversion;
        this.name = name;
        this.packversion = packversion;
        this.author = author;
    }

    public String getAddonversion() {
        if (addonversion == null)
            return "1";
        return addonversion;
    }

    public void setAddonversion(String addonversion) {
        this.addonversion = addonversion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackversion() {
        return packversion;
    }

    public void setPackversion(String packversion) {
        this.packversion = packversion;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isValid() {
        return name != null && packversion != null && author != null;
    }

    public static GenericContentPack fromJson(InputStream jsonInputStream) {
        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read;

        try {
            while ((read = jsonInputStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            throw new ContentPackException("Cant read landofsignals.json: " + e.getMessage());
        }

        String json = s.toString();
        Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, GenericContentPack.class);
    }

    public String toShortString(){
        return String.format("[Addonversion: %s; Name: %s; Packversion: %s; Author: %s]", addonversion, name, packversion, author);
    }

}
