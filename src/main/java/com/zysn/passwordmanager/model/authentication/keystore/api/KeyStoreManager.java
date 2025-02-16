package com.zysn.passwordmanager.model.authentication.keystore.api;

import java.security.KeyStore;

import com.zysn.passwordmanager.model.authentication.keystore.impl.KeyStoreConfig;
import com.zysn.passwordmanager.model.utils.FileManager;

/**
 * The KeyStoreManager interface provides methods for managing key store
 * operations.
 */
public interface KeyStoreManager {

    /**
     * Gets the key store.
     *
     * @return the key store
     */
    public KeyStore getKeyStore();

    /**
     * Gets the key store configuration.
     *
     * @return the key store configuration
     */
    public KeyStoreConfig getKeyStoreConfiguration();

    /**
     * Sets the file manager.
     *
     * @param fileManager the FileManager to manage file operations
     */
    public void setFileManager(FileManager fileManager);

    /**
     * Sets the key store configuration.
     *
     * @param keyStoreConfiguration the KeyStoreConfig for key store configuration
     */
    public void setKeyStoreConfiguration(KeyStoreConfig keyStoreConfiguration);

    /**
     * Creates a new key store with a generated encryption key.
     */
    public void createNewKeyStore();

    /**
     * Creates key store entries for TOTP encryption key, TOTP key, and salt.
     */
    public void createKeyStoreEntry();

    /**
     * Populates the new key store with the provided password and keys.
     *
     * @param password the password to protect the key store
     */
    public void populateNewKeyStore(byte[] password);

    /**
     * Loads the key store from the file system using the specified username.
     *
     * @param username the username to identify the key store file
     */
    public void loadKeyStore(String username);

    /**
     * Saves the key store to the file system using the specified username.
     *
     * @param username the username to identify the key store file
     */
    public void saveKeyStore(String username);

    /**
     * Obtains the key from the key store with the specified alias and protection
     * parameters.
     *
     * @param alias    the alias for the key
     * @param base64   whether the key is encoded in base64
     * @param password the password to access the key
     * @param salt     the salt to derive the password
     * @return the obtained key
     */
    public byte[] obtainKey(String alias, boolean base64, byte[] password, byte[] salt);

    /**
     * Closes the key store and cleans up resources.
     */
    public void close();
}
