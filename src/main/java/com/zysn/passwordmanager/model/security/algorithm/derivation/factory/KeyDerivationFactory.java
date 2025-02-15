package com.zysn.passwordmanager.model.security.algorithm.derivation.factory;

import com.zysn.passwordmanager.model.enums.AlgorithmName;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Argon2;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Bcrypt;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Scrypt;

/**
 * Factory class for creating instances of KeyDerivationAlgorithm.
 */
public class KeyDerivationFactory {
    /**
     * Creates a KeyDerivationAlgorithm based on the specified algorithm name.
     *
     * @param name the name of the key derivation algorithm
     * @return an instance of the specified KeyDerivationAlgorithm
     * @throws IllegalArgumentException if the specified algorithm name is not found, is null or empty
     */
    public static KeyDerivationAlgorithm createAlgorithm(final String name) {
        // Validates the parameter
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Error: Algorithm name cannot be null or empty.");
        }

        // Converts the string to an enum
        AlgorithmName algorithmName;
        try {
            algorithmName = AlgorithmName.returnEnumfromString(name);
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException("Error: Unsupported algorithm: " + name);
        }

        switch (algorithmName) {
            case AlgorithmName.ARGON2:
                return new Argon2();
            case AlgorithmName.SCRYPT:
                return new Scrypt();
            case AlgorithmName.BCRYPT:
                return new Bcrypt();
            default:
                throw new IllegalArgumentException("Error: The algorithm " + name + " has not been found.");
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private KeyDerivationFactory() {
        // Prevent instantiation
    }
}
