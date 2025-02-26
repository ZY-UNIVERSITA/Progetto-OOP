package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.authentication.passwordlist.api.ServiceCryptoConfigManager;
import com.zysn.passwordmanager.model.authentication.passwordlist.impl.DefaultServiceCryptoConfigManager;

public class CreateServiceConfigTest {
    private AuthenticationStep authenticationStep;

    private SessionManager sessionManager;
    private ServiceCryptoConfigManager serviceCryptoConfigManager;

    @BeforeEach
    void setup() {
        this.sessionManager = new DefaultSessionManager();

        this.serviceCryptoConfigManager = new DefaultServiceCryptoConfigManager(sessionManager);

        this.authenticationStep = new CreateServiceConfig(sessionManager, serviceCryptoConfigManager);
    }

    @Test
    void testExecuteStep() {
        this.authenticationStep.executeStep();

        assertNotNull(this.sessionManager.getServiceConfig(), "The service config is null.");
        assertNotNull(this.sessionManager.getServiceConfig().getFileName(), "The file name is null.");
        assertNotNull(this.sessionManager.getServiceConfig().getSaltForHKDF(), "The salt is null.");
        assertNotNull(this.sessionManager.getServiceConfig().getSaltForServiceEncryption(), "The salt is null.");
    }
}
