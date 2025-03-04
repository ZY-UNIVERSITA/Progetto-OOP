package com.zysn.passwordmanager.model.backup;

import com.zysn.passwordmanager.model.service.Service;
import com.zysn.passwordmanager.model.account.entity.impl.UserAccount;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.service.ServiceManager;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BackupManagerTest {

    private BackupManager backupManager;

    @Mock
    private FileManager fileManager;

    @Mock
    private ServiceManager serviceManager;

    @Mock
    private AccountManager accountManager;

    private UserAccount userAccount;
    private List<Service> services;

    @BeforeEach
    public void setUp() {
        fileManager = mock(FileManager.class);
        serviceManager = mock(ServiceManager.class);
        accountManager = mock(AccountManager.class);
        Service serviceMock = mock(Service.class);

        AlgorithmConfig algorithmConfig = mock(AlgorithmConfig.class);
        
        when(serviceMock.getEncryptionConfig()).thenReturn(algorithmConfig);
        when(serviceManager.getServices()).thenReturn(Arrays.asList(serviceMock));

        userAccount = new UserAccount();
        userAccount.setMasterKey(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });

        services = Arrays.asList(serviceMock);

        backupManager = new BackupManager(fileManager, null);
    }

    @Test
    public void testCreateBackup() {
        
    }

    @Test
    public void testRestoreBackup() {
        
    }
}
