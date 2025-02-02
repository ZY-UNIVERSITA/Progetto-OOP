package com.zysn.passwordmanager.model.security.algorithm.encryption.impl;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.encryption.api.EncryptionAlgorithm;
import com.zysn.passwordmanager.model.utils.CryptoUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import java.util.HashMap;
import java.util.Map;

public class AES implements EncryptionAlgorithm {
    // Algorithm version of AES to use
    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    // Nonce length: 12 * 8 = 96 bit
    private static final int GCM_IV_LENGTH = 12;
    // Tag length: 128 bit
    private static final int GCM_TAG_LENGTH = 128;

    public static Map<String, String> createMapParams() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("aes_algorithm", AES_ALGORITHM);
        parameters.put("gcm_iv_length", String.valueOf(GCM_IV_LENGTH));
        parameters.put("gcm_tag_length", String.valueOf(GCM_TAG_LENGTH));

        return parameters;
    }

    // Metodo per cifrare un messaggio
    public byte[] encrypt(byte[] data, SecretKeySpec key, AlgorithmConfig algorithmConfig) {
        byte[] encryptedText = null;

        try {
            // Istanzia l'algoritmo AES con la modalità e il padding scelto usando Bouncy
            // Castle
            final Cipher cipher = Cipher.getInstance(AES_ALGORITHM, "BC");

            // Crea le specifiche per GCM e inizializza il cipher
            final GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, algorithmConfig.getSalt());
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            encryptedText = cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("Errore nella criptazione del messaggio.");
        }

        return encryptedText;
    }

    // Metodo per decifrare un messaggio
    public byte[] decrypt(byte[] data, SecretKeySpec key, AlgorithmConfig algorithmConfig) {
        byte[] decryptedText = null;

        try {
            // Istanzia l'algoritmo AES con la modalità e il padding scelto usando Bouncy
            // Castle
            final Cipher cipher = Cipher.getInstance(AES_ALGORITHM, "BC");

            // Crea le specifiche per GCM e inizializza il cipher
            final GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, algorithmConfig.getSalt());
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            // Decifra il testo
            decryptedText = cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("Errore nella decriptazione del messaggio.");
        }

        return decryptedText;
    }
}
