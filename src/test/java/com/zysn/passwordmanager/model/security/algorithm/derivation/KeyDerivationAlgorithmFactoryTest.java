package com.zysn.passwordmanager.model.security.algorithm.derivation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KeyDerivationAlgorithmFactoryTest {

    @BeforeEach
    void setup() {
    }

    @Test
    void testCreateAlgorithm() {
        String algorithmName = "PBKDF2";
        KeyDerivationAlgorithm keyDerivationAlgorithm = KeyDerivationAlgorithmFactory.createAlgorithm(algorithmName);

        assertTrue(keyDerivationAlgorithm instanceof PBKDF2, "The object is not an istance of 'PBKDF2'");
    }
}
