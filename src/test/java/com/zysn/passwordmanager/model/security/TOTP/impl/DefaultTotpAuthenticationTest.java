package com.zysn.passwordmanager.model.security.totp.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bastiaanjansen.otp.TOTPGenerator;

import javafx.scene.image.WritableImage;

public class DefaultTotpAuthenticationTest {
    private DefaultTotpAuthentication totpAuthentication;

    @BeforeEach
    void setup() {

    }

    @Test
    void testSeedLength() {
        byte[] seed = new byte[] { 1, 2, 3 };

        assertThrows(IllegalArgumentException.class, () -> {
            new DefaultTotpAuthentication(seed);
        }, "The method doesn't throw an exception if the seed length is not 32 bytes");
    }

    @Test
    void testCreateTOTPGeneratorWithSeed() {
        byte[] seed = new byte[] { 79, 23, 100, -88, -95, 0, 110, 84, 39, -20, 53, -13, 3, -72, 12, -55, 45, 27, -75,
                -20, 11, 1, -1, 19, 71, 91, -54, -12, 110, -50, 38, -64 };

        this.totpAuthentication = new DefaultTotpAuthentication(seed);

        assertEquals("HmacSHA256", this.totpAuthentication.getTotpGenerator().getAlgorithm().getHMACName(),
                "The 2 algorithm are not the same.");
        assertEquals(8, this.totpAuthentication.getTotpGenerator().getPasswordLength(),
                "The 2 algorithm doesn't generate the same key length.");
        assertEquals(30, this.totpAuthentication.getTotpGenerator().getPeriod().getSeconds(),
                "The 2 algorithm doesn't have the same period length.");
    }

    @Test
    void testCreateTOTPGeneratorWithoutSeed() {
        this.totpAuthentication = new DefaultTotpAuthentication();

        TOTPGenerator TOTPGenerator = this.totpAuthentication.getTotpGenerator();

        assertNotNull(TOTPGenerator, "The generated TOTP class is null.");
    }

    @Test
    void testGenerateCode() {
        this.totpAuthentication = new DefaultTotpAuthentication();

        char[] generatedCode = this.totpAuthentication.generateCode();

        assertNotNull(generatedCode, "The generated code is null.");
        assertEquals(8, generatedCode.length, "The generated code length is not 8.");
    }

    @Test
    void testValidateCode() {
        byte[] seed = new byte[] { 79, 23, 100, -88, -95, 0, 110, 84, 39, -20, 53, -13, 3, -72, 12, -55, 45, 27, -75,
                -20, 11, 1, -1, 19, 71, 91, -54, -12, 110, -50, 38, -64 };

        this.totpAuthentication = new DefaultTotpAuthentication(seed);

        DefaultTotpAuthentication totpAuthenticationTest = new DefaultTotpAuthentication(seed);
        char[] codeTest = totpAuthenticationTest.generateCode();

        assertTrue(this.totpAuthentication.validateCode(codeTest), "The code is not equal.");
    }

    @Test
    void testGenerateOtpAuthURL() {
        byte[] seed = new byte[] { 79, 23, 100, -88, -95, 0, 110, 84, 39, -20, 53, -13, 3, -72, 12, -55, 45, 27, -75,
                -20, 11, 1, -1, 19, 71, 91, -54, -12, 110, -50, 38, -64 };

        this.totpAuthentication = new DefaultTotpAuthentication(seed);

        String account = "username";

        char[] OTPAuthURL = this.totpAuthentication.generateOtpAuthURL(account);

        char[] expectedOutput = "otpauth://totp/PassKeeper:username?secret=J4LWJKFBABXFIJ7MGXZQHOAMZEWRXNPMBMA76E2HLPFPI3WOE3AA====&issuer=PassKeeper&algorithm=SHA256&digits=8&period=30".toCharArray();

        assertNotNull(OTPAuthURL, "The link has not been generated.");

        assertArrayEquals(expectedOutput, OTPAuthURL, "The generated code is not equal.");
    }

    @Test
    void testGenerateQRCodeForJavaFX() {
        byte[] seed = new byte[] { 79, 23, 100, -88, -95, 0, 110, 84, 39, -20, 53, -13, 3, -72, 12, -55, 45, 27, -75,
                -20, 11, 1, -1, 19, 71, 91, -54, -12, 110, -50, 38, -64 };

        this.totpAuthentication = new DefaultTotpAuthentication(seed);

        String account = "username";
        int width = 300;
        int height = 300;

        WritableImage image = this.totpAuthentication.generateQRCodeForJavaFX(account, width, height);

        assertNotNull(image, "The image for Java FX has not been generated.");
        assertEquals((double) width, image.getWidth(), "The width of the image is not " + width);
        assertEquals((double) height, image.getHeight(), "The height of the image is not " + height);
    }

    @Test
    void testCleanClass() {
        byte[] seed = new byte[] { 79, 23, 100, -88, -95, 0, 110, 84, 39, -20, 53, -13, 3, -72, 12, -55, 45, 27, -75,
            -20, 11, 1, -1, 19, 71, 91, -54, -12, 110, -50, 38, -64 };

        this.totpAuthentication = new DefaultTotpAuthentication(seed);

        byte[] zeroArrayBytes = new byte[32];
        Arrays.fill(zeroArrayBytes, (byte) 0);

        assertNotNull(this.totpAuthentication.getEncodedSeed());

        this.totpAuthentication.destroy();

        assertArrayEquals(zeroArrayBytes, seed,
                "The encoded array has not been cleaned.");
        
        assertNull(this.totpAuthentication.getSeed());
        assertNull(this.totpAuthentication.getEncodedSeed());
        assertNull(this.totpAuthentication.getTotpGenerator());
    }
}
