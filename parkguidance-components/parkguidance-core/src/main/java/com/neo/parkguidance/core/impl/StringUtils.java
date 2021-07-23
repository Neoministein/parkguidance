package com.neo.parkguidance.core.impl;

/**
 * Utilities for {@link String}
 */
public class StringUtils {

    private StringUtils() {}

    /**
     * Checks if string is null or empty
     *
     * @param s string to check
     * @return true if it's null or empty
     */
    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
