package net.landofrails.stellwand.content.network;

import cam72cam.mod.entity.Player;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
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
        LoSPlayer lp = new LoSPlayer(getPlayer());
        lp.direct(message.name(), arguments);
    }

    // Calling other send() method
    public static void send(Player player, EMessage message) {
        String[] emptyArray = new String[0];
        send(player, message, emptyArray);
    }

    public static void send(Player player, EMessage message, String... args) {
        Packet p = new ServerMessagePacket(player, message, args);
        p.sendToPlayer(player);
    }

}
