package com.neo.parkguidance.ms.user.impl.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public class CredentialsUtils {

    public static final int HASH_LENGTH = 128;

    public static final String HASH_TYPE = MessageDigestAlgorithms.SHA3_512;

    private CredentialsUtils() {}

    /**
     * Hashes the string and returns it, if it's isn't the of a hash.
     *
     * @param password the password to hash
     * @param salt the salt of the hash
     * @return the hashed string
     */
    public static String hashPassword(String password, String salt) {
        if (password.length() != HASH_LENGTH) {
            return new DigestUtils(HASH_TYPE).digestAsHex((salt + password).getBytes());
        }
        return password;
    }
}
