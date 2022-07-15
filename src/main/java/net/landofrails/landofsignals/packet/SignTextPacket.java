package net.landofrails.landofsignals.packet;

import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.gui.GuiSignTextEditor;
import net.landofrails.landofsignals.serialization.EmptyStringMapper;
import net.landofrails.landofsignals.tile.TileSignPart;

public class SignTextPacket extends Packet {

    @TagField(typeHint = SignTextPhase.class)
    private SignTextPhase phase;
    @TagField
    private Vec3i signPos;
    @TagField(mapper = EmptyStringMapper.class)
    private String signText;

    public SignTextPacket() {

    }

    private SignTextPacket(SignTextPhase phase, Vec3i signPos, String signText) {
        this.phase = phase;
        this.signPos = signPos;
        this.signText = signText;
    }

    @Override
    protected void handle() {

        if (phase == SignTextPhase.OPENTEXTBOX) {
            GuiSignTextEditor.setInfo(signText, signPos);
            LOSGuis.SIGN_TEXTEDITOR.open(getPlayer());
        } else if (phase == SignTextPhase.SENDTEXTTOSERVER) {
            TileSignPart signPart = getWorld().getBlockEntity(signPos, TileSignPart.class);
            if (signPart != null) {
                signPart.setText(signText);
                sendTextToClients(signPos, signText);
            }
        } else if (phase == SignTextPhase.SENDTEXTTOCLIENTS) {
            TileSignPart signPart = getWorld().getBlockEntity(signPos, TileSignPart.class);
            if (signPart != null) {
                signPart.setText(signText);
            }
        }

    }

    public static void openTextBox(Player player, Vec3i signPos, String signText) {
        new SignTextPacket(SignTextPhase.OPENTEXTBOX, signPos, signText).sendToPlayer(player);
    }

    public static void sendTextToServer(Vec3i signPos, String signText) {
        new SignTextPacket(SignTextPhase.SENDTEXTTOSERVER, signPos, signText).sendToServer();
    }

    public static void sendTextToClients(Vec3i signPos, String signText) {
        new SignTextPacket(SignTextPhase.SENDTEXTTOSERVER, signPos, signText).sendToAll();
    }

    private enum SignTextPhase {
        OPENTEXTBOX, SENDTEXTTOSERVER, SENDTEXTTOCLIENTS;
    }


}
