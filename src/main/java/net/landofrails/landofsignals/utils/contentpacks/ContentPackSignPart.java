package net.landofrails.landofsignals.utils.contentpacks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContentPackSignPart {

    private static final Function<Map.Entry<Integer, ?>, Integer> INTKEY = Map.Entry::getKey;

    private String id;
    private String name;
    private Map<Integer, ContentPackSignObject> signObjects;

    public ContentPackSignPart(String id, String name, ContentPackSignObject... signObjects) {
        this.id = id;
        this.name = name;
        this.signObjects = new HashMap<>();
        for (int i = 0; i < signObjects.length; i++) {
            this.signObjects.put(i + 1, signObjects[i]);
        }
    }

    public ContentPackSignPart(String id, String name, Map<Integer, ContentPackSignObject> signObjects) {
        this.id = id;
        this.name = name;
        this.signObjects = signObjects;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, ContentPackSignObject> getSignObjects() {
        return signObjects;
    }

    public void setSignObjects(Map<Integer, ContentPackSignObject> signObjects) {
        this.signObjects = signObjects;
    }

    public static ContentPackSignPart fromJson(InputStream inputStream) {
        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read = 0;

        try {
            while ((read = inputStream.read(buffer, 0, 1024)) >= 0) {
                s.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            throw new ContentPackException("Cant read ContentPackSignPart: " + e.getMessage());
        }

        String json = s.toString();
        Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, ContentPackSignPart.class);
    }

    public Map<Integer, String> getModel() {
        return signObjects.entrySet().stream().collect(Collectors.toMap(INTKEY, v -> v.getValue().getModel()));
    }

    public Map<Integer, float[]> getTranslation() {
        return signObjects.entrySet().stream().collect(Collectors.toMap(INTKEY, v -> v.getValue().getTranslation()));
    }

    public Map<Integer, float[]> getScaling() {
        return signObjects.entrySet().stream().collect(Collectors.toMap(INTKEY, v -> v.getValue().getScaling()));
    }

    public Map<Integer, float[]> getItemScaling() {
        return signObjects.entrySet().stream().collect(Collectors.toMap(INTKEY, v -> v.getValue().getItemScaling()));
    }

    public Map<Integer, float[]> getItemTranslation() {
        return signObjects.entrySet().stream().collect(Collectors.toMap(INTKEY, v -> v.getValue().getItemTranslation()));
    }

    public Map<Integer, String[]> getRenderGroups() {
        Map<Integer, String[]> renderGroups = new HashMap<>();
        signObjects.forEach((objId, so) -> renderGroups.put(objId, so.getRenderGroups()));
        return renderGroups;
    }

    public Map<Integer, String> getTexture() {
        Map<Integer, String> textures = new HashMap<>();
        signObjects.forEach((objId, so) -> textures.put(objId, so.getTexture()));
        return textures;
    }

}
