package com.zysn.passwordmanager.model.backup;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import com.zysn.passwordmanager.model.service.Service;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;

public class BackupManagerTest {

    private BackupManager backupManager;
    private FileManager fileManager;
    private SessionManager session;
    private AccountManager accountManager;

    @BeforeEach
    public void setUp() {
        fileManager = mock(FileManager.class);
        session = mock(SessionManager.class);
        accountManager = mock(AccountManager.class);

        backupManager = new BackupManager(fileManager, session);
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
