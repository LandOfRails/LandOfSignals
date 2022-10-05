package net.landofrails.api.contentpacks.v1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ContentPackSignalSet {

    private String name;
    private List<String> signalparts;

    public ContentPackSignalSet(final String name, final List<String> signalparts) {
        this.name = name;
        this.signalparts = signalparts;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<String> getSignalparts() {
        return signalparts;
    }

    public void setSignalparts(final List<String> signalparts) {
        this.signalparts = signalparts;
    }

    public static ContentPackSignalSet fromJson(final InputStream inputStream) {
        final StringBuilder s = new StringBuilder();
        final byte[] buffer = new byte[1024];
        int read = 0;

        try {
            while ((read = inputStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (final IOException e) {
            throw new ContentPackException("Cant read ContentPackSignalSet: " + e.getMessage());
        }

        final String json = s.toString();
        final Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, ContentPackSignalSet.class);
    }
}
