package net.landofrails.landofsignals.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.math.Vec3i;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.configs.LegacyMode;
import net.landofrails.landofsignals.packet.legacymode.LegacyModePromptBlockPacket;

import java.util.function.Supplier;

public class GuiLegacyModeSelection implements IScreen {

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.LEGACYMODE_SELECTOR;
    private static Vec3i blockPos;
    private static String itemId;
    private static Integer blockRotation;
    //
    private LegacyMode legacyMode = null;

    @Override
    public void init(IScreenBuilder screen) {
        new Button(screen, -100, 0, GuiText.LABEL_LEGACYMODE_ON.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                legacyMode = LegacyMode.ON;
                screen.close();
            }
        };

        new Button(screen, -100, 20, GuiText.LABEL_LEGACYMODE_ON_DESCRIPTION.toString()) {
            @Override
            public void onClick(Player.Hand hand) {

            }
        }.setEnabled(false);

        new Button(screen, -100, 50, GuiText.LABEL_LEGACYMODE_OFF.toString().toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                legacyMode = LegacyMode.OFF;
                screen.close();
            }
        };


        new Button(screen, -100, 70, GuiText.LABEL_LEGACYMODE_OFF_DESCRIPTION.toString()) {
            @Override
            public void onClick(Player.Hand hand) {

            }
        }.setEnabled(false);
    }

    @Override
    public void onEnterKey(IScreenBuilder iScreenBuilder) {

    }

    @Override
    public void onClose() {
        if (legacyMode == null) {
            GUI.get().open(MinecraftClient.getPlayer());
        } else {
            new LegacyModePromptBlockPacket(blockPos, itemId, blockRotation, legacyMode).sendToServer();
        }
    }

    @Override
    public void draw(IScreenBuilder screen) {
    }

    public static void open(Player player, Vec3i blockPos, String itemId, Integer blockRotation) {
        GuiLegacyModeSelection.blockPos = blockPos;
        GuiLegacyModeSelection.itemId = itemId;
        GuiLegacyModeSelection.blockRotation = blockRotation;
        GUI.get().open(player);
    }

}
