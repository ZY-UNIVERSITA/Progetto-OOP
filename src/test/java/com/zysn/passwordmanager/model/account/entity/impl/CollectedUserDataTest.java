package com.zysn.passwordmanager.model.account.entity.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;

public class CollectedUserDataTest {
    private CollectedUserData collectedUserData;

    private byte[] password;
    private byte[] passwordConfirm;
    private AlgorithmConfig algConf;
    private AlgorithmConfig algConf2;

    @BeforeEach
    void setup() {
        this.collectedUserData = new CollectedUserData();

        this.collectedUserData.setUsername("test");

        this.password = new byte[] { 1, 2, 3 };
        this.collectedUserData.setPassword(password);

        this.passwordConfirm = new byte[] { 1, 2, 3 };
        this.collectedUserData.setConfirmPassword(passwordConfirm);

        this.algConf = AlgorithmConfigFactory.createAlgorithmConfig("AES", null, null);
        this.collectedUserData.setPasswordDerivationConfig(algConf);

        this.algConf2 = AlgorithmConfigFactory.createAlgorithmConfig("AES", null, null);
        this.collectedUserData.setKeyStoreConfigEncryptionConfig(algConf2);

        this.collectedUserData.setEnabled2FA(true);
    }

    @Test
    void testDestroy() {
        this.collectedUserData.destroy();

        assertNull(this.collectedUserData.getUsername(), "The username is not null.");

        byte[] zeroArrays = new byte[] { 0, 0, 0 };
        assertNull(this.collectedUserData.getPassword(), "The password is not null.");
        assertArrayEquals(zeroArrays, this.password, "The password is not full of zeros.");

        assertNull(this.collectedUserData.getConfirmPassword(), "The confirmed password is not null.");
        assertArrayEquals(zeroArrays, this.passwordConfirm, "The password is not full of zeros.");


        assertNull(this.collectedUserData.getPasswordDerivationConfig(), "The password config is not null.");
        assertNull(algConf.getAlgorithmName(), "The name is not null.");

        assertNull(this.collectedUserData.getKeyStoreConfigEncryptionConfig(), "The key store config is not null.");
        assertNull(algConf2.getAlgorithmName(), "The name is not null.");

        assertFalse(this.collectedUserData.isEnabled2FA(), "The 2FA is not false.");
    }
}
