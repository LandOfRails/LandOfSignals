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
    LABEL_LEGACYMODE_ON_DESCRIPTION("legacymode.on.description"),

    //CREATOR
    LABEL_CREATOR_NAME("creator.name"),
    LABEL_CREATOR_ID("creator.id"),
    LABEL_CREATOR_CONFIRM("creator.confirm"),
    LABEL_CREATOR_CREATE("creator.create"),
    LABEL_CREATOR_LOADOBJ("creator.loadobj"),
    LABEL_CREATOR_CREATESTATE("creator.createstate"),
    LABEL_CREATOR_TEXTURE("creator.texture"),
    LABEL_CREATOR_STATENAME("creator.statename"),
    LABEL_CREATOR_ADD("creator.add"),
    LABEL_CREATOR_REMOVE("creator.remove");

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
