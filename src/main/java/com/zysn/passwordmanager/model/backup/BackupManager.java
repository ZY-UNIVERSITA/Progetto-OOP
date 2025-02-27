package com.zysn.passwordmanager.model.backup;

import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import java.io.File;

import com.zysn.passwordmanager.model.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zysn.passwordmanager.model.account.entity.impl.UserAccount;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

/**
 * BackupManager is responsible for creating and restoring backups.
 */
public class BackupManager {
    private FileManager fileManager;
    private ServiceManager serviceManager = ServiceManager.getInstance();

    public BackupManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Creates a backup of the user account and the list of services.
     * The data is encrypted before being saved to the backup file.
     * 
     * @param userAccount The user account to include in the backup.
     * @param services The list of services to include in the backup.
     */
    public void createBackup(UserAccount userAccount, List<Service> services) {
        Object[] backupData = {userAccount, services};
        byte[] serializedBackup = EncodingUtils.serializeData(backupData);

        AlgorithmConfig config = serviceManager.getServices().get(0).getEncryptionConfig();

        byte[] encryptedBackup = serviceManager.getCryptoManager().encrypt(
            serializedBackup,
            new SecretKeySpec(userAccount.getMasterKey(), config.getAlgorithmName()),
            config
        );

        this.fileManager.saveData("backupFile.enc", encryptedBackup);
    }
    
    /**
     * Restores a backup from an encrypted backup file.
     * 
     * @param backupFile The backup file to restore.
     * @param accountManager The AccountManager used to restore the user account data.
     */
    public void restoreBackup(File backupFile, AccountManager accountManager) {
        byte[] encryptedBackup = fileManager.loadData(backupFile.getName());

        AlgorithmConfig config = serviceManager.getServices().get(0).getEncryptionConfig();

        UserAccount user = accountManager.getSessionManager().getUserAccount();

        byte[] decryptedBackup = serviceManager.getCryptoManager().decrypt(
            encryptedBackup,
            new SecretKeySpec(user.getMasterKey(), config.getAlgorithmName()),
            config
        );

        Object[] restoredData = EncodingUtils.deserializeData(decryptedBackup, new TypeReference<Object[]>() {});

        //UserAccount restoredUserAccount = (UserAccount) restoredData[0];
        List<Service> restoredServices = (List<Service>) restoredData[1];

        serviceManager.getServices().clear();
        serviceManager.getServices().addAll(restoredServices);
    }
}
