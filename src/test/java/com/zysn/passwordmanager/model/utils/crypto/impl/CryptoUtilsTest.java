package com.zysn.passwordmanager.model.utils.crypto.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;

public class CryptoUtilsTest {
    private char[] stringInArrayOfChar;
    private byte[] stringInArrayOfByte;

    @BeforeEach
    void setup() {
        this.stringInArrayOfChar = "ABCDE012".toCharArray();
        stringInArrayOfByte = new byte[] { 65, 66, 67, 68, 69, 48, 49, 50 };
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
    void testGenerateSalt() {
        int length = 32;
        byte[] salt1 = CryptoUtils.generateSalt(length);

        assertEquals(length, salt1.length, "The lenght of the array is not 32 but " + salt1.length);

        byte[] salt2 = CryptoUtils.generateSalt(length);

        assertFalse(Arrays.equals(salt1, salt2), "The 2 salt are equals.");
    }

    @Test
    void testGenerateSecureRandomNotUsablePassword() {
        int minimumLength = 63;
        char[] actualPassword = CryptoUtils.generateSecureRandomNotUsablePassword(minimumLength);

        assertNotNull(actualPassword, "The password is not null.");

        assertTrue(actualPassword.length >= minimumLength, "The password is shorter than the minimum length.");
    }
}
