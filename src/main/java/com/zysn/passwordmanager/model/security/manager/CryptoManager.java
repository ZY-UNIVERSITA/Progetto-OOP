package com.zysn.passwordmanager.model.security.manager;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.derivation.patterns.KeyDerivationAlgorithmFactory;
import com.zysn.passwordmanager.model.security.algorithm.encryption.api.EncryptionAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.encryption.patterns.EncryptionAlgorithmFactory;
import com.zysn.passwordmanager.model.utils.enumerations.AlgorithmTypeEnum;

public class CryptoManager {
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
    public byte[] deriveMasterKey(char[] password, AlgorithmConfig algorithmConfig) {
        if (algorithmConfig == null || password == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        String algorithmName = algorithmConfig.getAlgorithmName();
        String algorithmType = algorithmConfig.getAlgorithmType();

        if (!algorithmType.equalsIgnoreCase(AlgorithmTypeEnum.KEY_DERIVATION_ALGORITHM.getParameter())) {
            throw new IllegalStateException("The algorithm type is not compatible with this method.");
        }

        KeyDerivationAlgorithm keyDerivationAlgorithm = KeyDerivationAlgorithmFactory
                .createAlgorithm(algorithmName);

        byte[] masterKey = keyDerivationAlgorithm.deriveKey(password, algorithmConfig);

        return masterKey;
    }

    public byte[] encrypt(byte[] data, SecretKeySpec key, AlgorithmConfig algorithmConfig) {
        if (data == null || key == null || algorithmConfig == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        String algorithmType = algorithmConfig.getAlgorithmType();
        String algorithmName = algorithmConfig.getAlgorithmName();

        if (!algorithmType.equalsIgnoreCase(AlgorithmTypeEnum.ENCRYPTION_ALGORITHM.getParameter())) {
            throw new IllegalStateException("The algorithm type is not compatible with this method.");
        }

        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithmFactory.createAlgorithm(algorithmName);

        byte[] encryptedData = encryptionAlgorithm.encrypt(data, key, algorithmConfig);

        return encryptedData;
    }

    public byte[] decrypt(byte[] data, SecretKeySpec key, AlgorithmConfig algorithmConfig) {
        if (data == null || key == null || algorithmConfig == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        String algorithmType = algorithmConfig.getAlgorithmType();
        String algorithmName = algorithmConfig.getAlgorithmName();

        if (!algorithmType.equalsIgnoreCase(AlgorithmTypeEnum.ENCRYPTION_ALGORITHM.getParameter())) {
            throw new IllegalStateException("The algorithm type is not compatible with this method.");
        }

        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithmFactory.createAlgorithm(algorithmName);

        byte[] decryptedData = encryptionAlgorithm.decrypt(data, key, algorithmConfig);

        return decryptedData;
    }
}
