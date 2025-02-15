package com.zysn.passwordmanager.model.utils.enumerations;

public enum AlgorithmTypeEnum {
    KEY_DERIVATION_ALGORITHM("Key Derivation Algorithm"),
    ENCRYPTION_ALGORITHM("Encryption Algorithm"),
    
    ;

    private final String parameter;

    AlgorithmTypeEnum(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return this.parameter;
    }
}