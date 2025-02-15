package com.zysn.passwordmanager.model.security.algorithm.derivation.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;

public class ScryptTest {
    private KeyDerivationAlgorithm keyDerivationAlgorithm;
    private byte[] source;
    private byte[] salt;
    private AlgorithmConfig algorithmConfig;

    private byte[] expectedMasterKey;

    @BeforeEach
    void setup() {
        keyDerivationAlgorithm = new Scrypt();
        source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes();
        salt = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96, -89, -25, 34, -102, 77, -20, -8, -41,
                92, 87, 17, 35, 75, -11, -87, -87, -54, 22, 110, 8 };

        algorithmConfig = new AlgorithmConfig("Scrypt", "Key Derivation Algorithm", salt, new HashMap<>());

        String iterationsName = "cost_factor";
        String iterationsValue = "16384";
        algorithmConfig.addNewParameter(iterationsName, iterationsValue);

        String memoryCostName = "block_size";
        String memoryCostValue = "8";
        algorithmConfig.addNewParameter(memoryCostName, memoryCostValue);

        String parallelismName = "parallelism";
        String parallelismValue = "1";
        algorithmConfig.addNewParameter(parallelismName, parallelismValue);

        String keySizeName = "key_size";
        String keySizeValue = "128";
        algorithmConfig.addNewParameter(keySizeName, keySizeValue);

        expectedMasterKey = new byte[] { -107, 29, 119, 84, -17, 116, 114, 121, -127, -37, 72, 111, -77, -106,
                -110, -1 };
    }

    @Test
    void testDeriveKey() {
        byte[] actualMasterKey = keyDerivationAlgorithm.deriveKey(source, algorithmConfig);

        assertNotNull(actualMasterKey, "The Master Key is null.");
        assertArrayEquals(expectedMasterKey, actualMasterKey, "The Master Key arrays do not match.");
    }
}
