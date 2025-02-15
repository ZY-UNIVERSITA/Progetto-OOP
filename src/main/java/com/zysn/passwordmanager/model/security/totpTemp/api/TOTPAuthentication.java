package com.zysn.passwordmanager.model.security.totpTemp.api;

import javafx.scene.image.WritableImage;

public interface TOTPAuthentication {

    /**
     * Generates a TOTP code as a character array.
     *
     * @return a character array representing the generated TOTP code
     */
    public char[] generateCode();

    /**
     * Returns the remaining time until the next TOTP time window.
     *
     * @return the remaining time in seconds
     */
    public double remainingTime();

    /**
     * Validates a given TOTP code.
     *
     * @param code a character array representing the TOTP code to validate
     * @return true if the code is valid, otherwise false
     */
    public boolean validateCode(char[] code);

    /**
     * Generates the OTPAuth URL for a given account.
     *
     * @param account the name of the account for which to generate the URL
     * @return a character array representing the generated OTPAuth URL
     */
    public char[] generateOtpAuthURL(String account);

    /**
     * Generates a QR code as a JavaFX WritableImage.
     *
     * @param account the account for which to generate the QR code
     * @param width   the width of the QR code
     * @param height  the height of the QR code
     * @return the generated WritableImage containing the QR code
     */
    public WritableImage generateQRCodeForJavaFX(String account, int width, int height);
}