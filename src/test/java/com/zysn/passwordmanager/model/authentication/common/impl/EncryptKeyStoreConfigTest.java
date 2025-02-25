package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;
import com.zysn.passwordmanager.model.authentication.keystore.impl.DefaultKeyStoreManager;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;

public class EncryptKeyStoreConfigTest {
    private AuthenticationStep authenticationStep;

    private SessionManager sessionManager;
    private KeyStoreManager keyStoreManager;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();

        byte[] iv = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", iv, null);
        this.sessionManager.getUserAuthInfo().setKeyStoreEncryptionConfig(algorithmConfig);

        byte[] key = new byte[32];
        Arrays.fill(key, (byte) 1);
        this.sessionManager.getUserAuthKey().setPasswordDerivedKey(key);

        this.keyStoreManager = new DefaultKeyStoreManager(sessionManager);

        this.authenticationStep = new EncryptKeyStoreConfig(sessionManager, keyStoreManager);
    }

    @Test
    void testExecuteStep() {
        this.authenticationStep.executeStep();

        assertNotNull(this.sessionManager.getUserAuthInfo().getKeyStoreConfigEncryptedData(), "The data has not been encrypted.");
    }
}
