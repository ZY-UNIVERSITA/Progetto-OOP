package com.zysn.passwordmanager.model.utils.crypto;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.utils.encoding.EncodingClassForTesting;
import com.zysn.passwordmanager.model.utils.security.impl.PasswordGenerator;

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

        final char[] charArrayToCompare = new char[stringInArrayOfChar.length];

        for (int i = 0; i < charArrayToCompare.length; i++) {
            charArrayToCompare[i] = '\u0000';
        }

        assertArrayEquals(charArrayToCompare, this.stringInArrayOfChar,
                "The method didn't fill the array with NULL character.");
    }

    @Test
    void testCleanMemoryOfArrayOfByte() {
        CryptoUtils.cleanMemory(stringInArrayOfByte);

        final byte[] byteArrayToCompare = new byte[stringInArrayOfByte.length];

        for (int i = 0; i < byteArrayToCompare.length; i++) {
            byteArrayToCompare[i] = (byte) 0;
        }

        assertArrayEquals(byteArrayToCompare, this.stringInArrayOfByte, "The method didn't fill the array with 0.");
    }

    @Test
    void testGenerateSalt() {
        final int length = 32;
        final byte[] salt1 = CryptoUtils.generateSalt(length);

        assertEquals(length, salt1.length, "The lenght of the array is not 32 but " + salt1.length);

        final byte[] salt2 = CryptoUtils.generateSalt(length);

        assertFalse(Arrays.equals(salt1, salt2), "The 2 salt are equals.");
    }

    @Test
    void testGenerateSecureRandomNotUsablePassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        final int minimumLength = 63;
        final char[] actualPassword = passwordGenerator.generateHightEntropyKey(minimumLength);

        assertNotNull(actualPassword, "The password is not null.");

        assertTrue(actualPassword.length >= minimumLength, "The password is shorter than the minimum length.");
    }

    @Test
    void testDestroy() {
        EncodingClassForTesting encodingClassForTesting = new EncodingClassForTesting("Stringa", 112);

        Supplier<String> getterFunction = () -> encodingClassForTesting.getFirstField();
        Consumer<String> setterFunction = encodingClassForTesting::setFirstField;

        CryptoUtils.destroy(getterFunction, setterFunction);

        String stringValueAfterDestroy = encodingClassForTesting.getFirstField();
        
        assertNull(stringValueAfterDestroy, "The string is not null.");
    }
}
