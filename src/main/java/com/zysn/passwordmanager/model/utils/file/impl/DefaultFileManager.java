package com.zysn.passwordmanager.model.utils.file.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;

/**
* The DefaultFileManager class implements the FileManager interface.
* It provides methods to load and save data from and to files,
* using specific paths and file extensions.
*/
public class DefaultFileManager implements FileManager {
   private final boolean rootFolder;
   private final PathsConstant mainFolder;
   private final ExtensionsConstant extension;

   private ObjectMapper objectMapper;

   /**
    * Constructs a DefaultFileManager with specified root folder, main folder, and
    * file extension.
    * 
    * @param rootFolder whether to use the root folder
    * @param mainFolder the main folder path constant
    * @param extension  the file extension constant
    */
   public DefaultFileManager(final boolean rootFolder, final PathsConstant mainFolder,
           final ExtensionsConstant extension) {
       this.rootFolder = rootFolder;
       this.mainFolder = mainFolder;
       this.extension = extension;

       this.configureObjectMapper();
   }

   /**
    * Loads data from the specified file.
    * 
    * @param fileName the name of the file to load data from
    * @return a byte array containing the data loaded from the file
    * @throws IllegalArgumentException if the file name is null
    */
   public byte[] loadData(final String fileName) {
       if (fileName == null) {
           throw new IllegalArgumentException("The file name cannot be null.");
       }

       final Path filePath = this.createPath(fileName);

       byte[] readedData = null;

       try {
           readedData = objectMapper.readValue(filePath.toFile(), byte[].class);
       } catch (final IOException e) {
           System.err.println("Error trying to read the file.");
       }

       return readedData;
   }

   /**
    * Saves data to the specified file.
    * 
    * @param fileName the name of the file to save data to
    * @param data     a byte array containing the data to be saved
    * @throws IllegalArgumentException if the file name is null
    */
   public void saveData(final String fileName, final byte[] data) {
       if (fileName == null) {
           throw new IllegalArgumentException("The file name cannot be null.");
       }

       final Path filePath = this.createPath(fileName);

       try {
           // Create directory if it doesn't exist
           Files.createDirectories(filePath.getParent());
           objectMapper.writeValue(filePath.toFile(), data);
       } catch (final IOException e) {
           System.err.println("Error trying to save the file.");
       }
   }

   /**
    * Configures the ObjectMapper with custom settings.
    */
   private void configureObjectMapper() {
       this.objectMapper = new ObjectMapper();
       objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
       objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
   }

   /**
    * Creates a Path object based on the file name, root folder, main folder, and
    * file extension.
    * 
    * @param fileName the name of the file to create a path for
    * @return the created Path object
    */
   private Path createPath(final String fileName) {
       Path path = null;

       if (rootFolder) {
           final String userDir = System.getProperty(PathsConstant.USER_ROOT.getParameter());
           path = Paths.get(userDir, mainFolder.getParameter(), fileName + "." + extension.getParameter());
       } else {
           path = Paths.get(mainFolder.getParameter(), fileName + "." + extension.getParameter());
       }

       return path;
   }
}

