package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;

public class InsertUserDataTest {
    private AuthenticationStep authenticationStep;
    private SessionManager sessionManager;
    private CollectedUserData collectedUserData;

    @BeforeEach
    void setup() {
        this.sessionManager = new DefaultSessionManager();
        this.collectedUserData = new CollectedUserData();

        this.collectedUserData.setUsername("prova");
        this.collectedUserData.setPassword("prova".getBytes());
        this.collectedUserData.setEnabled2FA(true);

        this.authenticationStep = new InsertUserData(sessionManager, collectedUserData);
    }

    @Test
    void testExecuteStep() {
        this.authenticationStep.executeStep();

        assertNotNull(this.sessionManager.getUserAccount().getUsername());
        assertNotNull(this.sessionManager.getUserAuthKey().getPassword());
        assertNotNull(this.sessionManager.getUserAuthInfo().isEnabled2FA());
    }
}
