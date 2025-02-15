package com.zysn.passwordmanager.model.security.totpTemp.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.TOTPGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zysn.passwordmanager.model.enums.ShaHashLength;
import com.zysn.passwordmanager.model.security.totpTemp.api.TOTPAuthentication;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.crypto.api.MustBeDestroyed;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * This class provides an implementation for TOTP (Time-Based One-Time Password)
 * authentication.
 * It uses a provided seed to generate a TOTPGenerator instance for generating
 * one-time passwords.
 */
public class DefaultTotpAuthentication implements TOTPAuthentication, MustBeDestroyed {
    private static final String ISSUER = "ZYSN Password Manager";

    private byte[] seed;
    private byte[] encodedSeed;
    private TOTPGenerator totpGenerator;

    /**
     * Constructs a TOTPAuthentication object with the specified seed.
     *
     * @param seed the seed to use for TOTP generation.
     */
    public DefaultTotpAuthentication(final byte[] seed) {
        this.seed = this.verifySeedLength(seed);
        this.encodedSeed = EncodingUtils.byteToBase32Byte(seed);
        this.totpGenerator = this.createTOTPGenerator();
    }

    /**
     * Constructs a TOTPAuthentication object with a randomly generated seed.
     */
    public DefaultTotpAuthentication() {
        this(CryptoUtils.generateSalt(ShaHashLength.SHA256_BYTE.getParameter()));
    }

    /**
     * Generates a TOTP code as a character array.
     *
     * @return a character array representing the generated TOTP code
     */
    @Override
    public char[] generateCode() {
        return this.totpGenerator.now().toCharArray();
    }

    /**
     * Returns the remaining time until the next TOTP time window.
     *
     * @return the remaining time in seconds
     */
    @Override
    public double remainingTime() {
        return this.totpGenerator.durationUntilNextTimeWindow().toSeconds();
    }

    /**
     * Validates a given TOTP code.
     *
     * @param code a character array representing the TOTP code to validate
     * @return true if the code is valid, otherwise false
     */
    @Override
    public boolean validateCode(final char[] code) {
        return this.totpGenerator.verify(String.valueOf(code));
    }

    /**
     * Generates the OTPAuth URL for a given account.
     *
     * @param account the name of the account for which to generate the URL
     * @return a character array representing the generated OTPAuth URL
     */
    @Override
    public char[] generateOtpAuthURL(final String account) {
        char[] URL = null;

        try {
            final String encodedIssuer = URLEncoder.encode(ISSUER, "UTF-8").replace("+", "%20");
            final String encodedAccount = URLEncoder.encode(account, "UTF-8").replace("+", "%20");

            URL = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA256&digits=8&period=30",
                    encodedIssuer, encodedAccount, new String(EncodingUtils.byteToCharConverter(encodedSeed)),
                    encodedIssuer).toCharArray();
        } catch (final UnsupportedEncodingException e) {
            System.err.println(e.getMessage());
        }

        return URL;
    }

    /**
     * Generates a QR code as a JavaFX WritableImage.
     *
     * @param account the account for which to generate the QR code
     * @param width   the width of the QR code
     * @param height  the height of the QR code
     * @return the generated WritableImage containing the QR code
     */
    @Override
    public WritableImage generateQRCodeForJavaFX(final String account, final int width, final int height) {
        final BitMatrix bitMatrix = this.generateQrMatrix(account, width, height);

        final WritableImage image = new WritableImage(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Trasforma i bit 0 e 1 in neri e bianchi
                image.getPixelWriter().setColor(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return image;
    }

    /**
     * Cleans the class, erasing sensitive data.
     */
    @Override
    public void destroy() {
        CryptoUtils.cleanMemory(this.getSeed());
        this.seed = null;

        CryptoUtils.cleanMemory(this.getEncodedSeed());
        this.encodedSeed = null;

        this.totpGenerator = null;
    }

    /* GETTER and SETTER */
    public byte[] getSeed() {
        return seed;
    }

    public void setSeed(final byte[] seed) {
        this.seed = seed;
    }

    public byte[] getEncodedSeed() {
        return encodedSeed;
    }

    public void setEncodedSeed(final byte[] encodedSeed) {
        this.encodedSeed = encodedSeed;
    }

    public TOTPGenerator getTotpGenerator() {
        return totpGenerator;
    }

    public void setTotpGenerator(final TOTPGenerator totpGenerator) {
        this.totpGenerator = totpGenerator;
    }

    /**
     * Verifies that the seed length is 32 bytes (256 bits).
     *
     * @param seed the seed to verify.
     * @return the verified seed.
     * @throws IllegalArgumentException if the seed length is not 32 bytes.
     */
    private byte[] verifySeedLength(final byte[] seed) {
        if (seed.length != 32) {
            throw new IllegalArgumentException("The length of the seed must be 32 byte (256 bits).");
        }

        return seed;
    }

    /**
     * Creates a TOTPGenerator instance using the encoded seed.
     *
     * @return a TOTPGenerator instance.
     */
    private TOTPGenerator createTOTPGenerator() {
        final TOTPGenerator TOTPGenerator = new TOTPGenerator.Builder(this.encodedSeed)
                .withHOTPGenerator(builder -> {
                    builder.withPasswordLength(8);
                    builder.withAlgorithm(HMACAlgorithm.SHA256);

                })
                .withPeriod(Duration.ofSeconds(30))
                .build();

        return TOTPGenerator;
    }

    /**
     * Generates a QR code matrix for the given account.
     *
     * @param account the account for which to generate the QR code
     * @param width   the width of the QR code
     * @param height  the height of the QR code
     * @return the generated BitMatrix
     */
    private BitMatrix generateQrMatrix(final String account, final int width, final int height) {
        final QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // Crea una matrice di bit
        BitMatrix bitMatrix = null;

        try {
            // Popola la matrice di bit con 0 e 1 che rappresentano in pratica i quadrati
            // bianchi e neri
            bitMatrix = qrCodeWriter.encode(String.valueOf(this.generateOtpAuthURL(account)), BarcodeFormat.QR_CODE,
                    width,
                    height);
        } catch (final WriterException e) {
            System.err.println("The method generated an error.");
        }

        return bitMatrix;
    }

}
