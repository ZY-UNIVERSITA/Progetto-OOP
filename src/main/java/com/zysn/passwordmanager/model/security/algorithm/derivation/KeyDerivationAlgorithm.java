package com.zysn.passwordmanager.model.security.algorithm.derivation;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;

/**
 * Interface representing a key derivation algorithm.
 */
public interface KeyDerivationAlgorithm {
    
    /**
     * Derives a key based on the provided source, salt, and algorithm configuration.
     *
     * @param source the source password or passphrase
     * @param salt the salt value to use in the key derivation process
     * @param algorithmConfig the configuration parameters for the key derivation algorithm
     * @return a SecretKeySpec representing the derived key
     */
    public SecretKeySpec deriveKey(char[] source, AlgorithmConfig algorithmConfig);
}
