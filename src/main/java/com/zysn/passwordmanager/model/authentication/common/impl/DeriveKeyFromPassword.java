package com.zysn.passwordmanager.model.authentication.common.impl;

import org.bouncycastle.util.Arrays;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;

/**
 * This class handles the process of deriving a key from a password for authentication purposes.
 * It extends the {@code AuthenticationStepAbstract} class and uses a {@code CryptoManager} for cryptographic operations.
 */
public class DeriveKeyFromPassword extends AuthenticationStepAbstract {
    
    private final CryptoManager cryptoManager;
    private AlgorithmConfig algorithmConfig;
    private byte[] password;

    /**
     * Constructor that initializes the {@code DeriveKeyFromPassword} instance.
     * 
     * @param sessionManager The session manager to use.
     * @param cryptoManager The cryptographic manager to use for key derivation.
     */
    public DeriveKeyFromPassword(final SessionManager sessionManager, final CryptoManager cryptoManager) {
        super(sessionManager);
        this.cryptoManager = cryptoManager;
    }

    /**
     * Executes the key derivation step, deriving the key from the password and setting it.
     */
    @Override
    public void executeStep() {
        final byte[] key = this.deriveKeyFromPassword();
        this.setPasswordDerivedKey(key);
    }

    /**
     * Derives the key from the password using the specified algorithm configuration and key lengths.
     * 
     * @return The derived key as a byte array.
     */
    private byte[] deriveKeyFromPassword() {
        // get key legth
        this.password = super.getSessionManager().getUserAuthKey().getPassword();
        this.algorithmConfig = super.getSessionManager().getUserAuthInfo().getPasswordDerivedKeyConfig();

        final int passwordDerivedKeyLength = Integer.valueOf(algorithmConfig.getParameterValueByName("key_size"));
        final int encryptionKeyLength = Integer.valueOf(super.getSessionManager().getUserAuthInfo().getKeyStoreEncryptionConfig().getParameterValueByName("key_size"));

        // derive key
        if (encryptionKeyLength == passwordDerivedKeyLength) {
            return this.deriveKeyFromPasswordWithSameLength();
        } else if (passwordDerivedKeyLength > encryptionKeyLength) {
            return this.deriveKeyFromPasswordWithLongerLength(encryptionKeyLength);
        } else {
            return this.deriveKeyFromPasswordWithShorterLength(encryptionKeyLength);
        }
    }

    /**
     * Derives the key from the password with the same length as the encryption key.
     * 
     * @return The derived key as a byte array.
     */
    private byte[] deriveKeyFromPasswordWithSameLength() {
        final byte[] derivedKey = this.cryptoManager.deriveMasterKey(password, algorithmConfig);
        return derivedKey;
    }

    /**
     * Derives the key from the password with a length longer than the encryption key length.
     * 
     * @param encryptionKeyLength The length of the encryption key.
     * @return The derived key as a byte array.
     */
    private byte[] deriveKeyFromPasswordWithLongerLength(final int encryptionKeyLength) {
        final byte[] derivedKey = deriveKeyFromPasswordWithSameLength();
        return Arrays.copyOf(derivedKey, encryptionKeyLength / 8);
    }

    /**
     * Derives the key from the password with a length shorter than the encryption key length.
     * 
     * @param encryptionKeyLength The length of the encryption key.
     * @return The derived key as a byte array.
     */
    private byte[] deriveKeyFromPasswordWithShorterLength(final int encryptionKeyLength) {
        // deerive normale key
        final byte[] salt = Arrays.copyOf(this.algorithmConfig.getSalt(), this.algorithmConfig.getSalt().length);
        final int saltFirstPartLength = salt.length / 2;

        this.algorithmConfig.setSalt(Arrays.copyOf(salt, saltFirstPartLength));
        byte[] derivedKey = deriveKeyFromPasswordWithSameLength();
        this.algorithmConfig.setSalt(salt);

        // deriver higher lengt key
        final int saltSecondPartLength = salt.length - saltFirstPartLength;
        final AlgorithmConfig algorithmConfigHKDF = AlgorithmConfigFactory.createAlgorithmConfig("HKDF", Arrays.copyOfRange(salt, saltFirstPartLength, saltSecondPartLength), null);

        derivedKey = this.cryptoManager.deriveMasterKey(derivedKey, algorithmConfigHKDF);
        return Arrays.copyOf(derivedKey, encryptionKeyLength / 8);
    }

    /**
     * Sets the derived key obtained from the password.
     * 
     * @param key The derived key as a byte array.
     */
    private void setPasswordDerivedKey(final byte[] key) {
        super.getSessionManager().getUserAuthKey().setPasswordDerivedKey(key);
    }    
}
