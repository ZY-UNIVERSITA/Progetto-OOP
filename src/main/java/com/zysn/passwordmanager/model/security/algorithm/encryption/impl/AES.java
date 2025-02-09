package com.zysn.passwordmanager.model.security.algorithm.encryption.impl;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.encryption.api.EncryptionAlgorithm;
import com.zysn.passwordmanager.model.utils.enumerations.AesEnum;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;

public class AES implements EncryptionAlgorithm {
    public AES() {

    }

    // Metodo per cifrare un messaggio
    public byte[] encrypt(byte[] data, SecretKeySpec key, AlgorithmConfig algorithmConfig) {
        byte[] encryptedText = null;

        try {
            // Istanzia l'algoritmo AES con la modalità e il padding scelto usando come
            // provider Bouncy Castle
            String aesVersion = algorithmConfig.getParameterValueByName(AesEnum.AES_ALGORITHM.getParameter());
            final Cipher cipher = Cipher.getInstance(aesVersion, "BC");

            // Crea le specifiche per GCM e inizializza il cipher
            int gcmTagLength = Integer.valueOf(AesEnum.GCM_TAG_LENGTH.getParameter());
            final GCMParameterSpec spec = new GCMParameterSpec(gcmTagLength, algorithmConfig.getSalt());
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            encryptedText = cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("The encryption of the source generated an error.");
        }

        return encryptedText;
    }

    // Metodo per decifrare un messaggio
    public byte[] decrypt(byte[] data, SecretKeySpec key, AlgorithmConfig algorithmConfig) {
        byte[] decryptedText = null;

        try {
            // Istanzia l'algoritmo AES con la modalità e il padding scelto usando come
            // provider Bouncy Castle
            String aesVersion = algorithmConfig.getParameterValueByName(AesEnum.AES_ALGORITHM.getParameter());
            final Cipher cipher = Cipher.getInstance(aesVersion, "BC");

            // Crea le specifiche per GCM e inizializza il cipher
            int gcmTagLength = Integer.valueOf(AesEnum.GCM_TAG_LENGTH.getParameter());
            final GCMParameterSpec spec = new GCMParameterSpec(gcmTagLength, algorithmConfig.getSalt());
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            // Decifra il testo
            decryptedText = cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("The decryption of the source generated an error.");
        }

        return decryptedText;
    }
}
