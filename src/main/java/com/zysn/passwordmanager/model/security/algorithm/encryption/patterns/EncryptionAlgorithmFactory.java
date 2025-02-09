package com.zysn.passwordmanager.model.security.algorithm.encryption.patterns;

import com.zysn.passwordmanager.model.security.algorithm.encryption.api.EncryptionAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.encryption.impl.AES;
import com.zysn.passwordmanager.model.utils.enumerations.AlgorithmName;

public class EncryptionAlgorithmFactory {
    /**
     * Private constructor to prevent instantiation.
     */
    private EncryptionAlgorithmFactory() {
        // Prevent instantiation
    }

    /**
     * Creates an encryption algorithm instance based on the provided algorithm
     * name.
     *
     * @param name the name of the algorithm
     * @return an instance of the specified EncryptionAlgorithm
     * @throws IllegalArgumentException if the algorithm name is null, empty, or
     *                                  unsupported
     */
    public static EncryptionAlgorithm createAlgorithm(String name) {
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
            case AlgorithmName.AES:
                return new AES();
            default:
                throw new IllegalArgumentException("The algorithm " + name + " has not been found.");
        }
    }

}
