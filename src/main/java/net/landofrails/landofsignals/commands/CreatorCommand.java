package net.landofrails.landofsignals.commands;

import cam72cam.mod.entity.Player;
import cam72cam.mod.text.Command;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.landofsignals.creator.gui.Test;

import java.util.Optional;
import java.util.function.Consumer;

public class CreatorCommand extends Command {

    private static final String LANDOFSIGNALS_WIKI = "https://github.com/LandOfRails/LandOfSignals/wiki";

    @Override
    public String getPrefix() {
        return "los-create";
    }

    @Override
    public String getUsage() {
        return "Usage: /los-create <signal/signalbox/sign/deco>";
    }

    /*
    private String name; Pflicht
    private String id; Pflicht
    private Float rotationSteps;
    private String creativeTab;
    // objPath : objProperties
    private Map<String, ContentPackModel[]> base;
    // groupId : group
    private Map<String, ContentPackSignalGroup> signals; Pflicht
    // groupId : state
    private Map<String, String> itemGroupStates;
    private ContentPackReferences references;
     */

    @Override
    public boolean execute(Consumer<PlayerMessage> sender, Optional<Player> player, String[] args) {
        if (args.length != 1) {
            return false;
        }
        Test.open(player.get());

//        if (!player.isPresent()) {
//            sender.accept(PlayerMessage.direct("Only the player is allowed to enter this command!"));
//            return true;
//        }
//
//        Player realPlayer = player.get();
//
//        switch (args[0].toUpperCase()) {
//            case "SIGNAL":
//                GuiNameId.open(realPlayer, EntryType.BLOCKSIGNAL);
//                break;
//            case "SIGNALBOX":
//                sender.accept(PlayerMessage.direct("Signalbox is currently not available."));
//                break;
//            case "SIGN":
//                sender.accept(PlayerMessage.direct("Sign is currently not available."));
//                break;
//            case "ASSET":
//            case "DECO":
//                sender.accept(PlayerMessage.direct("Deco is currently not available."));
//                break;
//            case "HELP":
//            case "WIKI":
//            case "DOCU":
//            case "DOCUMENTATION":
//                sender.accept(PlayerMessage.direct("You can find more information in our github wiki:"));
//                sender.accept(PlayerMessage.url(LANDOFSIGNALS_WIKI));
//                break;
//            default:
//                return false;
//        }
//
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return PermissionLevel.NONE;
    }
}
