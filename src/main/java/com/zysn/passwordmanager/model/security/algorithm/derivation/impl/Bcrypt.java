package com.zysn.passwordmanager.model.security.algorithm.derivation.impl;

import org.bouncycastle.crypto.generators.BCrypt;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.enumerations.AlgorithmParameters;

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
    public byte[] deriveKey(char[] source, AlgorithmConfig algorithmConfig) {
        // Config cost
        int cost = Integer.valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.COST.getParameter()));

        byte[] salt = algorithmConfig.getSalt();

        // Convert password in char to bytes using UTF-8
        byte[] sourceBytes = EncodingUtils.charToByteConverter(source);

        byte[] keyBytes = null;

        try {
            keyBytes = BCrypt.generate(sourceBytes, salt, cost);
        } finally {
            // Delete the converted password in bytes from the memory
            CryptoUtils.cleanMemory(sourceBytes);
        }

        // return masterKey;
        return keyBytes;
    }
}
