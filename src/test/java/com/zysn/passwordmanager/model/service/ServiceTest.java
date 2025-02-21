package com.zysn.passwordmanager.model.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.crypto.spec.SecretKeySpec;
import com.zysn.passwordmanager.model.account.entity.UserAccount;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;

public class ServiceTest {

    private Service service;
    private String password;
    private UserAccount user;
    private CryptoManager manager;

    @BeforeEach
    void setUp() {
        user = new UserAccount("primary", new AlgorithmConfig(), new SecretKeySpec(new byte[] { 1, 2, 3, 4, 5 }, "AES"));
        password = "String1234";
        service = new ServiceBuilder(user, manager)
        .setName("Wokwi")
        .setUsername("user123")
        .setEmail("user@example.com")
        .setPassword(password)
        .setInfo("Some info")
        .build();
    }

    @Test
    void testBuilderAndGetters() {
        assertEquals("Wokwi", service.getName());
        assertEquals("user123", service.getUsername());
        assertEquals("user@example.com", service.getEmail());
        assertNotNull(service.getPassword());
        assertEquals("Some info", service.getInfo());
    }

    @Test
    void testEqualsAndHashCode() {
        Service ser2 = new ServiceBuilder(user, manager)
        .setName("Wokwi")
        .setUsername("user123")
        .setEmail("user@example.com")
        .setPassword(password)
        .setInfo("Some info")
        .build();

        assertTrue(service.equals(ser2));
        assertEquals(service.hashCode(), ser2.hashCode());

        Service ser3 = new ServiceBuilder(user, manager)
        .build();
        assertFalse(service.equals(ser3));
    }

    @Test
    void testToString() {
        String expectedString = "Service [name=Wokwi, username=user123, email=user@example.com, info=Some info]";
        assertEquals(expectedString, service.toString());
    }

}
