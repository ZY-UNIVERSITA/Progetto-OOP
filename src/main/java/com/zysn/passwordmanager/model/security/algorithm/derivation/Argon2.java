package com.zysn.passwordmanager.model.security.algorithm.derivation;

import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;

/**
 * Argon2 is a class that implements the KeyDerivationAlgorithm interface
 * and provides key derivation using the Argon2 algorithm.
 */
public class Argon2 implements KeyDerivationAlgorithm {
    private static final String ARGON2I = "argon2i";
    private static final String ARGON2D = "argon2d";
    private static final String ARGON2ID = "argon2id";
    
    private static final String VARIANT = "variant";
    private static final String ITERATIONS = "iterations";
    private static final String MEMORY_COST = "memory_cost";
    private static final String PARALLELISM = "parallelism";
    private static final String KEY_SIZE = "key_size";
    
    /**
     * Determines the Argon2 version based on the provided algorithm configuration.
     *
     * @param algorithmConfig the configuration of the algorithm
     * @return the version of Argon2
     * @throws IllegalArgumentException if the Argon2 version selected is not supported
     */
    private int getArgonVersion(AlgorithmConfig algorithmConfig) {
        int argonVersion;

        switch (algorithmConfig.getParameterValueByName(VARIANT)) {
            case ARGON2I:
                argonVersion = Argon2Parameters.ARGON2_i;
                break;
            case ARGON2D:
                argonVersion = Argon2Parameters.ARGON2_d;
                break;
            case ARGON2ID:
                argonVersion = Argon2Parameters.ARGON2_id;
                break;
            default:
                throw new IllegalArgumentException("The argon2 version selected is not supported.");
        }

        return argonVersion;
    }

    /**
     * Derives a key using the Argon2 algorithm.
     *
     * @param source the source data (password)
     * @param salt the salt value
     * @param algorithmConfig the configuration of the algorithm
     * @return the derived key as a SecretKeySpec
     */
    @Override
    public SecretKeySpec deriveKey(char[] source, byte[] salt, AlgorithmConfig algorithmConfig) {
        int argonVersion = this.getArgonVersion(algorithmConfig);

        // Builder initialization
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(argonVersion);

        // Add salt
        builder.withSalt(salt);

        // Add iterations
        builder.withIterations(Integer.valueOf(algorithmConfig.getParameterValueByName(ITERATIONS)));

        // Add memory cost
        builder.withMemoryAsKB(Integer.valueOf(algorithmConfig.getParameterValueByName(MEMORY_COST)));

        // Add parallelism
        builder.withParallelism(Integer.valueOf(algorithmConfig.getParameterValueByName(PARALLELISM)));

        // Create a block of bytes of given size
        byte[] keyBytes = new byte[Integer.valueOf(algorithmConfig.getParameterValueByName(KEY_SIZE))];

        // Initializer generator
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());

        // Create the master key
        generator.generateBytes(source, keyBytes);

        // Save master key from byte[] to KeySpec
        SecretKeySpec masterKey = new SecretKeySpec(keyBytes, "AES");

        return masterKey;
    }
}
