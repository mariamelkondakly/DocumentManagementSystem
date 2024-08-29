package com.AtosReady.DocumentManagementSystem.Creators;

import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class DocumentCreator {

    @Autowired
    CommonMethods commonMethods;

    public ResponseEntity<String> CreateNewFile(MultipartFile file, String path, boolean willReplace) throws IOException {
        File dir = Paths.get(commonMethods.baseFolderPath, path).toFile();

        if (dir.exists()) {
            File destinationFile = Paths.get(commonMethods.baseFolderPath, path, file.getOriginalFilename()).toFile();
            if (willReplace) {
                commonMethods.permanentlyDeleteDirectory(true, destinationFile);
            }
            file.transferTo(destinationFile);
        } else {
            throw new ResourceNotFoundException("The folder destination for the new file was not found, exception was" +
                    " raised in the CreateNewFile method of the directoryCreator class");
        }
        return ResponseEntity.ok("File uploaded, find it in: " + commonMethods.baseFolderPath + path + "\\" + file.getOriginalFilename());
    }

    public ResponseEntity<String> renameFile(boolean deletedPresent, String oldPath, String newPath) throws IOException {
        File currentFile = new File(commonMethods.baseFolderPath + "\\" + oldPath);
        File newFile = new File(commonMethods.baseFolderPath + "\\" + newPath);

        commonMethods.permanentlyDeleteDirectory(deletedPresent, newFile);

        if (!currentFile.renameTo(newFile)) {
            throw new IOException("Failed to rename file");
        }
        return ResponseEntity.ok("File successfully renamed to: " + newPath);
    }

    public ResponseEntity<String> moveFile(boolean deletedPresent, String oldPath, String newPath) throws IOException {
        File currentFile = new File(commonMethods.baseFolderPath+"\\"+oldPath);
        File newDestination = new File(commonMethods.baseFolderPath+"\\"+newPath);

        if (deletedPresent) {
            commonMethods.permanentlyDeleteDirectory(true, newDestination);
        }

        Files.move(currentFile.toPath(), newDestination.toPath());


        return ResponseEntity.ok("File successfully moved to: " + newPath);
    }

    public ResponseEntity<String> deleteDocument(String path) throws IOException {
        commonMethods.hideFile(path);
        return ResponseEntity.ok("file successfully deleted");
    }
}
