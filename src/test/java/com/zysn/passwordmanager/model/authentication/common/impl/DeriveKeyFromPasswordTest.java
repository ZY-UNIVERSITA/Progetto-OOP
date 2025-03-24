package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;

public class DeriveKeyFromPasswordTest {
    private AuthenticationStep authenticationStep;
    private SessionManager sessionManager;
    private CryptoManager cryptoManager;

    @BeforeEach
    void setup() {
        this.sessionManager =new DefaultSessionManager();
        this.sessionManager.getUserAuthKey().setPassword("test".getBytes());
        
        byte[] salt = new byte[] { 1, 2, 3 };
        AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("Argon2", salt, null);
        this.sessionManager.getUserAuthInfo().setPasswordDerivedKeyConfig(algorithmConfig);
        algorithmConfig.updateParameter("key_size", "192");

        this.cryptoManager = new CryptoManager();

        this.authenticationStep = new DeriveKeyFromPassword(sessionManager, cryptoManager);
    }

    @Test
    void testExecuteStepLongerLength() {
        AlgorithmConfig algorithmConfigEncryption = AlgorithmConfigFactory.createAlgorithmConfig("AES", null, null);
        this.sessionManager.getUserAuthInfo().setKeyStoreEncryptionConfig(algorithmConfigEncryption);

        this.authenticationStep.executeStep();

        byte[] deriveKey = this.sessionManager.getUserAuthKey().getPasswordDerivedKey();

        System.err.println(Arrays.toString(deriveKey));

        assertNotNull(deriveKey, "The derived key is null.");
    }

    @Test
    void testExecuteStepSameLength() {
        AlgorithmConfig algorithmConfigEncryption = AlgorithmConfigFactory.createAlgorithmConfig("AES", null, null);
        algorithmConfigEncryption.updateParameter("key_size", "192");
        this.sessionManager.getUserAuthInfo().setKeyStoreEncryptionConfig(algorithmConfigEncryption);

        this.authenticationStep.executeStep();

        byte[] deriveKey = this.sessionManager.getUserAuthKey().getPasswordDerivedKey();

        System.err.println(Arrays.toString(deriveKey));

        assertNotNull(deriveKey, "The derived key is null.");
    }

    @Test
    void testExecuteStepShorterLength() {
        AlgorithmConfig algorithmConfigEncryption = AlgorithmConfigFactory.createAlgorithmConfig("AES", null, null);
        algorithmConfigEncryption.updateParameter("key_size", "128");
        this.sessionManager.getUserAuthInfo().setKeyStoreEncryptionConfig(algorithmConfigEncryption);

        this.authenticationStep.executeStep();

        byte[] deriveKey = this.sessionManager.getUserAuthKey().getPasswordDerivedKey();

        System.err.println(Arrays.toString(deriveKey));

        assertNotNull(deriveKey, "The derived key is null.");
    }
}
