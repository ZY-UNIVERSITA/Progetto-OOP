package com.zysn.passwordmanager.model.security.algorithm.derivation.impl;

import org.bouncycastle.crypto.generators.BCrypt;

import com.zysn.passwordmanager.model.utils.enumerations.AlgorithmParameters;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;

/**
 * This class implements the KeyDerivationAlgorithm using the BCrypt algorithm.
 */
public class Bcrypt implements KeyDerivationAlgorithm {
    /**
     * Derives a key from the given source and algorithm configuration.
     *
     * @param source          the source character array used for key derivation
     * @param algorithmConfig the configuration parameters for the algorithm
     * @return an array of byte representing the derived key
     */
    @Override
    public byte[] deriveKey(final byte[] source, final AlgorithmConfig algorithmConfig) {
        // Config cost
        final int cost = Integer.valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.COST.getParameter()));

        final byte[] salt = algorithmConfig.getSalt();

        byte[] keyBytes = BCrypt.generate(source, salt, cost);

        // return masterKey;
        return keyBytes;
    }
}
