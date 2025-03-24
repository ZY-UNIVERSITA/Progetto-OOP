package com.zysn.passwordmanager.model.utils.file.api;

import java.nio.file.Path;

/**
 * The FileManager interface provides methods to load, save, and delete data.
 * It also includes a method to create a file path.
 */
public interface FileManager {

    /**
     * Loads data from the specified file.
     * 
     * @param fileName the name of the file to load data from
     * @return a byte array containing the data loaded from the file
     */
    public byte[] loadData(String fileName);

    /**
     * Saves data to the specified file.
     * 
     * @param fileName the name of the file to save data to
     * @param data     a byte array containing the data to be saved
     */
    public void saveData(String fileName, byte[] data);

    /**
     * Deletes data from the specified file.
     * 
     * @param fileName the name of the file to delete data from
     */
    public void deleteData(String fileName);

    /**
     * Creates a path for the specified file.
     * 
     * @param fileName the name of the file to create a path for
     * @return the Path object representing the file path
     */
    public Path createPath(String fileName);
}