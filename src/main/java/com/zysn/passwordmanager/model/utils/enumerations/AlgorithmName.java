package com.zysn.passwordmanager.model.utils.enumerations;

public enum AlgorithmName {
    AES("AES"),

    ARGON2I("argon2i"),
    ARGON2D("argon2d"),
    ARGON2ID("argon2id"),

    ;

    private final String algorithName;

    AlgorithmName(String algorithName) {
        this.algorithName = algorithName;
    }

    public String getAlgorithName() {
        return this.algorithName;
    }
}
