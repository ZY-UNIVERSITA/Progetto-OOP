package com.zysn.passwordmanager.model.account.entity.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.bouncycastle.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserAccountTest {
    private UserAccount userAccount;
    private byte[] masterKey;

    @BeforeEach
    void setup() {
        this.userAccount = new UserAccount("prova");

        this.masterKey = new byte[] { 1, 2, 3 };
        this.userAccount.setMasterKey(masterKey);
    }

    @Test
    void testDestroy() {
        this.userAccount.destroy();

        assertNull(this.userAccount.getUsername(), "The username is not null.");
        assertNull(this.userAccount.getMasterKey(), "The master key is not null.");

        byte[] fullZero = new byte[3];
        Arrays.fill(fullZero, (byte) 0);
        assertArrayEquals(fullZero, this.masterKey, "The master key has not been destroyed.");
    }
}
