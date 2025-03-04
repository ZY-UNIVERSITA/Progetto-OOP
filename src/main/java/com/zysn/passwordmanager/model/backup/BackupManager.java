package com.zysn.passwordmanager.model.backup;

import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import java.io.File;

import com.zysn.passwordmanager.model.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zysn.passwordmanager.model.utils.data.DataUtils;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

/**
 * BackupManager is responsible for creating and restoring backups.
 */
public class BackupManager {
    private FileManager fileManager;
    private SessionManager session;
    private ServiceManager serviceManager = ServiceManager.getInstance();

    public BackupManager(FileManager fileManager, SessionManager session) {
        this.session = session;
        this.fileManager = fileManager;
    }

    public void createBackup(List<Service> services) {
        byte[] salt = CryptoUtils.generateSalt(16);
        char[] password = serviceManager.generatePassword(32, true, true, true, true);
    
        byte[] passwordBytes = EncodingUtils.charToByteConverter(password);
        byte[] source = DataUtils.concatArray(passwordBytes, salt);
    
        AlgorithmConfig config = services.get(0).getEncryptionConfig();
    
        byte[] derivedKey = serviceManager.getCryptoManager().deriveMasterKey(source, config);
    
        Object[] backupData = {session, services};
        byte[] serializedBackup = EncodingUtils.serializeData(backupData);
    
        byte[] encryptedBackup = serviceManager.getCryptoManager().encrypt(
            serializedBackup,
            new SecretKeySpec(derivedKey, "AES"),
            config
        );
    
        this.fileManager.saveData("backupFile.enc", encryptedBackup);
    }

    public void restoreBackup(File backupFile, AccountManager accountManager, char[] password, byte[] salt) {
        byte[] encryptedBackup = fileManager.loadData(backupFile.getName());

        AlgorithmConfig config = serviceManager.getServices().get(0).getEncryptionConfig();
    
        byte[] passwordBytes = EncodingUtils.charToByteConverter(password);
        byte[] passwordSalt = DataUtils.concatArray(passwordBytes, salt);
        byte[] derivedKey = serviceManager.getCryptoManager().deriveMasterKey(passwordSalt, config);
    
        byte[] decryptedBackup = serviceManager.getCryptoManager().decrypt(
            encryptedBackup,
            new SecretKeySpec(derivedKey, "AES"),
            config
        );
    
        Object[] restoredData = EncodingUtils.deserializeData(decryptedBackup, new TypeReference<Object[]>() {});

        List<Service> services = (List<Service>) restoredData[1];
        serviceManager.getServices().clear();
        serviceManager.getServices().addAll(services);
    }   
    
}
