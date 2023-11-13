package net.landofrails.landofsignals.contentpacks;

import cam72cam.mod.ModCore;
import net.landofrails.api.contentpacks.v2.ContentPack;
import net.landofrails.api.contentpacks.v2.ContentPackException;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackComplexSignal;
import net.landofrails.api.contentpacks.v2.deco.ContentPackDeco;
import net.landofrails.api.contentpacks.v2.lever.ContentPackLever;
import net.landofrails.api.contentpacks.v2.parent.ContentPackSet;
import net.landofrails.api.contentpacks.v2.sign.ContentPackSign;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.api.contentpacks.v2.signalbox.ContentPackSignalbox;
import net.landofrails.landofsignals.LOSBlocks;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ContentPackHandlerV2 {

    private static final Consumer<String> MISSINGATTRIBUTESEXCEPTION = missing -> {
        throw new ContentPackException(String.format("There are missing attributes: %s", missing));
    };
    private static final String THEREAREMISSINGATTRIBUTESSNIPPET = "There are missing attributes";

    private ContentPackHandlerV2() {

    }

    public static void load(ZipFile zip, String name, boolean isUTF8) throws IOException {
        ContentPack contentPack = ContentPack.fromJson(zip.getInputStream(zip.getEntry(name)));
        ModCore.info("Content for %s:", contentPack.getId());

        boolean hasContent = contentPack.getContent() != null && !contentPack.getContent().isEmpty();
        boolean hasContentSets = contentPack.getContentSets() != null && !contentPack.getContentSets().isEmpty();

        if (hasContent) {
            contentPack.getContent().forEach((path, type) -> {
                switch (type) {
                    case BLOCKSIGNAL:
                        loadSignal(zip, path, contentPack, isUTF8);
                        break;
                    case BLOCKCOMPLEXSIGNAL:
                        loadComplexSignal(zip, path, contentPack, isUTF8);
                        break;
                    case BLOCKSIGN:
                        loadSign(zip, path, contentPack, isUTF8);
                        break;
                    case BLOCKSIGNALBOX:
                        loadSignalbox(zip, path, contentPack, isUTF8);
                        break;
                    case BLOCKDECO:
                        loadDeco(zip, path, contentPack, isUTF8);
                        break;
                    case BLOCKLEVER:
                        loadCustomLever(zip, path, contentPack, isUTF8);
                        break;
                    default:
                        ModCore.error("Type %s is currently not implemented in V2!", type.name());
                }
            });
        }
        if (hasContentSets) {
            contentPack.getContentSets().forEach(path -> loadSet(zip, path, contentPack, isUTF8));
        }
        if (!hasContent && !hasContentSets) {
            ModCore.warn("ContentPack %s does not contain any blocks", contentPack.getId());
        }
    }

    private static void loadSignal(ZipFile zip, String path, ContentPack contentPack, boolean isUTF8) {

        try {
            Predicate<ZipEntry> filterRelevantZipEntry = zipEntry -> zipEntry.getName().equalsIgnoreCase(path);
            Optional<? extends ZipEntry> zipEntry = zip.stream().filter(filterRelevantZipEntry).findFirst();

            if (zipEntry.isPresent()) {
                ContentPackSignal contentPackSignal = ContentPackSignal.fromJson(zip.getInputStream(zipEntry.get()));
                contentPackSignal.setUTF8(isUTF8);
                contentPackSignal.validate(MISSINGATTRIBUTESEXCEPTION, contentPack);
                ModCore.info("Signal: %s", contentPackSignal.getName());
                LOSBlocks.BLOCK_SIGNAL_PART.add(contentPackSignal);
            } else {
                ModCore.error("Couldn't find ContentPackSignal under path %s!", path);
            }

        } catch (Exception e) {
            ModCore.error("Couldn't load ContentPackSignal in path %s\nError: %s", path, e.getMessage());
            if (!e.getMessage().startsWith(THEREAREMISSINGATTRIBUTESSNIPPET)) {
                writeStacktraceHead();
                e.printStackTrace();
            }
        }
    }

    private static void loadComplexSignal(ZipFile zip, String path, ContentPack contentPack, boolean isUTF8) {

        try {
            Predicate<ZipEntry> filterRelevantZipEntry = zipEntry -> zipEntry.getName().equalsIgnoreCase(path);
            Optional<? extends ZipEntry> zipEntry = zip.stream().filter(filterRelevantZipEntry).findFirst();

            if (zipEntry.isPresent()) {
                ContentPackComplexSignal contentPackComplexSignal = ContentPackComplexSignal.fromJson(zip.getInputStream(zipEntry.get()));
                contentPackComplexSignal.setUTF8(isUTF8);
                contentPackComplexSignal.validate(MISSINGATTRIBUTESEXCEPTION, contentPack);
                ModCore.info("Signal: %s", contentPackComplexSignal.getName());
                LOSBlocks.BLOCK_COMPLEX_SIGNAL.add(contentPackComplexSignal);
            } else {
                ModCore.error("Couldn't find ContentPackComplexSignal under path %s!", path);
            }

        } catch (Exception e) {
            ModCore.error("Couldn't load ContentPackComplexSignal in path %s\nError: %s", path, e.getMessage());
            if (!e.getMessage().startsWith(THEREAREMISSINGATTRIBUTESSNIPPET)) {
                writeStacktraceHead();
                e.printStackTrace();
            }
        }
    }

    private static void loadSign(ZipFile zip, String path, ContentPack contentPack, boolean isUTF8) {
        try {
            Predicate<ZipEntry> filterRelevantZipEntry = zipEntry -> zipEntry.getName().equalsIgnoreCase(path);
            Optional<? extends ZipEntry> zipEntry = zip.stream().filter(filterRelevantZipEntry).findFirst();

            if (zipEntry.isPresent()) {
                ContentPackSign contentPackSign = ContentPackSign.fromJson(zip.getInputStream(zipEntry.get()));
                contentPackSign.setUTF8(isUTF8);
                contentPackSign.validate(MISSINGATTRIBUTESEXCEPTION, contentPack);
                ModCore.info("Sign: %s", contentPackSign.getName());

                LOSBlocks.BLOCK_SIGN_PART.add(contentPackSign);
            } else {
                ModCore.error("Couldn't find ContentPackSign under path %s!", path);
            }

        } catch (Exception e) {
            ModCore.error("Couldn't load ContentPackSign in path %s\nError: %s", path, e.getMessage());
            if (!e.getMessage().startsWith(THEREAREMISSINGATTRIBUTESSNIPPET)) {
                writeStacktraceHead();
                e.printStackTrace();
            }
        }
    }

    private static void loadSignalbox(ZipFile zip, String path, ContentPack contentPack, boolean isUTF8) {
        try {
            Predicate<ZipEntry> filterRelevantZipEntry = zipEntry -> zipEntry.getName().equalsIgnoreCase(path);
            Optional<? extends ZipEntry> zipEntry = zip.stream().filter(filterRelevantZipEntry).findFirst();

            if (zipEntry.isPresent()) {
                ContentPackSignalbox contentPackSignalbox = ContentPackSignalbox.fromJson(zip.getInputStream(zipEntry.get()));
                contentPackSignalbox.setUTF8(isUTF8);
                contentPackSignalbox.validate(MISSINGATTRIBUTESEXCEPTION, contentPack);
                ModCore.info("Signalbox: %s", contentPackSignalbox.getName());

                LOSBlocks.BLOCK_SIGNAL_BOX.add(contentPackSignalbox);
            } else {
                ModCore.error("Couldn't find ContentPackSignalbox under path %s!", path);
            }

        } catch (Exception e) {
            ModCore.error("Couldn't load ContentPackSignalbox in path %s\nError: %s", path, e.getMessage());
            if (!e.getMessage().startsWith(THEREAREMISSINGATTRIBUTESSNIPPET)) {
                writeStacktraceHead();
                e.printStackTrace();
            }
        }
    }

    private static void loadDeco(ZipFile zip, String path, ContentPack contentPack, boolean isUTF8) {
        try {
            Predicate<ZipEntry> filterRelevantZipEntry = zipEntry -> zipEntry.getName().equalsIgnoreCase(path);
            Optional<? extends ZipEntry> zipEntry = zip.stream().filter(filterRelevantZipEntry).findFirst();

            if (zipEntry.isPresent()) {
                ContentPackDeco contentPackDeco = ContentPackDeco.fromJson(zip.getInputStream(zipEntry.get()));
                contentPackDeco.setUTF8(isUTF8);
                contentPackDeco.validate(MISSINGATTRIBUTESEXCEPTION, contentPack);
                ModCore.info("Deco: %s", contentPackDeco.getName());

                LOSBlocks.BLOCK_DECO.add(contentPackDeco);
            } else {
                ModCore.error("Couldn't find ContentPackDeco under path %s!", path);
            }

        } catch (Exception e) {
            ModCore.error("Couldn't load ContentPackDeco in path %s\nError: %s", path, e.getMessage());
            if (!e.getMessage().startsWith(THEREAREMISSINGATTRIBUTESSNIPPET)) {
                writeStacktraceHead();
                e.printStackTrace();
            }
        }
    }

    private static void loadCustomLever(ZipFile zip, String path, ContentPack contentPack, boolean isUTF8) {
        try {
            Predicate<ZipEntry> filterRelevantZipEntry = zipEntry -> zipEntry.getName().equalsIgnoreCase(path);
            Optional<? extends ZipEntry> zipEntry = zip.stream().filter(filterRelevantZipEntry).findFirst();

            if (zipEntry.isPresent()) {
                ContentPackLever contentPackLever = ContentPackLever.fromJson(zip.getInputStream(zipEntry.get()));
                contentPackLever.setUTF8(isUTF8);
                contentPackLever.validate(MISSINGATTRIBUTESEXCEPTION, contentPack);
                ModCore.info("CustomLever: %s", contentPackLever.getName());

                LOSBlocks.BLOCK_CUSTOM_LEVER.add(contentPackLever);
            } else {
                ModCore.error("Couldn't find ContetnPackLever under path %s!", path);
            }

        } catch (Exception e) {
            ModCore.error("Couldn't load ContetnPackLever in path %s\nError: %s", path, e.getMessage());
            if (!e.getMessage().startsWith(THEREAREMISSINGATTRIBUTESSNIPPET)) {
                writeStacktraceHead();
                e.printStackTrace();
            }
        }
    }

    private static void loadSet(ZipFile zip, String path, ContentPack contentPack, boolean isUTF8) {
        try {
            Predicate<ZipEntry> filterRelevantZipEntry = zipEntry -> zipEntry.getName().equalsIgnoreCase(path);
            Optional<? extends ZipEntry> zipEntry = zip.stream().filter(filterRelevantZipEntry).findFirst();

            if (zipEntry.isPresent()) {
                ContentPackSet contentPackSet = ContentPackSet.fromJson(zip.getInputStream(zipEntry.get()));
                contentPackSet.validate(MISSINGATTRIBUTESEXCEPTION);
                ModCore.info("Set: %s", contentPackSet.getName());
                contentPackSet.getContent().forEach((entryPath, type) -> {
                    switch (type) {
                        case BLOCKSIGNAL:
                            loadSignal(zip, entryPath, contentPack, isUTF8);
                            break;
                        case BLOCKCOMPLEXSIGNAL:
                            loadComplexSignal(zip, entryPath, contentPack, isUTF8);
                            break;
                        case BLOCKSIGN:
                            loadSign(zip, entryPath, contentPack, isUTF8);
                            break;
                        case BLOCKSIGNALBOX:
                            loadSignalbox(zip, entryPath, contentPack, isUTF8);
                            break;
                        case BLOCKDECO:
                            loadDeco(zip, entryPath, contentPack, isUTF8);
                            break;
                        case BLOCKLEVER:
                            loadCustomLever(zip, entryPath, contentPack, isUTF8);
                            break;
                        default:
                            ModCore.error("Type %s is currently not implemented in V2!", type.name());
                    }
                });
            } else {
                ModCore.error("Couldn't find ContentPackSet under path %s!", path);
            }

        } catch (Exception e) {
            ModCore.error("Couldn't load ContentPackSet in path %s\nError: %s", path, e.getMessage());
            if (!e.getMessage().startsWith(THEREAREMISSINGATTRIBUTESSNIPPET)) {
                writeStacktraceHead();
                e.printStackTrace();
            }
        }
    }

    private static void writeStacktraceHead() {
        ModCore.error("Stacktrace:");
    }

}
