package com.zysn.passwordmanager.model.utils.file.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.zysn.passwordmanager.model.utils.file.api.AbstractFileManager;

/**
 * GenericFileManager is a concrete implementation of {@link AbstractFileManager}.
 * This class provides methods to create paths and open input streams for files.
 */
public class GenericFileManager extends AbstractFileManager {

    /**
     * Default constructor.
     * Initializes the AbstractFileManager with null values.
     */
    public GenericFileManager() {
        super(null, null);
    }

    /**
     * Creates a {@link Path} object from the given file name.
     * 
     * @param fileName the name of the file
     * @return a {@link Path} object representing the file path
     * @throws IllegalArgumentException if the file name is null
     */
    @Override
    public Path createPath(final String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("File path cannot be null.");
        }

        final Path path = Paths.get(fileName);

        return path;
    }

    /**
     * Opens an {@link InputStream} for the given file path.
     * 
     * @param path the path to the file
     * @return an {@link InputStream} for reading from the file
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected InputStream openInputStream(final Path path) throws IOException {
        final InputStream inputStream = new FileInputStream(path.toFile());

        return inputStream;
    }
}
