package net.landofrails.landofsignals.creator.utils;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.ModCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landofrails.api.contentpacks.v2.ContentPack;
import net.landofrails.api.contentpacks.v2.EntryType;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalState;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
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

    private Path getLandOfSignalsJson(FileSystem fs) {
        Path root = fs.getPath("/");
        final Path[] cp = {null};
        try {
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    if (path.endsWith("landofsignals.json")) {
                        cp[0] = path;
                    }
                    return cp[0] == null ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("No landofsignals.json found!", e);
        }
        return cp[0];
    }

    private void addEntryToLandOfSignalsJson(FileSystem fs, String path, EntryType type) throws IOException {
        Path losJsonPath = getLandOfSignalsJson(fs);
        List<String> lines = Files.readAllLines(losJsonPath, StandardCharsets.UTF_8);
        String allLines = String.join("\n", lines);
        ContentPack cp = GSON.fromJson(allLines, ContentPack.class);
        cp.getContent().put(path, type);
        String json = GSON.toJson(cp);
        try (Writer writer = Files.newBufferedWriter(losJsonPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(json);
        }
    }

    public Map<String, String> listSignals() {
        return newZipFileSystem(fs -> {
            List<String> lines = Files.readAllLines(getLandOfSignalsJson(fs), StandardCharsets.UTF_8);
            String allLines = String.join("\n", lines);
            ContentPack cp = GSON.fromJson(allLines, ContentPack.class);

            if (cp.getContent() == null)
                return Collections.emptyMap();
            // TODO Contentsets
            List<String> signalPaths = cp.getContent().entrySet().stream().filter(entry -> entry.getValue() == EntryType.BLOCKSIGNAL).map(Map.Entry::getKey).collect(Collectors.toList());
            Map<String, String> signals = new HashMap<>();
            for (String signalPath : signalPaths) {
                String[] signalPathParts = signalPath.split("/");
                String signalId = signalPathParts[signalPathParts.length - 1];
                Optional<ContentPackSignal> signal = getSignal(signalId);
                signal.ifPresent(s ->
                        signals.put(signalId, s.getName())
                );
                signals.putIfAbsent(signalId, "Not Found! Wrong path?");
                // TODO We have the signalPath, use it instead of the signalId.
            }
            return signals;
        });
    }

    public void createSignal(String signalId, String signalName) {

        ContentPackSignal signal = new ContentPackSignal();
        signal.setId(signalId);
        signal.setName(signalName);

        ContentPackSignalGroup group = new ContentPackSignalGroup("default", new LinkedHashMap<>());
        signal.setSignals(Collections.singletonMap("default", group));

        signal.setRotationSteps(10f);
        writeSignal(signalId, signal);
    }

    private void writeSignal(FileSystem fs, String signalId, ContentPackSignal signal) throws IOException {
        String signalJson = GSON.toJson(signal);
        String jsonpath = MessageFormat.format("assets/landofsignals/{0}/{0}.json", signalId);
        addEntryToLandOfSignalsJson(fs, jsonpath, EntryType.BLOCKSIGNAL);
        Path nf = fs.getPath(jsonpath);
        if (Files.notExists(nf.getParent()))
            Files.createDirectory(nf.getParent());
        try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(signalJson);
        }
    }

    public void writeSignal(String signalId, ContentPackSignal signal) {
        newZipFileSystem(fs -> {
            writeSignal(fs, signalId, signal);
        });
    }

    public Optional<ContentPackSignal> getSignal(String signalId) {
        return newZipFileSystem(fs -> {
            return getSignal(fs, signalId);
        });
    }

    public Optional<ContentPackSignal> getSignal(FileSystem fs, String signalId) throws IOException {
        ContentPackSignal signal = null;
        String jsonpath = MessageFormat.format("assets/landofsignals/{0}/{0}.json", signalId);
        Path nf = fs.getPath(jsonpath);
        if (Files.exists(nf)) {
            List<String> lines = Files.readAllLines(nf, StandardCharsets.UTF_8);
            String allLines = String.join("\n", lines);
            signal = GSON.fromJson(allLines, ContentPackSignal.class);
        }
        return Optional.ofNullable(signal);
    }

    public void createState(String signalId, String groupId, String stateId, String stateName) {
        newZipFileSystem(fs -> {
            ContentPackSignal signal = getSignal(fs, signalId).orElseThrow(() -> new RuntimeException("Oh oh"));
            ContentPackSignalState state = new ContentPackSignalState(stateName, new LinkedHashMap<>());
            signal.getSignals().get(groupId).getStates().put(stateId, state);
            writeSignal(fs, signalId, signal);
        });
    }

    public boolean containsOBJ(String signalId) {
        return Boolean.TRUE.equals(newZipFileSystem(fs -> {
            String path = MessageFormat.format("assets/landofsignals/{0}/", signalId);
            Path nf = fs.getPath(path);
            return Files.list(nf).anyMatch(p -> p.endsWith(signalId + ".obj"));
        }));
    }

    public void openSignalFolder(String signalId) {
        newZipFileSystem(fs -> {
            String abstractPath = MessageFormat.format("assets/landofsignals/{0}/", signalId);
            String path = contentPackZipFile.toPath().resolve(abstractPath).toAbsolutePath().toString();
            ModCore.info(path);
            try {
                String sevenZip = "C:\\Program Files\\7-Zip\\7zFM.exe";

                Runtime.getRuntime()
                        .exec(new String[]{
                                sevenZip,
                                path
                        });
            } catch (IOException e) {
                // Unpretty fallback
                e.printStackTrace();
                Runtime.getRuntime().exec("explorer.exe /select," + path);
            }
        });
    }

    private void newZipFileSystem(IOExceptionConsumer<FileSystem> zipFileSystem) {
        Map<String, String> env = Collections.singletonMap("create", "false");
        URI uri = URI.create("jar:file:" + contentPackZipFile.toURI().getPath());
        try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            zipFileSystem.acceptThrows(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> T newZipFileSystem(IOExceptionFunction<FileSystem, T> zipFileSystem) {
        Map<String, String> env = Collections.singletonMap("create", "false");
        URI uri = URI.create("jar:file:" + contentPackZipFile.toURI().getPath());
        try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            return zipFileSystem.applyThrows(fs);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FunctionalInterface
    private interface IOExceptionConsumer<T> extends Consumer<T> {

        @Override
        default void accept(T t) {
            try {
                acceptThrows(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void acceptThrows(T t) throws IOException;

    }

    @FunctionalInterface
    private interface IOExceptionFunction<T, R> extends Function<T, R> {

        @Override
        default R apply(T t) {
            try {
                return applyThrows(t);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        R applyThrows(T t) throws IOException;

    }

}
