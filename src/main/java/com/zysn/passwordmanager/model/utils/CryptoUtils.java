package com.zysn.passwordmanager.model.utils;

import java.security.SecureRandom;

import org.bouncycastle.util.Arrays;

public class CryptoUtils {
    private CryptoUtils() {

    }

    public static char[] generatePassword(int length, boolean useSpecialChar, boolean useNumbers, boolean useUpperCase,
            boolean useLowerCase) {
        return null;
    }

    /**
     * Generates a random salt of the specified length.
     *
     * @param length the length of the salt
     * @return a byte array containing the generated salt
     * @throws IllegalArgumentException if the length is 0 or less
     */
    public static byte[] generateSalt(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("The length of the salt cannot be 0 or less.");
        }

        byte[] salt = new byte[length];

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        return salt;
    }

    /**
     * Clears the contents of the specified char array by setting each element to
     * zero.
     *
     * @param source the char array to be cleared
     * @throws IllegalArgumentException if the source array is null
     */
    public static void cleanMemory(char[] source) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }

        Arrays.fill(source, '\u0000');
    }

}
