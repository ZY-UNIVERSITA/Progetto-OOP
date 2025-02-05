package com.zysn.passwordmanager.model.utils.encoding;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EncodingUtilsTest {
    private static final String CHARSET_NAME = "UTF-8";

    private char[] stringInArrayOfChar;
    private byte[] stringInArrayOfByte;

    @BeforeEach
    void setup() {
        this.stringInArrayOfChar = "ABCDE012".toCharArray();
        this.stringInArrayOfByte = new byte[] { 65, 66, 67, 68, 69, 48, 49, 50 };
    }

    @Test
    void testByteToBase64() {
        char[] actualBase64 = EncodingUtils.byteToBase64(stringInArrayOfByte);

        char[] expectedBase64 = "QUJDREUwMTI=".toCharArray();

        assertArrayEquals(expectedBase64, actualBase64, "The conversion didn't give the right answer.");
    }

    @Test
    void testByteToCharConverterWithCharsetName() {
        char[] actualConvertedArray = EncodingUtils.byteToCharConverter(this.stringInArrayOfByte, CHARSET_NAME);

        assertArrayEquals(stringInArrayOfChar, actualConvertedArray, "The 2 arrays doesn't contain the same characters.");
    }

    @Test
    void testByteToCharConverterWithoutCharsetName() {
        char[] actualConvertedArray = EncodingUtils.byteToCharConverter(this.stringInArrayOfByte, CHARSET_NAME);

        assertArrayEquals(stringInArrayOfChar, actualConvertedArray, "The 2 arrays doesn't contain the same characters.");
    }

    @Test
    void testCharToByteConverterWithCharsetName() {
        byte[] arrayOfCharIntoArrayOfByte = EncodingUtils.charToByteConverter(this.stringInArrayOfChar, CHARSET_NAME);

        assertArrayEquals(stringInArrayOfByte, arrayOfCharIntoArrayOfByte, "The 2 array of byte are not the same.");
    }

    @Test
    void testCharToByteConverterWithoutCharsetName() {
        byte[] arrayOfCharIntoArrayOfByte = EncodingUtils.charToByteConverter(this.stringInArrayOfChar);

        assertArrayEquals(stringInArrayOfByte, arrayOfCharIntoArrayOfByte, "The 2 array of byte are not the same.");
    }
}
