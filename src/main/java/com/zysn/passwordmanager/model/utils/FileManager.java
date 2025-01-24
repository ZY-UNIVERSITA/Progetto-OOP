package com.zysn.passwordmanager.model.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zysn.passwordmanager.model.account.entity.UserAccount;

public class FileManager {
    private static final String PATH = ".users";

    public FileManager() {

    }

    public UserAccount loadUserData(String username) {   
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null.");
        }
    
        String userDir = System.getProperty("user.dir");

        Path userPath = Paths.get(userDir, PATH, username + ".json");
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
        try {
            return objectMapper.readValue(userPath.toFile(), UserAccount.class);
        } catch (IOException e) {
            System.err.println("Error trying to read the file: " + e.getMessage());
            return null;
        }
    }

    public boolean saveUserData(UserAccount userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException("User account cannot be null.");
        }

        String userDir = System.getProperty("user.dir");

        Path userPath = Paths.get(userDir, PATH, userAccount.getUsername() + ".json");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {   
            // Create directory if it doesnt' exist
            Files.createDirectories(userPath.getParent());

            objectMapper.writeValue(userPath.toFile(), userAccount);
            return true;
        } catch (IOException e) {
            System.err.println("Error trying to write the file: " + e.getMessage());
            return false;
        }
    }

    public byte[] loadServicesFile(char[] fileName) {
        return null;
    }

    public boolean saveServicesFile(char[] fileName) {
        return false;
    }
}
