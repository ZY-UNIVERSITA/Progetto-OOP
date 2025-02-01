package com.zysn.passwordmanager.model.security.algorithm.derivation.impl;

import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.generators.SCrypt;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;
import com.zysn.passwordmanager.model.utils.CryptoUtils;
import com.zysn.passwordmanager.model.utils.enumerations.AlgorithmName;
import com.zysn.passwordmanager.model.utils.enumerations.AlgorithmParameters;

/**
 * This class implements the Scrypt Key Derivation Function (KDF) as defined by the KeyDerivationAlgorithm interface.
 * It generates a secret key using the Scrypt algorithm based on the provided source and configuration.
 */
public class Scrypt implements KeyDerivationAlgorithm {
    /**
     * Derives a secret key using the Scrypt KDF.
     * 
     * @param source the source password or passphrase to derive the key from.
     * @param algorithmConfig the configuration object containing algorithm parameters such as cost factor, block size, parallelism, key size, and salt.
     * @return the derived secret key as a SecretKeySpec.
     */
    @Override
    public SecretKeySpec deriveKey(char[] source, AlgorithmConfig algorithmConfig) {
        int costFactor = Integer.valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.COST_FACTOR.getParameter()));
        int blockSize = Integer.valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.BLOCK_SIZE.getParameter()));
        int parallelism = Integer.valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.PARALLELISM.getParameter()));
        int keySize = Integer.valueOf(algorithmConfig.getParameterValueByName(AlgorithmParameters.KEY_SIZE.getParameter())) / 8;

        byte[] salt = algorithmConfig.getSalt();

        byte[] sourceBytes = CryptoUtils.charToByteConverter(source);

        SecretKeySpec masterKey = null;

        try {
            byte[] keyBytes = SCrypt.generate(sourceBytes, salt, costFactor, blockSize, parallelism, keySize);
            masterKey = new SecretKeySpec(keyBytes, AlgorithmName.AES.getAlgorithName());
        } finally {
            CryptoUtils.cleanMemory(sourceBytes);
        }

        return masterKey;
    }
}
