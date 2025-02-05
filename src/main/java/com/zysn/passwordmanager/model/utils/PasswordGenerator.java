package com.zysn.passwordmanager.model.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public char[] generatePassword(int length, boolean useSpecialChar, boolean useNumbers, boolean useUpperCase, boolean useLowerCase) {

        if (length < 12) {
            throw new IllegalArgumentException("Password length must be greater than 11.");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder chars = new StringBuilder();
        List<Character> passwordChars = new ArrayList<>();
        int category = 0;

        if (useSpecialChar) {
            category++;
            chars.append(SPECIAL_CHAR);
            passwordChars.add(SPECIAL_CHAR.charAt(random.nextInt(SPECIAL_CHAR.length())));
        }
        if (useNumbers) {
            category++;
            chars.append(DIGITS);
            passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        if (useUpperCase) {
            category++;
            chars.append(UPPERCASE);
            passwordChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        }
        if (useLowerCase) {
            category++;
            chars.append(LOWERCASE);
            passwordChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        }

        if (category <= 1) {
            throw new IllegalArgumentException("You must select at least two character categories.");
        }

        for (int i = passwordChars.size(); i < length; i++) {
            passwordChars.add(chars.charAt(random.nextInt(chars.length())));
        }

        Collections.shuffle(passwordChars, random);

        char[] password = new char[length];
        for (int i = 0; i < length; i++) {
            password[i] = passwordChars.get(i);
        }

        return password;
    }
}
