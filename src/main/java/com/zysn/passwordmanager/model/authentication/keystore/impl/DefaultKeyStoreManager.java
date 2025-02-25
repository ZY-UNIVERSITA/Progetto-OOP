package com.zysn.passwordmanager.model.authentication.keystore.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreConfigService;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreCreator;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreEntryService;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreStorageService;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.data.DataUtils;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;
/**
 * The DefaultKeyStoreManager class is responsible for managing the key store
 * operations.
 * It implements the KeyStoreManager interface.
 */
public class DefaultKeyStoreManager implements KeyStoreManager {
    private static final String TOTP_ENCRYPTION_KEY_ALIAS = "TOTP Encryption Key";
    private static final String TOTP_KEY_ALIAS = "TOTP Key";

    private FileManager fileManager;
    private SessionManager sessionManager;

    private KeyStore keyStore;
    private KeyStoreConfigService keyStoreService;
    private KeyStoreCreator keyStoreCreator;
    private KeyStoreStorageService keyStoreStorage;
    private KeyStoreEntryService keyStoreEntryManager;

    /**
     * Initializes a new DefaultKeyStoreManager instance with the given session manager.
     *
     * @param sessionManager the session manager to be used for key store operations
     */
    public DefaultKeyStoreManager(SessionManager sessionManager) {
        this.fileManager = new DefaultFileManager(PathsConstant.KEY_STORE, ExtensionsConstant.BCFKS);

        this.sessionManager = sessionManager;

        this.keyStoreService = new DefaultKeyStoreConfigService();
        this.keyStoreStorage = new DefaultKeyStoreStorageService();
        this.keyStoreCreator = new DefaultKeyStoreCreator();
        this.keyStoreEntryManager = new DefaultKeyStoreEntryService();
    }

    /**
     * Creates a new key store with a generated encryption key.
     */
    @Override
    public void createNewKeyStore() {
        char[] keyStorePassword = this.sessionManager.getKeyStoreConfig().getKeyStoreEncryptionKeyChar();

        this.keyStore = this.keyStoreCreator.createKeyStore(keyStorePassword);
    }

    /**
     * Generates the key store configuration key and salt.
     */
    @Override
    public void createKeyStoreConfig() {
        this.keyStoreService.generateKeyStoreConfigKey(this.sessionManager.getKeyStoreConfig());
        this.keyStoreService.generateKeyStoreConfigSalt(this.sessionManager.getKeyStoreConfig());
    }

    /**
     * Generates a key store entry using the user authentication key.
     */
    public void createKeyStoreEntry() {
        this.keyStoreService.generateKeyStoreEntry(this.sessionManager.getUserAuthKey());
    }

    /**
     * Populates the new key store with the provided password and keys.
     */
    @Override
    public void populateNewKeyStore() {
        char[] totpEncryptionKeyPassword = DataUtils.concatArray(
                this.sessionManager.getUserAuthKey().getPasswordDerivedKeyChar(),
                this.sessionManager.getKeyStoreConfig().getSaltWithPasswordDerivedChar());

        char[] totpKeyPassword = DataUtils.concatArray(this.sessionManager.getUserAuthKey().getTotpEncryptionKeyChar(),
                this.sessionManager.getKeyStoreConfig().getSaltWithTotpEncryptionKeyChar());

        try {
            this.keyStoreEntryManager.insertKey(keyStore, TOTP_ENCRYPTION_KEY_ALIAS,
                    this.sessionManager.getUserAuthKey().getTotpEncryptionKey(), totpEncryptionKeyPassword);
            this.keyStoreEntryManager.insertKey(keyStore, TOTP_KEY_ALIAS,
                    this.sessionManager.getUserAuthKey().getTotpKey(), totpKeyPassword);
        } finally {
            CryptoUtils.cleanMemory(totpEncryptionKeyPassword);
            CryptoUtils.cleanMemory(totpKeyPassword);
        }

    }

