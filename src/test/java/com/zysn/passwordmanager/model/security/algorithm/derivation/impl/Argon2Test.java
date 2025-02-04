package com.zysn.passwordmanager.model.security.algorithm.derivation.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;

public class Argon2Test {
    private KeyDerivationAlgorithm keyDerivationAlgorithm;
    private char[] source;
    private byte[] salt;
    private AlgorithmConfig algorithmConfig;

    @BeforeEach
    void setup() {
        keyDerivationAlgorithm = new Argon2();
        source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        salt = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96, -89, -25, 34, -102, 77, -20, -8, -41,
                92, 87, 17, 35, 75, -11, -87, -87, -54, 22, 110, 8 };

        algorithmConfig = new AlgorithmConfig("Argon2", "Key Derivation Algorithm", salt, new HashMap<>());

        String variantName = "variant";
        String variantValue = "argon2id";
        algorithmConfig.addNewParameter(variantName, variantValue);

        String iterationsName = "iterations";
        String iterationsValue = "3";
        algorithmConfig.addNewParameter(iterationsName, iterationsValue);

        String memoryCostName = "memory_cost";
        String memoryCostValue = "8192";
        algorithmConfig.addNewParameter(memoryCostName, memoryCostValue);

        String parallelismName = "parallelism";
        String parallelismValue = "1";
        algorithmConfig.addNewParameter(parallelismName, parallelismValue);

        String keySizeName = "key_size";
        String keySizeValue = "128";
        algorithmConfig.addNewParameter(keySizeName, keySizeValue);

    }

    @Test
    void testDeriveKey() {
        byte[] actualMasterKey = keyDerivationAlgorithm.deriveKey(source, algorithmConfig);

        byte[] expectedMasterKey = new byte[] { 92, 124, 24, -128, 14, -101, -19, -96, -49, -59, 10, -77, -111, -39, -9,
                -91 };

        assertArrayEquals(expectedMasterKey, actualMasterKey, "The Master Key arrays do not match.");
    }
}
