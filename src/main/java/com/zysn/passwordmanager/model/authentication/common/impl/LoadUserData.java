package com.zysn.passwordmanager.model.authentication.common.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zysn.passwordmanager.model.account.entity.impl.UserAuthInfo;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;

/**
 * The LoadUserData class is an extension of the AuthenticationStepAbstract
 * class.
 * It provides functionality to load user data during the authentication
 * process.
 */
public class LoadUserData extends AuthenticationStepAbstract {
    private final FileManager fileManager;

    /**
     * Constructs a LoadUserData object with the specified session manager.
     * Initializes the FileManager with default settings.
     * 
     * @param sessionManager the SessionManager to manage user sessions
     */
    public LoadUserData(final SessionManager sessionManager) {
        super(sessionManager);

        this.fileManager = new DefaultFileManager(PathsConstant.USER_PERSONAL, ExtensionsConstant.JSON);
    }

    /**
     * Executes the step of loading user data.
     * Retrieves the serialized user data from the FileManager,
     * deserializes it into a UserAuthInfo object, and sets it in the
     * SessionManager.
     */
    @Override
    public void executeStep() {
        final byte[] serializedData = this.fileManager
                .loadData(super.getSessionManager().getUserAccount().getUsername());

        final UserAuthInfo userAuthInfo = EncodingUtils.deserializeData(serializedData,
                new TypeReference<UserAuthInfo>() {

                });

        super.getSessionManager().setUserAuthInfo(userAuthInfo);
    }
}