    /**
     * Saves the key store to the file system.
     */
    @Override
    public void saveKeyStore() {
        String fileName = this.sessionManager.getUserAccount().getUsername();

        Path keyStoreFilePath = this.fileManager.createPath(fileName);

        try {
            Files.createDirectories(keyStoreFilePath.getParent());
        } catch (IOException e) {
            System.err.println(
                    "An IO error occurred while trying to create the directory.");
        }

        char[] keyStorePassword = this.sessionManager.getKeyStoreConfig().getKeyStoreEncryptionKeyChar();

        this.keyStoreStorage.saveKeyStore(keyStoreFilePath, keyStore, keyStorePassword);
    }

    /**
     * Closes the key store and cleans up resources.
     */
    @Override
    public void closeKeyStore() {
        this.keyStoreStorage.closeKeyStore(keyStore);
        this.keyStore = null;
    }

    /**
     * Encrypts the key store configuration using the user's authentication key and algorithm config.
     */
    @Override
    public void encryptConfig() {
        byte[] serializedData = EncodingUtils.serializeData(this.sessionManager.getKeyStoreConfig());

        byte[] encryptionKey = this.sessionManager.getUserAuthKey().getPasswordDerivedKey();
        AlgorithmConfig algorithmConfig = this.sessionManager.getUserAuthInfo().getKeyStoreEncryptionConfig();

        byte[] encryptedData = this.keyStoreService.encryptConfig(serializedData, encryptionKey, algorithmConfig);

        this.sessionManager.getUserAuthInfo().setKeyStoreConfigEncryptedData(encryptedData);
    }

    /**
     * Decrypts the key store configuration using the user's authentication key and algorithm config.
     */
    @Override
    public void decryptConfig() {
        byte[] encryptedData = this.sessionManager.getUserAuthInfo().getKeyStoreConfigEncryptedData();

        byte[] encryptionKey = this.sessionManager.getUserAuthKey().getPasswordDerivedKey();
        AlgorithmConfig algorithmConfig = this.sessionManager.getUserAuthInfo().getKeyStoreEncryptionConfig();

        KeyStoreConfig keyStoreConfig = this.keyStoreService.decryptConfig(encryptedData, encryptionKey,
                algorithmConfig);

        this.sessionManager.setKeyStoreConfig(keyStoreConfig);
    }

    /**
     * Loads the key store from the file system using the specified username.
     */
    @Override
    public void loadKeyStore() {
        String username = this.sessionManager.getUserAccount().getUsername();

        byte[] keyStoreEncrypted = this.fileManager.loadData(username);
        char[] keyStorePassword = this.sessionManager.getKeyStoreConfig().getKeyStoreEncryptionKeyChar();

        this.keyStore = this.keyStoreStorage.loadKeyStore(keyStoreEncrypted, keyStorePassword);
    }

    /**
     * Retrieves the keys stored in the key store and updates the user authentication key.
     */
    @Override
    public void getKeyStoreKeys() {
        char[] totpEncryptionKeyPassword = DataUtils.concatArray(
                this.sessionManager.getUserAuthKey().getPasswordDerivedKeyChar(),
                this.sessionManager.getKeyStoreConfig().getSaltWithPasswordDerivedChar());

        try {
            byte[] key = this.keyStoreEntryManager.retrieveKey(keyStore, TOTP_ENCRYPTION_KEY_ALIAS,
                    totpEncryptionKeyPassword);
            this.sessionManager.getUserAuthKey().setTotpEncryptionKey(key);
        } finally {
            CryptoUtils.cleanMemory(totpEncryptionKeyPassword);
        }

        char[] totpKeyPassword = DataUtils.concatArray(this.sessionManager.getUserAuthKey().getTotpEncryptionKeyChar(),
                this.sessionManager.getKeyStoreConfig().getSaltWithTotpEncryptionKeyChar());

        try {
            byte[] key = this.keyStoreEntryManager.retrieveKey(keyStore, TOTP_KEY_ALIAS, totpKeyPassword);
            this.sessionManager.getUserAuthKey().setTotpKey(key);
        } finally {
            CryptoUtils.cleanMemory(totpKeyPassword);
        }
    }

    /**
     * Gets the current key store instance.
     *
     * @return the current key store
     */
    public KeyStore getKeyStore() {
        return keyStore;
    }
}