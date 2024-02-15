package net.landofrails.landofsignals.packet;

import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.api.contentpacks.GenericContentPack;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.configs.LandOfSignalsConfig;
import net.landofrails.landofsignals.contentpacks.ContentPackHandler;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

public class CommandClientPacket extends Packet {

    @TagField(typeHint = Command.class)
    private Command cmd;

    public CommandClientPacket() {
    }

    public CommandClientPacket(Command cmd) {
        this.cmd = cmd;
    }

    @Override
    protected void handle() {
        switch (cmd){
            case CONTENTPACKS:
                // Finish with #150
                break;
            case DEBUG:
                debug();
                break;
            case FOLDER:
                folder();
                break;
            case CONFIG:
                LOSGuis.CONFIG.open(getPlayer());
                break;
        }
    }

    private void debug() {
        Consumer<String> send = msg -> getPlayer().sendMessage(PlayerMessage.direct(msg));
        send.accept("---");
        send.accept("Client report:");
        send.accept("Version: " + LandOfSignals.VERSION);
        send.accept("Settings: ");
        for(Map.Entry<String, Object> conf : LandOfSignalsConfig.values().entrySet())
            send.accept(" - " + conf.getKey() + ": " + conf.getValue().toString());
        send.accept("Detected contentpacks: ");
        for(Map.Entry<GenericContentPack, Map.Entry<Boolean, String>> gcp : ContentPackHandler.getContentpackHeaders().entrySet()) {
            send.accept(" - " + gcp.getKey().toShortString());
            send.accept("   * UTF8: " + gcp.getValue().getKey() + "; Status: " + gcp.getValue().getValue());
        }
    }

    private void folder(){

        final File assetFolder = new File("./config/landofsignals");
        String assetUrl = "";
        try {
            assetUrl = assetFolder.getCanonicalPath();
        } catch (IOException e) {
            LandOfSignals.error("Couldn't find config folder! Something went horribly wrong here.", e);
            return;
        }

        boolean success = true;
        if (!assetFolder.exists()) {
            success = assetFolder.mkdirs();
        }

        if(!success){
            getPlayer().sendMessage(PlayerMessage.direct("Couldn't create folder! Target:"));
            getPlayer().sendMessage(PlayerMessage.url(assetUrl));
            return;
        }

        if(!Desktop.isDesktopSupported()){
            getPlayer().sendMessage(PlayerMessage.direct("Can't open the folder, but you can find it here:"));
            getPlayer().sendMessage(PlayerMessage.url(assetUrl));

            StringSelection stringSelection = new StringSelection(assetUrl);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            getPlayer().sendMessage(PlayerMessage.direct("Copied the link to your clipboard"));

            return;
        }

        try {
            Desktop.getDesktop().open(assetFolder);
        } catch (IOException e) {
            LandOfSignals.error("Couldn't open config folder!", e);
            getPlayer().sendMessage(PlayerMessage.direct("Can't open the folder, but you can find it here:"));
            getPlayer().sendMessage(PlayerMessage.url(assetUrl));

            StringSelection stringSelection = new StringSelection(assetUrl);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            getPlayer().sendMessage(PlayerMessage.direct("Copied the link to your clipboard"));
        }
    }

    public enum Command {
        DEBUG, CONFIG, FOLDER, CONTENTPACKS;
    }

}
