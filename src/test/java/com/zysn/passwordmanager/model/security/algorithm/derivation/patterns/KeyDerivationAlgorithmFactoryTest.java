package com.zysn.passwordmanager.model.security.algorithm.derivation.patterns;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Argon2;

public class KeyDerivationAlgorithmFactoryTest {

    @BeforeEach
    void setup() {
    }

    @Test
    void testCreateAlgorithm() {
        String algorithmName = "Argon2";
        KeyDerivationAlgorithm keyDerivationAlgorithm = KeyDerivationAlgorithmFactory.createAlgorithm(algorithmName);

        assertTrue(keyDerivationAlgorithm instanceof Argon2, "The object is not an istance of 'Argon2'");
    }

    @Test
    void testCreateWrongAlgorithm() {
        String algorithmName = "Argon-1";

        assertThrows(IllegalArgumentException.class, () -> {
            KeyDerivationAlgorithmFactory.createAlgorithm(algorithmName);
        });
    }
}
