package com.neo.parkguidance.framework.impl.utils;

/**
 * This class is a provides math utilities for the parkguidance components
 *
 */
public class MathUtils {

    private MathUtils() {}

    public static boolean isInBounds(Integer val, int min, int max) {
        if (val == null) {
            return false;
        }
        return isInBounds(val, min, max);
    }

    public static boolean isInBounds(int val, int min, int max) {
        return val >= min && val <= max;
    }

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static long clamp(long val, long min, long max) {
        return Math.max(min, Math.min(max, val));
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static Long parseLong(String value) {
        try {
            return Long.parseLong(value);
        }catch (NumberFormatException e) {
            return null;
        }
    }

    public static boolean isZero(Integer integer) {
        return integer == null || integer == 0;
    }
}
