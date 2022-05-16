package net.landofrails.landofsignals.gui;

import cam72cam.mod.text.TextUtil;
import net.landofrails.landofsignals.LandOfSignals;

public enum GuiText {

    //Signalbox
    LABEL_REDSTONE("signalbox.redstone"),
    LABEL_NOREDSTONE("signalbox.noredstone"),
    LABEL_SIGNALGROUP("signalbox.signalgroup"),

    //ManipulatorOverlay
    LABEL_UNATTACH("manipulator.unattach"),
    LABEL_EDITINGAME("manipulator.editingame"),
    LABEL_EDITROTATION("manipulator.editrotation"),
    LABEL_EDITHITBOX("manipulator.edithitbox"),
    LABEL_ROTATIONSLIDER("manipulator.rotationslider"),
    LABEL_EDITPOSITION("manipulator.editposition"),

    // LegacyModeGui
    LABEL_LEGACYMODE_OFF("legacymode.off.label"),
    LABEL_LEGACYMODE_OFF_DESCRIPTION("legacymode.off.description"),
    LABEL_LEGACYMODE_ON("legacymode.on.label"),
    LABEL_LEGACYMODE_ON_DESCRIPTION("legacymode.on.description");

    private final String value;

    GuiText(String value) {
        this.value = value;
    }

    public String getRaw() {
        return "gui." + LandOfSignals.MODID + ":" + value;
    }

    @Override
    public String toString() {
        return TextUtil.translate(getRaw());
    }

    public String toString(Object... objects) {
        return TextUtil.translate(getRaw(), objects);
    }
}
