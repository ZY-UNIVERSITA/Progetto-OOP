package com.zysn.passwordmanager.model.authentication.keystore.impl;

import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.security.api.MustBeDestroyed;

/**
 * Configuration class for managing key store settings.
 */
public class KeyStoreConfig implements MustBeDestroyed {
    /**
     * The master key used to open the key store.
     */
    private byte[] keyStoreEncryptionKey;

    /**
     * Salt added with the password-derived key to open the "TOTP encryption key".
     */
    private byte[] saltWithPasswordDerived;

    /**
     * Salt added with the TOTP encryption master key.
     */
    private byte[] saltWithTotpEncryptionKey;

    /**
     * Salt used for HKDF.
     */
    private byte[] saltForHKDF;

    /**
     * Salt used for service decryption.
     */
    private byte[] serviceDecryptionSalt;

    /**
     * Default constructor for KeyStoreConfig.
     */
    public KeyStoreConfig() {

    }

    /**
     * Serializes this KeyStoreConfig object to a byte array.
     * 
     * @return Serialized byte array of this KeyStoreConfig object.
     */
    public byte[] serialize() {
        return EncodingUtils.serializeData(this);
    }

    /**
     * Destroys the sensitive information in this KeyStoreConfig object.
     * Cleans and nullifies all the keys and salts.
     */
    public void destroy() {
        if (this.getKeyStoreEncryptionKey() != null) {
            CryptoUtils.cleanMemory(this.getKeyStoreEncryptionKey());
            this.setKeyStoreEncryptionKey(null);
        }

        if (this.getSaltWithPasswordDerived() != null) {
            CryptoUtils.cleanMemory(this.getSaltWithPasswordDerived());
            this.setSaltWithPasswordDerived(null);
        }

        if (this.getSaltWithTotpEncryptionKey() != null) {
            CryptoUtils.cleanMemory(this.getSaltWithTotpEncryptionKey());
            this.setSaltWithTotpEncryptionKey(null);
        }

        if (this.getSaltForHKDF() != null) {
            CryptoUtils.cleanMemory(this.getSaltForHKDF());
            this.setSaltForHKDF(null);
        }

        if (this.getServiceDecryptionSalt() != null) {
            CryptoUtils.cleanMemory(this.getServiceDecryptionSalt());
            this.setServiceDecryptionSalt(null);
        }
    }

    /* GETTER and SETTER */
    public byte[] getKeyStoreEncryptionKey() {
        return keyStoreEncryptionKey;
    }

    public char[] getKeyStoreEncryptionKeyChar() {
        return EncodingUtils.byteToBase64(this.getKeyStoreEncryptionKey());
    }

    public byte[] getSaltWithPasswordDerived() {
        return saltWithPasswordDerived;
    }

    public char[] getSaltWithPasswordDerivedChar() {
        return EncodingUtils.byteToCharConverter(this.getSaltWithPasswordDerived());
    }

    public byte[] getSaltWithTotpEncryptionKey() {
        return saltWithTotpEncryptionKey;
    }

    public char[] getSaltWithTotpEncryptionKeyChar() {
        return EncodingUtils.byteToCharConverter(this.getSaltWithTotpEncryptionKey());
    }

    public byte[] getServiceDecryptionSalt() {
        return serviceDecryptionSalt;
    }

    public void setKeyStoreEncryptionKey(final byte[] keyStoreEncryptionKey) {
        this.keyStoreEncryptionKey = keyStoreEncryptionKey;
    }

    public void setSaltWithPasswordDerived(final byte[] saltWithPasswordDerived) {
        this.saltWithPasswordDerived = saltWithPasswordDerived;
    }

    public void setSaltWithTotpEncryptionKey(final byte[] saltWithTotpEncryptionKey) {
        this.saltWithTotpEncryptionKey = saltWithTotpEncryptionKey;
    }

    public void setServiceDecryptionSalt(final byte[] serviceDecryptionSalt) {
        this.serviceDecryptionSalt = serviceDecryptionSalt;
    }

    public byte[] getSaltForHKDF() {
        return saltForHKDF;
    }

    public void setSaltForHKDF(final byte[] saltForHKDF) {
        this.saltForHKDF = saltForHKDF;
    }
}
