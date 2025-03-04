package com.zysn.passwordmanager.model.backup;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import com.zysn.passwordmanager.model.service.Service;
import com.zysn.passwordmanager.model.service.ServiceManager;
import com.zysn.passwordmanager.model.utils.data.DataUtils;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;


public class BackupManagerTest {

    private BackupManager backupManager;
    private FileManager fileManager;
    private DefaultSessionManager session;
    private AccountManager accountManager;
    private ServiceManager serviceManager;
    private CryptoManager cryptoManager;

    @BeforeEach
    public void setUp() {
        fileManager = mock(FileManager.class);
        session = mock(DefaultSessionManager.class);
        accountManager = mock(AccountManager.class);
        serviceManager = mock(ServiceManager.class);
        cryptoManager = mock(CryptoManager.class);

        try (MockedStatic<ServiceManager> mockedServiceManager = mockStatic(ServiceManager.class)) {
            mockedServiceManager.when(ServiceManager::getInstance).thenReturn(serviceManager);
           
            when(serviceManager.getCryptoManager()).thenReturn(cryptoManager);


            when(cryptoManager.encrypt(any(), any(), any())).thenReturn(new byte[]{1, 2, 3});
            when(cryptoManager.decrypt(any(), any(), any())).thenReturn(new byte[]{4, 5, 6});


            try (MockedStatic<DataUtils> mockedDataUtils = mockStatic(DataUtils.class)) {
                byte[] mockSource = new byte[]{7, 8, 9};
                mockedDataUtils.when(() -> DataUtils.concatArray(any(byte[].class), any(byte[].class))).thenReturn(mockSource);


                try (MockedStatic<EncodingUtils> mockedEncodingUtils = mockStatic(EncodingUtils.class)) {
                    BackupData mockBackupData = mock(BackupData.class);
                    mockedEncodingUtils.when(() -> EncodingUtils.deserializeData(any(), any())).thenReturn(mockBackupData);


                    backupManager = new BackupManager(fileManager, session);
                }
            }
        }
    }

    @Test
    public void testCreateBackup() {      
        List<Service> services = Arrays.asList(mock(Service.class), mock(Service.class));
       
        byte[] result = backupManager.createBackup(services);
        assertNotNull(result);
    }

    @Test
    public void testRestoreBackup() {
        File backupFileMock = mock(File.class);
       
        char[] password = "password123".toCharArray();
        byte[] salt = new byte[16];
       
        when(backupFileMock.getAbsolutePath()).thenReturn("/fake/path");
        when(fileManager.loadData("/fake/path")).thenReturn(new byte[10]);
       
        assertDoesNotThrow(() -> backupManager.restoreBackup(backupFileMock, accountManager, password, salt));
    }
}
