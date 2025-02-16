package com.zysn.passwordmanager.model.utils.enumerations;

public enum AesEnum {
    AES_ALGORITHM("variant"),
    GCM_IV_LENGTH("12"),
    GCM_TAG_LENGTH("128"),
    
    ;

    private final String parameter;

    AesEnum(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return this.parameter;
    }
}
