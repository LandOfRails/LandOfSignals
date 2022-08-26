package net.landofrails.landofsignals.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.TextField;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.text.TextUtil;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.packet.SignTextPacket;

public class GuiSignTextEditor implements IScreen {

    private static final String TEXTEDITORNEEDSNEWVERSIONMESSAGEKEY = "message." + LandOfSignals.MODID + ":sign.texteditor.needs.new.umc.version";
    private static final String SAVEBUTTONLABELKEY = "label." + LandOfSignals.MODID + ":signtexteditor.savebutton";

    private static String prevText;
    private static Vec3i signPos;

    public GuiSignTextEditor() {

    }

    public static void setInfo(String prevTextPar, Vec3i signPosPar) {
        prevText = prevTextPar;
        signPos = signPosPar;
    }

    @Override
    public void init(IScreenBuilder screen) {
        TextField textField = new TextField(screen, -100, 25, 200, 20);
        textField.setText(prevText != null ? prevText : "");
        textField.setFocused(true);
        new Button(screen, -100, 55, 200, 20, TextUtil.translate(SAVEBUTTONLABELKEY)) {
            @Override
            public void onClick(Player.Hand hand) {

                // FIXME Remove with new umc version
                MinecraftClient.getPlayer().sendMessage(PlayerMessage.translate(TEXTEDITORNEEDSNEWVERSIONMESSAGEKEY));
                //
                SignTextPacket.sendTextToServer(signPos, textField.getText());
                screen.close();
            }
        };
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void draw(IScreenBuilder builder) {

    }
}
