package com.zysn.passwordmanager.model.security.algorithm.derivation.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;

public class HKDFTest {
    private KeyDerivationAlgorithm keyDerivationAlgorithm;
    private AlgorithmConfig algorithmConfig;
    private byte[] source;
    private byte[] expectedKey;

    @BeforeEach
    void setup() {
        String algorithmName = "HKDF";
        String algorithmType = "High Entropy Key Derivation Algorithm";
        byte[] salt = "8e94ef805b93e683ff18".getBytes();
        Map<String, String> args = new HashMap<>();
        args.put("digest", "SHA256");
        args.put("info", "Test info");

        this.algorithmConfig = new AlgorithmConfig(algorithmName, algorithmType, salt, args);

        this.keyDerivationAlgorithm = new HKDF();

        source = "1° test to test: this is a key for a key to test the k3i? Test to test for a test ----------"
                .getBytes();

        expectedKey = new byte[] { 1, 93, -13, -21, 16, 40, 4, 74, -103, 31, 68, -68, -109, -20, 81, 24, -75, 2,
                8, -84, -41, -66, 126, 42, 114, -91, 109, 61, -117, -119, 21, -52 };
    }

    @Test
    void testDeriveKey() {
        byte[] actualKey = keyDerivationAlgorithm.deriveKey(source, algorithmConfig);

        assertNotNull(actualKey, "The key is null.");
        assertArrayEquals(expectedKey, actualKey, "The actual key is not the same as the expected.");
    }
}
