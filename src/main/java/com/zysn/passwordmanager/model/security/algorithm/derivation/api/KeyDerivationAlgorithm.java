package com.zysn.passwordmanager.model.security.algorithm.derivation.api;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;

/**
 * Interface representing a key derivation algorithm.
 */
public interface KeyDerivationAlgorithm {
    
    /**
     * Derives a key based on the provided source, salt, and algorithm configuration.
     *
     * @param source the source password or passphrase
     * @param algorithmConfig the configuration parameters for the key derivation algorithm
     * @return an array of bytes representing the derived key
     */
    public byte[] deriveKey(char[] source, AlgorithmConfig algorithmConfig);
}
