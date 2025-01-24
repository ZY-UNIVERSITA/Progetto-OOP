package com.zysn.passwordmanager.model.security.manager;

import java.security.SecureRandom;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.security.algorithm.derivation.KeyDerivationAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.derivation.KeyDerivationAlgorithmFactory;
import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;

public class CryptoManager {
    private final static String KEY_DERIVATION_ALGORITHM_KEY = "Key Derivation Algorithm";


    public CryptoManager() {

    }

    public SecretKeySpec deriveMasterKey(char[] password, byte[] salt, AlgorithmConfig algorithmConfig) {
        String algorithmType = algorithmConfig.getAlgorithmType();
        String algorithmName = algorithmConfig.getAlgorithmName();

        if (algorithmType != KEY_DERIVATION_ALGORITHM_KEY) {
            throw new IllegalArgumentException("Algorith type is wrong.");
        }

        KeyDerivationAlgorithm keyDerivationAlgorithm = KeyDerivationAlgorithmFactory.createAlgorithm(algorithmName);

        SecretKeySpec masterKey = keyDerivationAlgorithm.deriveKey(password, salt, algorithmConfig);

        return masterKey;
    }

    public byte[] encrypt(byte[] data, SecretKeySpec key, AlgorithmConfig algorithmConfig) {
        return null;
    }

    public byte[] decrypt(byte[] data, SecretKeySpec key, AlgorithmConfig algorithmConfig) {
        return null;
    }

    public char[] generatePassword(int length, boolean useSpecialChar, boolean useNumbers, boolean useUpperCase, boolean useLowerCase) {
        return null;
    }

    public byte[] generateSalt(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("The length of the salt cannot be 0 or less.");
        }

        byte[] salt = new byte[length];

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        return salt;
    } 

}
