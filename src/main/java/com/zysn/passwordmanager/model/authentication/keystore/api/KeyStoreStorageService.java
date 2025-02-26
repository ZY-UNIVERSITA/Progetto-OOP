package com.zysn.passwordmanager.model.authentication.keystore.api;

import java.nio.file.Path;
import java.security.KeyStore;

/**
 * An interface that defines methods for loading, saving, and closing a
 * KeyStore.
 */
public interface KeyStoreStorageService {

    /**
     * Loads a KeyStore from the provided byte array and initializes it with the
     * given password.
     *
     * @param input            the byte array containing the KeyStore data
     * @param keyStorePassword the password used to protect the KeyStore
     * @return the loaded KeyStore instance
     */
    public KeyStore loadKeyStore(byte[] input, char[] keyStorePassword);

    /**
     * Saves the given KeyStore to the specified file path, protecting it with the
     * provided password.
     *
     * @param filePath         the path of the file where the KeyStore will be saved
     * @param keyStore         the KeyStore instance to be saved
     * @param keyStorePassword the password used to protect the KeyStore
     */
    public void saveKeyStore(Path filePath, KeyStore keyStore, char[] keyStorePassword);

    /**
     * Closes the provided KeyStore, performing any necessary cleanup.
     *
     * @param keyStore the KeyStore instance to be closed
     */
    public void closeKeyStore(KeyStore keyStore);
}
