package net.landofrails.api.contentpacks.v2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContentPack {

    private String addonversion;
    private String name;
    private String packversion;
    private String author;

    // "content": "path": "type", "path": "type"
    private Map<String, EntryType> content = new LinkedHashMap<>();

    public ContentPack(String addonversion, String name, String packversion, String author) {
        this.addonversion = addonversion;
        this.name = name;
        this.packversion = packversion;
        this.author = author;
    }

    public String getAddonversion() {
        if (addonversion == null)
            addonversion = "1";
        return addonversion;
    }

    public String getName() {
        return name;
    }

    public String getPackversion() {
        return packversion;
    }

    public String getAuthor() {
        return author;
    }

    public Map<String, EntryType> getContent() {
        return content;
    }

    public String getId() {
        return name + "@" + author;
    }

    public static ContentPack fromJson(InputStream stellwandJsonStream) {

        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read;

        try {
            while ((read = stellwandJsonStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            throw new ContentPackException("Cant read landofsignals.json: " + e.getMessage());
        }

        String json = s.toString();
        Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, ContentPack.class);

    }

}
