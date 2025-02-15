package com.zysn.passwordmanager.model.security.algorithm.derivation.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Argon2;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Bcrypt;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.HKDF;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Scrypt;

public class KeyDerivationFactoryTest {

    private static final String ARGON2 = "ARGON2";
    private static final String SCRYPT = "SCRYPT";
    private static final String BCRYPT = "BCRYPT";
    private static final String HKDF = "HKDF";
    private static final String INVALID = "INVALID";
    private static final String NULL_NAME = null;
    private static final String EMPTY_NAME = "";

    @Test
    public void testCreateAlgorithmArgon2() {
        KeyDerivationAlgorithm algorithm = KeyDerivationFactory.createAlgorithm(ARGON2);
        assertNotNull(algorithm, "The algorithm is null.");
        assertTrue(algorithm instanceof Argon2, "The algorithm is not an instance of Argon2.");
    }

    @Test
    public void testCreateAlgorithmScrypt() {
        KeyDerivationAlgorithm algorithm = KeyDerivationFactory.createAlgorithm(SCRYPT);
        assertNotNull(algorithm, "The algorithm is null.");
        assertTrue(algorithm instanceof Scrypt, "The algorithm is not an instance of Scrypt.");
    }

    @Test
    public void testCreateAlgorithmBcrypt() {
        KeyDerivationAlgorithm algorithm = KeyDerivationFactory.createAlgorithm(BCRYPT);
        assertNotNull(algorithm, "The algorithm is null.");
        assertTrue(algorithm instanceof Bcrypt, "The algorithm is not an instance of Bcrypt.");
    }

    @Test
    public void testCreateAlgorithmHKDF() {
        KeyDerivationAlgorithm algorithm = KeyDerivationFactory.createAlgorithm(HKDF);
        assertNotNull(algorithm, "The algorithm is null.");
        assertTrue(algorithm instanceof HKDF, "The algorithm is not an instance of HKDF.");
    }

    @Test
    public void testCreateAlgorithmInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            KeyDerivationFactory.createAlgorithm(INVALID);
        }, "Expected an IllegalArgumentException to be thrown for an invalid name.");
    }

    @Test
    public void testCreateAlgorithmNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            KeyDerivationFactory.createAlgorithm(NULL_NAME);
        }, "Expected an IllegalArgumentException to be thrown for a null name.");
    }

    @Test
    public void testCreateAlgorithmEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            KeyDerivationFactory.createAlgorithm(EMPTY_NAME);
        }, "Expected an IllegalArgumentException to be thrown for an empty name.");
    }
}
