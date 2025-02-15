package com.zysn.passwordmanager.model.security.algorithm.derivation.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;

public class BCryptTest {
    private KeyDerivationAlgorithm keyDerivationAlgorithm;
    private byte[] source;
    private byte[] salt;
    private AlgorithmConfig algorithmConfig;
    private byte[] expectedMasterKey;

    @BeforeEach
    void setup() {
        keyDerivationAlgorithm = new Bcrypt();
        source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes();
        salt = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96, -89, -25, 34, -102 };

        algorithmConfig = new AlgorithmConfig("Bcrypt", "Key Derivation Algorithm", salt, new HashMap<>());

        String iterationsName = "cost";
        String iterationsValue = "12";
        algorithmConfig.addNewParameter(iterationsName, iterationsValue);

        expectedMasterKey = new byte[] {
            -43, -44, 54, 52, 108, -27, -46, -42, 123, -125, 84, 77, 54, -47, 37, 22,
            117, 104, 108, 32, -21, -114, -42, -91 };
    }

    @Test
    void testDeriveKey() {
        byte[] actualMasterKey = keyDerivationAlgorithm.deriveKey(source, algorithmConfig);

        assertNotNull(actualMasterKey, "The Master Key is null.");
        assertArrayEquals(expectedMasterKey, actualMasterKey, "The Master Key arrays do not match.");
    }
}
