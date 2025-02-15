package com.zysn.passwordmanager.model.security.algorithm.config.api;

import java.util.Map;

import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;

/**
 * Builder interface for creating an algorithm configuration.
 */
public interface AlgorithmConfigBuilder {

    /**
     * Sets the name of the algorithm.
     *
     * @param algorithmName the name of the algorithm
     * @return this builder
     */
    AlgorithmConfigBuilder setAlgorithmName(String algorithmName);

    /**
     * Sets the type of the algorithm.
     *
     * @param algorithmType the type of the algorithm
     * @return this builder
     */
    AlgorithmConfigBuilder setAlgorithmType(String algorithmType);

    /**
     * Sets the salt to be used by the algorithm.
     *
     * @param salt the salt as a byte array
     * @return this builder
     */
    AlgorithmConfigBuilder setSalt(byte[] salt);

    /**
     * Sets the parameters for the algorithm.
     *
     * @param parameters a map containing parameter names and values
     * @return this builder
     */
    AlgorithmConfigBuilder setParameters(Map<String, String> parameters);

    /**
     * Adds a single parameter to the algorithm configuration.
     *
     * @param key   the parameter name
     * @param value the parameter value
     * @return this builder
     */
    AlgorithmConfigBuilder addParameter(String key, String value);

    /**
     * Builds the algorithm configuration.
     *
     * @return the built {@link AlgorithmConfig}
     */
    AlgorithmConfig build();
}
