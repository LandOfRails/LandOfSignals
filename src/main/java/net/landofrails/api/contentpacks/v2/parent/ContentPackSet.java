package net.landofrails.api.contentpacks.v2.parent;

import net.landofrails.api.contentpacks.v2.EntryType;

import java.util.Map;
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
        
    }

}
