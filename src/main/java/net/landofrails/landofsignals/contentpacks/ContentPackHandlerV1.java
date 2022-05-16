package net.landofrails.landofsignals.contentpacks;

import cam72cam.mod.ModCore;
import net.landofrails.api.contentpacks.v1.ContentPackHead;
import net.landofrails.api.contentpacks.v1.ContentPackSignalPart;
import net.landofrails.api.contentpacks.v1.ContentPackSignalSet;
import net.landofrails.api.contentpacks.v2.ContentPackException;
import net.landofrails.api.contentpacks.v2.parent.ContentPackBlock;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItem;
import net.landofrails.api.contentpacks.v2.parent.ContentPackItemRenderType;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalModel;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalState;
import net.landofrails.landofsignals.LOSBlocks;

import java.io.IOException;
import java.util.*;
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
                                ModCore.debug("SignalPart v1: %s", contentPackSignalPart.getName());
                                List<String> states = contentPackSignalPart.getStates();
                                if (states == null) {
                                    states = new ArrayList<>();
                                }
                                states.add(0, null);
                                contentPackSignalPart.setStates(states);

                                convertToV2(contentPackSignalPart);

                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private static void convertToV2(ContentPackSignalPart contentPackSignalPart) {

        // ContentPackSignal
        ContentPackSignal contentPackSignal = new ContentPackSignal();
        contentPackSignal.setId(contentPackSignalPart.getId());
        contentPackSignal.setName(contentPackSignalPart.getName());
        contentPackSignal.setMetadata(Collections.singletonMap("addonversion", 1));

        // ContentPackSignalGroup
        ContentPackSignalGroup contentPackSignalGroup = new ContentPackSignalGroup();
        contentPackSignalGroup.setGroupName(contentPackSignal.getName());

        // ContentPackSignalState(s)
        int index = 1;
        LinkedHashMap<String, ContentPackSignalState> contentPackSignalStateMap = new LinkedHashMap<>();
        for (String texture : contentPackSignalPart.getStates()) {
            ContentPackSignalState contentPackSignalState = new ContentPackSignalState();
            contentPackSignalState.setSignalName("State " + index++);

            ContentPackSignalModel contentPackSignalModel = new ContentPackSignalModel();
            contentPackSignalModel.setTextures(new String[]{texture});

            // ContentPackBlock
            ContentPackBlock contentPackBlock = new ContentPackBlock();
            contentPackBlock.setTranslation(contentPackSignalPart.getTranslation());
            contentPackBlock.setScaling(contentPackSignalPart.getScaling());

            // ContentPackItem
            ContentPackItem contentPackItem = new ContentPackItem();
            contentPackItem.setTranslation(contentPackSignalPart.getItemTranslation());
            contentPackItem.setScaling(contentPackSignalPart.getItemScaling());
            Map<ContentPackItemRenderType, ContentPackItem> contentPackItemRenderTypeContentPackItemMap = new HashMap<>();
            contentPackItemRenderTypeContentPackItemMap.put(ContentPackItemRenderType.DEFAULT, contentPackItem);

            // Nesting
            contentPackSignalModel.setBlock(contentPackBlock);
            contentPackSignalModel.setItem(contentPackItemRenderTypeContentPackItemMap);
            ContentPackSignalModel[] contentPackSignalModels = new ContentPackSignalModel[]{contentPackSignalModel};
            contentPackSignalState.setModels(Collections.singletonMap(contentPackSignalPart.getModel(), contentPackSignalModels));
            contentPackSignalStateMap.put(texture, contentPackSignalState);
        }

        // Nesting
        contentPackSignalGroup.setStates(contentPackSignalStateMap);
        contentPackSignal.setSignals(Collections.singletonMap(contentPackSignal.getId(), contentPackSignalGroup));

        ModCore.info("Signal (v1->v2): %s", contentPackSignal.getName());
        // Validate
        contentPackSignal.validate(missing -> {
            throw new ContentPackException(String.format("There are missing attributes in converted contentpacksignal: %s", missing));
        });
        LOSBlocks.BLOCK_SIGNAL_PART.add(contentPackSignal);

    }

    // For method references
    private static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }
}
