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
import com.zysn.passwordmanager.model.authentication.passwordlist.api.ServiceCryptoConfigManager;
import com.zysn.passwordmanager.model.authentication.passwordlist.impl.DefaultServiceCryptoConfigManager;
import com.zysn.passwordmanager.model.authentication.passwordlist.impl.ServiceCryptoConfig;

public class EncryptServiceConfigTest {
    private AuthenticationStep authenticationStep;

    private SessionManager sessionManager;
    private ServiceCryptoConfigManager serviceCryptoConfigManager; 

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();

        byte[] key = new byte[32];
        Arrays.fill(key, (byte) 1);
        this.sessionManager.getUserAuthKey().setServiceConfigKey(key);

        byte[] iv = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        this.sessionManager.getKeyStoreConfig().setServiceDecryptionSalt(iv);

        ServiceCryptoConfig serviceCryptoConfig = new ServiceCryptoConfig();
        serviceCryptoConfig.setFileName("nome".toCharArray());
        this.sessionManager.setServiceConfig(serviceCryptoConfig);

        this.serviceCryptoConfigManager = new DefaultServiceCryptoConfigManager(sessionManager);

        this.authenticationStep = new EncryptServiceConfig(sessionManager, serviceCryptoConfigManager);
    }

    @Test
    void testExecuteStep() {
        this.authenticationStep.executeStep();

        assertNotNull(this.sessionManager.getUserAuthInfo().getServiceConfigEncryptedData(), "The config has not been encrypted.");
    }
}
