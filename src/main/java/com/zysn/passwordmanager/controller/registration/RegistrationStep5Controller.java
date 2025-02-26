/**
 * Sample Skeleton for 'Step5.fxml' Controller Class
 */

package com.zysn.passwordmanager.controller.registration;

import java.net.URL;
import java.util.ResourceBundle;

import org.bouncycastle.util.Arrays;

import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;
import com.zysn.passwordmanager.controller.scene.api.StepHandler;
import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.security.totp.impl.DefaultTotpAuthentication;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Controller for the fifth step of registration.
 * It extends {@link ControllerAbstract} and implements {@link StepHandler}.
 * 
 * @param <Pane>              the type of the root element managed by this
 *                            controller
 * @param <CollectedUserData> the type of data collected during registration
 */
public class RegistrationStep5Controller extends ControllerAbstract<Pane, CollectedUserData>
        implements StepHandler<Pane, CollectedUserData> {

    private static final int TOTAL_TIME = 28;

    /**
     * ResourceBundle that was given to the FXMLLoader.
     */
    @FXML
    private ResourceBundle resources;

    /**
     * URL location of the FXML file that was given to the FXMLLoader.
     */
    @FXML
    private URL location;

    /**
     * ImageView for displaying the 2FA QR code. Value injected by FXMLLoader.
     */
    @FXML
    private ImageView imageView2FA;

    /**
     * ProgressBar for displaying the time left for the current 2FA code. Value
     * injected by FXMLLoader.
     */
    @FXML
    private ProgressBar timeLeftProgressBar;

    /**
     * Label for displaying the 2FA code. Value injected by FXMLLoader.
     */
    @FXML
    private Label twoFactorAuthenticationCode;

    private DefaultTotpAuthentication totpAuthentication;
    private Timeline timeline;
    private double timeLeft;

    /**
     * Handles the current step of registration.
     * 
     * @return true, as this step always completes successfully
     */
    @Override
    public boolean handleStep() {
        return true;
    }

    /**
     * Initializes the data required for this controller.
     * Sets up the TOTP authentication and loads the 2FA image, updates the 2FA
     * code, and starts the timer.
     */
    public void initializeData() {
        this.totpAuthentication = new DefaultTotpAuthentication(Arrays.copyOf(super.getData().getTotpKey(), super.getData().getTotpKey().length));
        this.load2FAImage();
        this.update2FACode();
        this.setTimer();
    }

    /**
     * This method is called by the FXMLLoader when initialization is complete.
     * Ensures that all FXML fields are correctly injected.
     */
    @FXML
    void initialize() {
        assert imageView2FA != null : "fx:id=\"imageView2FA\" was not injected: check your FXML file 'Step5.fxml'.";
        assert timeLeftProgressBar != null
                : "fx:id=\"timeLeftProgressBar\" was not injected: check your FXML file 'Step5.fxml'.";
        assert twoFactorAuthenticationCode != null
                : "fx:id=\"twoFactorAuthenticationCode\" was not injected: check your FXML file 'Step5.fxml'.";
    }

    /**
     * Loads the 2FA QR code image and sets it to the ImageView.
     */
    private void load2FAImage() {
        final WritableImage image = totpAuthentication.generateQRCodeForJavaFX(super.getData().getUsername(), 1000,
                1000);
        this.imageView2FA.setImage(image);
    }

    /**
     * Updates the 2FA code displayed in the Label.
     */
    private void update2FACode() {
        twoFactorAuthenticationCode.setText(new String(totpAuthentication.generateCode()));
    }

    /**
     * Sets the timer for updating the 2FA code periodically.
     */
    private void setTimer() {
        timeLeft = totpAuthentication.remainingTime();
        this.startTimer(totpAuthentication);
    }

    /**
     * Starts the timer for updating the 2FA code and progress bar.
     *
     * @param totpAuthentication the TOTP authentication instance
     */
    private void startTimer(final DefaultTotpAuthentication totpAuthentication) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> {
            timeLeft--;

            final double progress = timeLeft / TOTAL_TIME;
            timeLeftProgressBar.setProgress(progress);

            if (timeLeft <= 0) {
                update2FACode();
                timeLeft = totpAuthentication.remainingTime();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
