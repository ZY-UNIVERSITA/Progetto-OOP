package com.zysn.passwordmanager.model.utils.crypto.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import com.zysn.passwordmanager.model.enums.CryptoLength;

/**
 * Utility class for cryptographic operations, including generating salts,
 * secure random passwords, and clearing memory.
 *
 * <p>
 * This class provides static methods to generate salts, generate secure
 * random passwords, and clear memory by overwriting char and byte arrays.
 * The methods in this class are designed to be used for cryptographic
 * purposes where secure random values and secure clearing of sensitive
 * data are required.
 * </p>
 */
public class CryptoUtils {
    /**
     * Generates a random salt of the specified length.
     *
     * @param length the length of the salt
     * @return a byte array containing the generated salt
     * @throws IllegalArgumentException if the length is 0 or less
     */
    public static byte[] generateSalt(final int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("The length of the salt cannot be 0 or less.");
        }

        final byte[] salt = new byte[length];

        try {
            final SecureRandom random = SecureRandom.getInstanceStrong();
            random.nextBytes(salt);
        } catch (final NoSuchAlgorithmException e) {
            System.err.println("Errore nella generazione del satl.");
        }

        return salt;
    }

    /**
     * Generates a secure random password that is not usable, meaning it may contain
     * undefined or surrogate characters.
     *
     * @param minimumLength the minimum length of the password
     * @return a char array representing the generated password
     * @throws IllegalArgumentException if the minimum length is less than 16
     */
    public static char[] generateSecureRandomNotUsablePassword(final int minimumLength) {
        final int lowerBound = CryptoLength.MINIMUM_PASSWORD_LENGTH.getParameter();
        final int higherBound = CryptoLength.PASSHPRASE_PASSWORD_LENGTH.getParameter();

        if (minimumLength < lowerBound) {
            throw new IllegalArgumentException("The minimum length of the password is " + lowerBound);
        }

        char[] password = null;

        try {
            final SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            final int length = secureRandom.nextInt(minimumLength, higherBound);

            password = new char[length];

            int characterPosition = 0;
            while (characterPosition < length) {
                int codePoint;
                if (characterPosition < length - 1) {
                    // Generate an integer between 0 and 0x10FFFF (values between 0 and the maximum
                    // value of Unicode code points currently available)
                    // This can use 1 char or 2 chars per character
                    codePoint = secureRandom.nextInt(0x10FFFF + 1);
                } else {
                    // Generate an integer between 0 and 65535 (2 bytes, therefore 1 char)
                    codePoint = secureRandom.nextInt(0xFFFF + 1);
                }

                // Check if the code point actually exists for a char and it should not be a
                // surrogate char
                if (Character.isDefined(codePoint)) {
                    final char[] character = Character.toChars(codePoint);

                    if (character.length == 2) {
                        password[characterPosition++] = character[0];
                        password[characterPosition++] = character[1];
                    } else if (character.length == 1 && !Character.isSurrogate(character[0])) {
                        password[characterPosition] = character[0];
                        characterPosition++;
                    }
                }
            }
        } catch (final NoSuchAlgorithmException e) {
            System.err.println("Error trying to create password because the istance of the generator is not available.");
        }

        return password;
    }

    /**
     * Clears the contents of the specified char array by setting each element to
     * zero.
     *
     * @param source the char array to be cleared
     * @throws IllegalArgumentException if the source array is null
     */
    public static void cleanMemory(final char[] source) {
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
    public static void cleanMemory(final byte[] source) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }

        Arrays.fill(source, (byte) 0);
    }

    private CryptoUtils() {

    }
}
