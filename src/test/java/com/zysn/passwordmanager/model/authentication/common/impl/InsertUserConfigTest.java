package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;

public class InsertUserConfigTest {
    private AuthenticationStep authenticationStep;

    private SessionManager sessionManager;
    private CollectedUserData collectedUserData;

    @BeforeEach
    void setup() {
        this.sessionManager = new DefaultSessionManager();
        this.collectedUserData = new CollectedUserData();

        this.collectedUserData.setPasswordDerivationConfig(AlgorithmConfigFactory.createAlgorithmConfig("Argon2", null, null));
        this.collectedUserData.setKeyStoreConfigEncryptionConfig(AlgorithmConfigFactory.createAlgorithmConfig("AES", null, null));

        this.authenticationStep = new InsertUserConfig(sessionManager, collectedUserData);
    }

    @Test
    void testExecuteStep() {
        this.authenticationStep.executeStep();
        
        assertNotNull(this.sessionManager.getUserAuthInfo().getPasswordDerivedKeyConfig(), "The derivation config is null.");
        assertNotNull(this.sessionManager.getUserAuthInfo().getPasswordDerivedKeyConfig(), "The encryption config is null.");

        assertTrue(this.sessionManager.getUserAuthInfo().getPasswordDerivedKeyConfig().getAlgorithmName().equalsIgnoreCase("Argon2"), "The algorithm name is not argon2.");
        assertTrue(this.sessionManager.getUserAuthInfo().getKeyStoreEncryptionConfig().getAlgorithmName().equalsIgnoreCase("AES"), "The algorithm name is not AES.");
    }
}
