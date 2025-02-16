package com.zysn.passwordmanager.model.authentication.keystore.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreService;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.enumerations.PathsEnum;
import com.zysn.passwordmanager.model.utils.FileManager;

/**
 * The DefaultKeyStoreManager class is responsible for managing the key store operations.
 * It implements the KeyStoreManager interface.
 */
public class DefaultKeyStoreManager implements KeyStoreManager {

    private FileManager fileManager;
    private KeyStore keyStore;
    private KeyStoreService keyStoreService;
    private KeyStoreConfig keyStoreConfiguration;

    /**
     * Constructs a new DefaultKeyStoreManager with the specified FileManager and KeyStoreConfig.
     *
     * @param fileManager the FileManager to manage file operations
     * @param keyStoreConfiguration the KeyStoreConfig for key store configuration
     */
    public DefaultKeyStoreManager(FileManager fileManager, KeyStoreConfig keyStoreConfiguration) {
        this.fileManager = fileManager;
        this.keyStoreConfiguration = keyStoreConfiguration;
        this.keyStoreService = new DefaultKeyStoreService(keyStoreConfiguration);
    }

    /**
     * Constructs a new DefaultKeyStoreManager with the specified FileManager and a default KeyStoreConfig.
     *
     * @param fileManager the FileManager to manage file operations
     */
    public DefaultKeyStoreManager(FileManager fileManager) {
        this(fileManager, new KeyStoreConfig());
    }

    /**
     * Constructs a new DefaultKeyStoreManager with a default FileManager and KeyStoreConfig.
     */
    public DefaultKeyStoreManager() {
        this(null);
    }

    /**
     * Creates a new key store with a generated encryption key.
     */
    @Override
    public void createNewKeyStore() {
        this.keyStoreService.generateKeyStoreEncryptionKey();

        char[] keyStorePassword = EncodingUtils
                .byteToCharConverter(this.keyStoreConfiguration.getKeyStoreEncryptionKey());

        try {
            this.keyStore = KeyStore.getInstance("BCFKS", "BC");
            this.keyStore.load(null, keyStorePassword);
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException
                | NoSuchProviderException e) {
            System.err.println("An error occurred while trying to create the key store.");
        } finally {
            CryptoUtils.cleanMemory(keyStorePassword);
        }
    }

    /**
     * Creates key store entries for TOTP encryption key, TOTP key, and salt.
     */
    @Override
    public void createKeyStoreEntry() {
        this.keyStoreService.generateTotpEncryptionKey();
        this.keyStoreService.generateTotpKey();
        this.keyStoreService.generateSalt();
    }

    /**
     * Populates the new key store with the provided password and keys.
     *
     * @param password the password to protect the key store
     */
    @Override
    public void populateNewKeyStore(byte[] password) {
        String totpEncryptionKeyAlias = "TOTP Encryption Key";
        String totpKeyAlias = "TOTP Key";

        this.insertKey(totpEncryptionKeyAlias, this.keyStoreConfiguration.getTotpEncryptionKey(), true, password,
                this.keyStoreConfiguration.getSaltWithPasswordDerived());

        this.insertKey(totpKeyAlias, this.keyStoreConfiguration.getTotpKey(), false,
                this.keyStoreConfiguration.getTotpEncryptionKey(),
                this.keyStoreConfiguration.getSaltWithTotpEncryptionKey());
    }

    /**
     * Inserts a key into the key store with the specified alias and protection parameters.
     *
     * @param alias the alias for the key
     * @param key the key to be inserted
     * @param base64 whether the key is encoded in base64
     * @param password the password to protect the key
     * @param salt the salt to derive the password
     */
    private void insertKey(String alias, byte[] key, boolean base64, byte[] password, byte[] salt) {
        char[] fullPassword = CryptoUtils.concatAndConvertPasswordAndSalt(base64, password, salt);

        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(new SecretKeySpec(key, "AES"));
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(fullPassword);

        try {
            this.keyStore.setEntry(alias, secretKeyEntry, protParam);
        } catch (KeyStoreException e) {
            System.err.println("An error occurred while trying to obtain the key.");
        } finally {
            CryptoUtils.cleanMemory(fullPassword);
            secretKeyEntry = null;
            protParam = null;
        }
    }

    /**
     * Loads the key store from the file system using the specified username.
     *
     * @param username the username to identify the key store file
     */
    @Override
    public void loadKeyStore(String username) {
        Path keyStoreFilePath = this.fileManager.createPath(true, PathsEnum.KEY_STORE.getParameter(),
                username.concat(".bcfks"));

        char[] keyStorePassword = EncodingUtils
                .byteToCharConverter(this.keyStoreConfiguration.getKeyStoreEncryptionKey());

        try (InputStream in = new FileInputStream(keyStoreFilePath.toFile())) {
            this.keyStore = KeyStore.getInstance("BCFKS", "BC");
            this.keyStore.load(in, keyStorePassword);
        } catch (IOException | KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException
                | CertificateException e) {
            System.err.println("An error occurred while trying to open the file.");
        } finally {
            CryptoUtils.cleanMemory(keyStorePassword);
        }
    }

    /**
     * Saves the key store to the file system using the specified username.
     *
     * @param username the username to identify the key store file
     */
    @Override
    public void saveKeyStore(String username) {
        Path keyStoreFilePath = this.fileManager.createPath(true, PathsEnum.KEY_STORE.getParameter(),
                username.concat(".bcfks"));

        this.fileManager.createFolder(keyStoreFilePath);

        char[] keyStorePassword = EncodingUtils
                .byteToCharConverter(this.keyStoreConfiguration.getKeyStoreEncryptionKey());

        try (OutputStream out = new FileOutputStream(keyStoreFilePath.toFile())) {
            keyStore.store(out, keyStorePassword);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            System.err.println("An error occurred while trying to save the file.");
        } finally {
            CryptoUtils.cleanMemory(keyStorePassword);
        }
    }

    /**
     * Obtains the key from the key store with the specified alias and protection parameters.
     *
     * @param alias the alias for the key
     * @param base64 whether the key is encoded in base64
     * @param password the password to access the key
     * @param salt the salt to derive the password
     * @return the obtained key
     */
    @Override
    public byte[] obtainKey(String alias, boolean base64, byte[] password, byte[] salt) {
        char[] fullPassword = CryptoUtils.concatAndConvertPasswordAndSalt(base64, password, salt);

        byte[] key = null;

        try {
            key = this.keyStore.getKey(alias, fullPassword).getEncoded();
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            System.err.println("An error occurred while trying to obtain the key.");
        } finally {
            CryptoUtils.cleanMemory(fullPassword);
        }

        return key;
    }

    /**
     * Closes the key store and cleans up resources.
     */
    @Override
    public void close() {
        try {
            while (this.keyStore.aliases().hasMoreElements()) {
                String alias = this.keyStore.aliases().nextElement();
                keyStore.deleteEntry(alias);
            }
            
            this.keyStore = null;

            this.getKeyStoreConfiguration().destroy();
            this.setKeyStoreConfiguration(null);
        } catch (KeyStoreException e) {
            System.err.println("An error occurred while trying to delete the key store.");
        }
    }

    /* GETTER and SETTER */
    public KeyStore getKeyStore() {
        return keyStore;
    }
    public KeyStoreConfig getKeyStoreConfiguration() {
        return keyStoreConfiguration;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void setKeyStoreConfiguration(KeyStoreConfig keyStoreConfiguration) {
        this.keyStoreConfiguration = keyStoreConfiguration;
    }
}
