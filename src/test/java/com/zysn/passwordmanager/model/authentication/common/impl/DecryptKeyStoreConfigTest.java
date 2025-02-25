package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.security.Security;

import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;
import com.zysn.passwordmanager.model.authentication.keystore.impl.DefaultKeyStoreManager;
import com.zysn.passwordmanager.model.authentication.keystore.impl.KeyStoreConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

public class DecryptKeyStoreConfigTest {
    private AuthenticationStep authenticationStep;

    private KeyStoreManager keyStoreManager;
    private SessionManager sessionManager;
    
    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();

        KeyStoreConfig keyStoreConfig = new KeyStoreConfig();
        keyStoreConfig.setSaltForHKDF(new byte[] { 1, 2, 3, 4, 5 });
        this.sessionManager.setKeyStoreConfig(keyStoreConfig);

        byte[] key = new byte[32];
        Arrays.fill(key, (byte) 1);
        this.sessionManager.getUserAuthKey().setPasswordDerivedKey(key);
        
        byte[] iv = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        AlgorithmConfig tempConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", iv, null);
        this.sessionManager.getUserAuthInfo().setKeyStoreEncryptionConfig(tempConfig);

        byte[] serializedData = EncodingUtils.serializeData(this.sessionManager.getKeyStoreConfig());
        byte[] encryptionKey = this.sessionManager.getUserAuthKey().getPasswordDerivedKey();
        AlgorithmConfig algorithmConfig = this.sessionManager.getUserAuthInfo().getKeyStoreEncryptionConfig();

        CryptoManager cryptoManager = new CryptoManager();
        byte[] encryptedData = cryptoManager.encrypt(serializedData, new SecretKeySpec(encryptionKey, algorithmConfig.getAlgorithmName()), algorithmConfig);

        this.sessionManager.getUserAuthInfo().setKeyStoreConfigEncryptedData(encryptedData);

        this.keyStoreManager = new DefaultKeyStoreManager(sessionManager);
        this.authenticationStep = new DecryptKeyStoreConfig(sessionManager, keyStoreManager);
    }

    @Test
    void testExecuteStep() {
        this.sessionManager.setKeyStoreConfig(null);

        this.authenticationStep.executeStep();

        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, this.sessionManager.getKeyStoreConfig().getSaltForHKDF(), "The actual salt is not the expected one.");
    }
}
