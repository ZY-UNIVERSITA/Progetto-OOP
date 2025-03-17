package com.zysn.passwordmanager.model.utils.security.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PasswordGeneratorTest {

    private PasswordGenerator generator;

    @BeforeEach
    void setup() {
        generator = new PasswordGenerator();
    }

    @Test
    void testGeneratePassword() {
        assertThrows(IllegalArgumentException.class, () -> {generator.generatePassword(12, false, false, false, false);});

        assertThrows(IllegalArgumentException.class, () -> {generator.generatePassword(9, true, true, true, true);});

        char[] password = generator.generatePassword(16, true, true, true, true);
        assertNotNull(password);
        assertEquals(16, password.length);
    }
}
