package com.zysn.passwordmanager.model.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CryptoUtilsTest {
    private static final String CHARSET_NAME = "UTF-8";

    private char[] stringInArrayOfChar;
    private byte[] stringInArrayOfByte;

    @BeforeEach
    void setup() {
        this.stringInArrayOfChar = "ABCDE012".toCharArray();
        stringInArrayOfByte = new byte[] { 65, 66, 67, 68, 69, 48, 49, 50 };
    }

    @Test
    void testCharToByteConverter() {
        byte[] arrayOfCharIntoArrayOfByte = CryptoUtils.charToByteConverter(this.stringInArrayOfChar, CHARSET_NAME);

        assertArrayEquals(stringInArrayOfByte, arrayOfCharIntoArrayOfByte, "The 2 array of byte are not the same.");
    }

    @Test
    void testCharToByteConverterWithoutCharsetName() {
        byte[] arrayOfCharIntoArrayOfByte = CryptoUtils.charToByteConverter(this.stringInArrayOfChar);

        assertArrayEquals(stringInArrayOfByte, arrayOfCharIntoArrayOfByte, "The 2 array of byte are not the same.");
    }

    @Test
    void testCleanMemoryOfArrayOfChar() {
        CryptoUtils.cleanMemory(stringInArrayOfChar);

        char[] charArrayToCompare = new char[stringInArrayOfChar.length];

        for (int i = 0; i < charArrayToCompare.length; i++) {
            charArrayToCompare[i] = '\u0000';
        }

        assertArrayEquals(charArrayToCompare, this.stringInArrayOfChar,
                "The method didn't fill the array with NULL character.");
    }

    @Test
    void testCleanMemoryOfArrayOfByte() {
        CryptoUtils.cleanMemory(stringInArrayOfByte);

        byte[] byteArrayToCompare = new byte[stringInArrayOfByte.length];

        for (int i = 0; i < byteArrayToCompare.length; i++) {
            byteArrayToCompare[i] = (byte) 0;
        }

        assertArrayEquals(byteArrayToCompare, this.stringInArrayOfByte, "The method didn't fill the array with 0.");
    }

    @Test
    void testGeneratePassword() {

    }

    @Test
    void testGenerateSalt() {
        int length = 32;
        byte[] salt1 = CryptoUtils.generateSalt(length);

        assertEquals(length, salt1.length, "The lenght of the array is not 32 but " + salt1.length);

        byte[] salt2 = CryptoUtils.generateSalt(length);

        assertFalse(Arrays.equals(salt1, salt2), "The 2 salt are equals.");
    }
}
