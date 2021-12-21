package com.neo.parkguidance.web.user.impl.utils;

public class ColorGenerator {

    //CSS STYLE
    public static final String COLOR_EMPTY = "#32CD32"; //LimeGreen
    public static final String COLOR_MEDIUM = "#FFA500"; //Orange
    public static final String COLOR_FULL = "#FF0000"; //Red

    public static final String BLIND_EMPTY = "#32CD32"; //LimeGreen
    public static final String BLIND_MEDIUM = "#FFACAC"; //Pink
    public static final String BLIND_FULL = "#D81B60"; //Maroon

    private ColorGenerator() {}

    public static String getStyleColor(int max, int amount, boolean colorBlind) {
        return getStyleColor(100d * amount / max, colorBlind);
    }

    public static String getStyleColor(double percent, boolean colorBlind) {
        return "color: " + getColor(percent, colorBlind) + ";";
    }

    public static String getColor(int max, int amount, boolean colorBlind) {
        return getColor( 100d * amount / max, colorBlind);
    }

    public static String getColor(double percent, boolean colorBlind) {
        if (!colorBlind) {
            if (percent > 20) {
                return COLOR_EMPTY;
            }

            if (percent > 5) {
                return COLOR_MEDIUM;
            }
            return COLOR_FULL;
        } else {
            if (percent > 20) {
                return BLIND_EMPTY;
            }
            if (percent > 5) {
                return BLIND_MEDIUM;
            }
            return BLIND_FULL;
        }
    }
}
