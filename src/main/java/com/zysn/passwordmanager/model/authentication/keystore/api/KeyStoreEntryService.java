package com.zysn.passwordmanager.model.authentication.keystore.api;

import java.security.KeyStore;

/**
 * Interface for services that handle key store entries.
 */
public interface KeyStoreEntryService {

    /**
     * Retrieves a secret key from the KeyStore.
     *
     * @param keyStore                the KeyStore instance from which the key will be retrieved
     * @param alias                   the alias under which the key is stored
     * @param passwordToDecryptEntry  the password to unlock the key entry
     * @return                        the key in byte array format
     */
    public byte[] retrieveKey(KeyStore keyStore, String alias, char[] passwordToDecryptEntry);

    /**
     * Inserts a secret key into the KeyStore.
     *
     * @param keyStore                the KeyStore instance where the key will be stored
     * @param alias                   the alias under which the key will be stored
     * @param keyToInsert             the key to be inserted, in byte array format
     * @param passwordToEncryptEntry  the password to protect the key entry
     */
    public void insertKey(KeyStore keyStore, String alias, byte[] keyToInsert, char[] passwordToEncryptEntry);
}

