package net.landofrails.stellwand.utils.compact;

import cam72cam.mod.entity.Player;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.stellwand.content.messages.EMessage;

import javax.annotation.Nullable;
import java.util.Arrays;

public class LoSPlayer extends Player {

    private Player player;

    public LoSPlayer(Player player) {
        super(player.internal);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void direct(String message) {
        this.sendMessage(PlayerMessage.direct(message));
    }

    public void direct(String message, String... args) {

        if (args != null) {
            for (int index = 0; index < args.length; index++) {
                EMessage eMessage = getEMsg(args[index]);
                if (eMessage != null) {
                    args[index] = eMessage.toString();
                }
            }
        } else {
            args = new String[0];
        }

        EMessage eMessage = getEMsg(message);
        String msg;

        if (eMessage != null) {
            Object[] objects = Arrays.copyOf(args, args.length);
            msg = args.length > 0 ? eMessage.toString(objects) : eMessage.toString();
        } else {
            msg = message;
        }
        this.direct(msg);

    }

    @Nullable
    public EMessage getEMsg(String msg) {
        try {
            return EMessage.valueOf(msg);
        } catch (IllegalArgumentException e) {
            try {
                return EMessage.fromRaw(msg);
            } catch (Exception e2) {
                return null;
            }
        }
    }

}
