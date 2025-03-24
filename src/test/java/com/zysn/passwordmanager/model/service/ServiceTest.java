package com.zysn.passwordmanager.model.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.zysn.passwordmanager.model.account.entity.impl.UserAccount;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;

public class ServiceTest {

    private Service service;
    private byte[] password;
    
    @Mock
    private UserAccount user;
    
    @Mock
    private CryptoManager manager;
    
    @Mock
    private AlgorithmConfig encryptionConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(user.getMasterKey()).thenReturn(new byte[]{1, 2, 3, 4, 5});
        when(encryptionConfig.getAlgorithmName()).thenReturn("AES");
        when(manager.encrypt(any(), any(), any())).thenReturn(new byte[]{10, 20, 30});
        
        password = "String1234".getBytes();

        service = new ServiceBuilder(user, manager)
            .setName("Wokwi")
            .setUsername("user123")
            .setEmail("user@example.com")
            .setEncryptionConfig(encryptionConfig)
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
            .setEncryptionConfig(encryptionConfig)
            .setPassword(password)
            .setInfo("Some info")
            .build();

        assertEquals(service, ser2);
        assertEquals(service.hashCode(), ser2.hashCode());

        Service ser3 = new ServiceBuilder(user, manager)
            .setName("Different")
            .setEncryptionConfig(encryptionConfig)
            .setPassword(password)
            .build();
        
        assertNotEquals(service, ser3);
    }

    @Test
    void testToString() {
        String expectedString = "Service [name=Wokwi, username=user123, email=user@example.com, info=Some info]";
        assertEquals(expectedString, service.toString());
    }
}
