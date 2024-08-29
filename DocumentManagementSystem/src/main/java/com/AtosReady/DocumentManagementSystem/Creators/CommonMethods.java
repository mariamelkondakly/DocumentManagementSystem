package com.AtosReady.DocumentManagementSystem.Creators;

import com.AtosReady.DocumentManagementSystem.Exceptions.DirectoryDeletionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.Objects;

@Component
public class CommonMethods {
    @Value("${app.base-folder}")
    protected String baseFolderPath;

    private boolean deleteDirectoryRecursively(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (!deleteDirectoryRecursively(file)) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    protected void hideRecursively(File file) throws IOException {
        if (file.isDirectory()) {
            // Hide the directory itself
            hideFile(file.getAbsolutePath());

            // Recursively hide all subdirectories and files
            for (File child : Objects.requireNonNull(file.listFiles())) {
                hideRecursively(child);
            }
        } else {
            // Hide the file
            hideFile(file.getAbsolutePath());
        }
    }

    public void hideFile(String filePath) throws IOException {
        Path path = new File(filePath).toPath();

        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }

        // Set the hidden attribute
        DosFileAttributeView attr = Files.getFileAttributeView(path, DosFileAttributeView.class);
        if (attr == null) {
            throw new IOException("DOS file attribute view not supported on this file system.");
        }
        attr.setHidden(true);
    }


    public void permanentlyDeleteDirectory(boolean present, File dir) {
        if (present && dir.exists()) {
            boolean deleted = deleteDirectoryRecursively(dir);
            if (!deleted) {
                throw new DirectoryDeletionException("Failed to delete the existing directory: " + dir.getAbsolutePath());
            }
        }
    }
}
