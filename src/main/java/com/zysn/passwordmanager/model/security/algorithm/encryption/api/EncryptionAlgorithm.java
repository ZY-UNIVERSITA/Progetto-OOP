package com.zysn.passwordmanager.model.security.algorithm.encryption.api;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;

/**
 * This interface defines the methods for encryption and decryption using a specified algorithm.
 */
public interface EncryptionAlgorithm {
    
    /**
     * Encrypts the provided source byte array using the given key and algorithm configuration.
     *
     * @param source the byte array to be encrypted
     * @param key the secret key specification
     * @param algorithmConfig the configuration for the algorithm
     * @return the encrypted byte array
     */
    public byte[] encrypt(byte[] source, SecretKeySpec key, AlgorithmConfig algorithmConfig);

    /**
     * Decrypts the provided source byte array using the given key and algorithm configuration.
     *
     * @param source the byte array to be decrypted
     * @param key the secret key specification
     * @param algorithmConfig the configuration for the algorithm
     * @return the decrypted byte array
     */
    public byte[] decrypt(byte[] source, SecretKeySpec key, AlgorithmConfig algorithmConfig);
}
