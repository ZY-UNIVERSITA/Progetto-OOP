package com.zysn.passwordmanager.model.authentication.common.impl;

import java.util.HashMap;
import java.util.Map;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.utils.data.DataUtils;

/**
 * This class handles the derivation of the master key as part of the
 * authentication process.
 * It extends the {@code AuthenticationStepAbstract} class and uses a
 * {@code CryptoManager} for cryptographic operations.
 */
public class DeriveMasterKey extends AuthenticationStepAbstract {

    private final CryptoManager cryptoManager;

    /**
     * Constructor that initializes the {@code DeriveMasterKey} instance.
     * 
     * @param sessionManager The session manager to use.
     * @param cryptoManager  The cryptographic manager to use for key derivation.
     */
    public DeriveMasterKey(final SessionManager sessionManager, final CryptoManager cryptoManager) {
        super(sessionManager);
        this.cryptoManager = cryptoManager;
    }

    /**
     * Executes the step necessary to derive the master key.
     */
    @Override
    public void executeStep() {
        final byte[] fullConcatenatedKey = retrieveFullConcatenatedKey();
        final AlgorithmConfig hkdfAlgorithmConfig = buildHkdfAlgorithmConfig();
        final byte[] masterKey = this.cryptoManager.deriveMasterKey(fullConcatenatedKey, hkdfAlgorithmConfig);
        super.getSessionManager().getUserAccount().setMasterKey(masterKey);
    }

    /**
     * Retrieves the full concatenated key by combining the password-derived key,
     * key store encryption key, and TOTP encryption key.
     * 
     * @return The full concatenated key as a byte array.
     */
    private byte[] retrieveFullConcatenatedKey() {
        final byte[] passwordDerivedKey = super.getSessionManager().getUserAuthKey().getPasswordDerivedKey();
        final byte[] keyStoreEncryptionKey = super.getSessionManager().getKeyStoreConfig().getKeyStoreEncryptionKey();
        final byte[] firstConcatenation = DataUtils.concatArray(passwordDerivedKey, keyStoreEncryptionKey);
        final byte[] totpEncryptionKey = super.getSessionManager().getUserAuthKey().getTotpEncryptionKey();
        return DataUtils.concatArray(firstConcatenation, totpEncryptionKey);
    }

    /**
     * Builds the HKDF algorithm configuration using the specified salt.
     * 
     * @return The HKDF algorithm configuration.
     */
    private AlgorithmConfig buildHkdfAlgorithmConfig() {
        final byte[] salt = super.getSessionManager().getServiceConfig().getSaltForHKDF();
        final Map<String, String> params = new HashMap<>();
        params.put("info", "Service decryption key");
        return AlgorithmConfigFactory.createAlgorithmConfig("HKDF", salt, params);
    }
}
