package com.zysn.passwordmanager.model.utils.file.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.encoding.EncodingClassForTesting;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;

public class DefaultFileManagerTest {
    private FileManager fileManager;
    private EncodingClassForTesting encodingClassForTesting;
    private byte[] encodedClass;

    @BeforeEach
    void setup() {
        this.fileManager = new DefaultFileManager(true, PathsConstant.USER_PERSONAL, ExtensionsConstant.JSON);

        encodingClassForTesting = new EncodingClassForTesting("Prova", 1);

        encodedClass = EncodingUtils.serializeData(encodingClassForTesting);
    }

    @Test
    void testSaveData() {
        this.fileManager.saveData("prova", encodedClass);

        final File savedFile = Paths
                .get(PathsConstant.USER_PERSONAL.getParameter(), "prova." + ExtensionsConstant.JSON.getParameter())
                .toFile();

        final boolean fileExist = savedFile.exists();
        final long fileIsNotNull = savedFile.length();

        assertTrue(fileExist, "The file doesn't exist.");
        assertTrue(fileIsNotNull > 0, "The file has no content.");

        savedFile.delete();
    }

    @Test
    void testLoadData() {
        this.fileManager.saveData("prova", encodedClass);

        final byte[] loadedData = this.fileManager.loadData("prova");

        final EncodingClassForTesting encodingClassForTesting = EncodingUtils.deserializeData(loadedData,
                new TypeReference<EncodingClassForTesting>() {

                });

        assertNotNull(loadedData, "The file has not been loaded.");

        assertEquals("Prova", encodingClassForTesting.getFirstField(), "The result is not 'Campo 1'");
        assertEquals(1, encodingClassForTesting.getSecondField(), "The result is not 'Campo 1'");

        final File savedFile = Paths
                .get(PathsConstant.USER_PERSONAL.getParameter(), "prova." + ExtensionsConstant.JSON.getParameter())
                .toFile();
        savedFile.delete();
    }
}
