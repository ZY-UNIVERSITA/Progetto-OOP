package com.zysn.passwordmanager.model.security.algorithm.encryption.api;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;

public interface EncryptionAlgorithm {
    public byte[] encrypt(byte[] source, SecretKeySpec key, AlgorithmConfig algorithmConfig);
    public byte[] decrypt(byte[] source, SecretKeySpec key, AlgorithmConfig algorithmConfig);
}
