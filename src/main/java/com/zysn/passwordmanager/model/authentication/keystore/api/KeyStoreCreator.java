package com.zysn.passwordmanager.model.authentication.keystore.api;

import java.security.KeyStore;

/**
 * An interface that defines a method to create a new KeyStore instance.
 */
public interface KeyStoreCreator {

    /**
     * Creates a new KeyStore instance using the provided password.
     *
     * @param keyStorePassword the password used to protect the KeyStore
     * @return the created KeyStore instance
     */
    public KeyStore createKeyStore(char[] keyStorePassword);
}
