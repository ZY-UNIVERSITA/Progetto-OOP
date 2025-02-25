package com.zysn.passwordmanager.model.utils.data;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class DataUtilsTest {

    @Test
    void testConcatArrayByte() {
        byte[] array1 = new byte[] { 1, 2, 3 };
        byte[] array2 = new byte[] { 4, 5, 6 };

        byte[] expectedArray = new byte[] { 1, 2, 3, 4, 5, 6 };
        byte[] actualArray = DataUtils.concatArray(array1, array2);

        assertArrayEquals(expectedArray, actualArray, "The concatenated byte array should be [1, 2, 3, 4, 5, 6]");
    }

    @Test
    void testConcatArrayChar() {
        char[] array1 = "abc".toCharArray();
        char[] array2 = "def".toCharArray();

        char[] expectedArray = "abcdef".toCharArray();
        char[] actualArray = DataUtils.concatArray(array1, array2);

        assertArrayEquals(expectedArray, actualArray, "The concatenated char array should be 'abcdef'");
    }
}

