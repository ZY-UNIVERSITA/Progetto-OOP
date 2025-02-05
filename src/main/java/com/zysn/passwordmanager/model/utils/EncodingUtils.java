package com.zysn.passwordmanager.model.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Base64;

/**
 * The EncodingUtils class provides utility methods for encoding and decoding data.
 */
public class EncodingUtils {
    /**
     * Converts the specified char array to a byte array using UTF-8 encoding.
     *
     * @param source the char array to be converted
     * @return the byte array, or null if the conversion fails
     */
    public static byte[] charToByteConverter(char[] source) {
        return charToByteConverter(source, "UTF-8");
    }

    /**
     * Converts the specified char array to a byte array using custom encoding.
     *
     * @param source      the char array to be converted
     * @param charsetName the name of the charset
     * @return the byte array, or null if the conversion fails
     */
    public static byte[] charToByteConverter(char[] source, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        CharsetEncoder encoder = charset.newEncoder();

        CharBuffer charBuffer = CharBuffer.wrap(source);

        byte[] byteArray = null;

        try {
            ByteBuffer byteBuffer = encoder.encode(charBuffer);
            byteArray = new byte[byteBuffer.remaining()];
            byteBuffer.get(byteArray);

        } catch (CharacterCodingException e) {
            System.err.println("Error trying to convert char[] into byte[].");
        }

        return byteArray;
    }

    /**
     * Converts a byte array to a character array using the default charset (UTF-8).
     *
     * @param source the byte array to be converted
     * @return the resulting character array
     */
    public static char[] byteToCharConverter(byte[] source) {
        return byteToCharConverter(source, "UTF-8");
    }

    /**
     * Converts a byte array to a character array using the specified charset.
     *
     * @param source      the byte array to be converted
     * @param charsetName the name of the charset to be used for conversion
     * @return the resulting character array
     * @throws IllegalArgumentException if the specified charset is not supported
     */
    public static char[] byteToCharConverter(byte[] source, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        CharsetDecoder decoder = charset.newDecoder();

        ByteBuffer byteBuffer = ByteBuffer.wrap(source);

        char[] charArray = null;

        try {
            CharBuffer charBuffer = decoder.decode(byteBuffer);
            charArray = new char[charBuffer.remaining()];
            charBuffer.get(charArray);

        } catch (CharacterCodingException e) {
            System.err.println("Error trying to convert byte[] into char[].");
        }

        return charArray;
    }

    /**
     * Encodes the given byte array into a Base64 char array.
     *
     * @param source the byte array to be encoded
     * @return a char array representing the Base64-encoded string of the source
     *         byte array
     */
    public static char[] byteToBase64(byte[] source) {
        return Base64.getEncoder().encodeToString(source).toCharArray();
    }
}
