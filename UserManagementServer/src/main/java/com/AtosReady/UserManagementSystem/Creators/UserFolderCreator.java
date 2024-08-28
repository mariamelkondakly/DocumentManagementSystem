package com.AtosReady.UserManagementSystem.Creators;

import com.AtosReady.UserManagementSystem.DTO.UserDTO;
import com.AtosReady.UserManagementSystem.Exceptions.DirectoryCreationException;
import com.AtosReady.UserManagementSystem.Exceptions.DirectoryExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

@Component
public class UserFolderCreator {

    @Value("${app.base-folder}")
    private String baseFolderPath;

    public ResponseEntity<HashMap<String,Object>> createUserDirectory(String userId, UserDTO user) {
        File dir = Paths.get(baseFolderPath, userId).toFile();
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                HashMap<String,Object>responseMap= new HashMap<>();
                responseMap.put("Directory created: " , dir.getAbsolutePath());
                responseMap.put("User created: ",user.toString());
                return ResponseEntity.ok( responseMap);
            } else {
                throw new DirectoryCreationException("Directory could not be created"+dir.getAbsolutePath());
            }
        } else {
            throw new DirectoryExistsException("Directory already exists: " + dir.getAbsolutePath());
        }
    }
}
