package com.zysn.passwordmanager.model.security.algorithm.encryption.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.encryption.api.EncryptionAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.encryption.impl.AES;

public class EncryptionAlgorithmFactoryTest {
    @Test
    void testCreateAlgorithm() {
        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithmFactory.createAlgorithm("AES");

        assertNotNull(encryptionAlgorithm, "The created algorithm is null.");
        assertTrue(encryptionAlgorithm instanceof AES, "The created algorithm is not an instance of AES.");
    }
}
