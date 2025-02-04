package com.zysn.passwordmanager.model.security.algorithm.derivation.impl;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;
import com.zysn.passwordmanager.model.utils.enumerations.AlgorithmName;
import com.zysn.passwordmanager.model.utils.enumerations.AlgorithmParameters;

/**
 * Argon2 is a class that implements the KeyDerivationAlgorithm interface
 * and provides key derivation using the Argon2 algorithm.
 */
public class Argon2 implements KeyDerivationAlgorithm {

    /**
     * Retrieves the Argon2 version as an integer based on the provided algorithm
     * configuration.
     *
     * @param algorithmConfig the algorithm configuration containing parameters
     * @return an integer representing the Argon2 version
     * @throws IllegalArgumentException if the algorithm version is null, empty, or
     *                                  unsupported
     */
    private int getArgonVersion(AlgorithmConfig algorithmConfig) {
        // Retrieves the value of the parameter
        String algorithmVersion = algorithmConfig.getParameterValueByName(AlgorithmParameters.VARIANT.getParameter());

        // Validates the parameter
        if (algorithmVersion == null || algorithmVersion.isBlank()) {
            throw new IllegalArgumentException("Algorithm version cannot be null or empty.");
        }

        // Converts the string to an enum
        AlgorithmName algorithmName;
        try {
            algorithmName = AlgorithmName.returnEnumfromString(algorithmVersion);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported algorithm version: " + algorithmVersion);
        }

        switch (algorithmName) {
            case AlgorithmName.ARGON2I:
                return Argon2Parameters.ARGON2_i;
            case AlgorithmName.ARGON2D:
                return Argon2Parameters.ARGON2_d;
            case AlgorithmName.ARGON2ID:
                return Argon2Parameters.ARGON2_id;
            default:
                throw new IllegalArgumentException("The argon2 version selected is not supported.");
        }
    }

    /**
     * Derives a key using the Argon2 algorithm.
     *
     * @param source          the source data
     * @param algorithmConfig the configuration of the algorithm
     * @return the derived key as an array of byte
     */
    @Override
    public byte[] deriveKey(char[] source, AlgorithmConfig algorithmConfig) {
        int argonVersion = this.getArgonVersion(algorithmConfig);

        // Builder initialization
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(argonVersion);

        // Add salt
        builder.withSalt(algorithmConfig.getSalt());

        // Add iterations
        builder.withIterations(Integer
                .valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.ITERATIONS.getParameter())));

        // Add memory cost
        builder.withMemoryAsKB(Integer
                .valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.MEMORY_COST.getParameter())));

        // Add parallelism
        builder.withParallelism(Integer
                .valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.PARALLELISM.getParameter())));

        // Create a block of bytes of given size
        int key_size = Integer
                .valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.KEY_SIZE.getParameter()));
        byte[] keyBytes = new byte[key_size / 8];

        // Initializer generator
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());

        // Create the master key
        generator.generateBytes(source, keyBytes);

        // Return master key
        return keyBytes;
    }
}
