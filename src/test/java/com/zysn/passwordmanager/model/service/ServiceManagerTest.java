package com.zysn.passwordmanager.model.service;

import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

//import com.fasterxml.jackson.core.type.TypeReference;
import com.zysn.passwordmanager.model.account.entity.impl.UserAccount;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
//import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;

import java.util.List;

//import javax.crypto.spec.SecretKeySpec;

public class ServiceManagerTest {

    private ServiceManager serviceManager;

    @Mock
    private UserAccount user;
    
    @Mock
    private CryptoManager cryptoManager;
    
    @Mock
    private AlgorithmConfig encryptionConfig;
    
    @Mock
    private FileManager fileManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ServiceManager.resetInstance();
        serviceManager = ServiceManager.getInstance();

        serviceManager.setUserAccount(user);
        serviceManager.setCryptoManager(cryptoManager);
        serviceManager.setAlgorithmConfig(encryptionConfig);
        serviceManager.setFileName("testServices");
    }

    @Test
    void testAddService() {
        Service service = mock(Service.class);
        when(service.getName()).thenReturn("Test Service");

        boolean added = serviceManager.addService(service);

        assertTrue(added);
        assertEquals(1, serviceManager.getServices().size());
    }

    @Test
    void testRemoveService() {
        Service service = mock(Service.class);
        when(service.getName()).thenReturn("Test Service");
        serviceManager.addService(service);

        boolean removed = serviceManager.removeService("Test Service");

        assertTrue(removed);
        assertTrue(serviceManager.getServices().isEmpty());
    }

    @Test
    void testSelectService() {
        Service service = mock(Service.class);
        when(service.getName()).thenReturn("Test Service");
        serviceManager.addService(service);

        Service selectedService = serviceManager.selectService("Test Service");

        assertNotNull(selectedService);
        assertEquals("Test Service", selectedService.getName());
    }

    @Test
    void testSearchService() {
        Service service1 = mock(Service.class);
        when(service1.getName()).thenReturn("Test Service 1");
        Service service2 = mock(Service.class);
        when(service2.getName()).thenReturn("Test Service 2");

        serviceManager.addService(service1);
        serviceManager.addService(service2);

        List<Service> result = serviceManager.searchService("Test");

        assertEquals(2, result.size());
        assertEquals("Test Service 1", result.get(0).getName());
    }

    /*
    @Test
    void testModifyService() {
        Service service = mock(Service.class);
        when(service.getName()).thenReturn("Test Service");
        when(service.getPassword()).thenReturn(new byte[]{1, 2, 3});
        serviceManager.addService(service);

        Service updatedService = mock(Service.class);
        when(updatedService.getName()).thenReturn("Updated Service");
        when(updatedService.getPassword()).thenReturn(new byte[]{3, 2, 1});
        when(updatedService.getEncryptionConfig()).thenReturn(encryptionConfig);

        boolean modified = serviceManager.modifyService("Test Service", updatedService);

        assertTrue(modified);
        assertEquals(1, serviceManager.getServices().size());
        assertEquals("Updated Service", serviceManager.getServices().get(0).getName());
    }
    */

    /*
    @Test
    void testGetDecryptedPassword() {
        Service service = mock(Service.class);

        when(service.getPassword()).thenReturn(new byte[]{1, 2, 3});
        when(service.getEncryptionConfig()).thenReturn(encryptionConfig);
        when(encryptionConfig.getAlgorithmName()).thenReturn("AES");
        when(user.getMasterKey()).thenReturn("masterKey".getBytes());

        when(cryptoManager.decrypt(
            eq(new byte[]{1, 2, 3}),
            any(SecretKeySpec.class),
            eq(encryptionConfig)
            )).thenReturn(new byte[]{65, 66, 67});

        String password = serviceManager.getDecryptedPassword(service);

        assertEquals("ABC", password);
    }
    */

    /*
    @Test
    void testSaveServices() {
        Service service = mock(Service.class);
        when(service.getName()).thenReturn("Test Service");
        when(service.getPassword()).thenReturn(new byte[]{1, 2, 3});
        
        serviceManager.addService(service);

        byte [] services = new byte[]{1, 2, 3};
        try (MockedStatic<EncodingUtils> utilities = mockStatic(EncodingUtils.class)) {
            utilities.when(() -> EncodingUtils.serializeData(serviceManager.getServices())).thenReturn(services);
        
        doNothing().when(fileManager).saveData(anyString(), any(byte[].class));

        boolean result = serviceManager.saveServices();

        verify(fileManager, times(1)).saveData(anyString(), any(byte[].class));
        assertTrue(result);
        }
    }
    */

    /*
    @Test
    void testLoadServices() {
        Service service = mock(Service.class);
        when(service.getName()).thenReturn("Test Service");

        byte[] encryptedData = new byte[]{10, 20, 30};
        when(fileManager.loadData(anyString())).thenReturn(encryptedData);

        when(cryptoManager.decrypt(any(byte[].class), any(SecretKeySpec.class), any(AlgorithmConfig.class))).thenReturn(encryptedData);
        when(EncodingUtils.deserializeData(eq(encryptedData), any(TypeReference.class))).thenReturn(List.of(service));

        boolean loaded = serviceManager.loadServices();

        assertTrue(loaded);
        assertFalse(serviceManager.getServices().isEmpty());
        assertEquals(1, serviceManager.getServices().size());
        assertEquals("Test Service", serviceManager.getServices().get(0).getName());
    }
    */
}
