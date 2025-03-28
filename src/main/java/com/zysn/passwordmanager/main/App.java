package com.zysn.passwordmanager.main;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javafx.application.Application;

/** Main application entry-point's class. */

public final class App {
    private App() {
    }

    /**
     * Main application entry-point.
     * 
     * @param args
     */
    public static void main(final String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        Application.launch(JavaFXApp.class, args);
    }
}