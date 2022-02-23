package net.landofrails.landofsignals.gui;

import cam72cam.mod.text.TextUtil;
import net.landofrails.landofsignals.LandOfSignals;

public enum GuiText {

    //Signalbox
    LABEL_REDSTONE("signalbox.redstone"),
    LABEL_NOREDSTONE("signalbox.noredstone"),

    //ManipulatorOverlay
    LABEL_UNATTACH("manipulator.unattach"),
    LABEL_EDITINGAME("manipulator.editingame"),
    LABEL_EDITROTATION("manipulator.editrotation"),
    LABEL_EDITHITBOX("manipulator.edithitbox"),
    LABEL_ROTATIONSLIDER("manipulator.rotationslider"),
    LABEL_EDITPOSITION("manipulator.editposition");

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
