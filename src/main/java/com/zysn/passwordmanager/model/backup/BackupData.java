package com.zysn.passwordmanager.model.backup;

import java.util.List;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.service.Service;

/**
 * Util class for managing backup data.
 */
public class BackupData {
    private DefaultSessionManager sessionManager;
    private List<Service> services;

    public BackupData(DefaultSessionManager sessionManager, List<Service> services) {
        this.sessionManager = sessionManager;
        this.services = services;
    }

    public BackupData() {

    }

    public SessionManager getSessionManager() {
        return this.sessionManager;
    }

    public void setSessionManager(DefaultSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
