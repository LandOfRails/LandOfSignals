package net.landofrails.landofsignals.contentpacks;

import cam72cam.mod.ModCore;
import net.landofrails.api.contentpacks.v1.ContentPackHead;
import net.landofrails.api.contentpacks.v1.ContentPackSignalPart;
import net.landofrails.api.contentpacks.v1.ContentPackSignalSet;
import net.landofrails.api.contentpacks.v2.ContentPack;
import net.landofrails.api.contentpacks.v2.ContentPackException;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSBlocks;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ContentPackHandlerV1 {

    private ContentPackHandlerV1() {

    }

    public static void load(ZipFile zip, String name, boolean isUTF8) throws IOException {
        ContentPackHead contentPack = ContentPackHead.fromJson(zip.getInputStream(zip.getEntry(name)));
        // @formatter:off
        List<ZipEntry> files = zip.stream().
                filter(not(ZipEntry::isDirectory)).
                filter(f -> f.getName().endsWith(".json") && !f.getName().endsWith("landofsignals.json")).collect(Collectors.toList());
        // @formatter:on

        ModCore.info("Content for %s:", contentPack.getId());
        addSignals(contentPack, files, zip, isUTF8);
    }

    private static void addSignals(ContentPackHead contentPack, List<ZipEntry> files, ZipFile zip, boolean isUTF8) throws IOException {
        for (String pathToContentPackSignalSet : contentPack.getSignals()) {
            for (ZipEntry zipEntry : files) {
                if (zipEntry.getName().equalsIgnoreCase(pathToContentPackSignalSet)) {
                    ContentPackSignalSet contentPackSignalSet = ContentPackSignalSet.fromJson(zip.getInputStream(zipEntry));
                    ModCore.info("SignalSet: %s", contentPackSignalSet.getName());
                    for (String pathToContentPackSignalPart : contentPackSignalSet.getSignalparts()) {
                        for (ZipEntry zipEntry1 : files) {
                            if (zipEntry1.getName().equalsIgnoreCase(pathToContentPackSignalPart)) {
                                ContentPackSignalPart contentPackSignalPart = ContentPackSignalPart.fromJson(zip.getInputStream(zipEntry1));
                                ModCore.debug("SignalPart v1: %s", contentPackSignalPart.getName());
                                String[] cpStates = contentPackSignalPart.getStates();
                                String[] states = new String[1 + (cpStates != null ? cpStates.length : 0)];
                                states[0] = null;
                                if (cpStates != null && cpStates.length > 0) {
                                    System.arraycopy(cpStates, 0, states, 1, cpStates.length);
                                }
                                contentPackSignalPart.setStates(states);

                                convertToV2(contentPackSignalPart, new ContentPack(contentPack), isUTF8);

                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public static void convertToV2(ContentPackSignalPart contentPackSignalPart, ContentPack contentPack, boolean isUTF8) {

        // ContentPackSignal
        ContentPackSignal contentPackSignal = new ContentPackSignal();

        contentPackSignal.setId(contentPackSignalPart.getId());
        contentPackSignal.setName(contentPackSignalPart.getName());
        contentPackSignal.setMetadata(Collections.singletonMap("addonversion", 1));
        contentPackSignal.setUTF8(isUTF8);
        contentPackSignal.setModel(contentPackSignalPart.getModel());
        contentPackSignal.setStates(contentPackSignalPart.getStates());
        contentPackSignal.setTranslation(contentPackSignalPart.getTranslation());
        contentPackSignal.setItemTranslation(contentPackSignalPart.getItemTranslation());
        contentPackSignal.setScaling(contentPackSignalPart.getScaling());
        contentPackSignal.setItemScaling(contentPackSignalPart.getItemScaling());

        ModCore.info("Signal (v1->v2): %s", contentPackSignal.getName());
        // Validate
        contentPackSignal.validate(missing -> {
            throw new ContentPackException(String.format("There are missing attributes in converted contentpacksignal: %s", missing));
        }, contentPack);
        LOSBlocks.BLOCK_SIGNAL_PART.add(contentPackSignal);

    }

    // For method references
    private static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }
}
