package com.zysn.passwordmanager.model.enums;

public enum AlgorithmType {
    KEY_DERIVATION_ALGORITHM("Key Derivation Algorithm"),
    ENCRYPTION_ALGORITHM("Encryption Algorithm"),
    
    ;

    private final String parameter;

    AlgorithmType(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return this.parameter;
    }
}