package com.zysn.passwordmanager.model.security.algorithm.encryption.impl;

import com.zysn.passwordmanager.model.enums.AesAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.encryption.api.EncryptionAlgorithm;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;

/**
 * This class provides methods for encrypting and decrypting data using the AES
 * algorithm.
 *
 * <p>
 * The AES class implements the EncryptionAlgorithm interface and provides
 * functionality
 * for encrypting and decrypting byte arrays using the AES algorithm with GCM
 * mode and
 * configurable parameters. The class uses the Bouncy Castle provider for
 * cryptographic operations.
 * </p>
 */
public class AES implements EncryptionAlgorithm {
    public AES() {

    }

    /**
     * Encrypts the given data using the AES algorithm with GCM mode and the
     * specified configuration.
     *
     * @param data            the byte array containing the data to be encrypted
     * @param key             the SecretKeySpec object containing the key for
     *                        encryption
     * @param algorithmConfig the AlgorithmConfig object containing the algorithm
     *                        configuration parameters
     * @return a byte array containing the encrypted data
     * @throws IllegalArgumentException if any configuration parameters are invalid
     */
    public byte[] encrypt(final byte[] data, final SecretKeySpec key, final AlgorithmConfig algorithmConfig) {
        byte[] encryptedText = null;

        try {
            // Istanzia l'algoritmo AES con la modalità e il padding scelto usando come
            // provider Bouncy Castle
            final String aesMode = algorithmConfig.getParameterValueByName(AesAlgorithm.AES_MODE.getParameter());
            final String aesPadding = algorithmConfig.getParameterValueByName(AesAlgorithm.AES_PADDING.getParameter());
            final String aesVersion = String.join("/", algorithmConfig.getAlgorithmName(), aesMode, aesPadding);

            final Cipher cipher = Cipher.getInstance(aesVersion, "BC");

            // Crea le specifiche per GCM e inizializza il cipher
            final int gcmTagLength = Integer.valueOf(AesAlgorithm.GCM_TAG_LENGTH.getParameter());
            final GCMParameterSpec spec = new GCMParameterSpec(gcmTagLength, algorithmConfig.getSalt());
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            encryptedText = cipher.doFinal(data);
        } catch (final Exception e) {
            System.err.println("The encryption of the source generated an error.");
        }

        return encryptedText;
    }

    /**
     * Decrypts the given data using the AES algorithm with GCM mode and the
     * specified configuration.
     *
     * @param data            the byte array containing the data to be decrypted
     * @param key             the SecretKeySpec object containing the key for
     *                        decryption
     * @param algorithmConfig the AlgorithmConfig object containing the algorithm
     *                        configuration parameters
     * @return a byte array containing the decrypted data
     * @throws IllegalArgumentException if any configuration parameters are invalid
     */
    public byte[] decrypt(final byte[] data, final SecretKeySpec key, final AlgorithmConfig algorithmConfig) {
        byte[] decryptedText = null;

        try {
            // Istanzia l'algoritmo AES con la modalità e il padding scelto usando come
            // provider Bouncy Castle
            final String aesMode = algorithmConfig.getParameterValueByName(AesAlgorithm.AES_MODE.getParameter());
            final String aesPadding = algorithmConfig.getParameterValueByName(AesAlgorithm.AES_PADDING.getParameter());
            final String aesVersion = String.join("/", algorithmConfig.getAlgorithmName(), aesMode, aesPadding);
            
            final Cipher cipher = Cipher.getInstance(aesVersion, "BC");

            // Crea le specifiche per GCM e inizializza il cipher
            final int gcmTagLength = Integer.valueOf(AesAlgorithm.GCM_TAG_LENGTH.getParameter());
            final GCMParameterSpec spec = new GCMParameterSpec(gcmTagLength, algorithmConfig.getSalt());
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            // Decifra il testo
            decryptedText = cipher.doFinal(data);
        } catch (final Exception e) {
            System.err.println("The decryption of the source generated an error.");
        }

        return decryptedText;
    }

}
