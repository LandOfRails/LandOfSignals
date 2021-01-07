package net.landofrails.landofsignals.gui;

import cam72cam.mod.text.TextUtil;
import net.landofrails.landofsignals.LandOfSignals;

public enum GuiText {
    LABEL_REDSTONE("signalbox.redstone"),
    LABEL_NOREDSTONE("signalbox.noredstone");
    private String value;

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
