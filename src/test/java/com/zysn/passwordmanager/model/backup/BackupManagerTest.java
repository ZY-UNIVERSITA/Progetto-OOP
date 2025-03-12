package com.zysn.passwordmanager.model.backup;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import com.zysn.passwordmanager.model.service.Service;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultAccountManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class BackupManagerTest {

    private BackupManager backupManager;
    private SessionManager sessionManager;
    private FileManager fileManager;

    @BeforeEach
    public void setUp() {
        sessionManager = Mockito.mock(DefaultSessionManager.class);
        fileManager = Mockito.mock(FileManager.class);
        backupManager = new BackupManager(fileManager, sessionManager);
    }

    @Test
    public void testCreateBackupThrowsIllegalStateException() {
        List<Service> services = new ArrayList<>();
        when(sessionManager.getUserAccount()).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> {
            backupManager.createBackup(services);
        });
    }

    @Test
    public void testRestoreBackupThrowsIllegalStateException() {
        File backupFile = Mockito.mock(File.class);
        DefaultAccountManager accountManager = Mockito.mock(DefaultAccountManager.class);
        char[] password = "password".toCharArray();
        byte[] salt = new byte[16];

        when(sessionManager.getUserAccount()).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> {
            backupManager.restoreBackup(backupFile, accountManager, password, salt);
        });
    }
}