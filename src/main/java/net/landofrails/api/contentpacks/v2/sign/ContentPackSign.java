package net.landofrails.api.contentpacks.v2.sign;

import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalModel;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalReferences;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ContentPackSign {

    private String name;
    private String id;
    private Float rotationSteps;
    private String creativeTab;
    // objPath : objProperties
    private Map<String, ContentPackSignalModel[]> base;
    private ContentPackSignalReferences references;
    // metadataId : data
    private Map<String, ?> metadata;

    // Processed data
    private Map<String, Set<String>> objTextures;

    // TODO Constructors


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getRotationSteps() {
        return rotationSteps;
    }

    public void setRotationSteps(Float rotationSteps) {
        this.rotationSteps = rotationSteps;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public void setCreativeTab(String creativeTab) {
        this.creativeTab = creativeTab;
    }

    public Map<String, ContentPackSignalModel[]> getBase() {
        return base;
    }

    public void setBase(Map<String, ContentPackSignalModel[]> base) {
        this.base = base;
    }

    public ContentPackSignalReferences getReferences() {
        return references;
    }

    public void setReferences(ContentPackSignalReferences references) {
        this.references = references;
    }

    public Map<String, ?> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, ?> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Set<String>> getObjTextures() {
        return objTextures;
    }

    public void setObjTextures(Map<String, Set<String>> objTextures) {
        this.objTextures = objTextures;
    }

    public void validate(Consumer<String> invalid) {
        // FIXME
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    public static ContentPackSign fromJson(InputStream inputStream) {
        // FIXME
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    // FIXME fill this

}
