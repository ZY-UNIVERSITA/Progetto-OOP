package com.zysn.passwordmanager.model.security.algorithm.config.impl;

import java.util.HashMap;
import java.util.Map;

import com.zysn.passwordmanager.model.security.algorithm.config.api.AlgorithmConfigBuilder;

/**
 * DefaultAlgorithmConfigBuilder is a builder class that implements the AlgorithmConfigBuilder interface.
 * It is used to create and configure an instance of AlgorithmConfig.
 */
public class DefaultAlgorithmConfigBuilder implements AlgorithmConfigBuilder {
    String algorithmName;
    String algorithmType;
    byte[] salt;
    Map<String, String> parameters;

    /**
     * Constructs a new DefaultAlgorithmConfigBuilder with an empty parameters map.
     */
    public DefaultAlgorithmConfigBuilder() {
        this.parameters = new HashMap<>();
    }

    /**
     * Sets the algorithm name.
     *
     * @param algorithmName the name of the algorithm
     * @return the current instance of DefaultAlgorithmConfigBuilder
     */
    @Override
    public AlgorithmConfigBuilder setAlgorithmName(final String algorithmName) {
        this.algorithmName = algorithmName;
        return this;
    }

    /**
     * Sets the algorithm type.
     *
     * @param algorithmType the type of the algorithm
     * @return the current instance of DefaultAlgorithmConfigBuilder
     */
    @Override
    public AlgorithmConfigBuilder setAlgorithmType(final String algorithmType) {
        this.algorithmType = algorithmType;
        return this;
    }

    /**
     * Sets the salt.
     *
     * @param salt the salt as a byte array
     * @return the current instance of DefaultAlgorithmConfigBuilder
     */
    @Override
    public AlgorithmConfigBuilder setSalt(final byte[] salt) {
        this.salt = salt;
        return this;
    }

    /**
     * Sets the parameters.
     *
     * @param parameters a map of parameters
     * @return the current instance of DefaultAlgorithmConfigBuilder
     */
    @Override
    public AlgorithmConfigBuilder setParameters(final Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * Adds a single parameter to the parameters map.
     *
     * @param key the key of the parameter
     * @param value the value of the parameter
     * @return the current instance of DefaultAlgorithmConfigBuilder
     */
    @Override
    public AlgorithmConfigBuilder addParameter(final String key, final String value) {
        this.parameters.put(key, value);
        return this;
    }

    /**
     * Builds and returns an instance of AlgorithmConfig.
     *
     * @return a new instance of AlgorithmConfig
     */
    @Override
    public AlgorithmConfig build() {
        return new AlgorithmConfig(this);
    }
}
