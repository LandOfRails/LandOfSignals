package net.landofrails.landofsignals.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.screen.*;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.items.ItemManipulator;
import net.landofrails.landofsignals.packet.ManipulatorToClientPacket;
import net.landofrails.landofsignals.utils.IManipulate;
import net.landofrails.landofsignals.utils.Static;

import java.util.function.Predicate;

public class GuiManipualtor implements IScreen {

    private CheckBox positionBox;
    private CheckBox heightBox;
    private CheckBox rotationBox;
    private CheckBox hitboxBox;

    private Button ingameButton;

    private Slider rotationSlider;

    private TextField positionXField;
    private TextField positionZField;
    private TextField heightYField;

    private String textXBefore;
    private String textYBefore;
    private String textZBefore;

    private final Vec3d offset;
    private int rotation;
    private final Vec3i blockPos;

    private final Predicate<String> doubleFilter = inputString -> {
        if (inputString == null || inputString.length() == 0) {
            return true;
        }
        try {
            Double.parseDouble(inputString);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    };

    public GuiManipualtor(final BlockEntity be) {
        final IManipulate manipulate = (IManipulate) be;
        offset = manipulate.getOffset();
        rotation = manipulate.getRotation();
        blockPos = be.getPos();
    }

    @Override
    public void init(final IScreenBuilder screen) {
        positionBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 90, GuiText.LABEL_EDITPOSITION + " (X, Z)", true) {
            @Override
            public void onClick(final Player.Hand hand) {
                uncheckOtherBoxes(this);
                setAllInvisible();
                positionXField.setVisible(true);
                positionZField.setVisible(true);
                ingameButton.setVisible(true);
                ItemManipulator.editHeight = false;
            }
        };
        heightBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 110, GuiText.LABEL_EDITPOSITION + " (Y)", false) {
            @Override
            public void onClick(final Player.Hand hand) {
                uncheckOtherBoxes(this);
                setAllInvisible();
                ingameButton.setVisible(true);
                heightYField.setVisible(true);
                ItemManipulator.editHeight = true;
            }
        };
        rotationBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 130, GuiText.LABEL_EDITROTATION.toString(), false) {
            @Override
            public void onClick(final Player.Hand hand) {
                uncheckOtherBoxes(this);
                setAllInvisible();
                rotationSlider.setVisible(true);
            }
        };
        hitboxBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 150, GuiText.LABEL_EDITHITBOX.toString(), false) {
            @Override
            public void onClick(final Player.Hand hand) {
                uncheckOtherBoxes(this);
                setAllInvisible();
            }
        };

        ingameButton = new Button(screen, screen.getWidth() / 2 - 120, 90, 100, 20, GuiText.LABEL_EDITINGAME.toString()) {
            @Override
            public void onClick(final Player.Hand hand) {
                ItemManipulator.editIngame = true;
                LOSItems.ITEM_MANIPULATOR.setPlayerMainPos(MinecraftClient.getPlayer().getPosition());
                screen.close();
            }
        };

        rotationSlider = new Slider(screen, screen.getWidth() / 2 - 170, 90, GuiText.LABEL_ROTATIONSLIDER + " : " + rotation, 0, 360, rotation, false) {
            @Override
            public void onSlider() {
                rotation = rotationSlider.getValueInt();
                rotationSlider.setText(GuiText.LABEL_ROTATIONSLIDER + " : " + rotation);
                send();
            }
        };

        positionXField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 120, 100, 20);
        positionZField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 100, 100, 20);
        heightYField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 120, 100, 20);

        positionXField.setValidator(doubleFilter);
        positionZField.setValidator(doubleFilter);
        heightYField.setValidator(doubleFilter);

        setAllInvisible();
        positionXField.setVisible(true);
        positionZField.setVisible(true);
        ingameButton.setVisible(true);

        positionXField.setText(String.valueOf(offset.x));
        positionZField.setText(String.valueOf(offset.z));
        heightYField.setText(String.valueOf(offset.y));

        ItemManipulator.editHeight = false;

    }

    @Override
    public void onEnterKey(final IScreenBuilder builder) {
        LOSItems.ITEM_MANIPULATOR.clearBlock();
        builder.close();
    }

    @SuppressWarnings("java:S125")
    @Override
    public void onClose() {
        send();
        //Server
//        ManipulatorToServerPacket serverPacket = new ManipulatorToServerPacket(new Vec3d(Double.parseDouble(positionXField.getText()), Double.parseDouble(heightYField.getText()), Double.parseDouble(positionZField.getText())), rotation, blockPos);
//        serverPacket.sendToServer();
    }

    @Override
    public void draw(final IScreenBuilder builder) {

        final String tempX = positionXField.getText();
        final String tempY = heightYField.getText();
        final String tempZ = positionZField.getText();
        if (!positionXField.getText().isEmpty() && !tempX.equals(textXBefore)) {
            positionXField.setText(String.valueOf(Static.round(Double.parseDouble(tempX), 3)));
            textXBefore = tempX;
            send();
        }

        if (!heightYField.getText().isEmpty() && !tempY.equals(textYBefore)) {
            heightYField.setText(String.valueOf(Static.round(Double.parseDouble(tempY), 3)));
            textYBefore = tempY;
            send();
        }

        if (!positionZField.getText().isEmpty() && !tempZ.equals(textZBefore)) {
            positionZField.setText(String.valueOf(Static.round(Double.parseDouble(tempZ), 3)));
            textZBefore = tempZ;
            send();
        }

    }

    private void send() {
        //Client
        final ManipulatorToClientPacket clientPacket = new ManipulatorToClientPacket(new Vec3d(Double.parseDouble(positionXField.getText()), Double.parseDouble(heightYField.getText()), Double.parseDouble(positionZField.getText())), rotation, blockPos, ItemManipulator.sneak);
        clientPacket.sendToAll();
    }

    private void setAllInvisible() {
        positionXField.setVisible(false);
        positionZField.setVisible(false);
        heightYField.setVisible(false);
        ingameButton.setVisible(false);
        rotationSlider.setVisible(false);
    }

    private void uncheckOtherBoxes(final CheckBox box) {
        positionBox.setChecked(false);
        heightBox.setChecked(false);
        rotationBox.setChecked(false);
        hitboxBox.setChecked(false);
        box.setChecked(true);
    }
}
