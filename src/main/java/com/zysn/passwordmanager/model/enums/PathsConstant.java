package com.zysn.passwordmanager.model.enums;

public enum PathsConstant {
    USER_ROOT("user.dir"),
    KEY_STORE(".keystore"),
    SERVICE(".services"),
    USER_PERSONAL(".users"),

    ;

    private final String parameter;

    PathsConstant(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return this.parameter;
    }
}
