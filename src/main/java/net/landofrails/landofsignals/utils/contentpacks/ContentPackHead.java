package net.landofrails.landofsignals.utils.contentpacks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ContentPackHead {

    private String name;
    private String packversion;
    private String author;

    private List<String> signals;

    public ContentPackHead(String name, String packversion, String author, List<String> signals) {
        this.name = name;
        this.packversion = packversion;
        this.author = author;
        this.signals = signals;
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

    public List<String> getSignals() {
        return signals;
    }

    public void setSignals(List<String> signals) {
        this.signals = signals;
    }

    public String getId() {
        return name + "@" + author;
    }

    public static ContentPackHead fromJson(InputStream landofsignalsJsonStream) {
        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read = 0;

        try {
            while ((read = landofsignalsJsonStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            throw new ContentPackException("Cant read landofsignals.json: " + e.getMessage());
        }

        String json = s.toString();
        Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, ContentPackHead.class);
    }
}
