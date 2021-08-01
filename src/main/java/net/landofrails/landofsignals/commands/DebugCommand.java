package net.landofrails.landofsignals.commands;

import cam72cam.mod.entity.Player;
import cam72cam.mod.text.Command;
import cam72cam.mod.text.PlayerMessage;

import java.util.Optional;
import java.util.function.Consumer;

public class DebugCommand extends Command {
    @Override
    public String getPrefix() {
        return "los-debug";
    }

    @Override
    public String getUsage() {
        return "Usage: /los-debug <SOON>";
    }

    @Override
    public boolean execute(Consumer<PlayerMessage> sender, Optional<Player> player, String[] args) {
        if (args.length != 1) return false;
        if (args[0].equalsIgnoreCase("changing")) {
            return true;
        }
        return false;
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return PermissionLevel.LEVEL4;
    }
}
