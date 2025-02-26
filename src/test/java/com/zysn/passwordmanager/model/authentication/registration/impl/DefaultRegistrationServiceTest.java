package com.zysn.passwordmanager.model.authentication.registration.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.registration.api.RegistrationService;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.service.ServiceManager;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;

public class DefaultRegistrationServiceTest {
    private RegistrationService registrationService;

    private SessionManager sessionManager;
    private CryptoManager cryptoManager;

    private CollectedUserData collectedUserData;

    private FileManager fileManagerUsers;
    private FileManager fileManagerKeyStore;
    private FileManager fileManagerService;

    @BeforeEach
    void setup() {        
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();

        this.registrationService = new DefaultRegistrationService(sessionManager);

        this.cryptoManager = new CryptoManager();

        ServiceManager serviceManager = ServiceManager.getInstance();
        serviceManager.setCryptoManager(cryptoManager);
        
        this.collectedUserData = new CollectedUserData();
        collectedUserData.setUsername("test");
        collectedUserData.setPassword("testing password".getBytes());
        collectedUserData.setConfirmPassword("testing password".getBytes());

        byte[] salt = new byte[] { 1, 2, 3 };
        AlgorithmConfig algorithmConfigDerivation = AlgorithmConfigFactory.createAlgorithmConfig("Argon2", salt, null);
        collectedUserData.setPasswordDerivationConfig(algorithmConfigDerivation);

        byte[] iv = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        AlgorithmConfig algorithmConfigEncryption = AlgorithmConfigFactory.createAlgorithmConfig("AES", iv, null);
        collectedUserData.setKeyStoreConfigEncryptionConfig(algorithmConfigEncryption);

        this.fileManagerUsers = new DefaultFileManager(PathsConstant.USER_PERSONAL, ExtensionsConstant.JSON);
        this.fileManagerKeyStore = new DefaultFileManager(PathsConstant.KEY_STORE, ExtensionsConstant.BCFKS);
        this.fileManagerService = new DefaultFileManager(PathsConstant.SERVICE, ExtensionsConstant.ENC);
    }

    @Test
    void testRegister() {
        this.registrationService.register(collectedUserData);

        System.err.println(String.valueOf(this.sessionManager.getServiceConfig().getFileName()));

        assertTrue(this.fileManagerUsers.createPath(this.sessionManager.getUserAccount().getUsername()).toFile().exists(), "User file has not been created.");
        assertTrue(this.fileManagerKeyStore.createPath(this.sessionManager.getUserAccount().getUsername()).toFile().exists(), "Key store has not been created.");
        assertTrue(this.fileManagerService.createPath(String.valueOf(this.sessionManager.getServiceConfig().getFileName())).toFile().exists(), "Services file has not been created.");
    }
}
