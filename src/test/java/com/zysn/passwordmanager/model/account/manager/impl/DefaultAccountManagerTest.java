package com.zysn.passwordmanager.model.account.manager.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.login.api.LoginService;
import com.zysn.passwordmanager.model.authentication.login.impl.DefaultLoginService;
import com.zysn.passwordmanager.model.authentication.registration.api.RegistrationService;
import com.zysn.passwordmanager.model.authentication.registration.impl.DefaultRegistrationService;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.service.ServiceManager;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;

public class DefaultAccountManagerTest {
    private SessionManager sessionManager; 
    private CryptoManager cryptoManager;
    private ServiceManager serviceManager;
    private RegistrationService registrationService; 
    private LoginService loginService; 

    private DefaultAccountManager accountManager;

    private CollectedUserData collectedUserData;

    private FileManager serviceFileManager;
    private FileManager keyStoreFileManager;
    private FileManager userFileManager;

    @BeforeEach
    public void setUp() {
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();

        this.serviceManager = ServiceManager.getInstance();

        this.cryptoManager = new CryptoManager();
        this.serviceManager.setCryptoManager(cryptoManager);

        this.registrationService = new DefaultRegistrationService(sessionManager);
        this.loginService = new DefaultLoginService(sessionManager);

        this.accountManager = new DefaultAccountManager(sessionManager, serviceManager, registrationService, loginService);

        this.collectedUserData = new CollectedUserData();
        this.collectedUserData.setUsername("prova");
        this.collectedUserData.setPassword(new byte[] { 1, 2, 3 });
        this.collectedUserData.setConfirmPassword(new byte[] { 1, 2, 3});
        this.collectedUserData.setPasswordDerivationConfig(AlgorithmConfigFactory.createAlgorithmConfig("Argon2", new byte[] { 1, 2, 3 }, null));
        this.collectedUserData.setKeyStoreConfigEncryptionConfig(AlgorithmConfigFactory.createAlgorithmConfig("AES", new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 }, null));
        this.collectedUserData.setEnabled2FA(true);

        serviceFileManager = new DefaultFileManager(PathsConstant.SERVICE, ExtensionsConstant.ENC);
        keyStoreFileManager = new DefaultFileManager(PathsConstant.KEY_STORE, ExtensionsConstant.BCFKS);
        userFileManager = new DefaultFileManager(PathsConstant.USER_PERSONAL, ExtensionsConstant.JSON);
    }

    @Test
    public void testRegister() {
        this.accountManager.register(collectedUserData);

        assertTrue(serviceFileManager.createPath(String.valueOf(this.sessionManager.getServiceConfig().getFileName())).toFile().exists(), "The service file has not been created");
        assertTrue(keyStoreFileManager.createPath("prova").toFile().exists(), "The key store file has not been created");
        assertTrue(userFileManager.createPath("prova").toFile().exists(), "The user file has not been created");
    }

    @Test
    public void testLogin() {
        this.accountManager.register(collectedUserData);

        byte[] masterKey = Arrays.copyOf(this.sessionManager.getUserAccount().getMasterKey(), this.sessionManager.getUserAccount().getMasterKey().length);

        this.accountManager.logout();
        
        this.accountManager.login(collectedUserData);

        assertEquals("prova", this.sessionManager.getUserAccount().getUsername(), "The username is not the exptected one.");
        assertArrayEquals(masterKey, this.sessionManager.getUserAccount().getMasterKey(), "The master key is not the generated in the registration phase.");
    }

    @Test
    public void testLogout() {
        this.accountManager.register(collectedUserData);

        char[] fileName = Arrays.copyOf(this.sessionManager.getServiceConfig().getFileName(), this.sessionManager.getServiceConfig().getFileName().length);

        this.accountManager.logout();

        assertNull(this.sessionManager.getUserAccount().getMasterKey(), "The master key has not been erased.");
        assertNull(this.sessionManager.getUserAccount().getUsername(), "The username has not been erased.");

        this.sessionManager.getServiceConfig().setFileName(fileName);
    }

    @AfterEach
    public void cleanup() {
        this.serviceFileManager.createPath(String.valueOf(this.sessionManager.getServiceConfig().getFileName())).toFile().delete();
        this.keyStoreFileManager.createPath("prova").toFile().delete();
        this.userFileManager.createPath("prova").toFile().delete();
    }
}
