package com.zysn.passwordmanager.model.security.algorithm.derivation;

import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.generators.BCrypt;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.utils.CryptoUtils;
import com.zysn.passwordmanager.model.utils.enumerations.AlgorithmName;
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
     * @return a SecretKeySpec representing the derived key
     */
    @Override
    public SecretKeySpec deriveKey(char[] source, AlgorithmConfig algorithmConfig) {
        int cost = Integer.valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.COST.getParameter()));

        byte[] salt = algorithmConfig.getSalt();

        byte[] sourceBytes = CryptoUtils.charToByteConverter(source);

        SecretKeySpec masterKey = null;

        try {
            byte[] keyBytes = BCrypt.generate(sourceBytes, salt, cost);
            masterKey = new SecretKeySpec(keyBytes, AlgorithmName.AES.getAlgorithName());
        } finally {
            CryptoUtils.cleanMemory(sourceBytes);
        }

        return masterKey;
    }
}
