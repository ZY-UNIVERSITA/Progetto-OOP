package com.zysn.passwordmanager.model.security.algorithm.derivation.impl;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;

import com.zysn.passwordmanager.model.enums.AlgorithmParameters;
import com.zysn.passwordmanager.model.enums.ShaHashLength;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.derivation.api.KeyDerivationAlgorithm;

/**
 * The HKDF (HMAC-based Extract-and-Expand Key Derivation Function) class
 * implements the HighEntropyKeyDerivationAlgorithm interface.
 * This class provides a method to derive keys using the HKDF algorithm with a
 * specified digest function.
 */
public class HKDF implements KeyDerivationAlgorithm {

    /**
     * Derives a key using the HKDF algorithm.
     *
     * @param source          the source key material.
     * @param algorithmConfig the configuration for the algorithm, containing
     *                        parameters such as the digest name, salt, and info.
     * @return the derived key as a byte array.
     */
    @Override
    public byte[] deriveKey(final byte[] source, final AlgorithmConfig algorithmConfig) {
        final String digest = algorithmConfig.getParameters().get(AlgorithmParameters.DIGEST.getParameter());
        final byte[] salt = algorithmConfig.getSalt();
        final byte[] info = algorithmConfig.getParameters().get(AlgorithmParameters.INFO.getParameter()).getBytes();

        final HKDFBytesGenerator hkdf = new HKDFBytesGenerator(this.getDigest(digest));

        final HKDFParameters params = new HKDFParameters(source, salt, info);

        hkdf.init(params);

        final byte[] key = new byte[ShaHashLength.SHA256_BYTE.getParameter()];

        hkdf.generateBytes(key, 0, key.length);

        return key;
    }

    /**
     * Returns a Digest instance based on the provided digest name.
     *
     * @param digest the name of the digest algorithm.
     * @return a Digest instance for the specified algorithm.
     * @throws IllegalArgumentException if the specified digest is not supported.
     */
    private Digest getDigest(final String digest) {
        switch (digest.toLowerCase()) {
            case "sha256":
                return new SHA256Digest();
            default:
                throw new IllegalArgumentException("The chosen digest is not supported.");
        }
    }
}
