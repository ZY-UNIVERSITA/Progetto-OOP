package com.zysn.passwordmanager.model.enums;

public enum ShaHashLength {
    SHA1_BIT(160),
    SHA1_BYTE(20),

    SHA224_BIT(224),
    SHA224_BYTE(28),

    SHA256_BIT(256),
    SHA256_BYTE(32),

    SHA384_BIT(384),
    SHA384_BYTE(48),

    SHA512_BIT(512),
    SHA512_BYTE(64),
    
    ;

    private final int parameter;

    ShaHashLength(int parameter) {
        this.parameter = parameter;
    }

    public int getParameter() {
        return this.parameter;
    }
}
