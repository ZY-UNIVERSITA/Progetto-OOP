package com.zysn.passwordmanager.model.authentication.common.impl;

import java.util.HashMap;
import java.util.Map;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;

/**
 * This class handles the derivation of the service configuration key as part of the authentication process.
 * It extends the {@code AuthenticationStepAbstract} class and uses a {@code CryptoManager} for cryptographic operations.
 */
public class DeriveServiceConfigKey extends AuthenticationStepAbstract {

    private final CryptoManager cryptoManager;

    /**
     * Constructor that initializes the {@code DeriveServiceConfigKey} instance.
     * 
     * @param sessionManager The session manager to use.
     * @param cryptoManager The cryptographic manager to use for key derivation.
     */
    public DeriveServiceConfigKey(final SessionManager sessionManager, final CryptoManager cryptoManager) {
        super(sessionManager);
        this.cryptoManager = cryptoManager;
    }

    /**
     * Executes the step necessary to derive the service configuration key.
     */
    @Override
    public void executeStep() {
        final byte[] masterKey = this.deriveKey();
        this.setMasterKey(masterKey);
    }

    /**
     * Derives the service configuration key using the password-derived key and salt for HKDF (HMAC-based Key Derivation Function).
     * 
     * @return The derived key as a byte array.
     */
    private byte[] deriveKey() {
        final byte[] key = super.getSessionManager().getUserAuthKey().getPasswordDerivedKey();
        final byte[] salt = super.getSessionManager().getKeyStoreConfig().getSaltForHKDF();
        final AlgorithmConfig algorithmConfig = this.createAlgorithmConfig(salt);
        final byte[] derivedKey = this.cryptoManager.deriveMasterKey(key, algorithmConfig);
        return derivedKey;
    }

    /**
     * Creates the algorithm configuration for HKDF using the specified salt.
     * 
     * @param salt The salt to use for the HKDF.
     * @return The algorithm configuration.
     */
    private AlgorithmConfig createAlgorithmConfig(final byte[] salt) {
        final Map<String, String> params = new HashMap<>();
        params.put("info", "Service decryption key");
        final AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("HKDF", salt, params);
        return algorithmConfig;
    }

    /**
     * Sets the derived master key in the user authentication key.
     * 
     * @param masterKey The derived master key as a byte array.
     */
    private void setMasterKey(final byte[] masterKey) {
        super.getSessionManager().getUserAuthKey().setServiceConfigKey(masterKey);
    }
}

