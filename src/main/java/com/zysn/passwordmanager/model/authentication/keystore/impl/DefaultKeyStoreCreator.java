package com.zysn.passwordmanager.model.authentication.keystore.impl;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreCreator;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;

/**
 * A class that implements the {@link KeyStoreCreator} interface and provides
 * a method to create a new KeyStore instance.
 */
public class DefaultKeyStoreCreator implements KeyStoreCreator {

    /**
     * Creates a new instance of a KeyStore using the BouncyCastle provider and
     * initializes it with the given password.
     *
     * @param keyStorePassword the password used to protect the KeyStore
     * @return the initialized KeyStore instance, or {@code null} if an error
     *         occurred
     */
    @Override
    public KeyStore createKeyStore(final char[] keyStorePassword) {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("BCFKS", "BC");
            keyStore.load(null, keyStorePassword);
        } catch (final IOException e) {
            System.err.println("An IO error occurred while trying to initialize the KeyStore with the given password.");
        } catch (final NoSuchAlgorithmException e) {
            System.err.println(
                    "The specified algorithm for the KeyStore integrity check is not available. Ensure the algorithm is supported.");
        } catch (final CertificateException e) {
            System.err.println(
                    "A certificate error occurred while initializing the KeyStore. Ensure all certificates are valid and properly formatted.");
        } catch (final KeyStoreException e) {
            System.err.println(
                    "A problem occurred with the KeyStore instance, which could not be initialized or loaded.");
        } catch (final NoSuchProviderException e) {
            System.err.println(
                    "The specified provider 'BC' was not found. Make sure BouncyCastle is correctly configured.");
        } finally {
            CryptoUtils.cleanMemory(keyStorePassword);
        }

        return keyStore;
    }
}
