package com.zysn.passwordmanager.model.enums;

public enum AesAlgorithm {
    AES_MODE("mode"),
    AES_PADDING("padding"),
    GCM_IV_LENGTH("12"),
    GCM_TAG_LENGTH("128"),
    
    ;

    private final String parameter;

    AesAlgorithm(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return this.parameter;
    }
}
