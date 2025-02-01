package com.zysn.passwordmanager.model.security.manager;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.derivation.patterns.KeyDerivationAlgorithmFactory;
import com.zysn.passwordmanager.model.utils.CryptoUtils;

public class CryptoManager {
    private final static String KEY_DERIVATION_ALGORITHM_KEY = "Key Derivation Algorithm";

    public CryptoManager() {

    }

    /**
     * Derives a master key from the given password and algorithm configuration.
     *
     * @param password        the password used for key derivation
     * @param algorithmConfig the algorithm configuration specifying the key
     *                        derivation algorithm
     * @return the derived master key
     * @throws IllegalArgumentException if the algorithm type is incorrect
     */
    public SecretKeySpec deriveMasterKey(char[] password, AlgorithmConfig algorithmConfig) {
        String algorithmType = algorithmConfig.getAlgorithmType();
        String algorithmName = algorithmConfig.getAlgorithmName();

        if (algorithmType != KEY_DERIVATION_ALGORITHM_KEY) {
            throw new IllegalArgumentException("Algorithm type is wrong.");
        }

        SecretKeySpec masterKey = null;

        try {
            KeyDerivationAlgorithm keyDerivationAlgorithm = KeyDerivationAlgorithmFactory
                    .createAlgorithm(algorithmName);
            masterKey = keyDerivationAlgorithm.deriveKey(password, algorithmConfig);
        } finally {
            CryptoUtils.cleanMemory(password);
        }

        return masterKey;
    }

    public byte[] encrypt(byte[] data, SecretKeySpec key, AlgorithmConfig algorithmConfig) {
        return null;
    }

    public byte[] decrypt(byte[] data, SecretKeySpec key, AlgorithmConfig algorithmConfig) {
        return null;
    }
}
