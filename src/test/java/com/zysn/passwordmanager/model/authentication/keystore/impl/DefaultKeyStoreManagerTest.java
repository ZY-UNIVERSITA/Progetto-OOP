package com.zysn.passwordmanager.model.authentication.keystore.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;

public class DefaultKeyStoreManagerTest {
    private SessionManager sessionManager;
    private DefaultKeyStoreManager keyStoreManager;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();

        this.sessionManager.getKeyStoreConfig().setKeyStoreEncryptionKey(new byte[] { 1, 1, 1, 1, 1});

        byte[] key = new byte[32];
        Arrays.fill(key, (byte) 1);

        this.sessionManager.getUserAuthKey().setPasswordDerivedKey(key);

        this.sessionManager.getUserAccount().setUsername("test user");

        this.keyStoreManager = new DefaultKeyStoreManager(this.sessionManager);
    }

    // FATTO
    @Test
    void testCloseKeyStore() throws KeyStoreException {
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.createKeyStoreConfig();
        this.keyStoreManager.createKeyStoreEntry();
        this.keyStoreManager.populateNewKeyStore();

        KeyStore keyStore = this.keyStoreManager.getKeyStore();

        this.keyStoreManager.closeKeyStore();

        assertNull(this.keyStoreManager.getKeyStore(), "The Key store has not been eliminated.");
        assertFalse(keyStore.aliases().hasMoreElements(), "The Key store entries has not been eliminated.");
    }

    // FATTO
    @Test
    void testCreateKeyStoreEntry() {
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.createKeyStoreEntry();

        assertNotNull(this.sessionManager.getUserAuthKey().getTotpEncryptionKey(), "The TOTP encryption key is null.");
        assertNotNull(this.sessionManager.getUserAuthKey().getTotpKey(), "The TOTP key is null.");
    }

    // FATTO
    @Test
    void testCreateNewKeyStore() {
        this.keyStoreManager.createNewKeyStore();

        assertNotNull(this.keyStoreManager.getKeyStore(), "The key store is null.");
    }

    // FATTO
    @Test
    void testLoadKeyStore() {
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.saveKeyStore();

        this.keyStoreManager.closeKeyStore();

        assertNull(this.keyStoreManager.getKeyStore(), "The key store has not been closed.");

        this.keyStoreManager.loadKeyStore();

        assertNotNull(this.keyStoreManager.getKeyStore(), "The key store has not been loaded.");

        Path tempDir = Paths.get(System.getProperty(PathsConstant.USER_ROOT.getParameter()), PathsConstant.KEY_STORE.getParameter());

        File file = tempDir.resolve("test user.bcfks").toFile();

        assertNotNull(file, "Non esiste." + tempDir.toAbsolutePath());

        file.delete();
    }

    // FATTO
    @Test
    void testPopulateNewKeyStore() throws KeyStoreException {
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.createKeyStoreConfig();
        this.keyStoreManager.createKeyStoreEntry();
        this.keyStoreManager.populateNewKeyStore();

        boolean totpEncryptionKey = this.keyStoreManager.getKeyStore().containsAlias("TOTP Encryption Key");
        boolean totpKey = this.keyStoreManager.getKeyStore().containsAlias("TOTP Key");

        assertTrue(totpEncryptionKey, "TOTP Encryption Key is not present.");
        assertTrue(totpKey, "TOTP Key is not present.");
    }

    // FATTO
    @Test
    void testSaveKeyStore() throws IOException {
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.saveKeyStore();

        Path tempDir = Paths.get(System.getProperty(PathsConstant.USER_ROOT.getParameter()), PathsConstant.KEY_STORE.getParameter());

        File file = tempDir.resolve("test user.bcfks").toFile();

        assertNotNull(file, "Non esiste." + tempDir.toAbsolutePath());

        file.delete();
    }

    //FATTO
    @Test
    void testCreateKeyStoreConfig() {
        this.keyStoreManager.createKeyStoreConfig();
        
        assertNotNull(this.sessionManager.getKeyStoreConfig().getKeyStoreEncryptionKey(), "The key store encryption key is null.");
        assertNotNull(this.sessionManager.getKeyStoreConfig().getSaltWithPasswordDerived(), "The salt with password is null.");
        assertNotNull(this.sessionManager.getKeyStoreConfig().getSaltWithTotpEncryptionKey(), "The salt with totp encryption key is null.");
        assertNotNull(this.sessionManager.getKeyStoreConfig().getSaltForHKDF(), "The salt with HKDF key is null.");
        assertNotNull(this.sessionManager.getKeyStoreConfig().getServiceDecryptionSalt(), "The salt for service config decryption key is null.");
    }

    // FATTO
    @Test
    void testDecryptConfig() {
        this.keyStoreManager.createKeyStoreConfig();
        
        byte[] salt = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", salt, null);

        this.sessionManager.getUserAuthInfo().setKeyStoreEncryptionConfig(algorithmConfig);

        this.keyStoreManager.encryptConfig();

        this.sessionManager.setKeyStoreConfig(null);

        this.keyStoreManager.decryptConfig();

        assertNotNull(this.sessionManager.getKeyStoreConfig(), "The data has not been decrypted.");

        assertTrue(this.sessionManager.getKeyStoreConfig().getKeyStoreEncryptionKey().length > 0, "The key store encryption key is not existent.");
    }

    // FATTO
    @Test
    void testEncryptConfig() {
        this.keyStoreManager.createKeyStoreConfig();
        
        byte[] salt = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", salt, null);

        this.sessionManager.getUserAuthInfo().setKeyStoreEncryptionConfig(algorithmConfig);

        this.keyStoreManager.encryptConfig();

        byte[] encryptedData = this.sessionManager.getUserAuthInfo().getKeyStoreConfigEncryptedData();

        assertNotNull(encryptedData, "The encrypted data is null.");
    }


    @Test
    void testGetKeyStoreKeys() {
        this.keyStoreManager.createKeyStoreConfig();
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.createKeyStoreEntry();
        this.keyStoreManager.populateNewKeyStore();
        this.keyStoreManager.saveKeyStore();

        this.keyStoreManager.closeKeyStore();

        this.keyStoreManager.loadKeyStore();
        this.sessionManager.getUserAuthKey().setTotpEncryptionKey(null);
        this.sessionManager.getUserAuthKey().setTotpKey(null);

        assertNull(this.sessionManager.getUserAuthKey().getTotpEncryptionKey(), "The totp encryption key should be null.");
        assertNull(this.sessionManager.getUserAuthKey().getTotpKey(), "The totp key should be null.");

        this.keyStoreManager.getKeyStoreKeys();

        assertNotNull(this.sessionManager.getUserAuthKey().getTotpEncryptionKey(), "The totp encryption key should not be null.");
        assertNotNull(this.sessionManager.getUserAuthKey().getTotpKey(), "The totp key should not be null.");

        Path tempDir = Paths.get(System.getProperty(PathsConstant.USER_ROOT.getParameter()), PathsConstant.KEY_STORE.getParameter());

        File file = tempDir.resolve("test user.bcfks").toFile();

        file.delete();
    }
}
