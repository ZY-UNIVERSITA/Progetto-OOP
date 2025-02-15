package com.zysn.passwordmanager.model.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.utils.FileManager;

public class ServiceManagerTest {

    private ServiceManager serviceManager;

    @BeforeEach
    void setUp() {
        ServiceManager.resetInstance();
        serviceManager = ServiceManager.getInstance();
    }

    @Test
    void testAddService() {
        Service service = new Service("Netflix", "user", "email@gmail.com", new byte[] {1,2,3,4}, new AlgorithmConfig(), "Streaming");
        assertTrue(serviceManager.addService(service));
        assertEquals(1, serviceManager.getServices().size());
        assertEquals(service, serviceManager.getServices().get(0));
    }

    @Test
    void testGetServices() {
        Service ser = new Service("Sky", "user", "user@gmail.com", new byte[] {1, 2, 3}, null, "Info");
        serviceManager.addService(ser);
        assertEquals(1, serviceManager.getServices().size());
        assertEquals(ser, serviceManager.getServices().get(0));
    }

    @Test
    void testModifyService() {
        Service newSer = new Service("Netflix", "user", "email@gmail.com", new byte[] {1,2,3,4}, new AlgorithmConfig(), "Streaming");
        Service ser = new Service("Sky", "user", "user@gmail.com", new byte[] {1, 2, 3}, null, "Info");
        serviceManager.addService(ser);
        serviceManager.modifyService("Sky", newSer);

        Service updatedSer = serviceManager.getServices().get(0);

        assertEquals(newSer.getName(), updatedSer.getName());
        assertEquals(newSer.getUsername(), updatedSer.getUsername());
        assertEquals(newSer.getEmail(), updatedSer.getEmail());
        assertEquals(newSer.getPassword(), updatedSer.getPassword());
        assertEquals(newSer.getEncryptionConfig(), updatedSer.getEncryptionConfig());
        assertEquals(newSer.getInfo(), updatedSer.getInfo());
    }

    @Test
    void testRemoveService() {
        Service ser = new Service("Sky", "user", "user@gmail.com", new byte[] {1, 2, 3}, null, "Info");
        serviceManager.addService(ser);
        
        assertTrue(serviceManager.removeService("Sky"));
        assertEquals(0, serviceManager.getServices().size());
        assertTrue(serviceManager.getServices().isEmpty());
    }

    @Test
    void testSearchService() {
        Service ser1 = new Service("Netflix", "user1", "user1@gmail.com", new byte[] {1, 2, 3}, null, "Streaming");
        Service ser2 = new Service("Amazon Prime", "user2", "user2@gmail.com", new byte[] {4, 5, 6}, null, "Streaming");
        Service ser3 = new Service("Spotify", "user3", "user3@gmail.com", new byte[] {7, 8, 9}, null, "Music");
        serviceManager.addService(ser1);
        serviceManager.addService(ser2);
        serviceManager.addService(ser3);

        assertTrue(serviceManager.searchService("Sky").isEmpty());

        List <Service> res = serviceManager.searchService("Spotify");
        assertEquals(1, res.size());
        assertEquals(ser3, res.get(0));

        List<Service> res2 = serviceManager.searchService("Am");
        assertEquals(1, res2.size());
        assertEquals(ser2, res2.get(0));
    }

    @Test
    void testLoadServices() {
        CryptoManager cryptoManager = new CryptoManager();
        FileManager fileManager = new FileManager();
        SecretKeySpec secretKey = new SecretKeySpec("mysecretkey12345".getBytes(), "AES");

        boolean res = serviceManager.loadServices(secretKey, cryptoManager, fileManager);

        assertTrue(res);
        assertNotNull(serviceManager.getServices());
    }

    @Test
    void testSaveServices() {
        CryptoManager cryptoManager = new CryptoManager();
        FileManager fileManager = new FileManager();
        SecretKeySpec secretKey = new SecretKeySpec("mysecretkey12345".getBytes(), "AES");

        serviceManager.addService(new Service("Netflix", "user1", "user1@gmail.com", new byte[] {1, 2, 3}, null, "Streaming"));
        serviceManager.addService(new Service("Spotify", "user3", "user3@gmail.com", new byte[] {7, 8, 9}, null, "Music"));

        boolean result = serviceManager.saveServices(secretKey, cryptoManager, fileManager);

        assertTrue(result);
    }
}
