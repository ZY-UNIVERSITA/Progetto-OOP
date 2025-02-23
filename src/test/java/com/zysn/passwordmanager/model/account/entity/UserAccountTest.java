package com.zysn.passwordmanager.model.account.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;

public class UserAccountTest {
    private UserAccount userAccount;

    @BeforeEach
    void setup() {
        this.userAccount = new UserAccount();

        // Algorithm configurations
        String algorithmType = "Key Derivation Algorithm";
        String algorithName = "argon2"; 

        Map<String, String> parameters = new HashMap<>(); 
        parameters.put("key_size", "128"); 
        parameters.put("round", "12");
        
        AlgorithmConfig algorithmConfig = new AlgorithmConfig(algorithmType, algorithName, null, parameters);

        // Username
        String username = "mongodb";

        // Master key
        SecretKeySpec masterKey = new SecretKeySpec(new byte[] { 1, 2, 3, 4, 5 }, "AES");

        this.userAccount.setUsername(username);
        this.userAccount.setAlgorithmConfig(algorithmConfig);
        this.userAccount.setMasterKey(masterKey);
    }

    @Test
    void testRemoveAccount() {
        assertNotNull(this.userAccount.getUsername());
        assertNotNull(this.userAccount.getAlgorithmConfig());
        assertNotNull(this.userAccount.getMasterKey());

        assertNotNull(this.userAccount);

        this.userAccount.removeAccount();

        assertNull(this.userAccount.getUsername());
        assertNull(this.userAccount.getAlgorithmConfig());
        assertNull(this.userAccount.getMasterKey());
    }
}
=======
package com.zysn.passwordmanager.model.account.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;

public class UserAccountTest {
    private UserAccount userAccount;

    @BeforeEach
    void setup() {
        this.userAccount = new UserAccount();

        // Algorithm configurations
        String algorithmType = "Key Derivation Algorithm";
        String algorithName = "argon2"; 

        Map<String, String> parameters = new HashMap<>(); 
        parameters.put("key_size", "128"); 
        parameters.put("round", "12");
        
        AlgorithmConfig algorithmConfig = new AlgorithmConfig(algorithmType, algorithName, null, parameters);

        // Username
        String username = "mongodb";

        // Master key
        SecretKeySpec masterKey = new SecretKeySpec(new byte[] { 1, 2, 3, 4, 5 }, "AES");

        this.userAccount.setUsername(username);
        this.userAccount.setAlgorithmConfig(algorithmConfig);
        this.userAccount.setMasterKey(masterKey);
    }

    @Test
    void testRemoveAccount() {
        assertNotNull(this.userAccount.getUsername());
        assertNotNull(this.userAccount.getAlgorithmConfig());
        assertNotNull(this.userAccount.getMasterKey());

        assertNotNull(this.userAccount);

        this.userAccount.removeAccount();

        assertNull(this.userAccount.getUsername());
        assertNull(this.userAccount.getAlgorithmConfig());
        assertNull(this.userAccount.getMasterKey());
    }
}