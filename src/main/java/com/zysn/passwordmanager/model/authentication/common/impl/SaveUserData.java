package com.zysn.passwordmanager.model.authentication.common.impl;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;

/**
 * This class handles the saving of user data as part of the authentication
 * process.
 * It extends the {@code AuthenticationStepAbstract} class and uses a
 * {@code FileManager} to manage the saving process.
 */
public class SaveUserData extends AuthenticationStepAbstract {

    private final FileManager fileManager;

    /**
     * Constructor that initializes the {@code SaveUserData} instance.
     * 
     * @param sessionManager The session manager to use.
     */
    public SaveUserData(final SessionManager sessionManager) {
        super(sessionManager);
        this.fileManager = new DefaultFileManager(PathsConstant.USER_PERSONAL, ExtensionsConstant.JSON);
    }

    /**
     * Executes the step necessary to save the user data.
     */
    @Override
    public void executeStep() {
        final byte[] serializedData = EncodingUtils.serializeData(this.getSessionManager().getUserAuthInfo());
        this.fileManager.saveData(this.getSessionManager().getUserAccount().getUsername(), serializedData);
    }
}
