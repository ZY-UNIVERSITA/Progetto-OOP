package com.zysn.passwordmanager.model.enums;

public enum ExtensionsConstant {
    JSON(".json"),
    ENC(".enc"),

    ;

    private final String parameter;

    ExtensionsConstant(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return this.parameter;
    }
}
