package com.neo.parkguidance.core.impl;

public class StringUtils {

    private StringUtils() {}

    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
