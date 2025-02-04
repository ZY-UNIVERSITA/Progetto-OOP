package com.zysn.passwordmanager.model.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base32;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bastiaanjansen.otp.TOTPGenerator;
import com.google.zxing.WriterException;

public class TOTPAuthenticationTest {
    private TOTPAuthentication totpAuthentication;

    @BeforeEach
    void setup() {

    }

    @Test
    void testCreateTOTPAuthenticationWithoutSeed() {
        byte[] seed = new byte[] { 79, 23, 100, -88, -95, 0, 110, 84, 39, -20, 53, -13, 3, -72, 12, -55, 45, 27, -75,
                -20, 11, 1, -1, 19, 71, 91, -54, -12, 110, -50, 38, -64 };

        this.totpAuthentication = new TOTPAuthentication(seed);

        assertNotNull(this.totpAuthentication);
        assertEquals("HmacSHA256", this.totpAuthentication.getTotpGenerator().getAlgorithm().getHMACName(),
                "The 2 algorithm are not the same.");
        assertEquals(8, this.totpAuthentication.getTotpGenerator().getPasswordLength(),
                "The 2 algorithm doesn't generate the same key length.");
        assertEquals(30, this.totpAuthentication.getTotpGenerator().getPeriod().getSeconds(),
                "The 2 algorithm doesn't have the same period length.");
    }

    @Test
    void testCreateTOTPAuthenticationWithSeed() {
        this.totpAuthentication = new TOTPAuthentication();

        assertNotNull(this.totpAuthentication);
        assertEquals("HmacSHA256", this.totpAuthentication.getTotpGenerator().getAlgorithm().getHMACName(),
                "The 2 algorithm are not the same.");
        assertEquals(8, this.totpAuthentication.getTotpGenerator().getPasswordLength(),
                "The 2 algorithm doesn't generate the same key length.");
        assertEquals(30, this.totpAuthentication.getTotpGenerator().getPeriod().getSeconds(),
                "The 2 algorithm doesn't have the same period length.");
    }

    @Test
    void testGenerateCode() {
        this.totpAuthentication = new TOTPAuthentication();

        assertNotNull(this.totpAuthentication.generateCode());
        assertInstanceOf(String.class, this.totpAuthentication.generateCode());
    }

    @Test
    void testValidateCode() {
        byte[] seed = new byte[] { 79, 23, 100, -88, -95, 0, 110, 84, 39, -20, 53, -13, 3, -72, 12, -55, 45, 27, -75,
            -20, 11, 1, -1, 19, 71, 91, -54, -12, 110, -50, 38, -64 };

        this.totpAuthentication = new TOTPAuthentication(seed);

        TOTPAuthentication totpAuthenticationTest = new TOTPAuthentication(seed);
        String codeTest = totpAuthenticationTest.generateCode();

        assertTrue(this.totpAuthentication.validateCode(codeTest), "The code is not equal.");
    }
}
