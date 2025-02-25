package com.zysn.passwordmanager.model.authentication.keystore.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

public class KeyStoreConfigTest {
    private KeyStoreConfig keyStoreConfig;

    private byte[] initialKeyStoreEncryptionKey;
    private byte[] initialSaltWithPasswordDerived;
    private byte[] initialSaltWithTotpEncryptionKey;
    private byte[] initialSaltForHKDF;
    private byte[] initialServiceDecryptionSalt;

    @BeforeEach
    void setup() {
        this.keyStoreConfig = new KeyStoreConfig();

        this.initialKeyStoreEncryptionKey = new byte[] { 1, 2, 3 };
        this.initialSaltWithPasswordDerived = new byte[] { 4, 5, 6 };
        this.initialSaltWithTotpEncryptionKey = new byte[] { 7, 8, 9 };
        this.initialSaltForHKDF = new byte[] { 10, 11, 12 };
        this.initialServiceDecryptionSalt = new byte[] { 13, 14, 15 };

        this.keyStoreConfig.setKeyStoreEncryptionKey(this.initialKeyStoreEncryptionKey);
        this.keyStoreConfig.setSaltWithPasswordDerived(this.initialSaltWithPasswordDerived);
        this.keyStoreConfig.setSaltWithTotpEncryptionKey(this.initialSaltWithTotpEncryptionKey);
        this.keyStoreConfig.setSaltForHKDF(this.initialSaltForHKDF);
        this.keyStoreConfig.setServiceDecryptionSalt(this.initialServiceDecryptionSalt);
    }

    @Test
    void testDestroy() {
        this.keyStoreConfig.destroy();

        assertNull(this.keyStoreConfig.getKeyStoreEncryptionKey(), "KeyStoreEncryptionKey should be null");
        assertNull(this.keyStoreConfig.getSaltWithPasswordDerived(), "SaltWithPasswordDerived should be null");
        assertNull(this.keyStoreConfig.getSaltWithTotpEncryptionKey(), "SaltWithTotpEncryptionKey should be null");
        assertNull(this.keyStoreConfig.getSaltForHKDF(), "SaltForHKDF should be null");
        assertNull(this.keyStoreConfig.getServiceDecryptionSalt(), "ServiceDecryptionSalt should be null");

        byte[] zeroBytes = new byte[] { 0, 0, 0 };

        assertArrayEquals(zeroBytes, this.initialKeyStoreEncryptionKey,
                "Initial KeyStoreEncryptionKey should be zeroed out");
        assertArrayEquals(zeroBytes, this.initialSaltWithPasswordDerived,
                "Initial SaltWithPasswordDerived should be zeroed out");
        assertArrayEquals(zeroBytes, this.initialSaltWithTotpEncryptionKey,
                "Initial SaltWithTotpEncryptionKey should be zeroed out");
        assertArrayEquals(zeroBytes, this.initialSaltForHKDF, "Initial SaltForHKDF should be zeroed out");
        assertArrayEquals(zeroBytes, this.initialServiceDecryptionSalt,
                "Initial ServiceDecryptionSalt should be zeroed out");
    }

    @Test
    void testSerializeObject() {
        byte[] actualSerializedArray = this.keyStoreConfig.serialize();

        KeyStoreConfig keyStoreConfigDeserialized = EncodingUtils.deserializeData(actualSerializedArray, new TypeReference<KeyStoreConfig>() {
            
        });

        byte[] actualKeyStoreEncryptionKey = keyStoreConfigDeserialized.getKeyStoreEncryptionKey();
        byte[] actualSaltForHKDF = keyStoreConfigDeserialized.getSaltForHKDF();

        assertArrayEquals(initialKeyStoreEncryptionKey, actualKeyStoreEncryptionKey);
        assertArrayEquals(initialSaltForHKDF, actualSaltForHKDF);
    }
}
