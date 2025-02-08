package com.zysn.passwordmanager.model.authentication.keystore.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KeyStoreConfigTest {
    private KeyStoreConfig keyStoreConfig;
    private byte[] keyStoreEncryptionKey = new byte[] { 1, 2, 3 };
    private byte[] totpKey = new byte[] { 4, 5, 6 };

    @BeforeEach
    void setup() {
        this.keyStoreConfig = new KeyStoreConfig();

        this.keyStoreConfig.setKeyStoreEncryptionKey(keyStoreEncryptionKey);
        this.keyStoreConfig.setTotpKey(totpKey);
    }

    @Test
    void testDestroy() {
        this.keyStoreConfig.destroy();

        assertNull(this.keyStoreConfig.getKeyStoreEncryptionKey(), "The key store encryption key is not null.");
        assertNull(this.keyStoreConfig.getTotpKey(), "The totp key is not null.");

        byte[] fullZeros = new byte[] { (byte) 0, (byte) 0, (byte) 0 };

        assertArrayEquals(fullZeros, keyStoreEncryptionKey, "The key has not been cleaned");
        assertArrayEquals(fullZeros, totpKey, "The key has not been cleaned.");
    }

    @Test
    void testSerializeObject() {
        byte[] expectedSerializedArray = new byte[] { 123, 34, 107, 101, 121, 83, 116, 111, 114, 101, 69, 110, 99, 114,
                121, 112, 116, 105, 111, 110, 75, 101, 121, 34, 58, 34, 65, 81, 73, 68, 34, 44, 34, 115, 97, 108, 116,
                87, 105, 116, 104, 80, 97, 115, 115, 119, 111, 114, 100, 68, 101, 114, 105, 118, 101, 100, 34, 58, 110,
                117, 108, 108, 44, 34, 115, 97, 108, 116, 87, 105, 116, 104, 84, 111, 116, 112, 69, 110, 99, 114, 121,
                112, 116, 105, 111, 110, 75, 101, 121, 34, 58, 110, 117, 108, 108, 44, 34, 115, 101, 114, 118, 105, 99,
                101, 68, 101, 99, 114, 121, 112, 116, 105, 111, 110, 83, 97, 108, 116, 34, 58, 110, 117, 108, 108,
                125 };

        byte[] actualSerializedArray = this.keyStoreConfig.serializeObject();

        assertArrayEquals(expectedSerializedArray, actualSerializedArray, "The serialized object is not the same as expected.");
    }
}
