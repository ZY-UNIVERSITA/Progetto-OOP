package com.zysn.passwordmanager.model.utils.data;

import java.util.Arrays;

/**
 * Utility class for data operations.
 */
public class DataUtils {

    /**
     * Concatenates two character arrays.
     *
     * @param firstData  the first character array
     * @param secondData the second character array
     * @return a new character array containing the concatenated data
     * @throws IllegalArgumentException if either input array is null
     */
    public static char[] concatArray(final char[] firstData, final char[] secondData) {
        if (firstData == null || secondData == null) {
            throw new IllegalArgumentException("Password or salt cannot be null.");
        }

        final char[] concatData = Arrays.copyOf(firstData, firstData.length + secondData.length);
        System.arraycopy(secondData, 0, concatData, firstData.length, secondData.length);

        return concatData;
    }

    /**
     * Concatenates two byte arrays.
     *
     * @param firstData  the first byte array
     * @param secondData the second byte array
     * @return a new byte array containing the concatenated data
     * @throws IllegalArgumentException if either input array is null
     */
    public static byte[] concatArray(final byte[] firstData, final byte[] secondData) {
        if (firstData == null || secondData == null) {
            throw new IllegalArgumentException("Password or salt cannot be null.");
        }

        final byte[] concatData = Arrays.copyOf(firstData, firstData.length + secondData.length);
        System.arraycopy(secondData, 0, concatData, firstData.length, secondData.length);

        return concatData;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private DataUtils() {
        // Prevent instantiation
    }
}