package com.zysn.passwordmanager.model.security.algorithm.config.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.utils.enumerations.AlgorithmParameters;

public class AlgorithmConfigFactoryTest {
    @Test
    void testCreateAlgorithmConfig() {
        final String algorithmName = "Argon2";
        final byte[] salt = "randomSalt".getBytes();
        final Map<String, String> params = new HashMap<>();
        params.put("memory_cost", "65536");

        final AlgorithmConfig config = AlgorithmConfigFactory.createAlgorithmConfig(algorithmName, salt, params);

        assertNotNull(config, "Algorithm Config is null.");
        assertEquals("Argon2", config.getAlgorithmName(), "Algorithm name should be ARGON2");
        assertArrayEquals(salt, config.getSalt(), "Salt should match the provided value.");
        assertEquals("65536", config.getParameters().get("memory_cost"), "Memory cost parameter should be 65536.");
    }

    @Test
    void testCreateAlgorithmConfigDefault() {
        final String algorithmName = "Argon2";
        final byte[] salt = "randomSalt".getBytes();

        final AlgorithmConfig config = AlgorithmConfigFactory.createAlgorithmConfig(algorithmName, salt, null);

        assertNotNull(config, "AlgorithmConfig is null.");
        assertEquals("1", config.getParameters().get(AlgorithmParameters.PARALLELISM.getParameter()),
                "Parallelism parameter should be 1.");
        assertEquals("65536", config.getParameters().get(AlgorithmParameters.MEMORY_COST.getParameter()),
                "Memory cost parameter should be 65536.");
    }
}
