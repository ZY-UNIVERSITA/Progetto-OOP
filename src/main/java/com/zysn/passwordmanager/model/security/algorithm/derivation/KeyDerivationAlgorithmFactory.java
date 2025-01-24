package com.zysn.passwordmanager.model.security.algorithm.derivation;

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
     * @throws IllegalArgumentException if the specified algorithm name is not found
     */
    public static KeyDerivationAlgorithm createAlgorithm(String name) {
        switch (name) {
            // case "PBKDF2":
            //     return new PBKDF2();
            case "Argon2":
                return new Argon2();
            default:
                throw new IllegalArgumentException("The algorithm " + name + " has not been found.");
        }
    }
}
