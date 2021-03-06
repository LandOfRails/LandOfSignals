package net.landofrails.stellwand.utils.compact;

import java.text.MessageFormat;

import cam72cam.mod.entity.Player;
import cam72cam.mod.text.PlayerMessage;

public class LoSPlayer extends Player {

	public LoSPlayer(Player player) {
		super(player.internal);
	}

	public void direct(String message) {
		this.sendMessage(PlayerMessage.direct(message));
	}

	public void direct(String message, Object... args) {
		direct(MessageFormat.format(message, args));
	}

}
