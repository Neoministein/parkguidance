package com.neo.parkguidance.framework.impl.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utilities for {@link String}
 */
public class StringUtils {

    public static final int HASH_LENGTH = 128;
    public static final String HASH_TYPE = MessageDigestAlgorithms.SHA3_512;

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

    /**
     * Hashes the string and returns it, if it's isn't the of a hash.
     *
     * @param s the string to hash
     * @return the hashed string
     */
    public static String hashString(String s) {
        if (s.length() != HASH_LENGTH) {
            return new DigestUtils(HASH_TYPE).digestAsHex(s.getBytes());
        }
        return s;
    }

    /**
     * Returns empty string if object is null otherwise the received object
     * @param o object to check
     * @return an empty string if object is null
     */
    public static Object parseToEmptyString(Object o) {
        return (o == null) ? "" : o;
    }

    /**
     * Returns a list of strings which are separated by comma
     * @param s the string to split
     * @return list of strings
     */
    public static List<String> commaSeparatedStrToList(String s) {
        return characterSeparatedStrToList(s, ',');
    }

    /**
     * Returns a list of strings which are separated by semicolon
     * @param s the string to list
     * @return list of strings
     */
    public static List<String> semicolonSeparatedStrToList(String s) {
        return characterSeparatedStrToList(s, ';');
    }

    /**
     * Returns a list of string which are separated by a char
     * @param s the string to split
     * @param c the char to split the string by
     * @return list of strings
     */
    private static List<String> characterSeparatedStrToList(String s, char c) {
        if (s == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(s.trim().split("\\s*" + c + "\\s*"));
    }

}
