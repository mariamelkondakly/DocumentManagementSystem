package com.AtosReady.DocumentManagementSystem.Creators;

import com.AtosReady.DocumentManagementSystem.Exceptions.*;
import com.AtosReady.DocumentManagementSystem.Models.Directories;
import com.AtosReady.DocumentManagementSystem.Models.Workspaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

@Component
public class DirectoryCreator {

    @Autowired
    private CommonMethods commonMethods;

    public String rootPathSetter(Directories dir, Workspaces workspace) {
        return workspace.getUserId() + "\\" + workspace.getName() + "\\" + dir.getName();
    }

    public String pathSetter(Directories dir, Directories parent) {
        return parent.getPath() + "\\" + dir.getName();
    }


    public ResponseEntity<HashMap<String, Object>> createDirectory(String Path, boolean present) {
        File dir = Paths.get(commonMethods.baseFolderPath, Path).toFile();

        commonMethods.permanentlyDeleteDirectory(present, dir);

        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                HashMap<String, Object> responseMap = new HashMap<>();
                responseMap.put("Directory created: ", dir.getAbsolutePath());
                responseMap.put("Find it in path: ", commonMethods.baseFolderPath + "\\" + Path);
                return ResponseEntity.ok(responseMap);
            } else {
                throw new DirectoryCreationException("Directory could not be created" + dir.getAbsolutePath());
            }
        } else {
            throw new DirectoryExistsException("Directory already exists: " + dir.getAbsolutePath());
        }
    }

    public void renameDirectory(String oldPath, String newPath) {
        File sourceDir = Paths.get(commonMethods.baseFolderPath, oldPath).toFile();
        File targetDir = Paths.get(commonMethods.baseFolderPath, newPath).toFile();

        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new ResourceNotFoundException("Source directory does not exist: " + sourceDir.getAbsolutePath());
        }
        if (targetDir.exists()) {
            throw new ResourceExistsException("Target directory already exists: " + targetDir.getAbsolutePath());
        }

        boolean moved = sourceDir.renameTo(targetDir);
        if (!moved) {
            throw new DirectoryMoveException("Failed to rename directory from " + sourceDir.getAbsolutePath() + " to " + targetDir.getAbsolutePath());
        }

        // Log the successful rename
        System.out.println("Directory renamed successfully from " + sourceDir.getAbsolutePath() + " to " + targetDir.getAbsolutePath());
    }


    public void moveDirectory(String oldPath, String newPath, boolean present) {
        File sourceDir = Paths.get(commonMethods.baseFolderPath, oldPath).toFile();
        File targetDir = Paths.get(commonMethods.baseFolderPath, newPath).toFile();

        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new ResourceNotFoundException("Source directory does not exist: " + sourceDir.getAbsolutePath());
        }
        if (targetDir.exists() && present) {
            throw new ResourceExistsException("Target directory already exists: " + targetDir.getAbsolutePath());
        }

        commonMethods.permanentlyDeleteDirectory(present, targetDir);

        boolean moved = sourceDir.renameTo(targetDir);
        if (!moved) {
            throw new DirectoryMoveException("Failed to move directory from " + sourceDir.getAbsolutePath() + " to " + targetDir.getAbsolutePath());
        }
        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("Source directory moved: ", sourceDir.getAbsolutePath());
        responseMap.put("New directory location: ", targetDir.getAbsolutePath());

        ResponseEntity.ok(responseMap);
    }

    public void hideDirectory(String path) throws IOException {
        File directory = new File(path);
        if (directory.exists()) {
            // Recursively hide all contents
            commonMethods.hideRecursively(directory);
        }
    }

    public void deletePermanently(boolean present, String path) {
        File dir = Paths.get(commonMethods.baseFolderPath, path).toFile();
        commonMethods.permanentlyDeleteDirectory(present, dir);
    }

}
