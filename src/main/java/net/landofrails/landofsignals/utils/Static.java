package net.landofrails.landofsignals.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Static {

    private Static() {

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //    public static Map<String, BlockSignalPart> blockSignalPartList = new HashMap<>();
//    public static List<ItemSignalPart> itemSignalPartList = new ArrayList<>();
    public static final String MISSING = "missing";

}
