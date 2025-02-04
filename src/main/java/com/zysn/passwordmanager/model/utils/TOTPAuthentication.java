package com.zysn.passwordmanager.model.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base32;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.TOTPGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class TOTPAuthentication {
    private static final int SHA256 = 32;
    private static final String ISSUER = "PassKeeper";

    private byte[] seed;
    private byte[] encodedSeed;
    private TOTPGenerator totpGenerator;

    public TOTPAuthentication(byte[] seed) {
        this.seed = seed;
        this.encodedSeed = this.encodeToBase32Seed(seed);
        this.totpGenerator = this.createTOTPGenerator();
    }

    public TOTPAuthentication() {
        this(CryptoUtils.generateSalt(TOTPAuthentication.SHA256));
    }

    private byte[] encodeToBase32Seed(byte[] seed) {
        Base32 base32 = new Base32();
        return base32.encode(this.seed);
    }

    private String seedToString() {
        return Arrays.toString(this.encodedSeed);
    }

    private TOTPGenerator createTOTPGenerator() {
        if (seed.length != 32) {
            throw new IllegalArgumentException("The length of the seed must be 32 byte (256 bits).");
        }

        // Gli authenticator solitamente supportano solo SHA1
        TOTPGenerator TOTPGenerator = new TOTPGenerator.Builder(this.encodedSeed)
                .withHOTPGenerator(builder -> {
                    builder.withPasswordLength(8);
                    builder.withAlgorithm(HMACAlgorithm.SHA256);

                })
                .withPeriod(Duration.ofSeconds(30))
                .build();

        return TOTPGenerator;
    }

    public String generateCode() {
        String code = this.totpGenerator.now();

        return code;
    }

    public boolean validateCode(String code) {
        return this.totpGenerator.verify(code);
    }

    //
    public void cleanEncodedSeed() {
        CryptoUtils.cleanMemory(encodedSeed);
    }

    public byte[] getSeed() {
        return seed;
    }

    public byte[] getEncodedSeed() {
        return encodedSeed;
    }

    public TOTPGenerator getTotpGenerator() {
        return totpGenerator;
    }

    
}
