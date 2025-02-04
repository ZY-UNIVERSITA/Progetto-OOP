package com.zysn.passwordmanager.model.security.algorithm.derivation.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;

public class BCryptTest {
    private KeyDerivationAlgorithm keyDerivationAlgorithm;
    private char[] source;
    private byte[] salt;
    private AlgorithmConfig algorithmConfig;

    @BeforeEach
    void setup() {
        keyDerivationAlgorithm = new Bcrypt();
        source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        salt = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96, -89, -25, 34, -102 };

        algorithmConfig = new AlgorithmConfig("Key Derivation Algorithm", "Bcrypt", salt, new HashMap<>());

        String iterationsName = "cost";
        String iterationsValue = "12";
        algorithmConfig.addNewParameter(iterationsName, iterationsValue);

    }

    @Test
    void testDeriveKey() {
        byte[] actualMasterKey = keyDerivationAlgorithm.deriveKey(source, algorithmConfig);

        byte[] expectedMasterKey = new byte[] {
                -43, -44, 54, 52, 108, -27, -46, -42, 123, -125, 84, 77, 54, -47, 37, 22,
                117, 104, 108, 32, -21, -114, -42, -91 };

        assertArrayEquals(expectedMasterKey, actualMasterKey, "The Master Key arrays do not match.");
    }
}
