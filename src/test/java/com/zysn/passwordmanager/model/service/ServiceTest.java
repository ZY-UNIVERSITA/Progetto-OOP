package com.zysn.passwordmanager.model.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;

public class ServiceTest {

    private Service service;
    private byte[] password;
    private AlgorithmConfig encryptionConfig;

    @BeforeEach
    void setUp() {
        password = new byte[]{1, 2, 3};
        encryptionConfig = new AlgorithmConfig();
        service = new Service("Wokwi", "user123", "user@example.com", password, encryptionConfig, "Some info");
    }

    @Test
    void testCostructor() {
        assertEquals("Wokwi", service.getName());
        assertEquals("user123", service.getUsername());
        assertEquals("user@example.com", service.getEmail());
        assertEquals(password, service.getPassword());
        assertNotNull(service.getEncryptionConfig());
        assertEquals("Some info", service.getInfo());
    }

    @Test
    void testGettersAndSetters() {
        service.setName("NewName");
        service.setUsername("newUsername");
        service.setEmail("newEmail@example.com");
        service.setPassword(new byte[]{4, 5, 6});
        service.setEncryptionConfig(new AlgorithmConfig());
        service.setInfo("New info");

        assertEquals("NewName", service.getName());
        assertEquals("newUsername", service.getUsername());
        assertEquals("newEmail@example.com", service.getEmail());
        assertArrayEquals(new byte[]{4, 5, 6}, service.getPassword());
        assertNotNull(service.getEncryptionConfig());
        assertEquals("New info", service.getInfo());
    }

    @Test
    void testEqualsAndHashCode() {
        Service ser2 = new Service("Wokwi", "user123", "user@example.com", password, encryptionConfig, "Some info");
        assertTrue(service.equals(ser2));
        assertEquals(service.hashCode(), ser2.hashCode());

        Service ser3 = new Service();
        assertFalse(service.equals(ser3));
    }

    @Test
    void testToString() {
        String expectedString = "Service [name=Wokwi, username=user123, email=user@example.com, info=Some info]";
        assertEquals(expectedString, service.toString());
    }

}
