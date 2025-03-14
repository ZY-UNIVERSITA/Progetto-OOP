package com.zysn.passwordmanager.model.utils.file.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.file.api.AbstractFileManager;

/**
 * ResourcesFileManager is an implementation of AbstractFileManager which is responsible for
 * managing file paths and streams for resources within the classpath.
 */
public class ResourcesFileManager extends AbstractFileManager {

    /**
     * Constructs a new ResourcesFileManager object with the specified main folder and file extension.
     *
     * @param mainFolder the main folder constant
     * @param extension the file extension constant
     */
    public ResourcesFileManager(final PathsConstant mainFolder, final ExtensionsConstant extension) {
        super(mainFolder, extension);
    }

    /**
     * Creates a file path by concatenating the main folder and file name with the extension.
     *
     * @param fileName the name of the file
     * @return the constructed file path
     */
    @Override
    public Path createPath(final String fileName) {
        return Paths.get(mainFolder.getParameter(), fileName.concat(extension.getParameter()));
    }

    /**
     * Throws an UnsupportedOperationException as saving data is not supported for resources.
     *
     * @param fileName the name of the file
     * @param data the data to be saved
     * @throws UnsupportedOperationException always thrown as this operation is not supported
     */
    @Override
    public void saveData(final String fileName, final byte[] data) {
        throw new UnsupportedOperationException("Saving data is not supported for resources.");
    }

    /**
     * Opens an InputStream for the specified file path from the classpath resources.
     *
     * @param path the file path
     * @return the input stream for the file
     * @throws IOException if the resource is not found
     */
    @Override
    protected InputStream openInputStream(final Path path) throws IOException {

        final InputStream inputStream = ClassLoader.getSystemResourceAsStream(path.toString().replace('\\', '/'));

        if (inputStream == null) {
            throw new IOException("Resource not found: " + path);
        }
        return inputStream;
    }
}
