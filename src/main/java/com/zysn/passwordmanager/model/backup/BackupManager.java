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
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;
import com.zysn.passwordmanager.model.authentication.keystore.impl.DefaultKeyStoreManager;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;
import com.zysn.passwordmanager.model.utils.file.impl.GenericFileManager;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

/**
 * Manages the creation and restoration of backups for services and user sessions.
 */
public class BackupManager {
    private FileManager fileManager;
    private SessionManager session;
    private ServiceManager serviceManager = ServiceManager.getInstance();

    public BackupManager(FileManager fileManager, SessionManager session) {
        this.session = session;
        this.fileManager = fileManager;
    }

    /**
     * Creates an encrypted backup of the provided services.
     * @param services List of services to be saved in the backup.
     * @return Byte array containing the password and salt used for encryption,
     *          or throws an exception if the user is not authenticated.
     * @throws IllegalStateException If the user is not authenticated.
     */
    public byte[] createBackup(List<Service> services) {
        if (this.session.getUserAccount() == null || this.session.getUserAccount().getUsername() == null) {
            throw new IllegalStateException("Must be authenticated user to create backup.");
        }
        byte[] salt = CryptoUtils.generateSalt(16);
        char[] password = serviceManager.generatePassword(32, true, true, true, true);
    
        byte[] passwordBytes = EncodingUtils.charToByteConverter(password);

        byte[] source = DataUtils.concatArray(passwordBytes, salt);
    
        AlgorithmConfig config = AlgorithmConfigFactory.createAlgorithmConfig("AES", salt, null);

        BackupData backupData = new BackupData((DefaultSessionManager)session, services);

        byte[] serializedBackup = EncodingUtils.serializeData(backupData);
    
        byte[] encryptedBackup = serviceManager.getCryptoManager().encrypt(
            serializedBackup,
            new SecretKeySpec(passwordBytes, config.getAlgorithmName()),
            config
        );
    
        this.fileManager.saveData(session.getUserAccount().getUsername(), encryptedBackup);

        return source;
    }

     /**
     * Restores an encrypted backup.
     * @param backupFile Backup file.
     * @param accountManager Account manager instance.
     * @param password Password used for decryption.
     * @param salt Salt used for decryption.
     * @throws IllegalStateException If the user is not authenticated.
     */
    public void restoreBackup(File backupFile, AccountManager accountManager, char[] password, byte[] salt) {
        if (this.session.getUserAccount() == null || this.session.getUserAccount().getUsername() == null) {
            throw new IllegalStateException("Must be authenticated user to restore backup.");
        }
        FileManager fileManager = new GenericFileManager();
        byte[] encryptedBackup = fileManager.loadData(backupFile.getAbsolutePath());

        AlgorithmConfig config = AlgorithmConfigFactory.createAlgorithmConfig("AES", salt, null);
    
        byte[] passwordBytes = EncodingUtils.charToByteConverter(password);
    
        byte[] decryptedBackup = serviceManager.getCryptoManager().decrypt(
            encryptedBackup,
            new SecretKeySpec(passwordBytes, config.getAlgorithmName()),
            config
        );
    
        BackupData restoredData = EncodingUtils.deserializeData(decryptedBackup, new TypeReference<BackupData>() {});

        this.setSession(restoredData);
        this.setServices(restoredData);
        this.setKeyStore();
        this.saveUserData();
    }

    private void setSession(BackupData restoredData) {
        SessionManager sessionManager = restoredData.getSessionManager();

        session.setKeyStoreConfig(sessionManager.getKeyStoreConfig());
        session.setServiceConfig(sessionManager.getServiceConfig());
        session.setUserAccount(sessionManager.getUserAccount());
        session.setUserAuthInfo(sessionManager.getUserAuthInfo());
        session.setUserAuthKey(sessionManager.getUserAuthKey());
    }

    private void setServices(BackupData restoredData) {
        List<Service> services = restoredData.getServices();

        serviceManager.getServices().clear();
        serviceManager.getServices().addAll(services);
        
        serviceManager.setUserAccount(session.getUserAccount());
        serviceManager.setAlgorithmConfig(AlgorithmConfigFactory.createAlgorithmConfig("AES", session.getServiceConfig().getSaltForServiceEncryption(), null));
        serviceManager.setFileName(String.valueOf(session.getServiceConfig().getFileName()));
    }

    private void setKeyStore() {
        KeyStoreManager keyStoreManager = new DefaultKeyStoreManager(session);
        keyStoreManager.createNewKeyStore();
        keyStoreManager.populateNewKeyStore();
        keyStoreManager.saveKeyStore();
    }

    private void saveUserData() {
        FileManager userFileManager = new DefaultFileManager(PathsConstant.USER_PERSONAL, ExtensionsConstant.JSON);
        byte[] userData = EncodingUtils.serializeData(session.getUserAuthInfo());
        userFileManager.saveData(session.getUserAccount().getUsername(), userData);

        serviceManager.saveServices();
    } 
}
