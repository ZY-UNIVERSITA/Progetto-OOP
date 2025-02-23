package com.zysn.passwordmanager.model.authentication.common.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;

public class SaveUserDataTest {
    private AuthenticationStep authenticationStep;
    
    private SessionManager sessionManager;

    @BeforeEach
    void setup() {
        this.sessionManager = new DefaultSessionManager();
        this.sessionManager.getUserAccount().setUsername("prova");
        this.sessionManager.getUserAuthInfo().setUsername("prova");
        this.sessionManager.getUserAuthInfo().setEnabled2FA(true);
        this.sessionManager.getUserAuthInfo().setKeyStoreConfigEncryptedData(new byte[] { 1, 2, 3, 4, 5 });

        this.authenticationStep = new SaveUserData(sessionManager);
    }

    @Test
    void testExecuteStep() {
        this.authenticationStep.executeStep();
    }
}
