package com.zysn.passwordmanager.model.authentication.registration.impl;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.registration.api.RegistrationService;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;

public class DefaultRegistrationServiceTest {
    private RegistrationService registrationService;

    private SessionManager sessionManager;

    @BeforeEach
    void setup() {        
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();

        this.registrationService = new DefaultRegistrationService(sessionManager);
    }

    @Test
    void testRegister() {
        CollectedUserData collectedUserData = new CollectedUserData();
        collectedUserData.setUsername("test");
        collectedUserData.setPassword("testing password".getBytes());
        collectedUserData.setConfirmPassword("testing password".getBytes());

        byte[] salt = new byte[] { 1, 2, 3 };
        AlgorithmConfig algorithmConfigDerivation = AlgorithmConfigFactory.createAlgorithmConfig("Argon2", salt, null);
        collectedUserData.setPasswordDerivationConfig(algorithmConfigDerivation);

        byte[] iv = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        AlgorithmConfig algorithmConfigEncryption = AlgorithmConfigFactory.createAlgorithmConfig("AES", iv, null);
        collectedUserData.setKeyStoreConfigEncryptionConfig(algorithmConfigEncryption);

        this.registrationService.register(collectedUserData);
    }
}
