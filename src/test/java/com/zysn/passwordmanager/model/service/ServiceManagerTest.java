// package com.zysn.passwordmanager.model.service;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// import java.util.List;

// import javax.crypto.spec.SecretKeySpec;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.zysn.passwordmanager.model.account.entity.UserAccount;
// import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
// import com.zysn.passwordmanager.model.security.manager.CryptoManager;
// import com.zysn.passwordmanager.model.utils.file.api.FileManager;

// @ExtendWith(MockitoExtension.class)
// public class ServiceManagerTest {

//     private ServiceManager serviceManager;

//     @Mock
//     private UserAccount user;

//     @Mock
//     private CryptoManager cryptoManager;

//     @Mock
//     private FileManager fileManager;

//     private SecretKeySpec secretKey;

//     @Mock
//     private AlgorithmConfig algorithmConfig;

//     @BeforeEach
//     void setUp() {
//         ServiceManager.resetInstance();
//         serviceManager = ServiceManager.getInstance();

//         secretKey = new SecretKeySpec("mysecretkey12345".getBytes(), "AES");

//         serviceManager.setUserAccount(user);
//         serviceManager.setCryptoManager(cryptoManager);
//     }

//     @Test
//     void testAddAndGetService() {
//         Service service = new ServiceBuilder(user, cryptoManager)
//         .setName("Netflix")
//         .setUsername("user")
//         .setEmail("email@gmail.com")
//         .setPassword("Password1234")
//         .setInfo("Streaming")
//         .build();

//         assertTrue(serviceManager.addService(service));
//         assertEquals(1, serviceManager.getServices().size());
//         assertEquals(service, serviceManager.getServices().get(0));
//     }

//     @Test
//     void testModifyService() {
//         Service oldService = new ServiceBuilder(user, cryptoManager)
//         .setName("Netflix")
//         .setUsername("user")
//         .setEmail("email@gmail.com")
//         .setPassword("Password1234")
//         .setInfo("Streaming")
//         .build();

//         Service newService = new ServiceBuilder(user, cryptoManager)
//         .setName("Netflix")
//         .setUsername("user2")
//         .setEmail("user@live.it")
//         .setPassword("Netflix1234")
//         .setInfo("Info")
//         .build();
        
//         serviceManager.addService(oldService);
//         serviceManager.modifyService("Netflix", "Netflix", "user2", "user@live.it", "Netflix1234", "Info");

//         Service updatedService = serviceManager.getServices().get(0);

//         assertEquals(newService.getName(), updatedService.getName());
//         assertEquals(newService.getUsername(), updatedService.getUsername());
//         assertEquals(newService.getEmail(), updatedService.getEmail());
//         assertEquals(newService.getPassword(), updatedService.getPassword());
//         assertEquals(newService.getEncryptionConfig(), updatedService.getEncryptionConfig());
//         assertEquals(newService.getInfo(), updatedService.getInfo());
//     }

//     @Test
//     void testRemoveService() {
//         Service service = new ServiceBuilder(user, cryptoManager)
//         .setName("Sky")
//         .setUsername("user4")
//         .setEmail("user@libero.it")
//         .setPassword("Sky1234")
//         .setInfo("")
//         .build();

//         serviceManager.addService(service);
        
//         assertTrue(serviceManager.removeService("Sky"));
//         assertEquals(0, serviceManager.getServices().size());
//         assertTrue(serviceManager.getServices().isEmpty());
//     }

//     @Test
//     void testSearchService() {
//         Service ser1 = new ServiceBuilder(user, cryptoManager)
//         .setName("Sky")
//         .setUsername("user4")
//         .setEmail("user@libero.it")
//         .setPassword("Sky1234")
//         .setInfo("")
//         .build();

//         Service ser2 = new ServiceBuilder(user, cryptoManager)
//         .setName("Netflix")
//         .setUsername("user")
//         .setEmail("email@gmail.com")
//         .setPassword("Password1234")
//         .setInfo("Streaming")
//         .build();

//         Service ser3 = new ServiceBuilder(user, cryptoManager)
//         .setName("Spotify")
//         .setUsername("user1")
//         .setEmail("user1@gmail.com")
//         .setPassword("Spotify1234")
//         .setInfo("Music")
//         .build();

//         serviceManager.addService(ser1);
//         serviceManager.addService(ser2);
//         serviceManager.addService(ser3);

//         assertTrue(serviceManager.searchService("Amazon").isEmpty());

//         List <Service> res = serviceManager.searchService("Spotify");
//         assertEquals(1, res.size());
//         assertEquals(ser3, res.get(0));

//         List<Service> res2 = serviceManager.searchService("Net");
//         assertEquals(1, res2.size());
//         assertEquals(ser2, res2.get(0));
//     }

//     @Test
//     void testGetDecryptedPassword() {
//         Service service = new ServiceBuilder(user, cryptoManager)
//                 .setName("Netflix")
//                 .setUsername("user")
//                 .setPassword("EncryptedPassword")
//                 .setInfo("Streaming")
//                 .build();

//         serviceManager.addService(service);

//         String password = "EncryptedPassword";
//         String decryptedPassword = serviceManager.getDecryptedPassword(service);

//         assertNotNull(decryptedPassword);
//         assertEquals(password, decryptedPassword);

//         verify(cryptoManager).decrypt(any(), any(), any());
//     }

//     @Test
//     void testLoadServices() {
//         boolean res = serviceManager.loadServices(secretKey, cryptoManager, fileManager);

//         assertTrue(res);
//         assertNotNull(serviceManager.getServices());

//         verify(fileManager).loadData(any());
//     }

//     @Test
//     void testSaveServices() {
//         Service ser1 = new ServiceBuilder(user, cryptoManager)
//                 .setName("Sky")
//                 .setUsername("user4")
//                 .setEmail("user@libero.it")
//                 .setPassword("Sky1234")
//                 .setInfo("")
//                 .build();

//         Service ser2 = new ServiceBuilder(user, cryptoManager)
//                 .setName("Netflix")
//                 .setUsername("user")
//                 .setEmail("email@gmail.com")
//                 .setPassword("Password1234")
//                 .setInfo("Streaming")
//                 .build();

//         serviceManager.addService(ser1);
//         serviceManager.addService(ser2);

//         boolean result = serviceManager.saveServices(secretKey, cryptoManager, fileManager);

//         assertTrue(result);

//         verify(fileManager).saveData(any(), any());
//     }
// }
