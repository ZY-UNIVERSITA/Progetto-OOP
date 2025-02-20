package com.zysn.passwordmanager.model.authentication.keystore.impl;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreStorageService;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;

/**
 * A class that implements the {@link KeyStoreStorageService} interface and
 * provides
 * methods for loading, saving, and closing a KeyStore.
 */
public class DefaultKeyStoreStorageService implements KeyStoreStorageService {

    /**
     * Loads a KeyStore from the provided byte array and initializes it with the
     * given password.
     *
     * @param input            the byte array containing the KeyStore data
     * @param keyStorePassword the password used to protect the KeyStore
     * @return the loaded KeyStore instance, or {@code null} if an error occurred
     */
    @Override
    public KeyStore loadKeyStore(final byte[] input, final char[] keyStorePassword) {
        KeyStore keyStore = null;

        try (InputStream in = new ByteArrayInputStream(input)) {
            keyStore = KeyStore.getInstance("BCFKS", "BC");
            keyStore.load(in, keyStorePassword);
        } catch (final IOException e) {
            System.err.println("An IO error occurred while trying to read the key store data from the input stream.");
        } catch (final KeyStoreException e) {
            System.err.println(
                    "A problem occurred with the KeyStore instance, which could not be initialized or loaded.");
        } catch (final NoSuchProviderException e) {
            System.err.println(
                    "The specified provider 'BC' was not found. Make sure BouncyCastle is correctly configured.");
        } catch (final NoSuchAlgorithmException e) {
            System.err.println(
                    "The specified algorithm for the KeyStore integrity check is not available. Ensure the algorithm is supported.");
        } catch (final CertificateException e) {
            System.err.println(
                    "A certificate error occurred while loading the KeyStore. Ensure all certificates are valid and properly formatted.");
        } finally {
            CryptoUtils.cleanMemory(keyStorePassword);
        }

        return keyStore;
    }

    /**
     * Saves the given KeyStore to the specified file path, protecting it with the
     * provided password.
     *
     * @param filePath         the path of the file where the KeyStore will be saved
     * @param keyStore         the KeyStore instance to be saved
     * @param keyStorePassword the password used to protect the KeyStore
     */
    @Override
    public void saveKeyStore(final Path filePath, final KeyStore keyStore, final char[] keyStorePassword) {
        try (OutputStream out = new FileOutputStream(filePath.toFile())) {
            keyStore.store(out, keyStorePassword);
        } catch (final KeyStoreException e) {
            System.err.println("A problem occurred with the KeyStore instance, which could not be stored.");
        } catch (final NoSuchAlgorithmException e) {
            System.err.println(
                    "The specified algorithm for the KeyStore integrity check is not available. Ensure the algorithm is supported.");
        } catch (final CertificateException e) {
            System.err.println(
                    "A certificate error occurred while storing the KeyStore. Ensure all certificates are valid and properly formatted.");
        } catch (final IOException e) {
            System.err.println("An IO error occurred while trying to write the key store data to the output stream.");
        } finally {
            CryptoUtils.cleanMemory(keyStorePassword);
        }
    }

    /**
     * Closes the provided KeyStore, deleting all entries and performing any
     * necessary cleanup.
     *
     * @param keyStore the KeyStore instance to be closed
     */
    @Override
    public void closeKeyStore(final KeyStore keyStore) {
        try {
            while (keyStore.aliases().hasMoreElements()) {
                final String alias = keyStore.aliases().nextElement();
                keyStore.deleteEntry(alias);
            }
        } catch (final KeyStoreException e) {
            System.err.println("An error occurred while trying to delete the key store entries.");
        }
    }
}
