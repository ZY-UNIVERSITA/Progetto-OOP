package com.zysn.passwordmanager.model.utils.file.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.enums.AlgorithmType;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;

public class ResourcesFileManagerTest {
    private ResourcesFileManager resourcesFileManager;

    @BeforeEach
    void setup() {
        this.resourcesFileManager = new ResourcesFileManager(PathsConstant.CONFIGURATIONS, ExtensionsConstant.JSON);
    }

    @Test
    void testCreatePath() {
        Path expectedPath = Paths.get(PathsConstant.CONFIGURATIONS.getParameter(), AlgorithmType.ENCRYPTION_ALGORITHM.getParameter().concat(ExtensionsConstant.JSON.getParameter()));
    
        Path actualePath = this.resourcesFileManager.createPath(AlgorithmType.ENCRYPTION_ALGORITHM.getParameter());

        assertEquals(expectedPath, actualePath, "The created path is not the expected path.");
    }

    @Test
    void testOpenInputStream() throws IOException {
        try (InputStream inputStream = this.resourcesFileManager.openInputStream((this.resourcesFileManager.createPath(AlgorithmType.ENCRYPTION_ALGORITHM.getParameter())))) {
            assertNotNull(inputStream, "The file has not been loaded.");
        } 

    }

    @Test
    void testSaveData() {

    }
}
