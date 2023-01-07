package net.landofrails.landofsignals.creator.utils;

import cam72cam.mod.MinecraftClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.api.contentpacks.v2.ContentPack;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ContentPackZipHandler {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final File ASSET_FOLDER = new File("./config/landofsignals");
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

        final File target = new File(ASSET_FOLDER, packName + ".zip");

        if (target.exists()) {
            return Optional.empty();
        }

        // Create empty ZIP
        // Create assets/landofsignals folders
        // Create ignore file
        try (ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)))) {
            zipStream.putNextEntry(new ZipEntry("ignore.me"));
            zipStream.putNextEntry(new ZipEntry("assets/"));
            zipStream.putNextEntry(new ZipEntry("assets/landofsignals/"));
            createLandOfSignalsJson(zipStream, packId, MinecraftClient.getPlayer().internal.getGameProfile().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.of(new ContentPackZipHandler(target));
    }

    public static boolean contentPackFileExists(String packName) {
        return new File(ASSET_FOLDER, packName + ".zip").exists();
    }

    private static Optional<File> tryFindContentPack(String packId) {

        final File[] contentpacks = ASSET_FOLDER.listFiles();

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


    public void createSignal(String signalIdText, String signalNameText) {

        ContentPackSignal signal = new ContentPackSignal();
        signal.setId(signalIdText);
        signal.setName(signalNameText);
        signal.setSignals(new HashMap<>());
        signal.setRotationSteps(10f);
        String signalJson = GSON.toJson(signal);

        Map<String, String> env = Collections.singletonMap("create", "false");
        URI uri = URI.create("jar:file:" + contentPackZipFile.toURI().getPath());
        try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {

            String jsonpath = MessageFormat.format("assets/landofsignals/{0}/{0}.json", signalIdText);
            Path nf = fs.getPath(jsonpath);
            if (Files.notExists(nf.getParent()))
                Files.createDirectory(nf.getParent());
            try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW)) {
                writer.write(signalJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
