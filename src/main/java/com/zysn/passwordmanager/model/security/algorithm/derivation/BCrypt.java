package com.zysn.passwordmanager.model.security.algorithm.derivation;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.utils.CryptoUtils;


public class BCrypt implements KeyDerivationAlgorithm {
    private static final String COST = "cost";

    @Override
    public SecretKeySpec deriveKey(char[] source, AlgorithmConfig algorithmConfig) {
        int cost = Integer.valueOf(algorithmConfig.getParameterValueByName(COST));

        byte[] salt = algorithmConfig.getSalt();

        byte[] sourceBytes = CryptoUtils.charToByteConverter(source);

        SecretKeySpec masterKey = null;

        try {
            byte[] keyBytes = org.bouncycastle.crypto.generators.BCrypt.generate(sourceBytes, salt, cost);
            masterKey = new SecretKeySpec(keyBytes, "AES");
        } finally {
            CryptoUtils.cleanMemory(sourceBytes);
        }

        return masterKey;
    }
}
