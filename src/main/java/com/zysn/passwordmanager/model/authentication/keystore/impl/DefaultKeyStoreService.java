package com.zysn.passwordmanager.model.authentication.keystore.impl;

import java.util.function.Consumer;

import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreService;
import com.zysn.passwordmanager.model.enums.CryptoLength;
import com.zysn.passwordmanager.model.utils.crypto.impl.CryptoUtils;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

/**
 * DefaultKeyStoreService is an implementation of the KeyStoreService interface.
 * This service handles the generation of various encryption keys and salts
 * used within a keystore configuration.
 */
public class DefaultKeyStoreService implements KeyStoreService {

    private KeyStoreConfig keyStoreConfig;

    /**
     * Constructs a DefaultKeyStoreService with the specified KeyStoreConfig.
     *
     * @param keyStoreConfig the KeyStoreConfig to be used by this service
     */
    public DefaultKeyStoreService(KeyStoreConfig keyStoreConfig) {
        this.keyStoreConfig = keyStoreConfig;
    }

    /**
     * Generates a secure and usable password of default length using the provided lambda function.
     *
     * @param setterFunction the lambda function to accept and set the generated password in bytes
     * @return true if the password has been generated successfully, false otherwise
     */
    private boolean generateSecureUsablePassword(Consumer<byte[]> setterFunction) {
        int secureValueLength = CryptoLength.OPTIMAL_PASSWORD_LENGTH.getParameter();

        return this.generateSecureUsablePassword(secureValueLength, setterFunction);
    }

    /**
     * Generates a secure and usable password of the specified length using the provided lambda function.
     *
     * @param passwordLength the length of the password to be generated
     * @param setterFunction the lambda function to accept and set the generated password in bytes
     * @return true if the password has been generated successfully, false otherwise
     */
    private boolean generateSecureUsablePassword(int passwordLength, Consumer<byte[]> setterFunction) {
        char[] password = null;
        boolean hasValueBeenGenerated = false;

        try {
            password = CryptoUtils.generateSecureRandomNotUsablePassword(passwordLength);
            byte[] passwordInByte = EncodingUtils.charToByteConverter(password);

            if (passwordInByte != null) {
                hasValueBeenGenerated = !hasValueBeenGenerated;
            }

            setterFunction.accept(passwordInByte);
        } finally {
            CryptoUtils.cleanMemory(password);
        }

        return hasValueBeenGenerated;
    }

    /**
     * Generates the keystore encryption key.
     *
     * @return true if the encryption key has been generated successfully, false otherwise
     */
    public boolean generateKeyStoreEncryptionKey() {
        return this.generateSecureUsablePassword(this.getKeyStoreConfig()::setKeyStoreEncryptionKey);
    }

    /**
     * Generates the TOTP encryption key.
     *
     * @return true if the TOTP encryption key has been generated successfully, false otherwise
     */
    public boolean generateTotpEncryptionKey() {
        return this.generateSecureUsablePassword(this.getKeyStoreConfig()::setTotpEncryptionKey);
    }

    /**
     * Generates the TOTP key.
     *
     * @return true if the TOTP key has been generated successfully, false otherwise
     */
    public boolean generateTotpKey() {
        int totpLength = CryptoLength.OPTIMAL_PASSWORD_LENGTH.getParameter();
        this.getKeyStoreConfig().setTotpKey(CryptoUtils.generateSalt(totpLength));
        return this.getKeyStoreConfig().getTotpKey() != null;
    }

    /**
     * Generates salts for password-derived and TOTP encryption key storage.
     *
     * @return true if the salts have been generated successfully, false otherwise
     */
    public boolean generateSalt() {
        int saltLength = CryptoLength.MINIMUM_PASSWORD_LENGTH.getParameter();
        return this.generateSecureUsablePassword(saltLength, this.getKeyStoreConfig()::setSaltWithPasswordDerived)
                && this.generateSecureUsablePassword(saltLength, this.getKeyStoreConfig()::setSaltWithTotpEncryptionKey)
                && this.generateSecureUsablePassword(saltLength*2, this.getKeyStoreConfig()::setServiceDecryptionSalt);
    }

    /**
     * Returns the KeyStoreConfig used by this service.
     *
     * @return the KeyStoreConfig used by this service
     */
    public KeyStoreConfig getKeyStoreConfig() {
        return keyStoreConfig;
    }
}
