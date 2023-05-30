package net.landofrails.landofsignals.gui;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.screen.*;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import net.landofrails.landofsignals.packet.ManipulatorToClientPacket;
import net.landofrails.landofsignals.packet.ManipulatorToServerPacket;
import net.landofrails.landofsignals.utils.IManipulate;
import net.landofrails.landofsignals.utils.Static;

import java.util.function.Predicate;

public class GuiManipualtor implements IScreen {

    private CheckBox positionBox;
    private CheckBox heightBox;
    private CheckBox rotationBox;

    private Slider rotationSlider;
    private Button rotationAddition;
    private Button rotationSubtraction;

    private TextField positionXField;
    private Button positionXAddition;
    private Button positionXSubtraction;
    private TextField positionZField;
    private Button positionZAddition;
    private Button positionZSubtraction;
    private TextField heightYField;
    private Button heightYAddition;
    private Button heightYSubtraction;

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
        rotation = manipulate.getRotation() % 360;
        if (rotation < 0)
            rotation += 360;
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
                positionXAddition.setVisible(true);
                positionXSubtraction.setVisible(true);
                positionZField.setVisible(true);
                positionZAddition.setVisible(true);
                positionZSubtraction.setVisible(true);
            }
        };
        heightBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 110, GuiText.LABEL_EDITPOSITION + " (Y)", false) {
            @Override
            public void onClick(final Player.Hand hand) {
                uncheckOtherBoxes(this);
                setAllInvisible();
                heightYField.setVisible(true);
                heightYAddition.setVisible(true);
                heightYSubtraction.setVisible(true);
            }
        };
        rotationBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 130, GuiText.LABEL_EDITROTATION.toString(), false) {
            @Override
            public void onClick(final Player.Hand hand) {
                uncheckOtherBoxes(this);
                setAllInvisible();
                rotationSlider.setVisible(true);
                rotationAddition.setVisible(true);
                rotationSubtraction.setVisible(true);
            }
        };

        renewSlider(screen);
        rotationSubtraction = new Button(screen,screen.getWidth() / 2 - 220, 100, 20, 20, "-") {
            @Override
            public void onClick(Player.Hand hand) {
                rotation--;
                renewSlider(screen);
                send();
            }
        };
        rotationAddition = new Button(screen, screen.getWidth() / 2 - 50, 100, 20, 20, "+") {
            @Override
            public void onClick(Player.Hand hand) {
                rotation++;
                renewSlider(screen);
                send();
            }
        };

        positionXField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 120, 40, 20);
        positionXAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 120, 20, 20, "+") {
            @Override
            public void onClick(Player.Hand hand) {
                double positionX = Static.round(Double.parseDouble(positionXField.getText()), 3);
                positionX += getModifier(hand);
                positionXField.setText(String.valueOf(positionX));
            }
        };
        positionXSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 120, 20, 20, "-") {
            @Override
            public void onClick(Player.Hand hand) {
                double positionX = Static.round(Double.parseDouble(positionXField.getText()), 3);
                positionX -= getModifier(hand);
                positionXField.setText(String.valueOf(positionX));
            }
        };

        positionZField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 100, 40, 20);
        positionZAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 100, 20, 20, "+") {
            @Override
            public void onClick(Player.Hand hand) {
                double positionZ = Static.round(Double.parseDouble(positionZField.getText()), 3);
                positionZ += getModifier(hand);
                positionZField.setText(String.valueOf(positionZ));
            }
        };
        positionZSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 100, 20, 20, "-") {
            @Override
            public void onClick(Player.Hand hand) {
                double positionZ = Static.round(Double.parseDouble(positionZField.getText()), 3);
                positionZ -= getModifier(hand);
                positionZField.setText(String.valueOf(positionZ));
            }
        };

        heightYField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 120, 40, 20);
        heightYAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 120, 20, 20, "+") {
            @Override
            public void onClick(Player.Hand hand) {
                double heightY = Static.round(Double.parseDouble(heightYField.getText()), 3);
                heightY += getModifier(hand);
                heightYField.setText(String.valueOf(heightY));
            }
        };
        heightYSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 120, 20, 20, "-") {
            @Override
            public void onClick(Player.Hand hand) {
                double heightY = Static.round(Double.parseDouble(heightYField.getText()), 3);
                heightY -= getModifier(hand);
                heightYField.setText(String.valueOf(heightY));
            }
        };

        positionXField.setValidator(doubleFilter);
        positionZField.setValidator(doubleFilter);
        heightYField.setValidator(doubleFilter);

        setAllInvisible();
        positionXField.setVisible(true);
        positionXAddition.setVisible(true);
        positionXSubtraction.setVisible(true);
        positionZField.setVisible(true);
        positionZAddition.setVisible(true);
        positionZSubtraction.setVisible(true);

        positionXField.setText(String.valueOf(offset.x));
        positionZField.setText(String.valueOf(offset.z));
        heightYField.setText(String.valueOf(offset.y));

    }

    @Override
    public void onEnterKey(final IScreenBuilder builder) {
        builder.close();
    }

    @SuppressWarnings("java:S125")
    @Override
    public void onClose() {
        send();
        //Server
        ManipulatorToServerPacket serverPacket = new ManipulatorToServerPacket(new Vec3d(Double.parseDouble(positionXField.getText()), Double.parseDouble(heightYField.getText()), Double.parseDouble(positionZField.getText())), rotation, blockPos);
        serverPacket.sendToServer();
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
        final ManipulatorToClientPacket clientPacket = new ManipulatorToClientPacket(new Vec3d(Double.parseDouble(positionXField.getText()), Double.parseDouble(heightYField.getText()), Double.parseDouble(positionZField.getText())), rotation, blockPos, false);
        clientPacket.sendToAll();
    }

    private void setAllInvisible() {
        positionXField.setVisible(false);
        positionXAddition.setVisible(false);
        positionXSubtraction.setVisible(false);
        positionZField.setVisible(false);
        positionZAddition.setVisible(false);
        positionZSubtraction.setVisible(false);
        heightYField.setVisible(false);
        heightYAddition.setVisible(false);
        heightYSubtraction.setVisible(false);
        rotationSlider.setVisible(false);
        rotationAddition.setVisible(false);
        rotationSubtraction.setVisible(false);
    }

    private void uncheckOtherBoxes(final CheckBox box) {
        positionBox.setChecked(false);
        heightBox.setChecked(false);
        rotationBox.setChecked(false);
        box.setChecked(true);
    }

    private void renewSlider(IScreenBuilder screen){
        if(rotationSlider != null){
            rotationSlider.setVisible(false);
            rotationSlider = null;
        }
        rotationSlider = new Slider(screen, screen.getWidth() / 2 - 200, 100, GuiText.LABEL_ROTATIONSLIDER + ": ", 0, 360, rotation, false) {
            @Override
            public void onSlider() {
                rotation = rotationSlider.getValueInt();
                send();
            }
        };
    }
    private double getModifier(Player.Hand hand){
        return hand == Player.Hand.PRIMARY ? 1.0 : 0.1;
    }

}
