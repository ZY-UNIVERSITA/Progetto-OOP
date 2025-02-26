package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.Security;

import javax.crypto.spec.SecretKeySpec;

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
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

public class DecryptServiceConfigTest {
    private AuthenticationStep authenticationStep;

    private SessionManager sessionManager;
    private ServiceCryptoConfigManager serviceCryptoConfigManager;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();

        final byte[] key = new byte[32];
        Arrays.fill(key, (byte) 1);
        this.sessionManager.getUserAuthKey().setServiceConfigKey(key);

        final byte[] iv = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        this.sessionManager.getKeyStoreConfig().setServiceDecryptionSalt(iv); 

        this.serviceCryptoConfigManager = new DefaultServiceCryptoConfigManager(sessionManager);

        final ServiceCryptoConfig serviceCryptoConfig = new ServiceCryptoConfig();
        serviceCryptoConfig.setFileName("prova".toCharArray());

        final byte[] serializedData = EncodingUtils.serializeData(serviceCryptoConfig);

        final CryptoManager cryptoManager = new CryptoManager();
        final AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", iv, null);
        final byte[] encryptedData = cryptoManager.encrypt(serializedData, new SecretKeySpec(key, algorithmConfig.getAlgorithmName()), algorithmConfig);

        this.sessionManager.getUserAuthInfo().setServiceConfigEncryptedData(encryptedData);

        this.authenticationStep = new DecryptServiceConfig(sessionManager, serviceCryptoConfigManager);
    }

    @Test
    void testExecuteStep() {
        this.authenticationStep.executeStep();

        assertNotNull(this.sessionManager.getServiceConfig().getFileName(), "The file name is null.");
        assertArrayEquals("prova".toCharArray(), this.sessionManager.getServiceConfig().getFileName(), "The file name is not the expected one.");
    }
}
