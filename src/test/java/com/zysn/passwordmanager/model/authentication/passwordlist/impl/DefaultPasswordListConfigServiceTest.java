package com.zysn.passwordmanager.model.authentication.passwordlist.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

public class DefaultPasswordListConfigServiceTest {
    private DefaultPasswordListConfigService service;

    @BeforeEach
    public void setup() {
        Security.addProvider(new BouncyCastleProvider());

        service = new DefaultPasswordListConfigService();
    }

    @Test
    void testCreatePasswordListConfig() {
        ServiceCryptoConfig config = service.createPasswordListConfig();

        assertNotNull(config, "Config should not be null");
        assertNotNull(config.getFileName(), "FileName should not be null");
        assertNotNull(config.getSaltForHKDF(), "Salt for HKDF should not be null");
        assertNotNull(config.getSaltForServiceEncryption(), "Salt for service encryption should not be null");
    }

    @Test
    void testDecryptConfig() {
        byte[] key = new byte[32];
        Arrays.fill(key, (byte) 1);
        byte[] iv = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", iv, null);

        ServiceCryptoConfig config = service.createPasswordListConfig();

        byte[] encryptedData = service.encryptConfig(EncodingUtils.serializeData(config), key, algorithmConfig);

        assertNotNull(encryptedData, "Encrypted data should not be null");
    }

    @Test
    void testEncryptConfig() {
        byte[] key = new byte[32];
        Arrays.fill(key, (byte) 1);
        byte[] iv = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", iv, null);

        ServiceCryptoConfig config = service.createPasswordListConfig();

        byte[] encryptedData = service.encryptConfig(EncodingUtils.serializeData(config), key, algorithmConfig);

        byte[] decryptedData = service.decryptConfig(encryptedData, key, algorithmConfig);

        ServiceCryptoConfig decryptedConfig = EncodingUtils.deserializeData(decryptedData,
                new TypeReference<ServiceCryptoConfig>() {
                });

        assertNotNull(decryptedData, "Decrypted data should not be null");
        assertTrue(decryptedConfig.getSaltForHKDF().length == 12, "Salt for HKDF should have length 12");
    }

}
