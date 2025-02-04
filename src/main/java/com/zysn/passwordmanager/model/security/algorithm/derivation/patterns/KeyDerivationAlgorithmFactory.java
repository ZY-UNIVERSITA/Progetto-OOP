package com.zysn.passwordmanager.model.security.algorithm.derivation.patterns;

import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Argon2;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Bcrypt;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Scrypt;
import com.zysn.passwordmanager.model.utils.enumerations.AlgorithmName;

/**
 * Factory class for creating instances of KeyDerivationAlgorithm.
 */
public class KeyDerivationAlgorithmFactory {
    /**
     * Private constructor to prevent instantiation.
     */
    private KeyDerivationAlgorithmFactory() {
        // Prevent instantiation
    }

    /**
     * Creates a KeyDerivationAlgorithm based on the specified algorithm name.
     *
     * @param name the name of the key derivation algorithm
     * @return an instance of the specified KeyDerivationAlgorithm
     * @throws IllegalArgumentException if the specified algorithm name is not found, is null or empty
     */
    public static KeyDerivationAlgorithm createAlgorithm(String name) {
        // Validates the parameter
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Algorithm name cannot be null or empty.");
        }

        // Converts the string to an enum
        AlgorithmName algorithmName;
        try {
            algorithmName = AlgorithmName.returnEnumfromString(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported algorithm: " + name);
        }

        switch (algorithmName) {
            case AlgorithmName.ARGON2:
                return new Argon2();
            case AlgorithmName.SCRYPT:
                return new Scrypt();
            case AlgorithmName.BCRYPT:
                return new Bcrypt();
            default:
                throw new IllegalArgumentException("The algorithm " + name + " has not been found.");
        }
    }
}
