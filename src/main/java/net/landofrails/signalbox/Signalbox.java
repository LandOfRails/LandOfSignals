package net.landofrails.signalbox;

import cam72cam.mod.ModEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Signalbox {

    public static void commonEvent(ModEvent event) {

        File signalBoxFolder = new File("./config/signalbox");
        if (!signalBoxFolder.exists()) {
            signalBoxFolder.mkdirs();
        }
        try {
            PrintWriter latestRelease = new PrintWriter("./config/signalbox/latestRelease", "UTF-8");
            latestRelease.println();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        switch (event) {

        }

    }

}
