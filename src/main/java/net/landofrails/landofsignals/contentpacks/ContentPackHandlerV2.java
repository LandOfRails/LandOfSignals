package net.landofrails.landofsignals.contentpacks;

import cam72cam.mod.ModCore;
import net.landofrails.api.contentpacks.v2.ContentPack;
import net.landofrails.api.contentpacks.v2.ContentPackException;
import net.landofrails.api.contentpacks.v2.EntryType;
import net.landofrails.api.contentpacks.v2.parent.ContentPackSet;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSBlocks;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ContentPackHandlerV2 {

    private ContentPackHandlerV2() {

    }

    public static void load(ZipFile zip, String name) throws IOException {
        ContentPack contentPack = ContentPack.fromJson(zip.getInputStream(zip.getEntry(name)));
        ModCore.info("Content for %s:", contentPack.getId());

        boolean hasContent = contentPack.getContent() != null && !contentPack.getContent().isEmpty();
        boolean hasContentSets = contentPack.getContentSets() != null && !contentPack.getContentSets().isEmpty();

        if (hasContent) {
            contentPack.getContent().forEach((path, type) -> {
                if (type == EntryType.BLOCKSIGNAL) {
                    loadSignal(zip, path);
                } else {
                    ModCore.warn("Type %s is currently not implemented in V2!", type.name());
                }
            });
        }
        if (hasContentSets) {
            contentPack.getContentSets().forEach(path -> loadSet(zip, path));
        }
        if (!hasContent && !hasContentSets) {
            ModCore.warn("ContentPack %s does not contain any blocks");
        }
    }

    private static void loadSignal(ZipFile zip, String path) {

        try {
            Predicate<ZipEntry> filterRelevantZipEntry = zipEntry -> zipEntry.getName().equalsIgnoreCase(path);
            Optional<? extends ZipEntry> zipEntry = zip.stream().filter(filterRelevantZipEntry).findFirst();

            if (zipEntry.isPresent()) {
                ContentPackSignal contentPackSignal = ContentPackSignal.fromJson(zip.getInputStream(zipEntry.get()));
                contentPackSignal.validate(missing -> {
                    throw new ContentPackException(String.format("There are missing attributes: %s", missing));
                });
                ModCore.info("Signal: %s", contentPackSignal.getName());
                LOSBlocks.BLOCK_SIGNAL_PART.add(contentPackSignal);
            } else {
                ModCore.error("Couldn't find ContentPackSignal under path %s!", path);
            }

        } catch (Exception e) {
            ModCore.error("Couldn't load ContentPackSignal in path %s\nError: %s", path, e.getMessage());
            if (!e.getMessage().startsWith("There are missing attributes")) {
                ModCore.error("Stacktrace:");
                e.printStackTrace();
            }
        }
    }

    private static void loadSet(ZipFile zip, String path) {
        try {
            Predicate<ZipEntry> filterRelevantZipEntry = zipEntry -> zipEntry.getName().equalsIgnoreCase(path);
            Optional<? extends ZipEntry> zipEntry = zip.stream().filter(filterRelevantZipEntry).findFirst();

            if (zipEntry.isPresent()) {
                ContentPackSet contentPackSet = ContentPackSet.fromJson(zip.getInputStream(zipEntry.get()));
                contentPackSet.validate(missing -> {
                    throw new ContentPackException(String.format("There are missing attributes: %s", missing));
                });
                ModCore.info("Set: %s", contentPackSet.getName());
                contentPackSet.getContent().forEach((entryPath, type) -> {
                    if (type == EntryType.BLOCKSIGNAL) {
                        loadSignal(zip, entryPath);
                    } else {
                        ModCore.warn("Type %s is currently not implemented in V2!", type.name());
                    }
                });
            } else {
                ModCore.error("Couldn't find ContentPackSet under path %s!", path);
            }

        } catch (Exception e) {
            ModCore.error("Couldn't load ContentPackSet in path %s\nError: %s", path, e.getMessage());
            if (!e.getMessage().startsWith("There are missing attributes")) {
                ModCore.error("Stacktrace:");
                e.printStackTrace();
            }
        }
    }

}
