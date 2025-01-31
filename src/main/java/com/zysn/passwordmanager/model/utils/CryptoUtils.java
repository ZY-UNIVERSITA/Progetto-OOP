package com.zysn.passwordmanager.model.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.security.NoSuchAlgorithmException;
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

        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            random.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Errore nella generazione del satl.");
        }

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

    /**
     * Clears the contents of the specified byte array by setting each element to
     * zero.
     *
     * @param source the byte array to be cleared
     * @throws IllegalArgumentException if the source array is null
     */
    public static void cleanMemory(byte[] source) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }

        Arrays.fill(source, (byte) 0);
    }

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

}
