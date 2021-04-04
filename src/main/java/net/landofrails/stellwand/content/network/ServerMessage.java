package net.landofrails.stellwand.content.network;

import cam72cam.mod.entity.Player;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.content.messages.EMessage;
import net.landofrails.stellwand.utils.compact.LoSPlayer;

public class ServerMessage extends Packet {

	@TagField("message")
	private EMessage message;

	@TagField("arguments")
	private String[] args;

	public ServerMessage() {

	}

	public ServerMessage(EMessage message) {
		this.message = message;
		this.args = new String[0];
	}

	public ServerMessage(EMessage message, String... args) {
		this.message = message;
		this.args = args;
	}

	@Override
	protected void handle() {
		LoSPlayer l = new LoSPlayer(getPlayer());
		l.direct(message.getRaw(), args);
	}

	public static void send(Player player, EMessage message) {
		send(player, message, new String[0]);
	}

	public static void send(Player player, EMessage message, String... args) {
		ServerMessage msg = new ServerMessage(EMessage.MESSAGE_NO_SIGNALS_CONNECTED, args);
		msg.sendToObserving(player);
	}

}
