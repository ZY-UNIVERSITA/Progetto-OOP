package com.zysn.passwordmanager.model.account.entity.api;

import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.security.api.MustBeDestroyed;

/**
 * This abstract class represents a user account and implements the MustBeDestroyed interface.
 * It provides methods to get and set the username, as well as a mechanism for destruction.
 */
public abstract class UserAccountAbstract implements MustBeDestroyed {
    private String username;

    /**
     * Constructor with username parameter.
     *
     * @param username the username for the account
     */
    public UserAccountAbstract(final String username) {
        this.username = username;
    }

    /**
     * Default constructor.
     * Optional initialization logic can be placed here.
     */
    public UserAccountAbstract() {
        // Optional initialization logic can be placed here
    }

    /**
     * Override the destroy method from MustBeDestroyed interface.
     * Uses CryptoUtils to securely destroy the username.
     */
    @Override
    public void destroy() {
        CryptoUtils.destroy(this::getUsername, this::setUsername);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
