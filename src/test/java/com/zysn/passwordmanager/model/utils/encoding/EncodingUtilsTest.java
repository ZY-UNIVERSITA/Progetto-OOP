package com.zysn.passwordmanager.model.utils.encoding;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;

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
    void testBase64ToByte() {
        final char[] base64String = "QUJDREUwMTI=".toCharArray();

        final byte[] actualByte = EncodingUtils.base64ToByte(base64String);

        assertArrayEquals(stringInArrayOfByte, actualByte, "The conversion didn't give the right answer.");
    }

    @Test
    void testByteToBase64() {
        final char[] actualBase64 = EncodingUtils.byteToBase64(stringInArrayOfByte);

        final char[] expectedBase64 = "QUJDREUwMTI=".toCharArray();

        assertArrayEquals(expectedBase64, actualBase64, "The conversion didn't give the right answer.");
    }

    @Test
    void testByteToCharConverterWithCharsetName() {
        final char[] actualConvertedArray = EncodingUtils.byteToCharConverter(this.stringInArrayOfByte, CHARSET_NAME);

        assertArrayEquals(stringInArrayOfChar, actualConvertedArray,
                "The 2 arrays doesn't contain the same characters.");
    }

    @Test
    void testByteToCharConverterWithoutCharsetName() {
        final char[] actualConvertedArray = EncodingUtils.byteToCharConverter(this.stringInArrayOfByte, CHARSET_NAME);

        assertArrayEquals(stringInArrayOfChar, actualConvertedArray,
                "The 2 arrays doesn't contain the same characters.");
    }

    @Test
    void testCharToByteConverterWithCharsetName() {
        final byte[] arrayOfCharIntoArrayOfByte = EncodingUtils.charToByteConverter(this.stringInArrayOfChar,
                CHARSET_NAME);

        assertArrayEquals(stringInArrayOfByte, arrayOfCharIntoArrayOfByte, "The 2 array of byte are not the same.");
    }

    @Test
    void testCharToByteConverterWithoutCharsetName() {
        final byte[] arrayOfCharIntoArrayOfByte = EncodingUtils.charToByteConverter(this.stringInArrayOfChar);

        assertArrayEquals(stringInArrayOfByte, arrayOfCharIntoArrayOfByte, "The 2 array of byte are not the same.");
    }

    @Test
    void testDeserializeData() {
        final byte[] data = new byte[] { 123, 34, 102, 105, 114, 115, 116, 70, 105, 101, 108, 100, 34, 58, 34, 70,
                105, 101, 108, 100, 32, 49, 34, 44, 34, 115, 101, 99, 111, 110, 100, 70, 105, 101, 108, 100, 34, 58, 49,
                125 };

        final EncodingClassForTesting encodingClassForTesting = EncodingUtils.deserializeData(data, new TypeReference<EncodingClassForTesting>() {});

        assertEquals("Field 1", encodingClassForTesting.getFirstField());
        assertEquals(1, encodingClassForTesting.getSecondField());
    }

    @Test
    void testSerializeData() {
        final EncodingClassForTesting encodingClassForTesting = new EncodingClassForTesting("Field 1", 1);

        final byte[] serializeData = EncodingUtils.serializeData(encodingClassForTesting);

        final byte[] expectedData = new byte[] { 123, 34, 102, 105, 114, 115, 116, 70, 105, 101, 108, 100, 34, 58, 34, 70,
                105, 101, 108, 100, 32, 49, 34, 44, 34, 115, 101, 99, 111, 110, 100, 70, 105, 101, 108, 100, 34, 58, 49,
                125 };

        assertArrayEquals(expectedData, serializeData, "The serialization has not been a success.");
    }
    
    @Test
    void testByteToBase32Byte() {
        final byte[] byteToConvert = new byte[] { 2, 0, 2, -1, 100, -100, -128, 127, 50, -50 };

        final byte[] actualConvertedText = EncodingUtils.byteToBase32Byte(byteToConvert);
        final byte[] expectedConvertedText = new byte[] { 65, 73, 65, 65, 70, 55, 51, 69, 84, 83, 65, 72, 54, 77, 87, 79 };

        assertArrayEquals(expectedConvertedText, actualConvertedText, "The converted text is not equals to the expected text.");;
    }
}
