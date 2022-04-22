package net.landofrails.landofsignals.contentpacks;

import cam72cam.mod.ModCore;
import net.landofrails.api.contentpacks.v1.*;
import net.landofrails.landofsignals.LOSBlocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ContentPackHandlerV1 {

    private ContentPackHandlerV1() {

    }

    public static void load(ZipFile zip, String name) throws IOException {
        ContentPackHead contentPack = ContentPackHead.fromJson(zip.getInputStream(zip.getEntry(name)));
        // @formatter:off
        List<ZipEntry> files = zip.stream().
                filter(not(ZipEntry::isDirectory)).
                filter(f -> f.getName().endsWith(".json") && !f.getName().endsWith("landofsignals.json")).collect(Collectors.toList());
        // @formatter:on

        ModCore.info("Content for %s:", contentPack.getId());
        // TODO: Make Adapter for addonversion 1
        // TODO Remove old addX Methods
        addSignals(contentPack, files, zip);
        addSigns(contentPack, files, zip);
    }

    /**
     * @deprecated (0.1.0, New Contentpacks, Will be replaced with a conversion method to fit V2 ContentPacks)
     */
    @Deprecated
    private static void addSignals(ContentPackHead contentPack, List<ZipEntry> files, ZipFile zip) throws IOException {
        for (String pathToContentPackSignalSet : contentPack.getSignals()) {
            for (ZipEntry zipEntry : files) {
                if (zipEntry.getName().equalsIgnoreCase(pathToContentPackSignalSet)) {
                    ContentPackSignalSet contentPackSignalSet = ContentPackSignalSet.fromJson(zip.getInputStream(zipEntry));
                    ModCore.info("SignalSet: %s", contentPackSignalSet.getName());
                    for (String pathToContentPackSignalPart : contentPackSignalSet.getSignalparts()) {
                        for (ZipEntry zipEntry1 : files) {
                            if (zipEntry1.getName().equalsIgnoreCase(pathToContentPackSignalPart)) {
                                ContentPackSignalPart contentPackSignalPart = ContentPackSignalPart.fromJson(zip.getInputStream(zipEntry1));
                                ModCore.info("SignalPart: %s", contentPackSignalPart.getName());
                                List<String> states = contentPackSignalPart.getStates();
                                if (states == null) {
                                    states = new ArrayList<>();
                                }
                                states.add(0, null);
                                contentPackSignalPart.setStates(states);
                                if (contentPackSignalPart.getAnimations() != null)
                                    LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.add(contentPackSignalPart);
                                else
                                    LOSBlocks.BLOCK_SIGNAL_PART.add(contentPackSignalPart);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * @deprecated (0.1.0, New Contentpacks, Will be replaced with a conversion method to fit V2 ContentPacks)
     */
    @Deprecated
    private static void addSigns(ContentPackHead contentPack, List<ZipEntry> files, ZipFile zip) throws IOException {
        for (String pathToContentPackSignSet : contentPack.getSigns()) {
            for (ZipEntry zipEntry : files) {
                if (zipEntry.getName().equalsIgnoreCase(pathToContentPackSignSet)) {
                    ContentPackSignSet contentPackSignSet = ContentPackSignSet.fromJson(zip.getInputStream(zipEntry));
                    ModCore.info("SignSet: %s", contentPackSignSet.getName());
                    for (String pathToContentPackSignalPart : contentPackSignSet.getSignparts()) {
                        for (ZipEntry zipEntry1 : files) {
                            if (zipEntry1.getName().equalsIgnoreCase(pathToContentPackSignalPart)) {
                                ContentPackSignPart contentPackSignPart = ContentPackSignPart.fromJson(zip.getInputStream(zipEntry1));
                                ModCore.info("SignPart: %s", contentPackSignPart.getName());
                                LOSBlocks.BLOCK_SIGN_PART.add(contentPackSignPart);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    // For method references
    private static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }
}
