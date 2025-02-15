package com.zysn.passwordmanager.model.utils.enumerations;

public enum AlgorithmName {
    AES("AES"),

    ARGON2("Argon2"),
    BCRYPT("Bcrypt"),
    SCRYPT("Scrypt"),

    ARGON2I("argon2i"),
    ARGON2D("argon2d"),
    ARGON2ID("argon2id"),

    ;

    private final String algorithmName;

    AlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmName() {
        return this.algorithmName;
    }

    public static AlgorithmName returnEnumfromString(String text) {
        for (AlgorithmName algorithm : AlgorithmName.values()) {
            if (algorithm.algorithmName.equalsIgnoreCase(text)) {
                return algorithm;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + text);
    }
}
