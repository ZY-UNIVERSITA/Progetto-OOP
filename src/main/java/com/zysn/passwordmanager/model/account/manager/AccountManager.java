package com.zysn.passwordmanager.model.account.manager;

import java.io.File;

import com.zysn.passwordmanager.model.security.manager.CryptoManager;

class AccountManager {
    private CryptoManager cryptoManager;
    // private FileManager fileManager;
    // private SessionManager sessionManager;
    // private ServiceManager serviceManager;
    // private BackupManager backupManager;

    // public AccountManager(CriptoManager cryptoManager, FileManager fileManager, SessionManager sessionManager, ServiceManager serviceManager, BackupManager backupManager) {

    // }

    public boolean login(String username, char[] password) {
        return false;
    }

    public boolean logout() {
        return false;
    }

    public void register(String username, char[] password) {

    }

    public boolean changePassword(char[] oldPassword, char[] newPassword) {
        return false;
    }

    public void createBackup() {
    
    }
    
    public void restoreBackup(File backupFile) {

    }
}