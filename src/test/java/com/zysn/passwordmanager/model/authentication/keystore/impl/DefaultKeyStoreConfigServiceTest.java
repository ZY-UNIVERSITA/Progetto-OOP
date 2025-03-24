package com.zysn.passwordmanager.model.authentication.keystore.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.security.Security;
import java.util.Arrays;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.entity.impl.UserAuthKey;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

public class DefaultKeyStoreConfigServiceTest {
    private DefaultKeyStoreConfigService keyStoreConfigService;

    @BeforeEach
    void setUp() {                
        Security.addProvider(new BouncyCastleProvider());

        keyStoreConfigService = new DefaultKeyStoreConfigService();
    }

    @Test
    void testGenerateKeyStoreConfigKey() {
        KeyStoreConfig keyStoreConfig = new KeyStoreConfig();
        keyStoreConfigService.generateKeyStoreConfigKey(keyStoreConfig);

        assertNotNull(keyStoreConfig.getKeyStoreEncryptionKey(), "The Key store config key has not been generated.");
    }

    @Test
    void testGenerateKeyStoreConfigSalt() {
        KeyStoreConfig keyStoreConfig = new KeyStoreConfig();
        keyStoreConfigService.generateKeyStoreConfigSalt(keyStoreConfig);

        assertNotNull(keyStoreConfig.getSaltWithPasswordDerived(), "The salt has not been generated.");
        assertNotNull(keyStoreConfig.getSaltWithTotpEncryptionKey(), "The salt has not been generated.");
        assertNotNull(keyStoreConfig.getSaltForHKDF(), "The salt has not been generated.");
        assertNotNull(keyStoreConfig.getServiceDecryptionSalt(), "The salt has not been generated.");
    }

    @Test
    void testGenerateKeyStoreEntry() {
        UserAuthKey userAuthKey = new UserAuthKey();
        keyStoreConfigService.generateKeyStoreEntry(userAuthKey);

        assertNotNull(userAuthKey.getTotpEncryptionKey(), "The key has not been generated.");
        assertNotNull(userAuthKey.getTotpKey(), "The key has not been generated.");
    }

    @Test
    void testEncryptConfig() {
        KeyStoreConfig keyStoreConfig = new KeyStoreConfig();
        keyStoreConfig.setSaltForHKDF(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
        byte[] key = new byte[32];
        Arrays.fill(key, (byte) 1);
        AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", keyStoreConfig.getSaltForHKDF(), null);
        
        byte[] data = EncodingUtils.serializeData(keyStoreConfig);

        byte[] encryptedData = keyStoreConfigService.encryptConfig(data, key, algorithmConfig);

        assertNotNull(encryptedData, "The encrypted data is null.");
    }

    @Test
    void testDecryptConfig() {
        KeyStoreConfig keyStoreConfig = new KeyStoreConfig();
        keyStoreConfig.setSaltForHKDF(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
        byte[] key = new byte[32];
        Arrays.fill(key, (byte) 1);
        AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", keyStoreConfig.getSaltForHKDF(), null);

        byte[] data = EncodingUtils.serializeData(keyStoreConfig);

        byte[] encryptedData = keyStoreConfigService.encryptConfig(data, key, algorithmConfig);

        KeyStoreConfig decryptedData = keyStoreConfigService.decryptConfig(encryptedData, key, algorithmConfig);

        assertNotNull(decryptedData, "The decrypted data is null.");
        assertArrayEquals(keyStoreConfig.getSaltForHKDF(), decryptedData.getSaltForHKDF(), "The salt is not the expected key.");
    }
}
