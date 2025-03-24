package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.entity.impl.UserAuthInfo;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;

public class LoadUserDataTest {
    private AuthenticationStep authentication;

    private SessionManager sessionManager;
    private FileManager fileManager;

    @BeforeEach
    void setup() {
        this.sessionManager = new DefaultSessionManager();
        this.sessionManager.getUserAccount().setUsername("prova");

        this.authentication = new LoadUserData(sessionManager);

        this.fileManager = new DefaultFileManager(PathsConstant.USER_PERSONAL, ExtensionsConstant.JSON);
    }

    @Test
    void testExecuteStep() {
        final UserAuthInfo userAuthInfo = new UserAuthInfo("prova");

        final byte[] serializedData = EncodingUtils.serializeData(userAuthInfo);

        this.fileManager.saveData("prova", serializedData);
        
        this.authentication.executeStep();

        assertNotNull(this.sessionManager.getUserAuthInfo(), "The user data has not been loaded");
        assertEquals("prova", this.sessionManager.getUserAuthInfo().getUsername(), "The loaded username is not the expected one.");

        this.fileManager.deleteData("prova");
    }
}
