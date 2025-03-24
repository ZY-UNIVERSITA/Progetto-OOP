package com.zysn.passwordmanager.model.account.entity.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;

public class UserAuthInfoTest {
    private UserAuthInfo userAuthInfo;
    private AlgorithmConfig originalPasswordDerivedKeyConfig;
    private AlgorithmConfig originalKeyStoreEncryptionConfig;
    private byte[] originalKeyStoreConfigEncryptedData;
    private byte[] originalServiceConfigEncryptedData;
    private byte[] fullZero;

    @BeforeEach
    public void setUp() {
        userAuthInfo = new UserAuthInfo("testUsername");

        originalPasswordDerivedKeyConfig = new AlgorithmConfig("PBKDF2", "HMACSHA256");
        originalKeyStoreEncryptionConfig = new AlgorithmConfig("AES", "GCM");

        // Configuriamo i campi della classe
        userAuthInfo.setPasswordDerivedKeyConfig(originalPasswordDerivedKeyConfig);
        userAuthInfo.setKeyStoreEncryptionConfig(originalKeyStoreEncryptionConfig);
        userAuthInfo.setKeyStoreConfigEncryptedData(new byte[]{10, 20, 30});
        userAuthInfo.setServiceConfigEncryptedData(new byte[]{40, 50, 60});
        userAuthInfo.setEnabled2FA(true);

        // Conserviamo i riferimenti degli array originali
        originalKeyStoreConfigEncryptedData = userAuthInfo.getKeyStoreConfigEncryptedData();
        originalServiceConfigEncryptedData = userAuthInfo.getServiceConfigEncryptedData();

        // Creiamo un array di confronto pieno di zeri
        fullZero = new byte[3];
        Arrays.fill(fullZero, (byte) 0);
    }

    @Test
    public void testDestroy() { 
        userAuthInfo.destroy();

        assertNull(userAuthInfo.getUsername(), "Username should be null");

        assertNull(userAuthInfo.getPasswordDerivedKeyConfig(), "Password config should be null after destroy");
        assertNull(userAuthInfo.getKeyStoreEncryptionConfig(), "KeyStore config should be null after destroy");

        assertNull(originalPasswordDerivedKeyConfig.getAlgorithmName(), "Algorithm name of PasswordDerivedKeyConfig should be null after destroy");
        assertNull(originalPasswordDerivedKeyConfig.getAlgorithmType(), "Algorithm type of PasswordDerivedKeyConfig should be null after destroy");
        assertNull(originalKeyStoreEncryptionConfig.getAlgorithmName(), "Algorithm name of KeyStoreEncryptionConfig should be null after destroy");
        assertNull(originalKeyStoreEncryptionConfig.getAlgorithmType(), "Algorithm type of KeyStoreEncryptionConfig should be null after destroy");

        assertNull(userAuthInfo.getKeyStoreConfigEncryptedData(), "KeyStore Config Encrypted Data should be null after destroy");
        assertNull(userAuthInfo.getServiceConfigEncryptedData(), "Service config Encrypted Data should be null after destroy");

        assertArrayEquals(fullZero, originalKeyStoreConfigEncryptedData, "KeyStore Config Encrypted Data array should be zeroed out");
        assertArrayEquals(fullZero, originalServiceConfigEncryptedData, "Service Config Encrypted Data array should be zeroed out");

        assertFalse(userAuthInfo.isEnabled2FA(), "2fa should be false after destroy");
    }
}