package com.zysn.passwordmanager.model.security.algorithm.derivation.patterns;

import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Argon2;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Bcrypt;
import com.zysn.passwordmanager.model.security.algorithm.derivation.impl.Scrypt;

/**
 * Factory class for creating instances of KeyDerivationAlgorithm.
 */
public class KeyDerivationAlgorithmFactory {
    private static final String ARGON2 = "argon2";
    private static final String SCRYPT = "scrypt";
    private static final String BCRYPT = "bcrypt";
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
     * @throws IllegalArgumentException if the specified algorithm name is not found
     */
    public static KeyDerivationAlgorithm createAlgorithm(String name) {
        switch (name.toLowerCase()) {
            // case "PBKDF2":
            //     return new PBKDF2();
            case ARGON2:
                return new Argon2();
            case SCRYPT:
                return new Scrypt();
            case BCRYPT:
                return new Bcrypt();
            default:
                throw new IllegalArgumentException("The algorithm " + name + " has not been found.");
        }
    }
}
