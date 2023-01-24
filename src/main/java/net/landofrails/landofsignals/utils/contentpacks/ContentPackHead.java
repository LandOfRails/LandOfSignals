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
    private String addonversion;
    private String author;

    private List<String> signals;

    public ContentPackHead(final String name, final String packversion, final String author, final List<String> signals) {
        this.name = name;
        this.packversion = packversion;
        this.author = author;
        this.signals = signals;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPackversion() {
        return packversion;
    }

    public void setPackversion(final String packversion) {
        this.packversion = packversion;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public List<String> getSignals() {
        return signals;
    }

    public void setSignals(final List<String> signals) {
        this.signals = signals;
    }

    public String getId() {
        return name + "@" + author;
    }

    public boolean isValidAddonVersion() {
        return addonversion == null || "1".equalsIgnoreCase(addonversion);
    }

    public static ContentPackHead fromJson(final InputStream landofsignalsJsonStream) {
        final StringBuilder s = new StringBuilder();
        final byte[] buffer = new byte[1024];
        int read = 0;

        try {
            while ((read = landofsignalsJsonStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (final IOException e) {
            throw new ContentPackException("Cant read landofsignals.json: " + e.getMessage());
        }

        final String json = s.toString();
        final Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, ContentPackHead.class);
    }
}
