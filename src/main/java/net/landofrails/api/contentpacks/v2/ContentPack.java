package net.landofrails.api.contentpacks.v2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ContentPack {

    private String name;
    private String author;
    private String packversion;
    private String addonversion;

    // "content": "path": "type", "path": "type"
    private Map<String, EntryType> content;
    private List<String> contentSets;

    public ContentPack(String name, String author, String packversion, String addonversion, Map<String, EntryType> content, List<String> contentSets) {
        this.name = name;
        this.author = author;
        this.packversion = packversion;
        this.addonversion = addonversion;
        this.content = content;
        this.contentSets = contentSets;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPackversion() {
        return packversion;
    }

    public String getAddonversion() {
        return addonversion;
    }

    @Nullable
    public Map<String, EntryType> getContent() {
        return content;
    }

    @Nullable
    public List<String> getContentSets() {
        return contentSets;
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
