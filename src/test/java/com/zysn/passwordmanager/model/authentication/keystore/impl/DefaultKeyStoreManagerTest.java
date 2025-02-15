package com.zysn.passwordmanager.model.authentication.keystore.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.security.KeyStoreException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;
import com.zysn.passwordmanager.model.utils.enumerations.PathsEnum;
import com.zysn.passwordmanager.model.utils.file.FileManager;

public class DefaultKeyStoreManagerTest {
    private KeyStoreManager keyStoreManager;
    private FileManager fileManager;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.keyStoreManager = new DefaultKeyStoreManager();
        this.fileManager = new FileManager();

        this.keyStoreManager.setFileManager(fileManager);
    }

    @Test
    void testClose() {
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.createKeyStoreEntry();
        this.keyStoreManager.populateNewKeyStore("prova password".getBytes());

        this.keyStoreManager.close();

        assertNull(this.keyStoreManager.getKeyStore(), "The Key store has not been eliminated.");
        assertNull(this.keyStoreManager.getKeyStoreConfiguration(), "The Key store configuration has not been eliminated.");
    }

    @Test
    void testCreateKeyStoreEntry() {
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.createKeyStoreEntry();

        assertNotNull(this.keyStoreManager.getKeyStoreConfiguration().getKeyStoreEncryptionKey(),
                "The keystore encryption key is null.");
        assertNotNull(this.keyStoreManager.getKeyStoreConfiguration().getTotpEncryptionKey(),
                "The TOTP encryption key is null.");
        assertNotNull(this.keyStoreManager.getKeyStoreConfiguration().getTotpKey(), "The TOTP key is null.");
        assertNotNull(this.keyStoreManager.getKeyStoreConfiguration().getSaltWithPasswordDerived(),
                "The salt with password is null.");
        assertNotNull(this.keyStoreManager.getKeyStoreConfiguration().getSaltWithTotpEncryptionKey(),
                "The salt with TOTP encryption key is null.");
    }

    @Test
    void testCreateNewKeyStore() {
        this.keyStoreManager.createNewKeyStore();

        assertNotNull(this.keyStoreManager.getKeyStore(), "The key store is null.");
    }

    @Test
    void testLoadKeyStore() {
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.createKeyStoreEntry();
        try {
            this.keyStoreManager.populateNewKeyStore("prova password".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error tryin to create key store");
        }

        byte[] keyStorePassword = Arrays.copyOf(
                this.keyStoreManager.getKeyStoreConfiguration().getKeyStoreEncryptionKey(),
                this.keyStoreManager.getKeyStoreConfiguration().getKeyStoreEncryptionKey().length);

        this.keyStoreManager.saveKeyStore("testLoading");
        this.keyStoreManager.close();

        this.keyStoreManager = new DefaultKeyStoreManager(this.fileManager);
        this.keyStoreManager.getKeyStoreConfiguration().setKeyStoreEncryptionKey(keyStorePassword);

        this.keyStoreManager.loadKeyStore("testLoading");

        boolean totpEncryptionKey = false;
        boolean totpKey = false;

        try {
            totpEncryptionKey = this.keyStoreManager.getKeyStore().containsAlias("TOTP Encryption Key");
            totpKey = this.keyStoreManager.getKeyStore().containsAlias("TOTP Key");
        } catch (KeyStoreException e) {
            System.err.println("Error trying to get aliases.");
        }

        assertTrue(totpEncryptionKey, "TOTP Encryption Key is not present.");
        assertTrue(totpKey, "TOTP Key is not present.");

        Path keyStoreFilePath = this.fileManager.createPath(true, PathsEnum.KEY_STORE.getParameter(),
                "testLoading".concat(".bcfks"));

        keyStoreFilePath.toFile().delete();
    }

    @Test
    void testObtainKey() {
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.createKeyStoreEntry();
        this.keyStoreManager.populateNewKeyStore("prova password".getBytes());

        byte[] totpKey = this.keyStoreManager.obtainKey("TOTP Key", false, this.keyStoreManager.getKeyStoreConfiguration().getTotpEncryptionKey(), this.keyStoreManager.getKeyStoreConfiguration().getSaltWithTotpEncryptionKey());
    
        assertNotNull(totpKey, "The totp key has not been obtained.");
    }

    @Test
    void testPopulateNewKeyStore() {
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.createKeyStoreEntry();
        this.keyStoreManager.populateNewKeyStore("prova password".getBytes());

        boolean totpEncryptionKey = false;
        boolean totpKey = false;

        try {
            totpEncryptionKey = this.keyStoreManager.getKeyStore().containsAlias("TOTP Encryption Key");
            totpKey = this.keyStoreManager.getKeyStore().containsAlias("TOTP Key");
        } catch (KeyStoreException e) {
            System.err.println("Error trying to get aliases.");
        }

        assertTrue(totpEncryptionKey, "TOTP Encryption Key is not present.");
        assertTrue(totpKey, "TOTP Key is not present.");
    }

    @Test
    void testSaveKeyStore() {
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.createKeyStoreEntry();
        this.keyStoreManager.populateNewKeyStore("prova password".getBytes());
        this.keyStoreManager.saveKeyStore("testKeyStoreSaving");

        Path keyStoreFilePath = this.fileManager.createPath(true, PathsEnum.KEY_STORE.getParameter(),
                "testKeyStoreSaving".concat(".bcfks"));
        try {
            assertTrue(keyStoreFilePath.toFile().exists(), "The file has not been saved.");
        } finally {
            if (keyStoreFilePath.toFile().exists()) {
                keyStoreFilePath.toFile().delete();
            }
        }

    }
}
