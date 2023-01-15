package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.api.contentpacks.v2.EntryType;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.creator.utils.ContentPackZipHandler;

import java.util.function.Supplier;

public class GuiSelectType implements IScreen {

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_SELECT_TYPE;
    private static ContentPackZipHandler contentPackZipHandler;

    @Override
    public void init(IScreenBuilder screen) {

        new Button(screen, -100, 40, 100, 20, "Signals") {
            @Override
            public void onClick(Player.Hand hand) {
                GuiTypeOverview.open(contentPackZipHandler, EntryType.BLOCKSIGNAL);
            }
        };

        new Button(screen, -100, 60, 100, 20, "Signalboxes") {
            @Override
            public void onClick(Player.Hand hand) {
                MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Not implemented yet."));
            }
        };

        new Button(screen, 0, 40, 100, 20, "Assets") {
            @Override
            public void onClick(Player.Hand hand) {
                MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Not implemented yet."));
            }
        };

        new Button(screen, 0, 60, 100, 20, "Signs") {
            @Override
            public void onClick(Player.Hand hand) {
                MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Not implemented yet."));
            }
        };
    }

    @Override
    public void draw(IScreenBuilder builder) {
        builder.drawCenteredString("Modifying ...", 0, 20, 0xFFFFFFFF);
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        // Nothing to do
    }

    @Override
    public void onClose() {
        // Nothing to do
    }

    public static void open(ContentPackZipHandler contentPackZipHandler) {
        GuiSelectType.contentPackZipHandler = contentPackZipHandler;
        GUI.get().open(MinecraftClient.getPlayer());
    }

}
