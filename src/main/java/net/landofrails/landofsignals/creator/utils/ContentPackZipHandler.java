package net.landofrails.landofsignals.creator.utils;

import cam72cam.mod.MinecraftClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.api.contentpacks.v2.ContentPack;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ContentPackZipHandler {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private File contentPackZipFile = null;

    private ContentPackZipHandler(File contentPackZipFile) {
        this.contentPackZipFile = contentPackZipFile;
    }

    public static Optional<ContentPackZipHandler> getInstanceOrCreate(String packName, String packId) {

        Optional<ContentPackZipHandler> cpzh = getInstance(packId);
        if (!cpzh.isPresent())
            cpzh = create(packName, packId);
        return cpzh;
    }

    public static Optional<ContentPackZipHandler> getInstance(String packId) {

        Optional<File> contentPackFile = tryFindContentPack(packId);
        return contentPackFile.map(ContentPackZipHandler::new);
    }

    private static Optional<ContentPackZipHandler> create(String packName, String packId) {

        final File assetFolder = new File("./config/landofsignals");
        final File target = new File(assetFolder, packName + ".zip");

        if (target.exists()) {
            return Optional.empty();
        }

        // Create empty ZIP
        // Create assets/landofsignals folders
        // Create ignore file
        try {
            try (ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)))) {
                zipStream.putNextEntry(new ZipEntry("ignore.me"));
                zipStream.putNextEntry(new ZipEntry("assets/"));
                zipStream.putNextEntry(new ZipEntry("assets/landofsignals/"));
                createLandOfSignalsJson(zipStream, packId, MinecraftClient.getPlayer().internal.getGameProfile().getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.of(new ContentPackZipHandler(target));
    }

    public static boolean contentPackFileExists(String packName) {
        final File assetFolder = new File("./config/landofsignals");
        return new File(assetFolder, packName + ".zip").exists();
    }

    private static Optional<File> tryFindContentPack(String packId) {

        final File assetFolder = new File("./config/landofsignals");
        final File[] contentpacks = assetFolder.listFiles();

        if (contentpacks == null)
            return Optional.empty();

        for (File contentPackFile : contentpacks) {
            try (ZipFile zipFile = new ZipFile(contentPackFile, StandardCharsets.UTF_8)) {

                Enumeration<? extends ZipEntry> entries = zipFile.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry entry = null;
                    try {
                        entry = entries.nextElement();
                    } catch (IllegalArgumentException illegalArgumentException) {
                        if ("MALFORMED".equals(illegalArgumentException.getMessage())) {
                            continue;
                        }
                        throw illegalArgumentException;
                    }
                    if (entry.getName().endsWith("landofsignals.json")) {
                        InputStream stream = zipFile.getInputStream(entry);

                        String landofsignalsJson = new BufferedReader(
                                new InputStreamReader(stream, StandardCharsets.UTF_8))
                                .lines()
                                .collect(Collectors.joining("\n"));

                        ContentPack contentPack = GSON.fromJson(landofsignalsJson, ContentPack.class);
                        if (contentPack.getId().equalsIgnoreCase(packId)) {
                            return Optional.of(contentPackFile);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();

    }

    public static void createLandOfSignalsJson(ZipOutputStream zos, String packId, String author) throws IOException {

        ContentPack contentPack = new ContentPack(packId, author, "1.0", "2", new HashMap<>(), new ArrayList<>());
        String landOfSignalsJson = GSON.toJson(contentPack);

        ZipEntry zipEntry = new ZipEntry("landofsignals.json");
        zos.putNextEntry(zipEntry);

        byte[] data = landOfSignalsJson.getBytes();
        zos.write(data, 0, data.length);
        zos.closeEntry();
    }


}
