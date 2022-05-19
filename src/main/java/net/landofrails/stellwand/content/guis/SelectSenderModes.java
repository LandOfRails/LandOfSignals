package net.landofrails.stellwand.content.guis;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3i;
import net.landofrails.landofsignals.gui.GuiText;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.messages.EMessage;
import net.landofrails.stellwand.content.network.ChangeSenderModes;
import net.landofrails.stellwand.utils.compact.SignalContainer;
import util.Matrix4;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings("java:S1192")
public class SelectSenderModes implements IScreen {

    // List of modes
    private Map<String, Map<String, String>> modes;
    private LinkedList<String> modeGroups;

    // Group
    private String signalGroup;

    // Position for packet
    private Vec3i pos;

    // Display
    private final ItemStack itemPowerOff;
    private final ItemStack itemPowerOn;
    private Button groupIdButton;

    @SuppressWarnings({"java:S112", "java:S1192"})
    public SelectSenderModes(BlockSenderStorageEntity entity) {

        if (entity.getSignal() != null) {

            SignalContainer<BlockEntity> signalContainer = entity.getSignal();
            if (signalContainer != null) {
                modes = signalContainer.getPossibleModes();
                modeGroups = signalContainer.getModeGroups();

                signalGroup = entity.signalGroup != null ? entity.signalGroup : getFirstValue(modes.keySet());

                // @formatter:off

				// Item Power off
				itemPowerOff = new ItemStack(signalContainer.getCustomItem(), 1);
				itemPowerOff.getTagCompound().setString("itemId", signalContainer.getContentPackBlockId());
				String customModePowerOff = entity.modePowerOff != null ? entity.modePowerOff : getFirstValue(modes.get(signalGroup).values());
                setItemMode(itemPowerOff, customModePowerOff);

				// Item Power on
				itemPowerOn = new ItemStack(signalContainer.getCustomItem(), 1);
				itemPowerOn.getTagCompound().setString("itemId", signalContainer.getContentPackBlockId());
				String customModePowerOn = entity.modePowerOn != null ? entity.modePowerOn : getFirstValue(modes.get(signalGroup).values());
				setItemMode(itemPowerOn, customModePowerOn);

				// @formatter:on

                // Position
                pos = entity.getPos();

                return;
            } else {
                ModCore.error("Couldnt get signal: %s", "Signal doesn't exist (anymore)");
            }
        } else {
            ModCore.error("Sender has no signals!");
        }
        throw new RuntimeException("Entity does not contain signals.");

    }

    private void setItemMode(ItemStack item, String customMode) {
        item.getTagCompound().setString("customMode", customMode);
    }

    @Override
    public void init(IScreenBuilder screen) {
        new Button(screen, -100, 40,
                "<-- " + GuiText.LABEL_NOREDSTONE) {
            @Override
            public void onClick(Player.Hand hand) {
                String mode = itemPowerOff.getTagCompound()
                        .getString("customMode");
                itemPowerOff.getTagCompound().setString("customMode",
                        nextMode(mode));
            }
        };
        new Button(screen, -100, 80,
                GuiText.LABEL_REDSTONE + " -->") {
            @Override
            public void onClick(Player.Hand hand) {
                String mode = itemPowerOn.getTagCompound()
                        .getString("customMode");
                itemPowerOn.getTagCompound().setString("customMode",
                        nextMode(mode));
            }
        };
        groupIdButton = new Button(screen, -100, 0,
                EMessage.GUI_STELLWAND_SELECTSENDERMODES_GROUP.toString(signalGroup != null ? signalGroup : "-")) {
            @Override
            public void onClick(Player.Hand hand) {
                boolean useNext = false;
                for (String iterationMode : modeGroups) {
                    if (useNext) {
                        setNewSignalGroup(iterationMode);
                        return;
                    } else if ((iterationMode == null && signalGroup == null) || (iterationMode != null && iterationMode.contentEquals(signalGroup))) {
                        useNext = true;
                    }
                }
                setNewSignalGroup(getFirstValue(modes.keySet()));
            }
        };
        groupIdButton.setEnabled(signalGroup != null);
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        builder.close();
    }

    @Override
    public void onClose() {
        String modePowerOff = itemPowerOff.getTagCompound()
                .getString("customMode");
        String modePowerOn = itemPowerOn.getTagCompound()
                .getString("customMode");
        ChangeSenderModes packet = new ChangeSenderModes(pos, signalGroup, modePowerOff,
                modePowerOn);
        packet.sendToServer();
    }

    @Override
    public void draw(IScreenBuilder builder) {
        drawBlocks(builder);
        drawGroupMenu(builder);
    }

    private void drawBlocks(IScreenBuilder builder) {
        int scale = 8;
        Matrix4 matrix = new Matrix4();
        matrix.translate(
                (double) GUIHelpers.getScreenWidth() / 2
                        + (double) builder.getWidth() / 4,
                (double) builder.getHeight() / 4, 0);
        matrix.scale(scale, scale, 1);
        GUIHelpers.drawItem(itemPowerOn, 0, 0);

        matrix = new Matrix4();
        matrix.translate(
                ((double) GUIHelpers.getScreenWidth() / 2
                        - (double) builder.getWidth() / 4) - 120,
                (double) builder.getHeight() / 4, 0);
        matrix.scale(scale, scale, 1);
        GUIHelpers.drawItem(itemPowerOff, 0, 0);

    }

    @SuppressWarnings("java:S2259")
    private void drawGroupMenu(IScreenBuilder builder) {
        groupIdButton.setText(EMessage.GUI_STELLWAND_SELECTSENDERMODES_GROUP.toString(signalGroup != null ? signalGroup : "-"));
    }

    private synchronized void setNewSignalGroup(String newMode) {
        signalGroup = newMode;
        if (modes != null && modes.size() > 1 && modes.get(signalGroup) != null) {
            setItemMode(itemPowerOff, getFirstValue(modes.get(signalGroup).values()));
            setItemMode(itemPowerOn, getFirstValue(modes.get(signalGroup).values()));
        }
    }

    private String nextMode(String mode) {
        boolean useNext = false;
        for (String m : modes.get(signalGroup).values()) {
            if (m.equalsIgnoreCase(mode))
                useNext = true;
            else if (useNext)
                return m;
        }
        return getFirstValue(modes.get(signalGroup).values());
    }

    @SuppressWarnings("java:S1751")
    private static <T> T getFirstValue(Collection<T> collection) {
        for (T object : collection)
            return object;
        return null;
    }

}
