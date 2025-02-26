package com.zysn.passwordmanager.model.authentication.common.impl;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.service.ServiceManager;

public class LoadUserPassword extends AuthenticationStepAbstract {
    private boolean firstAccess;
    
    private ServiceManager serviceManager;

    public LoadUserPassword(SessionManager sessionManager, boolean firstAccess) {
        super(sessionManager);

        this.firstAccess = firstAccess;

        this.serviceManager = ServiceManager.getInstance();
    }

    @Override
    public void executeStep() {
        this.prepareForServiceLoading();
        
        if (firstAccess) {
            this.serviceManager.saveServices();
        } else {
            this.serviceManager.loadServices();
        }
    }

    private void prepareForServiceLoading() {
        serviceManager.setFileName(String.valueOf(this.getSessionManager().getServiceConfig().getFileName()));

        AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", this.getSessionManager().getServiceConfig().getSaltForServiceEncryption(), null);
        serviceManager.setAlgorithmConfig(algorithmConfig);
        
        serviceManager.setUserAccount(this.getSessionManager().getUserAccount());
    }
    
}
