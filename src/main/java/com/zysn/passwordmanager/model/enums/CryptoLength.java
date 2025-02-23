package com.zysn.passwordmanager.model.enums;

public enum CryptoLength {
    MINIMUM_PASSWORD_LENGTH(16),
    OPTIMAL_PASSWORD_LENGTH(32),
    PASSHPRASE_PASSWORD_LENGTH(64),
    
    ;

    private final int parameter;

    CryptoLength(int parameter) {
        this.parameter = parameter;
    }

    public int getParameter() {
        return this.parameter;
    }
}
