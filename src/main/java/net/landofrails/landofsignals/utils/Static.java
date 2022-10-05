package net.landofrails.landofsignals.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Static {

    private Static() {

    }

    public static double round(final double value, final int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Missing ID
    public static final String MISSING = "missing";
    // Missing ID Name
    public static final String MISSING_NAME = "Missing! Check your content packs";
    // Missing OBJ
    public static final String MISSING_OBJ = "models/block/others/blocknotfound/blocknotfound.obj";
    // Missing attributes text
    public static final String MISSING_ATTRIBUTES = "There are missing attributes: %s";
    // Default SignalBox ID (without packId)
    public static final String SIGNALBOX_DEFAULT = "signalbox_default";
    // Default fully qualified SignalBox ID (including packId)
    public static final String SIGNALBOX_DEFAULT_FQ = "LandOfSignals@LandOfSignals:signalbox_default";

}
