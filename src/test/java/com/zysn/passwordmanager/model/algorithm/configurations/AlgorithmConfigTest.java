package com.zysn.passwordmanager.model.algorithm.configurations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;

class AlgorithmConfigTest {
    private AlgorithmConfig algorithmConfig;
    private String algorithmType;
    private String algorithName; 

    private Map<String, String> parameters;

    @BeforeEach 
    void setup() {
        algorithmType = "Key Derivation Algorithm";
        algorithName = "argon2"; 

        parameters = new HashMap<>(); 
        parameters.put("key_size", "128"); 
        parameters.put("round", "12"); 

        algorithmConfig = new AlgorithmConfig(algorithmType, algorithName, parameters);
    }

    @Test
    void testGetAlgorithmName() {
        assertEquals("argon2", algorithmConfig.getAlgorithmName(), "The algorithm name should be 'argon2'.");
    }

    @Test
    void testGetAlgorithmType() {
        assertEquals("Key Derivation Algorithm", algorithmConfig.getAlgorithmType(), "The algorithm type should be 'Key Derivation Algorithm'.");
    }

    @Test
    void testGetParameters() {
        final String firstParameterName = "key_size";
        final String firstParameterValue = "128";

        final String secondParameterName = "round";
        final String secondParameterValue = "12";

        final Map<String, String> params = new HashMap<>();
        params.put(firstParameterName, firstParameterValue);
        params.put(secondParameterName, secondParameterValue);

        assertEquals(params, algorithmConfig.getParameters(), "The algorithm parameters are not equals.");
    }

    @Test
    void testSetAlgorithmName() {
        final String newAlgorithmName = "bcrypt";
        algorithmConfig.setAlgorithmName(newAlgorithmName);

        assertEquals("bcrypt", algorithmConfig.getAlgorithmName(), "The algorithm name should be 'bcrypt'.");
    }

    @Test
    void testSetAlgorithmType() {
        final String newAlgorithmType = "Encryption Algorithm";
        algorithmConfig.setAlgorithmType(newAlgorithmType);

        assertEquals("Encryption Algorithm", algorithmConfig.getAlgorithmType(), "The algorithm type should be 'Encryption Algorithm'.");
    }

    @Test
    void testSetParameters() {
        final Map<String, String> newParameters = new HashMap<>();
        newParameters.put("key_size", "256");
        newParameters.put("round", "32");

        algorithmConfig.setParameters(newParameters);

        final Map<String, String> testMap = new HashMap<>();
        testMap.put("key_size", "256");
        testMap.put("round", "32");

        assertEquals(testMap, algorithmConfig.getParameters(), "The algorithm parameters are not equals.");
    }

    @Test
    void testAddNewParameter() {
        String newParameterName = "length";
        String newParameterValue = "128";

        this.algorithmConfig.addNewParameter(newParameterName, newParameterValue);

        assertEquals(true, this.algorithmConfig.getParameters().containsKey("length"), "The algorithm doesn't contain the key 'length'.");
        assertEquals("128", this.algorithmConfig.getParameters().get("length"), "The algorithm doesn't contain the key 'length' with the value '128'.");
    }

    @Test
    void testGetParameterValueByName() {
        assertEquals("128", this.algorithmConfig.getParameterValueByName("key_size"), "The value is not 128.");
    }

    @Test
    void testRemoveParameterByName() {
        this.algorithmConfig.removeParameterByName("key_size");
        assertEquals(null, this.algorithmConfig.getParameters().get("key_size"), "The key 'key_size' is still present.");
    }

    @Test
    void testUpdateParameter() {
        this.algorithmConfig.updateParameter("key_size", "256");
        assertEquals("256", this.algorithmConfig.getParameters().get("key_size"), "The value is not '256'.");
    }
}
