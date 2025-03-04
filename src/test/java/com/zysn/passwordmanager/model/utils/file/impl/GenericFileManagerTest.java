package com.zysn.passwordmanager.model.utils.file.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class GenericFileManagerTest {
    private GenericFileManager genericFileManager;

    @BeforeEach
    void setup() {
        this.genericFileManager = new GenericFileManager();
    }

    @Test
    void testCreatePath() {
        String fileName = "testFile.txt";
        Path expectedPath = Paths.get(fileName);
        Path actualPath = genericFileManager.createPath(fileName);

        assertEquals(expectedPath, actualPath, "The created path should match the expected path.");
    }

    @Test
    void testOpenInputStream(@TempDir Path tempDir) throws IOException {
        Path tempFile = Files.createTempFile(tempDir, "prova", ".txt");

        String content = "Hello, world!";
        Files.writeString(tempFile, content);

        try (InputStream inputStream = genericFileManager.openInputStream(tempFile)) {
            byte[] buffer = new byte[content.length()];
            int bytesRead = inputStream.read(buffer);

            assertEquals(content.length(), bytesRead, "The number of bytes read should match the content length.");
            assertEquals(content, new String(buffer), "The content read from the file should match the written content.");
        }
    }
}
