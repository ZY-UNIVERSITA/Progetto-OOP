package com.zysn.passwordmanager.model.utils.security.impl;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import com.zysn.passwordmanager.model.enums.CryptoLength;

/**
 * A utility class to generate random passwords.
 * The generated password can include uppercase letters, lowercase letters, digits, and special characters.
 * The password length must be greater than 11 characters.
 */
public class PasswordGenerator {

    public PasswordGenerator () {

    }

    /**
     * Generates a random password based on the provided criteria.
     * 
     * @param length The desired length of the generated password.
     * @param useSpecialChar Whether or not to include special characters.
     * @param useNumbers Whether or not to include digits.
     * @param useUpperCase Whether or not to include uppercase letters.
     * @param useLowerCase Whether or not to include lowercase letters.
     * @return The generated password.
     * @throws IllegalArgumentException if the password length is less than or equal to 11, 
     *         or if no character categories are selected.
     */
    public char[] generatePassword(int length, boolean useSpecialChar, boolean useNumbers, boolean useUpperCase, boolean useLowerCase) {

        if (length < 12) {
            throw new IllegalArgumentException("Password length must be greater than 11.");
        }

        List<CharacterRule> rules = new ArrayList<>();
        if (useUpperCase) rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        if (useLowerCase) rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        if (useNumbers) rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        if (useSpecialChar) rules.add(new CharacterRule(EnglishCharacterData.Special, 1));


        if (rules.size() < 2) {
            throw new IllegalArgumentException("You must select at least two character categories.");
        }
        
        org.passay.PasswordGenerator passayGenerator = new org.passay.PasswordGenerator();
        String password = passayGenerator.generatePassword(length, rules);
        
        return password.toCharArray();
    }

    /**
     * Generates a secure random password that is not usable, meaning it may contain
     * undefined or surrogate characters.
     *
     * @param minimumLength the minimum length of the password
     * @return a char array representing the generated password
     * @throws IllegalArgumentException if the minimum length is less than 16
     */
    public char[] generateHightEntropyKey(final int minimumLength) {
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
            System.err
                    .println("Error trying to create password because the istance of the generator is not available.");
        }

        return password;
    }
}
