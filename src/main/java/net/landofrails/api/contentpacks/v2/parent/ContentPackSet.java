package net.landofrails.api.contentpacks.v2.parent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.api.contentpacks.v2.ContentPackException;
import net.landofrails.api.contentpacks.v2.EntryType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class ContentPackSet {

    private String name;
    private String creativeTab;
    // "content": "path": "type", "path": "type"
    private Map<String, EntryType> content;

    public ContentPackSet() {

    }

    public ContentPackSet(String name, String creativeTab, Map<String, EntryType> content) {
        this.name = name;
        this.creativeTab = creativeTab;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public void setCreativeTab(String creativeTab) {
        this.creativeTab = creativeTab;
    }

    public Map<String, EntryType> getContent() {
        return content;
    }

    public void setContent(Map<String, EntryType> content) {
        this.content = content;
    }

    public void validate(Consumer<String> validationString) {
        if (creativeTab == null) {
            creativeTab = "default";
        }

        StringJoiner joiner = new StringJoiner(",", "[", "]");
        if (name == null) {
            joiner.add("name needs to be set!");
        }
        if (content == null || content.isEmpty()) {
            joiner.add("content needs to contain atleast 1 entry");
        }

        if (joiner.length() > 2) {
            validationString.accept("ContentPackSet: " + joiner.toString());
        }

    }

    public static ContentPackSet fromJson(InputStream inputStream) {
        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read;

        try {
            while ((read = inputStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            throw new ContentPackException("Cant read ContentPackSet: " + e.getMessage());
        }

        String json = s.toString();
        Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, ContentPackSet.class);
    }

}
