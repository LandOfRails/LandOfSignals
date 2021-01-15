package net.landofrails.landofsignals.models;

import net.landofrails.stellwand.content.loader.ContentPackEntry;

import java.util.ArrayList;
import java.util.List;

public class LOSContentPack {
    private String name;
    private String author;
    private String packVersion;

    private List<ContentPackEntry> entries = new ArrayList<>();

    public LOSContentPack(String name, String author, String packVersion) {
        this.name = name;
        this.author = author;
        this.packVersion = packVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPackVersion() {
        return packVersion;
    }

    public void setPackVersion(String packVersion) {
        this.packVersion = packVersion;
    }

    public String getId() {
        return name + "@" + author;
    }
}
