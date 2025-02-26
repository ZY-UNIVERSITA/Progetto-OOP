package com.zysn.passwordmanager.model.authentication.keystore.api;

/**
 * The KeyStoreManager interface provides methods for managing key store
 * operations.
 */
public interface KeyStoreManager {

    /**
     * Creates a new key store with a generated encryption key.
     */
    public void createNewKeyStore();

    /**
     * Loads the key store from the file system.
     */
    public void loadKeyStore();

    /**
     * Generates the key store configuration key and salt.
     */
    public void createKeyStoreConfig();

    /**
     * Generates a key store entry using the user authentication key.
     */
    public void createKeyStoreEntry();
    
    /**
     * Populates the new key store with the provided password and keys.
     */
    public void populateNewKeyStore();

    /**
     * Saves the key store to the file system.
     */
    public void saveKeyStore();

    /**
     * Closes the key store and cleans up resources.
     */
    public void closeKeyStore();

    /**
     * Encrypts the key store configuration using the user's authentication key and algorithm config.
     */
    public void encryptConfig();

    /**
     * Decrypts the key store configuration using the user's authentication key and algorithm config.
     */
    public void decryptConfig();

    /**
     * Retrieves the keys stored in the key store and updates the user authentication key.
     */
    public void getKeyStoreKeys();
}
