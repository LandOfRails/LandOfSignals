package net.landofrails.stellwand.content.network;

import cam72cam.mod.entity.Player;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.stellwand.content.messages.EMessage;
import net.landofrails.stellwand.utils.compact.LoSPlayer;
import net.landofrails.stellwand.utils.mapper.EMessageTagMapper;
import net.landofrails.stellwand.utils.mapper.StringArrayTagMapper;

public class ServerMessagePacket extends Packet {

	@TagField(value = "message", typeHint = EMessage.class, mapper = EMessageTagMapper.class)
	private EMessage message;

	@TagField(value = "arguments", typeHint = String[].class, mapper = StringArrayTagMapper.class)
	private String[] arguments;
	
	@TagField()
	private Player player;

	public ServerMessagePacket() {

	}

	public ServerMessagePacket(Player player, EMessage message) {
		this.player = player;
		this.message = message;
		this.arguments = new String[0];
	}

	public ServerMessagePacket(Player player, EMessage message, String[] arguments) {
		this.player = player;
		this.message = message;
		this.arguments = arguments;
	}


	@Override
	protected void handle() {
		if (player.getUUID().equals(getPlayer().getUUID())) {
			LoSPlayer lp = new LoSPlayer(getPlayer());
			lp.direct(message.name(), arguments);
		} else {
			player.sendMessage(PlayerMessage.direct("Something went wrong... i think"));
		}
	}

	// Calling other send() method
	public static void send(Player player, EMessage message) {
		send(player, message, new String[0]);
	}

	public static void send(Player player, EMessage message, String... args) {
		Packet p = new ServerMessagePacket(player, message, args);
		p.sendToAllAround(player.getWorld(), player.getPosition(), 1.0f);
	}

}
