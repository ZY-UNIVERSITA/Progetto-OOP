package com.zysn.passwordmanager.model.authentication.keystore.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreService;
import com.zysn.passwordmanager.model.utils.enumerations.CryptoLength;

public class DefaultKeyStoreServiceTest {
    private KeyStoreService keyStoreService;
    private KeyStoreConfig keyStoreConfig;
    private int keyLength;
    private int saltLength;

    @BeforeEach
    void setup() {
        this.keyStoreConfig = new KeyStoreConfig();
        this.keyStoreService = new DefaultKeyStoreService(keyStoreConfig);

        this.keyLength = CryptoLength.OPTIMAL_PASSWORD_LENGTH.getParameter();
        this.saltLength = CryptoLength.MINIMUM_PASSWORD_LENGTH.getParameter();
    }

    @Test
    void testGenerateKeyStoreEncryptionKey() {
        this.keyStoreService.generateKeyStoreEncryptionKey();

        assertNotNull(this.keyStoreConfig.getKeyStoreEncryptionKey(), "The key has not been generated.");
        assertTrue(this.keyStoreConfig.getKeyStoreEncryptionKey().length >= keyLength, "The key length is not at least 32.");
    }

    @Test
    void testGenerateSalt() {
        this.keyStoreService.generateSalt();

        assertNotNull(this.keyStoreConfig.getSaltWithPasswordDerived(), "The salt has not been generated.");
        assertNotNull(this.keyStoreConfig.getSaltWithTotpEncryptionKey(), "The salt has not been generated.");
        assertNotNull(this.keyStoreConfig.getServiceDecryptionSalt(), "The salt has not been generated.");
        assertTrue(this.keyStoreConfig.getSaltWithPasswordDerived().length >= saltLength, "The key length is not at least 16.");
        assertTrue(this.keyStoreConfig.getSaltWithTotpEncryptionKey().length >= saltLength, "The key length isn not at least 16.");
        assertTrue(this.keyStoreConfig.getServiceDecryptionSalt().length >= saltLength, "The key length isn not at least 16.");
    }

    @Test
    void testGenerateTotpEncryptionKey() {
        this.keyStoreService.generateTotpEncryptionKey();

        assertNotNull(this.keyStoreConfig.getTotpEncryptionKey(), "The key has not been generated.");
        assertTrue(this.keyStoreConfig.getTotpEncryptionKey().length >= keyLength, "The key length is not at least 32.");
    }

    @Test
    void testGenerateTotpKey() {
        this.keyStoreService.generateTotpKey();

        assertNotNull(this.keyStoreConfig.getTotpKey(), "The key has not been generated.");
        assertTrue(this.keyStoreConfig.getTotpKey().length == keyLength, "The key length is not at least 32.");
    }
}
