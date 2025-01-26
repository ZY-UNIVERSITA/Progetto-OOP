package com.zysn.passwordmanager.model.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.entity.UserAccount;

public class FileManagerTest {
    private FileManager fileManager;
    private UserAccount userAccount;

    @BeforeEach
    void setup() {
        this.fileManager = new FileManager();
    }

    @Test
    void testLoadServicesFile() {

    }

    @Test
    void testLoadUserData() {
        String username = "mongodb";
        this.userAccount = fileManager.loadUserData(username);

        assertEquals(username, this.userAccount.getUsername(), "Users are not the same.");

        String algorithName = "argon2";
        assertEquals(algorithName, this.userAccount.getAlgorithmConfig().getAlgorithmName(), "Algorithm name are not the same.");

        String algorithType = "Key Derivation Algorithm";
        assertEquals(algorithType, this.userAccount.getAlgorithmConfig().getAlgorithmType(), "Algorithm type are not the same.");

        String parameterName = "memory_cost";
        String parameterValue = "65536";
        assertEquals(parameterValue, this.userAccount.getAlgorithmConfig().getParameterValueByName(parameterName), "The value is not " + parameterValue);
    }

    @Test
    void testSaveServicesFile() {

    }

    @Test
    void testSaveUserData() {
        String username = "mongodb";
        this.userAccount = fileManager.loadUserData(username);

        String newUsername = "mongodb2";
        this.userAccount.setUsername(newUsername);

        fileManager.saveUserData(this.userAccount);

        UserAccount newUserAccount = fileManager.loadUserData(newUsername);
        assertEquals(this.userAccount, newUserAccount, "The 2 accounts are not the same.");
    }
}
