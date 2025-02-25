package com.zysn.passwordmanager.model.account.entity.impl;

import com.zysn.passwordmanager.model.account.entity.api.UserAccountAbstract;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;

/**
 * Represents a user account with a master key for cryptographic purposes.
 * This class extends the abstract UserAccountAbstract class.
 */
public class UserAccount extends UserAccountAbstract {
    private byte[] masterKey;

    /**
     * Constructs a UserAccount with the specified username.
     *
     * @param username The username for the user account.
     */
    public UserAccount(final String username) {
        super(username);
    }

    /**
     * Constructs an empty UserAccount.
     */
    public UserAccount() {

    }

    /**
     * Destroys the user account and securely wipes the master key.
     */
    @Override
    public void destroy() {
        super.destroy();
        CryptoUtils.destroy(this::getMasterKey, this::setMasterKey);
    }

    /* GETTER AND SETTER */
    public byte[] getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(final byte[] masterKey) {
        this.masterKey = masterKey;
    }
    
}
