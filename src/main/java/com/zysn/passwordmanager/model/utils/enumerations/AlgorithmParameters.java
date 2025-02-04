package com.zysn.passwordmanager.model.utils.enumerations;

public enum AlgorithmParameters {
    // ARGON2 
    VARIANT("variant"),
    ITERATIONS("iterations"),
    MEMORY_COST("memory_cost"),
    PARALLELISM("parallelism"),
    KEY_SIZE("key_size"),
    
    // BCRYTP
    COST("cost"),

    // SCRYPT
    COST_FACTOR("cost_factor"),
    BLOCK_SIZE("block_size"),
    
    ;

    private final String parameter;

    AlgorithmParameters(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return this.parameter;
    }
}
