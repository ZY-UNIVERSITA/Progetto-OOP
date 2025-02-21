package com.zysn.passwordmanager.model.authentication.passwordlist.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.security.Security;
import java.util.Arrays;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;

public class DefaultServiceCryptoConfigManagerTest {
    private DefaultServiceCryptoConfigManager defaultServiceCryptoConfigManager;
    private SessionManager sessionManager;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());
        
        this.sessionManager = new DefaultSessionManager();

        this.defaultServiceCryptoConfigManager = new DefaultServiceCryptoConfigManager(sessionManager);

        byte[] key = new byte[32];
        Arrays.fill(key, (byte) 1);
        this.sessionManager.getUserAuthKey().setPasswordListConfigKey(key);

        byte[] iv = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        this.sessionManager.getKeyStoreConfig().setServiceDecryptionSalt(iv);
    }

    @Test
    void testCreateServiceConfig() {
        this.defaultServiceCryptoConfigManager.createServiceConfig();

        assertNotNull(this.sessionManager.getServiceConfig(), "The service crypto config file has not been created.");
    }

    @Test
    void testDecryptConfig() {
        this.defaultServiceCryptoConfigManager.createServiceConfig();

        this.defaultServiceCryptoConfigManager.encryptConfig();

        this.sessionManager.setServiceConfig(null);

        this.defaultServiceCryptoConfigManager.decryptConfig();

        assertNotNull(this.sessionManager.getServiceConfig(), "The config file has not been decrypted.");

        assertNotNull(this.sessionManager.getServiceConfig().getFileName(), "The file name is null.");
    }

    @Test
    void testEncryptConfig() {
        this.defaultServiceCryptoConfigManager.createServiceConfig();

        this.defaultServiceCryptoConfigManager.encryptConfig();

        byte[] encryptedData = this.sessionManager.getUserAuthInfo().getServiceConfigEncryptedData();

        assertNotNull(encryptedData, "The service crypto config has not been encrypted");
    }
}
