package com.zysn.passwordmanager.model.authentication.login.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;

public class GeneralLoginHandlerTest {
    private CollectedUserData collectedUserData;

    private GeneralLoginHandler loginStepHandler;

    private FileManager fileManager;

    @BeforeEach
    void setup() {
        this.collectedUserData = new CollectedUserData();

        this.loginStepHandler = new GeneralLoginHandler(collectedUserData);

        this.fileManager = new DefaultFileManager(PathsConstant.USER_PERSONAL, ExtensionsConstant.JSON);
    }

    @Test
    void testHandleStepSuccess() {
        this.collectedUserData.setUsername("prova");
        this.collectedUserData.setPassword("prova".getBytes());

        this.fileManager.saveData("prova", new byte[] { 1, 2, 3 });

        assertDoesNotThrow(() -> {
            this.loginStepHandler.handleStep();
        }, "The method has thrown an Illegal Argument Exception. ");
    }

    @Test
    void testHandleStepFailing() {
        this.collectedUserData.setUsername("");
        this.collectedUserData.setPassword(null);

        this.fileManager.saveData("prova", new byte[] { 1, 2, 3 });

        assertThrows(IllegalArgumentException.class, () -> {
            this.loginStepHandler.handleStep();
        }, "The method has not thrown an Illegal Argument Exception.");
    }

    @AfterEach
    void cleanup() {
        this.fileManager.deleteData("prova");
    }
}
