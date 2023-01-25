package net.landofrails.landofsignals.commands;

import cam72cam.mod.entity.Player;
import cam72cam.mod.text.Command;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.landofsignals.packet.ConfigGuiPacket;

import java.util.Optional;
import java.util.function.Consumer;

public class ConfigCommand extends Command {
    @Override
    public String getPrefix() {
        return "los-config";
    }

    @Override
    public String getUsage() {
        return "Usage: /los-config";
    }

    @Override
    public boolean execute(final Consumer<PlayerMessage> sender, final Optional<Player> player, final String[] args) {
        new ConfigGuiPacket().sendToPlayer(player.get());
        return true;
    }
}
