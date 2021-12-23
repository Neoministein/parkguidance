package com.neo.parkguidance.core.impl.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
     * Encodes the string to be use in a url
     *
     * @param s the string to encode
     * @return UTF-8 url encoded string
     */
    public static String encodeUrl(String s) {
        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * Decodes the string
     *
     * @param s the string to decode
     * @return UTF-8 url decode string
     */
    public static String decodeUrl(String s) {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return "";
        }
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
