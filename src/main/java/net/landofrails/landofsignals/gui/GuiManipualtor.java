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
    private CheckBox scalingBox;

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
    private TextField scalingXField;
    private Button scalingXAddition;
    private Button scalingXSubtraction;
    private TextField scalingYField;
    private Button scalingYAddition;
    private Button scalingYSubtraction;
    private TextField scalingZField;
    private Button scalingZAddition;
    private Button scalingZSubtraction;
    private String textXBefore;
    private String textYBefore;
    private String textZBefore;
    private String scalingXBefore;
    private String scalingYBefore;
    private String scalingZBefore;

    private final Vec3d offset;
    private Vec3d scaling;
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
        scaling = manipulate.getScaling();
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
        scalingBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 150, GuiText.LABEL_EDITSCALING.toString(), false) {
            @Override
            public void onClick(final Player.Hand hand) {
                uncheckOtherBoxes(this);
                setAllInvisible();
                scalingXField.setVisible(true);
                scalingXAddition.setVisible(true);
                scalingXSubtraction.setVisible(true);
                scalingYField.setVisible(true);
                scalingYAddition.setVisible(true);
                scalingYSubtraction.setVisible(true);
                scalingZField.setVisible(true);
                scalingZAddition.setVisible(true);
                scalingZSubtraction.setVisible(true);
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

        scalingXField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 120, 40, 20);
        scalingXAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 120, 20, 20, "+") {
            @Override
            public void onClick(Player.Hand hand) {
                double scalingX = Static.round(Double.parseDouble(scalingXField.getText()), 3);
                scalingX += getModifier(hand);
                scalingXField.setText(String.valueOf(scalingX));
            }
        };
        scalingXSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 120, 20, 20, "-") {
            @Override
            public void onClick(Player.Hand hand) {
                double scalingX = Static.round(Double.parseDouble(scalingXField.getText()), 3);
                scalingX -= getModifier(hand);
                scalingXField.setText(String.valueOf(scalingX));
            }
        };
        scalingYField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 100, 40, 20);
        scalingYAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 100, 20, 20, "+") {
            @Override
            public void onClick(Player.Hand hand) {
                double scalingY = Static.round(Double.parseDouble(scalingYField.getText()), 3);
                scalingY += getModifier(hand);
                scalingYField.setText(String.valueOf(scalingY));
            }
        };
        scalingYSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 100, 20, 20, "-") {
            @Override
            public void onClick(Player.Hand hand) {
                double scalingY = Static.round(Double.parseDouble(scalingYField.getText()), 3);
                scalingY -= getModifier(hand);
                scalingYField.setText(String.valueOf(scalingY));
            }
        };
        scalingZField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 80, 40, 20);
        scalingZAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 80, 20, 20, "+") {
            @Override
            public void onClick(Player.Hand hand) {
                double scalingZ = Static.round(Double.parseDouble(scalingZField.getText()), 3);
                scalingZ += getModifier(hand);
                scalingZField.setText(String.valueOf(scalingZ));
            }
        };
        scalingZSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 80, 20, 20, "-") {
            @Override
            public void onClick(Player.Hand hand) {
                double scalingZ = Static.round(Double.parseDouble(scalingZField.getText()), 3);
                scalingZ -= getModifier(hand);
                scalingZField.setText(String.valueOf(scalingZ));
            }
        };

        positionXField.setValidator(doubleFilter);
        positionZField.setValidator(doubleFilter);
        heightYField.setValidator(doubleFilter);
        scalingXField.setValidator(doubleFilter);
        scalingYField.setValidator(doubleFilter);
        scalingZField.setValidator(doubleFilter);

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
        scalingXField.setText(String.valueOf(scaling.x));
        scalingYField.setText(String.valueOf(scaling.y));
        scalingZField.setText(String.valueOf(scaling.z));

    }

    @Override
    public void onEnterKey(final IScreenBuilder builder) {
        builder.close();
    }

    @SuppressWarnings("java:S125")
    @Override
    public void onClose() {
        scaling = new Vec3d(
                Double.parseDouble(scalingXField.getText()),
                Double.parseDouble(scalingYField.getText()),
                Double.parseDouble(scalingZField.getText())
        );
        send();
        //Server
        ManipulatorToServerPacket serverPacket = new ManipulatorToServerPacket(
                new Vec3d(Double.parseDouble(positionXField.getText()), Double.parseDouble(heightYField.getText()), Double.parseDouble(positionZField.getText())),
                rotation,
                scaling,
                blockPos
        );
        serverPacket.sendToServer();
    }

    @Override
    public void draw(final IScreenBuilder builder) {
        final String tempX = positionXField.getText();
        final String tempY = heightYField.getText();
        final String tempZ = positionZField.getText();
        final String tempScalingX = scalingXField.getText();
        final String tempScalingY = scalingYField.getText();
        final String tempScalingZ = scalingZField.getText();

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

        if(!scalingXField.getText().isEmpty() && !tempScalingX.equals(scalingXBefore)){
            scalingXField.setText(String.valueOf(Static.round(Double.parseDouble(tempScalingX), 3)));
            scalingXBefore = tempScalingX;
            send();
        }

        if(!scalingYField.getText().isEmpty() && !tempScalingY.equals(scalingYBefore)){
            scalingYField.setText(String.valueOf(Static.round(Double.parseDouble(tempScalingY), 3)));
            scalingYBefore = tempScalingY;
            send();
        }

        if(!scalingZField.getText().isEmpty() && !tempScalingZ.equals(scalingZBefore)){
            scalingZField.setText(String.valueOf(Static.round(Double.parseDouble(tempScalingZ), 3)));
            scalingZBefore = tempScalingZ;
            send();
        }

    }

    private void send() {
        //Client
        scaling = new Vec3d(
                Double.parseDouble(scalingXField.getText()),
                Double.parseDouble(scalingYField.getText()),
                Double.parseDouble(scalingZField.getText())
        );

        final ManipulatorToClientPacket clientPacket = new ManipulatorToClientPacket(
                new Vec3d(
                        Double.parseDouble(positionXField.getText()),
                        Double.parseDouble(heightYField.getText()),
                        Double.parseDouble(positionZField.getText())),
                rotation,
                blockPos,
                scaling,
                false
        );
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
        scalingXField.setVisible(false);
        scalingXAddition.setVisible(false);
        scalingXSubtraction.setVisible(false);
        scalingYField.setVisible(false);
        scalingYAddition.setVisible(false);
        scalingYSubtraction.setVisible(false);
        scalingZField.setVisible(false);
        scalingZAddition.setVisible(false);
        scalingZSubtraction.setVisible(false);
    }

    private void uncheckOtherBoxes(final CheckBox box) {
        positionBox.setChecked(false);
        heightBox.setChecked(false);
        rotationBox.setChecked(false);
        scalingBox.setChecked(false);
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
