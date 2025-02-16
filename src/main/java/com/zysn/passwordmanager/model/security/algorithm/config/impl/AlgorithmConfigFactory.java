package com.zysn.passwordmanager.model.security.algorithm.config.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.zysn.passwordmanager.model.enums.AlgorithmName;
import com.zysn.passwordmanager.model.enums.AlgorithmType;
import com.zysn.passwordmanager.model.security.algorithm.config.api.AlgorithmConfigBuilder;

/**
 * Factory class for creating algorithm configurations.
 */
public class AlgorithmConfigFactory {

    /**
     * Creates an {@link AlgorithmConfig} based on the provided algorithm name,
     * salt, and parameters.
     *
     * @param algorithmName the name of the algorithm
     * @param salt          the salt as a byte array
     * @param params        a map containing parameter names and values
     * @return the created {@link AlgorithmConfig}
     * @throws IllegalArgumentException if the algorithm name is null or unknown
     */
    public static AlgorithmConfig createAlgorithmConfig(final String algorithmName, final byte[] salt,
            final Map<String, String> params) {
        if (algorithmName == null) {
            throw new IllegalArgumentException("Algorithm name cannot be null");
        }

        final AlgorithmName algorithmNameEnum = AlgorithmName.returnEnumfromString(algorithmName);

        switch (algorithmNameEnum) {
            case ARGON2:
                return buildConfig(AlgorithmName.ARGON2.getAlgorithmName(),
                        AlgorithmType.KEY_DERIVATION_ALGORITHM.getParameter(), salt,
                        mergeParams(params, getArgon2Defaults()));

            case BCRYPT:
                return buildConfig(AlgorithmName.BCRYPT.getAlgorithmName(),
                        AlgorithmType.KEY_DERIVATION_ALGORITHM.getParameter(), salt,
                        mergeParams(params, getBcryptDefaults()));

            case SCRYPT:
                return buildConfig(AlgorithmName.SCRYPT.getAlgorithmName(),
                        AlgorithmType.KEY_DERIVATION_ALGORITHM.getParameter(), salt,
                        mergeParams(params, getScryptDefaults()));

            case HKDF:
                return buildConfig(AlgorithmName.HKDF.getAlgorithmName(),
                        AlgorithmType.KEY_DERIVATION_ALGORITHM.getParameter(), salt,
                        mergeParams(params, getHkdfDefaults()));

            case AES:
                return buildConfig(AlgorithmName.AES.getAlgorithmName(),
                        AlgorithmType.ENCRYPTION_ALGORITHM.getParameter(), salt, mergeParams(params, getAesDefaults()));

            default:
                throw new IllegalArgumentException("Error: Unknown algorithm: " + algorithmName);
        }
    }

    /**
     * Returns the default parameters for the Argon2 algorithm.
     *
     * @return a map containing the default parameters
     */
    private static Map<String, String> getArgon2Defaults() {
        return Map.of(
                "memory_cost", "65536",
                "iterations", "3",
                "parallelism", "1",
                "key_size", "256",
                "variant", "argon2id");
    }

    /**
     * Returns the default parameters for the HKDF algorithm.
     *
     * @return a map containing the default parameters
     */
    private static Map<String, String> getHkdfDefaults() {
        return Map.of(
                "digest", "SHA256");
    }

    /**
     * Returns the default parameters for the AES algorithm.
     *
     * @return a map containing the default parameters
     */
    private static Map<String, String> getAesDefaults() {
        return Map.of(
                "mode", "GCM",
                "padding", "NoPadding");
    }

    /**
     * Returns the default parameters for the BCrypt algorithm.
     *
     * @return a map containing the default parameters
     */
    private static Map<String, String> getBcryptDefaults() {
        return Map.of(
                "cost", "12",
                "key_size", "192");
    }

    /**
     * Returns the default parameters for the Scrypt algorithm.
     *
     * @return a map containing the default parameters
     */
    private static Map<String, String> getScryptDefaults() {
        return Map.of(
                "cost_factor", "16",
                "block_size", "8",
                "parallelism", "1",
                "key_size", "256");
    }

    /**
     * Merges user-provided parameters with the default parameters.
     *
     * @param userParams    a map containing user-provided parameters, which may be
     *                      null
     * @param defaultParams a map containing the default parameters
     * @return a map containing the merged parameters
     */
    private static Map<String, String> mergeParams(final Map<String, String> userParams,
            final Map<String, String> defaultParams) {
        // Creates a mutable default map
        final Map<String, String> merged = new HashMap<>(defaultParams);

        // Uses Optional to handle null. If null, creates a new map and merges,
        // otherwise merges with user-provided params.
        merged.putAll(Optional.ofNullable(userParams).orElseGet(HashMap::new));
        return merged;
    }

    /**
     * Builds an {@link AlgorithmConfig} using the specified parameters.
     *
     * @param name   the name of the algorithm
     * @param type   the type of the algorithm
     * @param salt   the salt as a byte array
     * @param params a map containing parameter names and values
     * @return the built {@link AlgorithmConfig}
     */
    private static AlgorithmConfig buildConfig(final String name, final String type, final byte[] salt,
            final Map<String, String> params) {

        final AlgorithmConfigBuilder algorithmConfigBuilder = new DefaultAlgorithmConfigBuilder();

        return algorithmConfigBuilder
                .setAlgorithmName(name)
                .setAlgorithmType(type)
                .setSalt(salt)
                .setParameters(params)
                .build();
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private AlgorithmConfigFactory() {
    }
}
