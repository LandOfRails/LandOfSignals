package net.landofrails.landofsignals.creator.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ContentPackZipHandler {

    private File contentPackZipFile = null;

    private ContentPackZipHandler(File contentPackZipFile) {
        this.contentPackZipFile = contentPackZipFile;
    }

    public static Optional<ContentPackZipHandler> getInstanceOrCreate(String packName) {
        Optional<ContentPackZipHandler> cpzh = getInstance(packName);
        if (!cpzh.isPresent())
            cpzh = Optional.of(create(packName));
        return cpzh;
    }

    public static Optional<ContentPackZipHandler> getInstance(String packName) {
        return Optional.empty();
    }

    private static ContentPackZipHandler create(String packName) {

        final File assetFolder = new File("./config/landofsignals");
        final File target = new File(assetFolder, packName + ".zip");

        if (!target.exists()) {
            // Create empty ZIP
            // Create assets/landofsignals folders
            try {
                try (ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)))) {
                    zipStream.putNextEntry(new ZipEntry("ignore.me"));
                    zipStream.putNextEntry(new ZipEntry("assets/"));
                    zipStream.putNextEntry(new ZipEntry("assets/landofsignals/"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Create ignore file

        return new ContentPackZipHandler(target);
    }

    // Creates contentpacks
    // Loads contentpacks
    // Modifys contentpacks


}
