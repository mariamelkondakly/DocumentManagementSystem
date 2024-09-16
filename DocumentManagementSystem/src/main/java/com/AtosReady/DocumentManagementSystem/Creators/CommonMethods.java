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

    public void permanentlyDeleteDirectory(boolean present, File dir) {
        if (present && dir.exists()) {
            boolean deleted = deleteDirectoryRecursively(dir);
            if (!deleted) {
                throw new DirectoryDeletionException("Failed to delete the existing directory: " + dir.getAbsolutePath());
            }
        }
    }
}
