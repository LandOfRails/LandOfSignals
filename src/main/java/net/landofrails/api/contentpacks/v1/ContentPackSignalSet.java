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

    public ContentPackSignalSet(String name, List<String> signalparts) {
        this.name = name;
        this.signalparts = signalparts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSignalparts() {
        return signalparts;
    }

    public void setSignalparts(List<String> signalparts) {
        this.signalparts = signalparts;
    }

    public static ContentPackSignalSet fromJson(InputStream inputStream) {
        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read = 0;

        try {
            while ((read = inputStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            throw new ContentPackException("Cant read ContentPackSignalSet: " + e.getMessage());
        }

        String json = s.toString();
        Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, ContentPackSignalSet.class);
    }
}
