package net.landofrails.stellwand.utils.compact;

import javax.annotation.Nullable;

import cam72cam.mod.entity.Player;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.stellwand.content.messages.EMessage;
import scala.actors.threadpool.Arrays;

public class LoSPlayer extends Player {

	public LoSPlayer(Player player) {
		super(player.internal);
	}

	public void direct(String message) {
		this.sendMessage(PlayerMessage.direct(message));
	}

	public void direct(String message, String... args) {

		for (int index = 0; index < args.length; index++) {
			EMessage eMessage = getEMsg(args[index]);
			if (eMessage != null) {
				args[index] = eMessage.toString();
			}
		}

		EMessage eMessage = getEMsg(message);
		String msg;

		if (eMessage != null) {
			Object[] objects = Arrays.copyOf(args, args.length);
			msg = args.length > 0 ? eMessage.toString(objects) : eMessage.toString();
		} else {
			msg = message;
		}
		this.sendMessage(PlayerMessage.direct(msg));
		
	}

	@Nullable
	public EMessage getEMsg(String msg) {
		try {
			return EMessage.valueOf(msg);
		} catch (Exception e) {
			try {
				return EMessage.fromRaw(msg);
			} catch (Exception e2) {
				return null;
			}
		}
	}

}
