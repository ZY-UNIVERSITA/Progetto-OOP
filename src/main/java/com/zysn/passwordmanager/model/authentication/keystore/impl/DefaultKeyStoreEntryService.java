package com.zysn.passwordmanager.model.authentication.keystore.impl;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreEntryService;

/**
 * A default implementation of the KeyStoreEntryService interface.
 */
public class DefaultKeyStoreEntryService implements KeyStoreEntryService {

    /**
     * Default constructor.
     */
    public DefaultKeyStoreEntryService() {

    }

    /**
     * Inserts a secret key into the KeyStore.
     *
     * @param keyStore                the KeyStore instance where the key will be stored
     * @param alias                   the alias under which the key will be stored
     * @param keyToInsert             the key to be inserted, in byte array format
     * @param passwordToEncryptEntry  the password to protect the key entry
     * @throws KeyStoreException      if there is an error with the KeyStore operations
     */
    @Override
    public void insertKey(final KeyStore keyStore, final String alias, final byte[] keyToInsert, final char[] passwordToEncryptEntry) {

        final KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(new SecretKeySpec(keyToInsert, "AES"));
        final KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(passwordToEncryptEntry);

        try {
            keyStore.setEntry(alias, secretKeyEntry, protParam);
        } catch (final KeyStoreException e) {
            System.err.println("An error occurred while trying to obtain the key.");
        }
    }

    /**
     * Retrieves a secret key from the KeyStore.
     *
     * @param keyStore                the KeyStore instance from which the key will be retrieved
     * @param alias                   the alias under which the key is stored
     * @param passwordToDecryptEntry  the password to unlock the key entry
     * @return                        the key in byte array format
     * @throws UnrecoverableKeyException   if the key cannot be recovered with the provided alias and password
     * @throws KeyStoreException           if there is an error with the KeyStore operations
     * @throws NoSuchAlgorithmException    if the algorithm used to recover the key is not available
     */
    @Override
    public byte[] retrieveKey(final KeyStore keyStore, final String alias, final char[] passwordToDecryptEntry) {
        byte[] key = null;

        try {
            key = keyStore.getKey(alias, passwordToDecryptEntry).getEncoded();
        } catch (final UnrecoverableKeyException e) {
            System.err.println(
                    "The key for the specified alias could not be recovered. Ensure the alias and password are correct.");
        } catch (final KeyStoreException e) {
            System.err.println(
                    "A problem occurred with the KeyStore instance while trying to retrieve the key. Ensure the KeyStore is correctly initialized.");
        } catch (final NoSuchAlgorithmException e) {
            System.err.println(
                    "The algorithm used to recover the key is not available. Ensure the algorithm is supported.");
        }

        return key;
    }
}