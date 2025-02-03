package com.zysn.passwordmanager.model.utils;

import java.security.SecureRandom;

/**
 * A utility class to generate random passwords.
 * The generated password can include uppercase letters, lowercase letters, digits, and special characters.
 * The password length must be greater than 11 characters.
 */
public class PasswordGenerator {
    
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHAR = "!@#$%^&*()-_=+<>?/[]{}";

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
    public char[] generatePassword (int length, boolean useSpecialChar, boolean useNumbers, boolean useUpperCase, boolean useLowerCase) {
        if (length <= 11) {
            throw new IllegalArgumentException("Password length must be greater than 11.");
        }

        StringBuilder chars = new StringBuilder();

        if (useSpecialChar) {
            chars.append(SPECIAL_CHAR);
        }
        if (useNumbers) {
            chars.append(DIGITS);
        }
        if (useUpperCase) {
            chars.append(UPPERCASE);
        }
        if (useLowerCase) {
            chars.append(LOWERCASE);
        }

        if (chars.length() <= 1) {
            throw new IllegalArgumentException("You must select at least two character categories.");
        }

        SecureRandom random = new SecureRandom();
        
        char[] password = new char[length];
        
        for (int i = 0; i < length; i++) {
            int rand = random.nextInt(chars.length());
            password[i] = chars.charAt(rand);
        }
        
        return password;
    }
}
