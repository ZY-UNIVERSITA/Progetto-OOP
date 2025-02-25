package com.zysn.passwordmanager.model.security.algorithm.config.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.api.AlgorithmConfigBuilder;

public class DefaultAlgorithmConfigBuilderTest {
    private AlgorithmConfigBuilder algorithmConfigBuilder;
    private AlgorithmConfig algorithmConfig;

    @BeforeEach
    void setup() {
        this.algorithmConfigBuilder = new DefaultAlgorithmConfigBuilder();
    }

    @Test
    void testAddParameter() {
        this.algorithmConfig = this.algorithmConfigBuilder.addParameter("key 1", "value 1").build();

        assertNotNull(this.algorithmConfig, "Algorithm config should not be null");
        assertEquals("value 1", this.algorithmConfig.getParameterValueByName("key 1"), "Parameter value should be 'value 1'");
    }

    @Test
    void testBuild() {
        this.algorithmConfig = this.algorithmConfigBuilder.addParameter("key 1", "value 1").build();

        assertNotNull(this.algorithmConfig, "Algorithm config should not be null");
        assertEquals("value 1", this.algorithmConfig.getParameterValueByName("key 1"), "Parameter value should be 'value 1'");
    }

    @Test
    void testSetAlgorithmName() {
        this.algorithmConfig = this.algorithmConfigBuilder.setAlgorithmName("algorithm name").build();

        assertNotNull(this.algorithmConfig, "Algorithm config should not be null");
        assertEquals("algorithm name", this.algorithmConfig.getAlgorithmName(), "Algorithm name should be 'algorithm name'");
    }

    @Test
    void testSetAlgorithmType() {
        this.algorithmConfig = this.algorithmConfigBuilder.setAlgorithmType("algorithm type").build();
        
        assertNotNull(this.algorithmConfig, "Algorithm config should not be null");
        assertEquals("algorithm type", this.algorithmConfig.getAlgorithmType(), "Algorithm type should be 'algorithm type'");
    }

    @Test
    void testSetParameters() {
        final Map<String, String> params = new HashMap<>();
        params.put("key 1", "value 1");

        this.algorithmConfig = this.algorithmConfigBuilder.setParameters(params).build();
        
        assertNotNull(this.algorithmConfig, "Algorithm config should not be null");
        assertEquals("value 1", this.algorithmConfig.getParameterValueByName("key 1"), "Parameter value should be 'value 1'");
    }

    @Test
    void testSetSalt() {
        final byte[] salt = new byte[] { 1, 2, 3 };

        this.algorithmConfig = this.algorithmConfigBuilder.setSalt(salt).build();
        
        assertNotNull(this.algorithmConfig, "Algorithm config should not be null");
        assertArrayEquals(salt, this.algorithmConfig.getSalt(), "Salt should match the provided value");
    }
}
