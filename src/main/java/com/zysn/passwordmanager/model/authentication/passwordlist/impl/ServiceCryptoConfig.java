package com.zysn.passwordmanager.model.authentication.passwordlist.impl;

import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.security.api.MustBeDestroyed;

/**
 * Configuration class for service cryptographic operations.
 * 
 * This class provides methods for serialization and secure destruction of sensitive data.
 */
public class ServiceCryptoConfig implements MustBeDestroyed {

    private char[] fileName;
    private byte[] saltForHKDF;
    private byte[] saltForServiceEncryption;

    /**
     * Constructs a new ServiceCryptoConfig.
     */
    public ServiceCryptoConfig() {

    }

    /**
     * Serializes the ServiceCryptoConfig instance into a byte array.
     * 
     * @return The serialized data as a byte array.
     */
    public byte[] serialize() {
        return EncodingUtils.serializeData(this);
    }

    /**
     * Destroys sensitive data stored in the ServiceCryptoConfig instance.
     * 
     * This method securely cleans memory and sets fields to null.
     */
    @Override
    public void destroy() {
        if (this.getFileName() != null) {
            CryptoUtils.cleanMemory(this.getFileName());
            this.setFileName(null);
        }

        if (this.getSaltForHKDF() != null) {
            CryptoUtils.cleanMemory(this.getSaltForHKDF());
            this.setSaltForHKDF(null);
        }

        if (this.getSaltForServiceEncryption() != null) {
            CryptoUtils.cleanMemory(this.getSaltForServiceEncryption());
            this.setSaltForServiceEncryption(null);
        }

    }

    /* GETTER and SETTER */
    public char[] getFileName() {
        return fileName;
    }

    public void setFileName(final char[] fileName) {
        this.fileName = fileName;
    }

    public byte[] getSaltForHKDF() {
        return saltForHKDF;
    }

    public void setSaltForHKDF(final byte[] saltForHKDF) {
        this.saltForHKDF = saltForHKDF;
    }

    public byte[] getSaltForServiceEncryption() {
        return saltForServiceEncryption;
    }

    public void setSaltForServiceEncryption(final byte[] serviceSalt) {
        this.saltForServiceEncryption = serviceSalt;
    }
}
