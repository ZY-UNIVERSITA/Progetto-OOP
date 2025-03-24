package com.zysn.passwordmanager.model.authentication.passwordlist.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.bouncycastle.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServiceCryptoConfigTest {
    private ServiceCryptoConfig serviceCryptoConfig;
    private char[] fileName;
    private byte[] saltForHKDF;
    private byte[] saltForServiceEncryption;

    @BeforeEach
    void setup() {
        this.serviceCryptoConfig = new ServiceCryptoConfig();

        this.fileName = "file test".toCharArray();
        this.saltForHKDF = new byte[] { 1, 2, 3 };
        this.saltForServiceEncryption = new byte[] { 1, 2, 3 };

        this.serviceCryptoConfig.setFileName(this.fileName);
        this.serviceCryptoConfig.setSaltForHKDF(this.saltForHKDF);
        this.serviceCryptoConfig.setSaltForServiceEncryption(this.saltForServiceEncryption);
    }

    @Test
    void testDestroy() {
        this.serviceCryptoConfig.destroy();

        assertNull(this.serviceCryptoConfig.getFileName(), "The filename should be null.");
        assertNull(this.serviceCryptoConfig.getSaltForHKDF(), "The filename should be null.");
        assertNull(this.serviceCryptoConfig.getSaltForServiceEncryption(), "The filename should be null.");

        byte[] zeroArrays = new byte[] { 0, 0, 0 };
        char[] nullCharArray = new char[this.fileName.length];
        Arrays.fill(nullCharArray, '\u0000');

        assertArrayEquals(zeroArrays, this.saltForHKDF, "The array is not full of zero in byte");
        assertArrayEquals(zeroArrays, this.saltForServiceEncryption, "The array is not full of zero in byte");
    }

    @Test
    void testSerialize() {
        byte[] serializedObject = this.serviceCryptoConfig.serialize();

        assertNotNull(serializedObject, "The config file has not been serialized into byte[].");
    }
}
