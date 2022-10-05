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
    public boolean execute(final Consumer<PlayerMessage> sender, final Optional<Player> player, final String[] args) {
        if (args.length != 1) return false;
        return args[0].equalsIgnoreCase("changing");
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return PermissionLevel.LEVEL4;
    }
}
