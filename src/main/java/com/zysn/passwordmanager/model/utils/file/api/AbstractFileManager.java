package com.zysn.passwordmanager.model.utils.file.api;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;

/**
 * Abstract class that implements the FileManager interface.
 * Manages loading and saving data operations using specific paths and
 * extensions.
 */
public abstract class AbstractFileManager implements FileManager {
    protected final PathsConstant mainFolder;
    protected final ExtensionsConstant extension;

    /**
     * Constructor for the AbstractFileManager class.
     *
     * @param mainFolder Main folder path.
     * @param extension  File extension.
     */
    public AbstractFileManager(final PathsConstant mainFolder, final ExtensionsConstant extension) {
        this.mainFolder = mainFolder;
        this.extension = extension;
    }

    /**
     * Template method for loading data.
     * Uses the abstract method openInputStream to get the correct stream.
     *
     * @param fileName Name of the file to load.
     * @return Byte array containing the file data.
     * @throws IllegalArgumentException if the file name is null.
     * @throws IOException              if an error occurs while reading the file.
     */
    @Override
    public byte[] loadData(final String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("The file name cannot be null.");
        }

        final Path filePath = createPath(fileName);

        try (InputStream inputStream = openInputStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("Input stream is null.");
            }
            return inputStream.readAllBytes();
        } catch (final IOException e) {
            System.err.println("Error trying to read the file: ");
        }
        return null;
    }

    /**
     * Method for saving data to a file.
     *
     * @param fileName Name of the file to save the data to.
     * @param data     Byte array containing the data to save.
     * @throws IllegalArgumentException if the file name is null.
     * @throws IOException              if an error occurs while creating
     *                                  directories or saving the file.
     */
    @Override
    public void saveData(final String fileName, final byte[] data) {
        if (fileName == null) {
            throw new IllegalArgumentException("The file name cannot be null.");
        }

        final Path filePath = createPath(fileName);

        try {
            // Create the directory if it does not exist
            Files.createDirectories(filePath.getParent());
        } catch (final IOException e) {
            System.err.println("Error trying to create directories.");
        }

        try (OutputStream outputStream = new FileOutputStream(filePath.toFile())) {
            outputStream.write(data);
        } catch (final IOException e) {
            System.err.println("Error trying to save the file");
        }
    }

    /**
     * Deletes data from the specified file.
     *
     * @param fileName the name of the file to delete data from
     * @throws RuntimeException if the file cannot be deleted
     */
    @Override
    public void deleteData(final String fileName) {
        final Path filePath = this.createPath(fileName);

        if (filePath.toFile().exists()) {
            final boolean deleted = filePath.toFile().delete();
            if (!deleted) {
                throw new RuntimeException("Failed to delete the file: " + fileName);
            }
        }
    }

    @Override
    public abstract Path createPath(String fileName);

    /**
     * Abstract method to obtain an InputStream given a Path.
     *
     * @param path Path of the file.
     * @return InputStream of the file.
     * @throws IOException if an error occurs while opening the file.
     */
    protected abstract InputStream openInputStream(Path path) throws IOException;
}
