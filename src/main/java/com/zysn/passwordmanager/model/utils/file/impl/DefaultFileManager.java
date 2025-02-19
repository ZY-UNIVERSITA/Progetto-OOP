package com.zysn.passwordmanager.model.utils.file.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.file.api.AbstractFileManager;

/**
 * DefaultFileManager is an implementation of AbstractFileManager which is responsible for
 * managing file paths and streams based on user-defined settings.
 */
public class DefaultFileManager extends AbstractFileManager {
    private final String userDir;

    /**
     * Constructs a new DefaultFileManager object with the specified main folder and file extension.
     * It also initializes the user directory path from the system properties.
     *
     * @param mainFolder the main folder constant
     * @param extension the file extension constant
     */
    public DefaultFileManager(final PathsConstant mainFolder, final ExtensionsConstant extension) {
        super(mainFolder, extension);

        this.userDir = System.getProperty(PathsConstant.USER_ROOT.getParameter());
    }

    /**
     * Creates a file path by concatenating the user directory, main folder, and file name with the extension.
     *
     * @param fileName the name of the file
     * @return the constructed file path
     */
    @Override
    public Path createPath(final String fileName) {
        final Path path = Paths.get(userDir, mainFolder.getParameter(), fileName.concat(extension.getParameter()));

        return path;
    }

    /**
     * Opens an InputStream for the specified file path.
     *
     * @param path the file path
     * @return the input stream for the file
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected InputStream openInputStream(final Path path) throws IOException {
        return new FileInputStream(path.toFile());
    }
}

