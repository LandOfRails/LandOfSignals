package net.landofrails.signalbox;

import cam72cam.mod.ModEvent;
import com.google.gson.Gson;
import net.landofrails.signalbox.github.Asset;
import net.landofrails.signalbox.github.GitHubRelease;
import net.landofrails.signalbox.socket.SocketHandler;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Signalbox {

    public static void commonEvent(ModEvent event) {

        File signalBoxFolder = new File("./config/signalbox");
        if (!signalBoxFolder.exists()) {
            signalBoxFolder.mkdirs();
            GitHubRelease release = getGitHubLatestRelease();
            downloadNewSignalbox(release);
        } else {
            String localReleaseString;
            try (BufferedReader br = new BufferedReader(new FileReader("./config/signalbox/latestRelease"))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }
                localReleaseString = sb.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            GitHubRelease release = getGitHubLatestRelease();

            LocalDateTime localRelease = LocalDateTime.parse(localReleaseString.replace("Z", ""));
            LocalDateTime remoteRelease = LocalDateTime.parse(release.getPublishedAt().replace("Z", ""));

            if (remoteRelease.isAfter(localRelease)) {
                //delete all files in signalbox folder
                File[] files = signalBoxFolder.listFiles();
                for (File file : files) {
                    file.delete();
                }
                downloadNewSignalbox(release);
            }
        }

        try {
            SocketHandler.startServer();
//            Runtime.getRuntime().exec("");
            ProcessBuilder pb = new ProcessBuilder("./config/signalbox/LandOfSignals-Signalbox.exe"/*, "--config", "./config/signalbox/signalbox.json"*/);
            pb.directory(new File("./config/signalbox"));
            pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (event) {

        }

    }

    private static void downloadNewSignalbox(GitHubRelease release) {
        createLatestReleaseFile(release);
        Asset signalboxAsset = release.getAssets().stream().filter(asset -> asset.getName().equals("signalbox.zip")).findFirst().get();
        downloadFile(signalboxAsset.getBrowserDownloadUrl(), "./config/signalbox/signalbox.zip");
        unzipFile(new File("./config/signalbox/signalbox.zip"), new File("./config/signalbox"));
        new File("./config/signalbox/signalbox.zip").delete();
    }

    //Unzip file
    private static void unzipFile(File fileZip, File destDir) {
        try {
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    //Download file from URL
    private static void downloadFile(String url, String fileName) {
        try {
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createLatestReleaseFile(GitHubRelease release) {
        try {
            PrintWriter latestRelease = new PrintWriter("./config/signalbox/latestRelease", "UTF-8");
            latestRelease.println(release.getPublishedAt());
            latestRelease.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static GitHubRelease getGitHubLatestRelease() {
        try {
            URL url = new URL("https://api.github.com/repos/LandOfRails/LandOfSIgnals-Signalbox/releases/latest");
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return new Gson().fromJson(baos.toString(encoding), GitHubRelease.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
