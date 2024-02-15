package net.landofrails.landofsignals.commands;

import cam72cam.mod.entity.Player;
import cam72cam.mod.text.Command;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.api.contentpacks.GenericContentPack;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.configs.LandOfSignalsConfig;
import net.landofrails.landofsignals.contentpacks.ContentPackHandler;
import net.landofrails.landofsignals.packet.CommandClientPacket;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class LandOfSignalsCommand extends Command {
    @Override
    public String getPrefix() {
        return "landofsignals";
    }

    @Override
    public String getUsage() {
        return "Usage: /landofsignals help";
    }

    /**
     *
     * landofsignals command (will only be executed server-side)
     *
     * @param sender
     * @param player
     * @param args
     * @return
     */
    @Override
    public boolean execute(final Consumer<PlayerMessage> sender, final Optional<Player> player, final String[] args) {

        String arg0 = args.length > 0 ? args[0].toUpperCase() : "";

        // HELP
        // DEBUG
        // LOS-CONFIG
        // CONFIGFOLDER

        switch (arg0){
            case "HELP":
            case "?":
                return help(sender);
            case "DEBUGINFO":
            case "DEBUG":
                return debug(sender, player);
            case "LOS-CONFIG":
            case "CONFIG":
                return losConfig(sender, player);
            case "CONFIGFOLDER":
            case "FOLDER":
                return configFolder(sender, player);
            default:
                return false;
        }
    }


    private boolean help(final Consumer<PlayerMessage> sender){
        Consumer<String> send = msg -> sender.accept(PlayerMessage.direct(msg));
        String text = "      ->- Help for /landofsignals (page 1/1) -<-" +
                "\n/landofsignals help : Shows the available commands" +
                "\n/landofsignals debug : Shows debug information for client and/or server" +
                "\n/landofsignals los-config : Opens config GUI (needs a restart to take effect)" +
                "\n/landofsignals configfolder : Opens/Returns the config folder (where contentpacks can be placed in)" +
                "\n/landofsignals contentpacks [load <contentpack] : >> NOT AVAILABLE YET << Loads contentpacks while ingame (only client-side)" +
                "\n      ->- Help for /landofsignals (page 1/1) -<-";
        for(String line : text.split("\\n"))
            send.accept(line);
        return true;
    }

    private boolean debug(final Consumer<PlayerMessage> sender, final Optional<Player> player) {

        Consumer<String> send = msg -> sender.accept(PlayerMessage.direct(msg));

        // Server
        send.accept("Server report:");
        send.accept("Version: " + LandOfSignals.VERSION);
        send.accept("Settings: ");
        for(Map.Entry<String, Object> conf : LandOfSignalsConfig.values().entrySet())
            send.accept(" - " + conf.getKey() + ": " + conf.getValue().toString());
        send.accept("Detected contentpacks: ");
        for(Map.Entry<GenericContentPack, Map.Entry<Boolean, String>> gcp : ContentPackHandler.getContentpackHeaders().entrySet()) {
            send.accept(" - " + gcp.getKey().toShortString());
            send.accept("   * UTF8: " + gcp.getValue().getKey() + "; Status: " + gcp.getValue().getValue());
        }

        // Client
        player.ifPresent(value -> new CommandClientPacket(CommandClientPacket.Command.DEBUG).sendToPlayer(value));

        return false;
    }

    private boolean losConfig(final Consumer<PlayerMessage> sender, final Optional<Player> optionalPlayer) {
        if(optionalPlayer.isPresent()){
            new CommandClientPacket(CommandClientPacket.Command.CONFIG).sendToPlayer(optionalPlayer.get());
        } else {
            sender.accept(PlayerMessage.direct("This command is only available for players!"));
        }
        return true;
    }

    private boolean configFolder(final Consumer<PlayerMessage> sender, final Optional<Player> optionalPlayer) {

        if(optionalPlayer.isPresent()){
            new CommandClientPacket(CommandClientPacket.Command.FOLDER).sendToPlayer(optionalPlayer.get());
            return true;
        }

        final File assetFolder = new File("./config/landofsignals");
        final String assetUrl = "file://" + assetFolder.getAbsolutePath();

        boolean success = true;
        if (!assetFolder.exists()) {
            success = assetFolder.mkdirs();
        }

        if(!success){
            sender.accept(PlayerMessage.direct("Couldn't create folder! Target:"));
            sender.accept(PlayerMessage.url(assetUrl));
            return true;
        }

        sender.accept(PlayerMessage.direct("Can't open the folder, but you can find it here:"));
        sender.accept(PlayerMessage.url(assetUrl));

        return true;
    }





}