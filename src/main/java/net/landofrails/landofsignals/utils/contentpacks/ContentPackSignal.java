package net.landofrails.landofsignals.utils.contentpacks;

import java.util.ArrayList;
import java.util.List;

public class ContentPackSignal {

    private String name;
    private List<String> signalparts;

    // Manually filled by ContentPackHandler.
    private List<ContentPackSignalPart> signalParts = new ArrayList<>();

    public ContentPackSignal(String name, List<String> signalparts) {
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

    public List<ContentPackSignalPart> getSignalParts() {
        return signalParts;
    }

    public void setSignalParts(List<ContentPackSignalPart> signalParts) {
        this.signalParts = signalParts;
    }
}
