package com.zysn.passwordmanager.model.enums;

public enum AlgorithmName {
    HKDF("HKDF"),
    
    AES("AES"),

    ARGON2("Argon2"),
    BCRYPT("Bcrypt"),
    SCRYPT("Scrypt"),

    ARGON2I("argon2i"),
    ARGON2D("argon2d"),
    ARGON2ID("argon2id"),

    ;

    public static AlgorithmName returnEnumfromString(final String text) {
        for (final AlgorithmName algorithm : AlgorithmName.values()) {
            if (algorithm.algorithmName.equalsIgnoreCase(text)) {
                return algorithm;
            }
        }
        throw new IllegalArgumentException("Error: No enum constant for value: " + text);
    }

    private final String algorithmName;

    AlgorithmName(final String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmName() {
        return this.algorithmName;
    }
}
