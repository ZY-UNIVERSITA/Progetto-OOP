package com.zysn.passwordmanager.model.authentication.login.impl;

import java.util.ArrayList;
import java.util.List;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.authentication.login.api.LoginStepHandler;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;

/**
 * The GeneralLoginHandler class is an implementation of the LoginStepHandler
 * interface.
 * It provides functionality to handle the general login process, including
 * validating username and password.
 */
public class GeneralLoginHandler implements LoginStepHandler {
    private final CollectedUserData collectedUserData;
    private final FileManager fileManager;

    private final List<String> errors;

    /**
     * Constructs a GeneralLoginHandler object with the specified collected user
     * data.
     * Initializes the FileManager with default settings and prepares an empty list
     * for errors.
     * 
     * @param collectedUserData the CollectedUserData object containing user data
     *                          for login
     */
    public GeneralLoginHandler(final CollectedUserData collectedUserData) {
        this.collectedUserData = collectedUserData;
        this.fileManager = new DefaultFileManager(PathsConstant.USER_PERSONAL, ExtensionsConstant.JSON);

        this.errors = new ArrayList<>();
    }

    /**
     * Handles the login step by validating the username and password.
     * If there are any validation errors, an IllegalArgumentException is thrown.
     */
    @Override
    public void handleStep() {
        this.handleUsernameValidation();
        this.handlePasswordValidation();

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }
    }

    /**
     * Validates the username.
     * Checks if the username is null, empty, or does not exist in the file system.
     * Adds appropriate error messages to the errors list.
     */
    private void handleUsernameValidation() {
        final String username = this.collectedUserData.getUsername();

        if (username == null) {
            errors.add("Username cannot be null.");
        }

        if (username.length() <= 0 || username.trim().isEmpty()) {
            errors.add("Username cannot be blank.");
        }

        if (!fileManager.createPath(username).toFile().exists()) {
            errors.add("The inserted username is incorrect.");
        }
    }

    /**
     * Validates the password.
     * Checks if the password is null or empty.
     * Adds appropriate error messages to the errors list.
     */
    private void handlePasswordValidation() {
        final byte[] password = this.collectedUserData.getPassword();

        if (password == null || password.length <= 0) {
            errors.add("A password is required.");
        }

    }
}
