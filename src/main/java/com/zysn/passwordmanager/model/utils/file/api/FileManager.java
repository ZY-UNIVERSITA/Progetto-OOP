package com.zysn.passwordmanager.model.utils.file.api;

/**
 * The FileManager interface provides methods to load and save data.
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
}

