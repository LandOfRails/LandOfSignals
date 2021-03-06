package net.landofrails.landofsignals.commands;

import cam72cam.mod.text.Command;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.utils.Static;

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
    public boolean opRequired() {
        return true;
    }

    @Override
    public boolean execute(World world, Consumer<PlayerMessage> sender, String[] args) {
        if (args.length != 1) return false;
        if (args[0].equals("changing")) {
            Static.changingSignalPartList.forEach((key, value) -> sender.accept(PlayerMessage.direct(key + " : " + value)));
            return true;
        }
        return false;
    }
}
