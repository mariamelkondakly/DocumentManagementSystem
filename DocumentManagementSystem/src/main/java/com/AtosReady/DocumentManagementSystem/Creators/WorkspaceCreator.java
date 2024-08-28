package com.AtosReady.DocumentManagementSystem.Creators;

import com.AtosReady.DocumentManagementSystem.DTO.WorkspacesDTO;
import com.AtosReady.DocumentManagementSystem.Exceptions.DirectoryCreationException;
import com.AtosReady.DocumentManagementSystem.Exceptions.DirectoryExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

@Component
public class WorkspaceCreator {

    @Autowired
    private CommonMethods commonMethods;



    public ResponseEntity<HashMap<String,Object>> createWorkspaceDirectory(String userId, WorkspacesDTO workspace, boolean present) {
        File dir = Paths.get(commonMethods.baseFolderPath, userId, workspace.getName()).toFile();

        commonMethods.permanentlyDeleteDirectory(present,dir);

        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                HashMap<String,Object>responseMap= new HashMap<>();
                responseMap.put("Directory created: " , dir.getAbsolutePath());
                responseMap.put("Workspace created: ",workspace.toString());
                return ResponseEntity.ok( responseMap);
            } else {
                throw new DirectoryCreationException("Directory could not be created"+dir.getAbsolutePath());
            }
        } else {
            throw new DirectoryExistsException("Directory already exists: " + dir.getAbsolutePath());
        }
    }
}
