package com.zysn.passwordmanager.model.security.algorithm.encryption.patterns;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.encryption.api.EncryptionAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.encryption.impl.AES;

public class EncryptionAlgorithmFactoryTest {
    @BeforeEach
    void setup() {
    }

    @Test
    void testCreateAlgorithm() {
        String algorithmName = "AES";
        EncryptionAlgorithm keyDerivationAlgorithm = EncryptionAlgorithmFactory.createAlgorithm(algorithmName);

        assertTrue(keyDerivationAlgorithm instanceof AES, "The object is not an istance of 'AES'");
    }

    @Test
    void testCreateWrongAlgorithm() {
        String algorithmName = "AES-Fake";

        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionAlgorithmFactory.createAlgorithm(algorithmName);
        }, "The method doesn't throw an exception.");
    }
}
