package net.landofrails.api.contentpacks.v2.signalbox;

import com.google.gson.Gson;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.parent.ContentPackReferences;

import java.util.Map;
import java.util.Set;

public class ContentPackSignalbox {

    private static final Gson GSON = new Gson();

    private String name;
    private String id;
    private Float rotationSteps;
    private String creativeTab;
    // objPath : objProperties
    private Map<String, ContentPackModel[]> base;
    private ContentPackReferences references;
    // metadataId : data
    private Map<String, ?> metadata;

    // Processed data
    private Map<String, Set<String>> objTextures;
    private String uniqueId;

}
