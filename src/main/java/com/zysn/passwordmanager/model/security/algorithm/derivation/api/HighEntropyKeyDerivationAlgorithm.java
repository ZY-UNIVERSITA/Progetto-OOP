package com.zysn.passwordmanager.model.security.algorithm.derivation.api;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;

/**
 * HighEntropyKeyDerivationAlgorithm is an interface for key derivation algorithms that produce high-entropy keys.
 */
public interface HighEntropyKeyDerivationAlgorithm {
    /**
     * Derives a high-entropy key from the given source and algorithm configuration.
     *
     * @param source the source byte array from which to derive the key
     * @param algorithmConfig the configuration of the algorithm to be used for key derivation
     * @return the derived key as a byte array
     */
    public byte[] deriveKey(byte[] source, AlgorithmConfig algorithmConfig);
}
