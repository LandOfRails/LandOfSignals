package net.landofrails.landofsignals.creator.utils;

import net.landofrails.api.contentpacks.v2.ContentPack;

import java.io.*;
import java.util.Enumeration;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ContentPackZipHandler {

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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return Optional.of(new ContentPackZipHandler(target));
    }

    private static Optional<File> tryFindContentPack(String packId) {

        final File assetFolder = new File("./config/landofsignals");
        final File[] contentpacks = assetFolder.listFiles();

        if (contentpacks == null)
            return Optional.empty();

        for (File contentPackFile : contentpacks) {
            try (ZipFile zipFile = new ZipFile(contentPackFile)) {

                Enumeration<? extends ZipEntry> entries = zipFile.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (entry.getName().endsWith("landofsignals.json")) {
                        ObjectInputStream stream = new ObjectInputStream(zipFile.getInputStream(entry));
                        ContentPack contentPack = (ContentPack) stream.readObject();
                        if (contentPack.getId().equalsIgnoreCase(packId)) {
                            return Optional.of(contentPackFile);
                        }
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();

    }

    // Creates contentpacks
    // Loads contentpacks
    // Modifys contentpacks


}
