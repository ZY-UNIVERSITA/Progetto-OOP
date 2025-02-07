package com.zysn.passwordmanager.model.authentication.keystore.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.file.FileManager;

/**
 * Configuration class for managing key store settings.
 */
public class KeyStoreConfig {
    /**
     * The master key used to open the key store (encrypted in file).
     */
    private byte[] keyStoreEncryptionKey;

    /**
     * Salt added with the password-derived key to open the "TOTP encryption key" (encrypted in file).
     */
    private byte[] saltWithPasswordDerived;

    /**
     * The TOTP encryption master key (saved in the key store).
     */
    @JsonIgnore
    private byte[] totpEncryptionKey;

    /**
     * Salt added with the TOTP encryption master key (encrypted in file).
     */
    private byte[] saltWithTotpEncryptionKey;

    /**
     * The TOTP master key (saved in the key store).
     */
    @JsonIgnore
    private byte[] totpKey;

    /**
     * Default constructor for KeyStoreConfig.
     */
    public KeyStoreConfig() {

    }

    /**
     * Serializes this KeyStoreConfig object to a byte array.
     * @return Serialized byte array of this KeyStoreConfig object.
     */
    public byte[] serializeObject() {
        return FileManager.serializeJSON(this);
    }

    /**
     * Destroys the sensitive information in this KeyStoreConfig object.
     * Cleans and nullifies all the keys and salts.
     */
    public void destroy() {
        if (this.getKeyStoreEncryptionKey() != null) {
            CryptoUtils.cleanMemory(keyStoreEncryptionKey);
            this.setKeyStoreEncryptionKey(null);
        }

        if (this.getSaltWithPasswordDerived() != null) {
            CryptoUtils.cleanMemory(saltWithPasswordDerived);
            this.setSaltWithPasswordDerived(null);
        }
        
        if (this.getTotpEncryptionKey() != null) {
            CryptoUtils.cleanMemory(totpEncryptionKey);
            this.setTotpEncryptionKey(null);
        }
        
        if (this.getSaltWithTotpEncryptionKey() != null) {
            CryptoUtils.cleanMemory(saltWithTotpEncryptionKey);
            this.setSaltWithTotpEncryptionKey(null);
        }
        
        if (this.getTotpKey() != null) {
            CryptoUtils.cleanMemory(totpKey);
            this.setTotpKey(null);
        }
    }

    /* GETTER and SETTER */
    public byte[] getKeyStoreEncryptionKey() {
        return keyStoreEncryptionKey;
    }

    public byte[] getSaltWithPasswordDerived() {
        return saltWithPasswordDerived;
    }

    public byte[] getTotpEncryptionKey() {
        return totpEncryptionKey;
    }

    public byte[] getSaltWithTotpEncryptionKey() {
        return saltWithTotpEncryptionKey;
    }

    public byte[] getTotpKey() {
        return totpKey;
    }

    public void setKeyStoreEncryptionKey(byte[] keyStoreEncryptionKey) {
        this.keyStoreEncryptionKey = keyStoreEncryptionKey;
    }

    public void setSaltWithPasswordDerived(byte[] saltWithPasswordDerived) {
        this.saltWithPasswordDerived = saltWithPasswordDerived;
    }

    public void setTotpEncryptionKey(byte[] totpEncryptionKey) {
        this.totpEncryptionKey = totpEncryptionKey;
    }

    public void setSaltWithTotpEncryptionKey(byte[] saltWithTotpEncryptionKey) {
        this.saltWithTotpEncryptionKey = saltWithTotpEncryptionKey;
    }

    public void setTotpKey(byte[] totpKey) {
        this.totpKey = totpKey;
    }    

    
}
