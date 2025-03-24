package com.zysn.passwordmanager.model.account.entity.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserAuthKeyTest {
    private UserAuthKey userAuthKey;

    private byte[] originalPassword;
    private byte[] originalPasswordDerivedKey;
    private byte[] originalTotpEncryptionKey;
    private byte[] originalTotpKey;
    private byte[] originalServiceConfigKey;
    
    private byte[] fullZero;

    @BeforeEach
    public void setUp() {
        userAuthKey = new UserAuthKey();
        userAuthKey.setPassword(new byte[]{1, 2, 3});
        userAuthKey.setPasswordDerivedKey(new byte[]{4, 5, 6});
        userAuthKey.setTotpEncryptionKey(new byte[]{7, 8, 9});
        userAuthKey.setTotpKey(new byte[]{10, 11, 12});
        userAuthKey.setServiceConfigKey(new byte[]{13, 14, 15});

        originalPassword = userAuthKey.getPassword();
        originalPasswordDerivedKey = userAuthKey.getPasswordDerivedKey();
        originalTotpEncryptionKey = userAuthKey.getTotpEncryptionKey();
        originalTotpKey = userAuthKey.getTotpKey();
        originalServiceConfigKey = userAuthKey.getServiceConfigKey();

        fullZero = new byte[3];
        Arrays.fill(fullZero, (byte) 0);
    }

    @Test
    public void testDestroy() {
        userAuthKey.destroy();

        assertNull(userAuthKey.getPassword(), "Password should be null after destroy");
        assertNull(userAuthKey.getPasswordDerivedKey(), "PasswordDerivedKey should be null after destroy");
        assertNull(userAuthKey.getTotpEncryptionKey(), "TotpEncryptionKey should be null after destroy");
        assertNull(userAuthKey.getTotpKey(), "TotpKey should be null after destroy");
        assertNull(userAuthKey.getServiceConfigKey(), "ServiceConfigKey should be null after destroy");

        assertArrayEquals(fullZero, originalPassword, "Password array should be zeroed out");
        assertArrayEquals(fullZero, originalPasswordDerivedKey, "PasswordDerivedKey array should be zeroed out");
        assertArrayEquals(fullZero, originalTotpEncryptionKey, "TotpEncryptionKey array should be zeroed out");
        assertArrayEquals(fullZero, originalTotpKey, "TotpKey array should be zeroed out");
        assertArrayEquals(fullZero, originalServiceConfigKey, "ServiceConfigKey array should be zeroed out");
    }
}