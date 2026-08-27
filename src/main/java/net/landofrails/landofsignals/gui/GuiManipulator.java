package net.landofrails.landofsignals.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.*;
import cam72cam.mod.input.Keyboard;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.packet.ManipulatorToServerPacket;
import net.landofrails.landofsignals.utils.IManipulate;
import net.landofrails.landofsignals.utils.Static;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class GuiManipulator implements IScreen {

    private CheckBox cascadeBox;
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

    private Vec3d offset;
    private Vec3d scaling;
    private int rotation;
    private final Vec3i blockPos;

    private final Predicate<String> doubleFilter = inputString -> {
        if (inputString == null || inputString.isEmpty()) {
            return true;
        }
        try {
            Double.parseDouble(inputString);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    };

    public GuiManipulator(final BlockEntity be) {
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

        cascadeBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 0, GuiText.LABEL_CASCADE.toString(), true, (_, _) -> {});
        cascadeBox.setChecked(false);

        positionBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 90, GuiText.LABEL_EDITPOSITION + " (X, Z)", true, (_, checkbox) -> {
                uncheckOtherBoxes(checkbox);
                setAllInvisible();
                positionXField.setVisible(true);
                positionXAddition.setVisible(true);
                positionXSubtraction.setVisible(true);
                positionZField.setVisible(true);
                positionZAddition.setVisible(true);
                positionZSubtraction.setVisible(true);
        });

        heightBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 110, GuiText.LABEL_EDITPOSITION + " (Y)", false, (_, checkbox) -> {
                uncheckOtherBoxes(checkbox);
                setAllInvisible();
                heightYField.setVisible(true);
                heightYAddition.setVisible(true);
                heightYSubtraction.setVisible(true);
        });

        rotationBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 130, GuiText.LABEL_EDITROTATION.toString(), false, (_, checkbox) -> {
                uncheckOtherBoxes(checkbox);
                setAllInvisible();
                rotationSlider.setVisible(true);
                rotationAddition.setVisible(true);
                rotationSubtraction.setVisible(true);
        });

        scalingBox = new CheckBox(screen, screen.getWidth() / 2 - screen.getWidth() + 50, 150, GuiText.LABEL_EDITSCALING.toString(), false, (_, checkbox) -> {
                uncheckOtherBoxes(checkbox);
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
        });

        new Button(screen, screen.getWidth() / 2 - screen.getWidth() + 50, -30, 140, 20, "Reset offset and scaling", (_, _) -> resetAll(screen));

        rotationSlider = new Slider(screen, screen.getWidth() / 2 - 200, 100, GuiText.LABEL_ROTATIONSLIDER + ": ", 0, 360, rotation, false, (_) -> {
                rotation = rotationSlider.getValueInt();
                updateClientBlock();
        });
        rotationSubtraction = new Button(screen,screen.getWidth() / 2 - 220, 100, 20, 20, "-", (_, _) -> {
                rotation--;
                rotationSlider.setValue(rotation);
                rotationSlider.setText(GuiText.LABEL_ROTATIONSLIDER + ": " + rotation);
                updateClientBlock();
        });
        rotationAddition = new Button(screen, screen.getWidth() / 2 - 50, 100, 20, 20, "+", (_, _) -> {
                rotation++;
                rotationSlider.setValue(rotation);
                rotationSlider.setText(GuiText.LABEL_ROTATIONSLIDER + ": " + rotation);
                updateClientBlock();
        });

        positionXField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 120, 40, 20);
        positionXAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 120, 20, 20, "+", (hand, _) -> addition(hand, positionXField));
        positionXSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 120, 20, 20, "-", (hand, _) -> subtraction(hand, positionXField));

        positionZField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 100, 40, 20);
        positionZAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 100, 20, 20, "+", (hand, _) -> addition(hand, positionZField));
        positionZSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 100, 20, 20, "-", (hand, _) -> subtraction(hand, positionZField));

        heightYField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 120, 40, 20);
        heightYAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 120, 20, 20, "+", (hand, _) -> addition(hand, heightYField));
        heightYSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 120, 20, 20, "-", (hand, _) -> subtraction(hand, heightYField));

        scalingXField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 120, 40, 20);
        scalingXAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 120, 20, 20, "+", (hand, _) -> addition(hand, scalingXField));
        scalingXSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 120, 20, 20, "-",  (hand, _) -> subtraction(hand,  scalingXField));

        scalingYField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 100, 40, 20);
        scalingYAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 100, 20, 20, "+",  (hand, _) -> addition(hand, scalingYField));
        scalingYSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 100, 20, 20, "-", (hand, _) -> subtraction(hand, scalingYField));

        scalingZField = new TextField(screen, screen.getWidth() / 2 - 120, screen.getHeight() / 2 - 80, 40, 20);
        scalingZAddition = new Button(screen, screen.getWidth() / 2 - 79, screen.getHeight() / 2 - 80, 20, 20, "+", (hand, _) -> addition(hand, scalingZField));
        scalingZSubtraction = new Button(screen, screen.getWidth() / 2 - 141, screen.getHeight() / 2 - 80, 20, 20, "-", (hand, _) -> subtraction(hand, scalingZField));

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
    public void onKeyType(IScreenBuilder builder, @Nullable Keyboard.KeyCode keyCode) {
        if (keyCode == Keyboard.KeyCode.NUMPADENTER || keyCode == Keyboard.KeyCode.RETURN) {
            builder.close();
        }
    }

    @SuppressWarnings("java:S125")
    @Override
    public void onClose() {
        refreshScalingAndOffset();

        ManipulatorToServerPacket serverPacket = new ManipulatorToServerPacket(
                blockPos,
                offset,
                rotation,
                scaling,
                cascadeBox.isChecked()
        );
        serverPacket.sendToServer();
    }

    @Override
    public void draw(final IScreenBuilder builder, final RenderState state) {
        final String tempX = positionXField.getText();
        final String tempY = heightYField.getText();
        final String tempZ = positionZField.getText();
        final String tempScalingX = scalingXField.getText();
        final String tempScalingY = scalingYField.getText();
        final String tempScalingZ = scalingZField.getText();

        if (!positionXField.getText().isEmpty() && !tempX.equals(textXBefore)) {
            positionXField.setText(String.valueOf(Static.round(Double.parseDouble(tempX), 3)));
            textXBefore = tempX;
            updateClientBlock();
        }

        if (!heightYField.getText().isEmpty() && !tempY.equals(textYBefore)) {
            heightYField.setText(String.valueOf(Static.round(Double.parseDouble(tempY), 3)));
            textYBefore = tempY;
            updateClientBlock();
        }

        if (!positionZField.getText().isEmpty() && !tempZ.equals(textZBefore)) {
            positionZField.setText(String.valueOf(Static.round(Double.parseDouble(tempZ), 3)));
            textZBefore = tempZ;
            updateClientBlock();
        }

        if(!scalingXField.getText().isEmpty() && !tempScalingX.equals(scalingXBefore)){
            scalingXField.setText(String.valueOf(Static.round(Double.parseDouble(tempScalingX), 3)));
            scalingXBefore = tempScalingX;
            updateClientBlock();
        }

        if(!scalingYField.getText().isEmpty() && !tempScalingY.equals(scalingYBefore)){
            scalingYField.setText(String.valueOf(Static.round(Double.parseDouble(tempScalingY), 3)));
            scalingYBefore = tempScalingY;
            updateClientBlock();
        }

        if(!scalingZField.getText().isEmpty() && !tempScalingZ.equals(scalingZBefore)){
            scalingZField.setText(String.valueOf(Static.round(Double.parseDouble(tempScalingZ), 3)));
            scalingZBefore = tempScalingZ;
            updateClientBlock();
        }

        if(!rotationBox.isChecked()){
            GUIHelpers.drawCenteredString(GuiText.LABEL_LEFTCLICK.toString(), builder.getWidth() / 2 + 155, builder.getHeight() / 2 - 80, 0xFFFFFF);
            GUIHelpers.drawCenteredString(GuiText.LABEL_RIGHTCLICK.toString(), builder.getWidth() / 2 + 155, builder.getHeight() / 2 - 70, 0xFFFFFF);
        }
    }

    private void refreshScalingAndOffset(){
        scaling = new Vec3d(
            Double.parseDouble(scalingXField.getText()),
            Double.parseDouble(scalingYField.getText()),
            Double.parseDouble(scalingZField.getText())
        );

        offset = new Vec3d(
            Double.parseDouble(positionXField.getText()),
            Double.parseDouble(heightYField.getText()),
            Double.parseDouble(positionZField.getText())
        );
    }

    private void updateClientBlock() {
        refreshScalingAndOffset();

        //Client
        World world = MinecraftClient.getPlayer().getWorld();
        IManipulate.applyChanges(world, blockPos, cascadeBox.isChecked(), offset, rotation, scaling);

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

    private double getModifier(Player.Hand hand){
        return hand == Player.Hand.PRIMARY ? 1.0 : 0.1;
    }

    private void resetAll(IScreenBuilder screen){

        scalingXField.setText("1");
        scalingYField.setText("1");
        scalingZField.setText("1");

        positionXField.setText("0");
        heightYField.setText("0");
        positionZField.setText("0");

        screen.close();
    }

    private void addition(Player.Hand hand, TextField textField){
        double value = Static.round(Double.parseDouble(textField.getText()), 3);
        value += getModifier(hand);
        textField.setText(String.valueOf(value));
    }

    private void subtraction(Player.Hand hand, TextField textField){
        double value = Static.round(Double.parseDouble(textField.getText()), 3);
        value -= getModifier(hand);
        textField.setText(String.valueOf(value));
    }

}
