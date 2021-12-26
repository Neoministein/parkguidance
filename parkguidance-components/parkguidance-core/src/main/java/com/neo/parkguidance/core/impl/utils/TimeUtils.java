package com.neo.parkguidance.core.impl.utils;

import java.util.Date;

public class TimeUtils {

    private TimeUtils() {}

    /**
     * Checks if the date is after the other
     *
     * @param before the date that should be before
     * @param after the date that should be after
     * @return true if the date statement is correct
     */
    public static boolean after(Date before, Date after) {
        if (before == null || after == null) {
            return false;
        }

        return  (after.after(before));
    }
}
