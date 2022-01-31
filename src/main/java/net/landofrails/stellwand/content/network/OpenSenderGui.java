package net.landofrails.stellwand.content.network;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.guis.CustomGuis;
import net.landofrails.stellwand.utils.compact.SignalContainer;
import net.landofrails.stellwand.utils.mapper.SignalContainerMapper;

public class OpenSenderGui extends Packet {

    @TagField("senderPos")
    private Vec3i senderPos;

    @TagField(value = "signalTile", mapper = SignalContainerMapper.class)
    private SignalContainer<BlockEntity> signalTile;

    public OpenSenderGui() {

    }

    public OpenSenderGui(Vec3i senderPos, SignalContainer<BlockEntity> signalTile) {
        this.senderPos = senderPos;
        this.signalTile = signalTile;
    }

    @Override
    public void sendToPlayer(Player player) {
        if (signalTile == null) {
            player.sendMessage(PlayerMessage.direct("SignalContainer is empty! This is a technical error."));
        }
        super.sendToPlayer(player);
    }

    @Override
    protected void handle() {

        Stellwand.info("Opening SelectSenderModesGui");

        if (signalTile != null) {
            BlockSenderStorageEntity sender = getWorld().getBlockEntity(senderPos, BlockSenderStorageEntity.class);
            sender.setSignal(signalTile);
            CustomGuis.selectSenderModes.open(getPlayer(), senderPos);
        } else {
            getPlayer().sendMessage(PlayerMessage.direct("No signalentity transmitted. This seems to be a technical error."));
        }

    }

}
