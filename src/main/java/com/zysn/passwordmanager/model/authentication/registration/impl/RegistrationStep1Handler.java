package com.zysn.passwordmanager.model.authentication.registration.impl;

import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.util.Arrays;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.authentication.registration.api.RegistrationStepHandler;
import com.zysn.passwordmanager.model.enums.CryptoLength;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;

/**
 * Handles the first step of the registration process.
 * This class implements the {@link RegistrationStepHandler} interface.
 */
public class RegistrationStep1Handler implements RegistrationStepHandler {

    private final FileManager fileManager;
    private final CollectedUserData collectedUserData;
    private final List<String> errors;

    /**
     * Constructs a new RegistrationStep1Handler.
     *
     * @param collectedUserData The collected user data object.
     */
    public RegistrationStep1Handler(final CollectedUserData collectedUserData) {
        this.fileManager = new DefaultFileManager(PathsConstant.USER_PERSONAL, ExtensionsConstant.JSON);
        this.collectedUserData = collectedUserData;
        this.errors = new ArrayList<>();
    }

    /**
     * Handles the current registration step.
     * Validates the username and password, and throws an exception if there are
     * errors.
     */
    @Override
    public void handleStep() {
        this.validateUsername();
        this.validatePassword();

        if (!errors.isEmpty()) {
            final String collectedErrors = String.join("\n", errors);
            this.errors.clear();
            throw new IllegalArgumentException(collectedErrors);
        }
    }

    /**
     * Validates the username provided by the user.
     */
    private void validateUsername() {
        final String username = this.collectedUserData.getUsername();

        if (username == null) {
            errors.add("Username cannot be null.");
        }

        if (username.length() <= 0 || username.trim().isEmpty()) {
            errors.add("Username cannot be blank.");
        }

        if (fileManager.createPath(username).toFile().exists()) {
            errors.add("Cannot create an account with this username.");
        }
    }

    /**
     * Validates the password and password confirmation provided by the user.
     */
    private void validatePassword() {
        final byte[] password = this.collectedUserData.getPassword();
        final byte[] confirmPassword = this.collectedUserData.getConfirmPassword();

        if (password == null || confirmPassword == null) {
            errors.add("Password or password confirmation cannot be null.");
        }

        if (!Arrays.areEqual(password, confirmPassword)) {
            errors.add("The passwords entered do not match.");
        }

        final int minPasswordLength = CryptoLength.MINIMUM_PASSWORD_LENGTH.getParameter();
        if (password.length < minPasswordLength) {
            errors.add("The password must be at least " + minPasswordLength + " characters long.");
        }

        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (final byte character : password) {
            if (Character.isLowerCase(character)) {
                hasLowerCase = true;
            }
            if (Character.isUpperCase(character)) {
                hasUpperCase = true;
            }
            if (Character.isDigit(character)) {
                hasDigit = true;
            }
            if (!Character.isLetterOrDigit(character)) {
                hasSpecialChar = true;
            }
        }

        if (!hasLowerCase) {
            errors.add("The password must contain at least one lowercase letter.");
        }
        if (!hasUpperCase) {
            errors.add("The password must contain at least one uppercase letter.");
        }
        if (!hasDigit) {
            errors.add("The password must contain at least one digit.");
        }
        if (!hasSpecialChar) {
            errors.add("The password must contain at least one special character.");
        }
    }
}