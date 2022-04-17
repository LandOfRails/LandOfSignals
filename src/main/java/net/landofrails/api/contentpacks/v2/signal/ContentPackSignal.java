package net.landofrails.api.contentpacks.v2.signal;

import java.util.Map;

public class ContentPackSignal {

    private String name;
    private String id;
    private Integer rotationSteps;
    private Map<String, ContentPackSignalGroup> signals;
    private Map<String, Object> metadata;

}