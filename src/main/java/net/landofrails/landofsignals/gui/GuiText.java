package net.landofrails.landofsignals.gui;

import cam72cam.mod.text.TextUtil;
import net.landofrails.landofsignals.LandOfSignals;

public enum GuiText {

    //Signalbox
    LABEL_REDSTONE("signalbox.redstone"),
    LABEL_NOREDSTONE("signalbox.noredstone"),
    LABEL_SIGNALGROUP("signalbox.signalgroup"),

    //Priority
    LABEL_PAGE("priority.page"),
    LABEL_GROUP("priority.group"),
    LABEL_PRIORITY("priority.priority"),
    LABEL_LOW("priority.low"),
    LABEL_HIGH("priority.high"),

    //ManipulatorOverlay
    LABEL_UNATTACH("manipulator.unattach"),
    LABEL_EDITINGAME("manipulator.editingame"),
    LABEL_EDITROTATION("manipulator.editrotation"),
    LABEL_ROTATIONSLIDER("manipulator.rotationslider"),
    LABEL_EDITPOSITION("manipulator.editposition"),
    LABEL_EDITSCALING("manipulator.editscaling"),
    LABEL_CASCADE("manipulator.cascade"),
    LABEL_LEFTCLICK("manipulator.leftclick"),
    LABEL_RIGHTCLICK("manipulator.rightclick");

    private final String value;

    GuiText(final String value) {
        this.value = value;
    }

    public String getRaw() {
        return "gui." + LandOfSignals.MODID + ":" + value;
    }

    @Override
    public String toString() {
        return TextUtil.translate(getRaw());
    }

    public String toString(final Object... objects) {
        return TextUtil.translate(getRaw(), objects);
    }
}
